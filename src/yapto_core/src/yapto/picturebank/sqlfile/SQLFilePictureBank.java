package yapto.picturebank.sqlfile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.picturebank.AbstractIdBasedPictureBrowser;
import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.IPictureBrowser;
import yapto.picturebank.PictureAddException;
import yapto.picturebank.PictureAddExceptionType;
import yapto.picturebank.PictureAddResult;
import yapto.picturebank.PictureInformation;
import yapto.picturebank.PictureListWithIndex;
import yapto.picturebank.index.lucene.PictureIndexer;
import yapto.picturebank.process.PictureProcessor;
import yapto.picturebank.sqlfile.config.IGlobalSQLFilePictureBankConfiguration;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;
import yapto.picturebank.tag.ITag;
import yapto.picturebank.tag.IWritableTagRepository;
import yapto.picturebank.tag.TagAddException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.eventbus.EventBus;

/**
 * {@link IPictureBank} using an SQLite file to stock the meta-informations, and
 * a standard filesystem for the pictures.
 * 
 * @author benobiwan
 * 
 */
public class SQLFilePictureBank implements IPictureBank<FsPicture>
{
	/**
	 * Logger object.
	 */
	protected static transient final Logger LOGGER = LoggerFactory.getLogger(SQLFilePictureBank.class);

	/**
	 * List of all picture id.
	 */
	protected final List<String> _pictureIdList = new Vector<>();

	/**
	 * Configuration for this {@link SQLFilePictureBank}.
	 */
	private final ISQLFilePictureBankConfiguration _conf;

	/**
	 * Global configuration common for all {@link SQLFilePictureBank}.
	 */
	private final IGlobalSQLFilePictureBankConfiguration _globalConfiguration;

	/**
	 * {@link LoadingCache} used to load the {@link FsPicture}.
	 */
	final LoadingCache<String, FsPicture> _pictureCache;

	/**
	 * {@link ImageLoader} used to load the {@link BufferedImage}.
	 */
	private final ImageLoader _imageLoader;

	/**
	 * Object holding the connection to the database and the prepared
	 * statements.
	 */
	private final SQLFileListConnection _fileListConnection;

	/**
	 * {@link EventBus} used to signal registered objects of changes in the
	 * {@link IPictureBrowser}.
	 */
	protected final EventBus _bus;

	/**
	 * Object used to index {@link FsPicture}s.
	 */
	private final PictureIndexer _pictureIndexer;

	/**
	 * Queue holding all the {@link FsPicture} to update.
	 */
	protected final BlockingQueue<FsPicture> _updateQueue = new LinkedBlockingQueue<>();

	/**
	 * Queue holding all the {@link FsPicture} to add.
	 */
	protected final BlockingQueue<FsPicture> _addQueue = new LinkedBlockingQueue<>();

	/**
	 * {@link Runnable} used to update modified {@link FsPicture}.
	 */
	private final PictureUpdater _updater;

	/**
	 * {@link PictureProcessor} used to execute external commands on pictures.
	 */
	private final SQLFilePictureProcessor _processor;

	/**
	 * {@link IWritableTagRepository} used to load and save {@link ITag}s.
	 */
	final IWritableTagRepository _tagRepository;

	/**
	 * Time to wait before writing picture information to the database.
	 */
	private final long _lWaitBeforeWrite;

	/**
	 * Random number generator.
	 */
	private final Random _rand = new Random(System.currentTimeMillis());

	/**
	 * Creates a new SQLFilePictureBank.
	 * 
	 * @param globalConfiguration
	 *            global configuration common for all {@link SQLFilePictureBank}
	 *            .
	 * @param conf
	 *            configuration for this {@link SQLFilePictureBank}.
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in the {@link IPictureBrowser}.
	 * @throws SQLException
	 *             if an SQL error occurred during the connection to the
	 *             database.
	 * @throws ClassNotFoundException
	 *             if the database driver class can't be found.
	 * @throws IOException
	 *             if there is an error in creating the required picture
	 *             directories.
	 */
	public SQLFilePictureBank(final IGlobalSQLFilePictureBankConfiguration globalConfiguration,
			final ISQLFilePictureBankConfiguration conf, final EventBus bus)
			throws SQLException, ClassNotFoundException, IOException
	{
		_bus = bus;
		_conf = conf;
		_globalConfiguration = globalConfiguration;
		_pictureIndexer = new PictureIndexer(_conf);
		_lWaitBeforeWrite = _globalConfiguration.getWaitBeforeWrite() * 1000;
		_processor = new SQLFilePictureProcessor(conf, _globalConfiguration.getMaxConcurrentIdentifyTask(),
				_globalConfiguration.getMaxConcurrentOtherTask());

		// create directories
		if (!checkAndCreateDirectories())
		{
			throw new IOException("Error creating the required picture directories : "
					+ _conf.getMainPictureLoaderConfiguration().getPictureDirectory());
		}

		// database connection
		_fileListConnection = new SQLFileListConnection(_conf.getDatabaseConfiguration());

		// tag repository
		_tagRepository = new SQLFileTagRepository(_conf, _fileListConnection, _bus);

		_imageLoader = new ImageLoader(_conf);
		// picture cache
		final CacheLoader<String, FsPicture> pictureLoader = new FsPictureCacheLoader(_fileListConnection, _imageLoader,
				_tagRepository, this);
		final RemovalListener<String, FsPicture> pictureListener = new FsPictureRemovalListener(this);
		_pictureCache = CacheBuilder.newBuilder().removalListener(pictureListener).build(pictureLoader);

		_fileListConnection.createTables();

		loadPictureIdList();
		_updater = new PictureUpdater();
		final Thread t = new Thread(_updater, "picture updater");
		t.start();
	}

	@Override
	public int getPictureCount()
	{
		return _pictureIdList.size();
	}

	@Override
	public void syncAddPicture(final Path pictureFile, final List<ITag> tagList) throws PictureAddException
	{
		if (!Files.isReadable(pictureFile))
		{
			throw new PictureAddException(PictureAddExceptionType.CAN_T_READ);
		}
		if (!Files.isRegularFile(pictureFile))
		{
			throw new PictureAddException(PictureAddExceptionType.NOT_A_FILE);
		}
		final long lAddedTimestamp = System.currentTimeMillis();
		// calc id
		MessageDigest mdSha256;
		try
		{
			mdSha256 = MessageDigest.getInstance("SHA-256");
		}
		catch (final NoSuchAlgorithmException e)
		{
			throw new PictureAddException(PictureAddExceptionType.NO_SUCH_ALGORITHM, e);
		}
		try
		{
			InputStream stream = null;
			try
			{
				stream = Files.newInputStream(pictureFile);
				final byte[] dataBytes = new byte[4096];

				int byteRead = 0;
				while ((byteRead = stream.read(dataBytes)) != -1)
				{
					mdSha256.update(dataBytes, 0, byteRead);
				}
			}
			catch (final FileNotFoundException e)
			{
				throw new PictureAddException(PictureAddExceptionType.FILE_NOT_FOUND, e);
			}
			finally
			{
				if (stream != null)
				{
					stream.close();
				}
			}
		}
		catch (final IOException e)
		{
			throw new PictureAddException(PictureAddExceptionType.IO_ERROR, e);
		}

		final byte[] mdbytes = mdSha256.digest();
		final StringBuffer sb = new StringBuffer();
		for (final byte mdbyte : mdbytes)
		{
			sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1).toUpperCase());
		}

		final String strPictureId = sb.toString();

		// check if already present
		if (_pictureIdList.contains(strPictureId))
		{
			// add the tag to the existing picture
			try
			{
				final FsPicture existingPicture = _pictureCache.get(strPictureId);
				for (final ITag tag : tagList)
				{
					existingPicture.addTag(tag);
				}
			}
			catch (final ExecutionException e)
			{
				throw new PictureAddException(strPictureId, PictureAddExceptionType.FILE_ALREADY_EXISTS_NO_OPEN, e);
			}
			throw new PictureAddException(strPictureId, PictureAddExceptionType.FILE_ALREADY_EXISTS);
		}

		PictureInformation info;
		try
		{
			info = _processor.identifyPicture(pictureFile);
		}
		catch (InterruptedException | ExecutionException e)
		{
			throw new PictureAddException(strPictureId, PictureAddExceptionType.IDENTIFY_EXECUTION_ERROR, e);
		}
		// copy file
		final Path destPath = FileSystems.getDefault().getPath(
				_conf.getMainPictureLoaderConfiguration().getPictureDirectory(), strPictureId.substring(0, 2),
				strPictureId + '.' + info.getExtension());
		try
		{
			Files.copy(pictureFile, destPath);
		}
		catch (final FileAlreadyExistsException e)
		{
			throw new PictureAddException(strPictureId, PictureAddExceptionType.FILE_ALREADY_EXISTS, e);
		}
		catch (final IOException e)
		{
			throw new PictureAddException(strPictureId, PictureAddExceptionType.COPY_ERROR, e);
		}
		// create object
		final FsPicture picture = new FsPicture(_imageLoader, this, strPictureId, lAddedTimestamp, lAddedTimestamp,
				info);
		for (final ITag tag : tagList)
		{
			picture.addTag(tag);
		}
		// create thumbnail
		createThumbnail(picture);
		if (!picture.getImageType().doesKeepFormat())
		{
			createDisplayPicture(picture);
		}
		// insert to base and index
		try
		{
			_fileListConnection.insertPicture(picture);
			_pictureIndexer.indexPicture(picture);
		}
		catch (final SQLException e)
		{
			throw new PictureAddException(strPictureId, PictureAddExceptionType.SQL_INSERT_ERROR, e);
		}
		catch (final CorruptIndexException e)
		{
			throw new PictureAddException(strPictureId, PictureAddExceptionType.CORRUPT_INDEX_ERROR, e);
		}
		catch (final IOException e)
		{
			throw new PictureAddException(strPictureId, PictureAddExceptionType.INDEX_ERROR, e);
		}
		_pictureIdList.add(strPictureId);
	}

	@Override
	public PictureAddResult asyncAddPicture(final Path picturePath, final List<ITag> tagList) throws PictureAddException
	{
		final AddingFileVisitor visitor = new AddingFileVisitor(this, tagList);
		try
		{
			Files.walkFileTree(picturePath, visitor);
			return visitor.getResult();
		}
		catch (final IOException e)
		{
			throw new PictureAddException(PictureAddExceptionType.IO_ERROR, e);
		}
	}

	@Override
	public void createThumbnail(final FsPicture picture)
	{
		_processor.createThumbnail(picture);
	}

	/**
	 * Create secondary image for picture types that can't be displayed.
	 * 
	 * @param picture
	 *            the picture for which a secondary picture has to be created.
	 */
	public void createDisplayPicture(final FsPicture picture)
	{
		if (!picture.getImageType().doesKeepFormat())
		{
			_processor.createDisplayPicture(picture);
		}
	}

	@Override
	public int getId()
	{
		return _conf.getPictureBankId();
	}

	/**
	 * Check the existence of all the required directories, and creates them if
	 * they don't exists. Also check if they are readable and writable.
	 * 
	 * @return true if every required directory exists.
	 */
	private boolean checkAndCreateDirectories()
	{
		boolean bRes = true;
		// bRes &= checkDirectory(new File(_conf.g));
		bRes &= checkDirectory(new File(_conf.getIndexDirectory()));
		bRes &= checkDirectory(new File(_conf.getIndexDirectory(), "picture"));
		bRes &= checkDirectory(new File(_conf.getIndexDirectory(), "tag"));
		// create the base picture directory
		final File fPictureBaseDirectory = new File(_conf.getMainPictureLoaderConfiguration().getPictureDirectory());
		bRes &= checkDirectory(fPictureBaseDirectory);
		if (bRes)
		{
			for (int i = 0; i < 256; i++)
			{
				String strFileName = Integer.toHexString(i).toUpperCase();
				if (strFileName.length() == 1)
				{
					strFileName = '0' + strFileName;
				}
				bRes &= checkDirectory(new File(fPictureBaseDirectory, strFileName));
			}
		}
		// create the base thumbnail directory
		final File fThumbnailBaseDirectory = new File(
				_conf.getThumbnailPictureLoaderConfiguration().getPictureDirectory());
		bRes &= checkDirectory(fThumbnailBaseDirectory);
		if (bRes)
		{
			for (int i = 0; i < 256; i++)
			{
				String strFileName = Integer.toHexString(i).toUpperCase();
				if (strFileName.length() == 1)
				{
					strFileName = '0' + strFileName;
				}
				bRes &= checkDirectory(new File(fThumbnailBaseDirectory, strFileName));
			}
		}
		// create the base secondary picture directory
		bRes &= checkDirectory(new File(_conf.getIndexDirectory()));
		final File fSecondaryPictureBaseDirectory = new File(
				_conf.getSecondaryPictureLoaderConfiguration().getPictureDirectory());
		bRes &= checkDirectory(fSecondaryPictureBaseDirectory);
		if (bRes)
		{
			for (int i = 0; i < 256; i++)
			{
				String strFileName = Integer.toHexString(i).toUpperCase();
				if (strFileName.length() == 1)
				{
					strFileName = '0' + strFileName;
				}
				bRes &= checkDirectory(new File(fSecondaryPictureBaseDirectory, strFileName));
			}
		}

		return bRes;
	}

	/**
	 * Check if the specified directory exists, tries to create it if it
	 * doesn't. Also check if it's readable and writable.
	 * 
	 * @param fDirectory
	 *            the directory to check.
	 * @return if the directory exists, and is readable and writable.
	 */
	private static boolean checkDirectory(final File fDirectory)
	{
		boolean bRes = true;
		if (!fDirectory.exists())
		{
			bRes = fDirectory.mkdir();
		}
		else
		{
			bRes &= fDirectory.canRead();
			bRes &= fDirectory.canWrite();
		}
		return bRes;
	}

	@Override
	public void close()
	{
		_updater.stop();
		_pictureCache.invalidateAll();
		_processor.shutdown();
		try
		{
			_pictureIndexer.close();
		}
		catch (final IOException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		try
		{
			_tagRepository.close();
		}
		catch (final IOException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		try
		{
			_fileListConnection.close();
		}
		catch (final SQLException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public IPictureBrowser<FsPicture> getAllPictures(final String strInitialPictureId) throws ExecutionException
	{
		return new PictureIterator(null, _pictureIdList, _pictureIdList.indexOf(strInitialPictureId));
	}

	@Override
	public IPictureBrowser<FsPicture> filterPictures(final Query query, final int iLimit,
			final String strInitialPictureId) throws IOException, ExecutionException
	{
		final PictureListWithIndex list = _pictureIndexer.searchPicture(query, iLimit, strInitialPictureId);
		return new PictureIterator(query, list.getPictureIdList(), list.getIndex());
	}

	@Override
	public IPictureBrowser<FsPicture> getRandomPictureList(final int iNbrPicture, final String strInitialPictureId)
			throws ExecutionException
	{
		int iInitialIndex = -1;
		IPictureBrowser<FsPicture> pictureBrowser = null;
		if (iNbrPicture > _pictureIdList.size())
		{
			pictureBrowser = getAllPictures(strInitialPictureId);
		}
		else if (iNbrPicture <= 0)
		{
			throw new IllegalArgumentException("Error, can't select less than 1 picture.");
		}
		else
		{
			final List<String> idList = new Vector<>(iNbrPicture);
			int iLeftToPick = iNbrPicture;
			int i = 0;
			int iLeftToLook = _pictureIdList.size();
			while (iLeftToPick > 0)
			{
				final int rand = _rand.nextInt(iLeftToLook);
				if (rand < iLeftToPick)
				{
					final String strSelectedId = _pictureIdList.get(i);
					if (strSelectedId.equals(strInitialPictureId))
					{
						iInitialIndex = idList.size();
					}
					idList.add(strSelectedId);
					iLeftToPick--;
				}
				iLeftToLook--;
				i++;
			}
			pictureBrowser = new PictureIterator(null, idList, iInitialIndex);
		}
		return pictureBrowser;
	}

	/**
	 * Load picture id list from the database.
	 * 
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	private void loadPictureIdList() throws SQLException
	{
		final ResultSet resLoad = _fileListConnection.loadPictureList();
		while (resLoad.next())
		{
			final String strId = resLoad.getString(SQLFileListConnection.PICTURE_ID_COLUMN_NAME);
			_pictureIdList.add(strId);
		}
	}

	/**
	 * Update the picture in the database and the index. A configured amount of
	 * time between picture update sand writing can be configured.
	 * 
	 * @param picture
	 *            the picture to update.
	 * @param bImmediat
	 *            true if the picture has to be updated right now.
	 */
	public void updatePicture(final FsPicture picture, final boolean bImmediat)
	{
		synchronized (picture)
		{
			if (picture.hasBeenModified())
			{
				if (!bImmediat)
				{
					final long waitTime = picture.getModifiedTimestamp() + _lWaitBeforeWrite
							- System.currentTimeMillis();
					if (waitTime > 0)
					{
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug("Waiting for : " + waitTime + " before updating.");
						}
						try
						{
							picture.wait(waitTime);
						}
						catch (final InterruptedException e1)
						{
							// nothing to do
						}
					}
				}
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("Updating picture, id : " + picture.getId());
				}
				try
				{
					_fileListConnection.updatePicture(picture);
					_pictureIndexer.indexPicture(picture);
					picture.unsetModified();
				}
				catch (final SQLException | IOException e)
				{
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Add a {@link FsPicture} to the list of pictures needing an update.
	 * 
	 * @param picture
	 *            the {@link FsPicture} needing an update.
	 */
	public void setPictureForUpdating(final FsPicture picture)
	{
		_updateQueue.add(picture);
	}

	/**
	 * Re-index the specified picture.
	 * 
	 * @param strPicId
	 *            id of the picture to re-index.
	 * @throws CorruptIndexException
	 *             if the index is corrupted.
	 * @throws IOException
	 *             if there is an error while writing the index.
	 */
	public void reIndexPicture(final String strPicId) throws CorruptIndexException, IOException
	{
		try
		{
			final FsPicture picture = _pictureCache.get(strPicId);
			synchronized (picture)
			{
				_pictureIndexer.indexPicture(picture);
			}
		}
		catch (final ExecutionException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Re-index all the pictures.
	 * 
	 * @throws CorruptIndexException
	 *             if the index is corrupted.
	 * @throws IOException
	 *             if there is an error while writing the index.
	 */
	public void reIndexAllPictures() throws CorruptIndexException, IOException
	{
		for (final String strPicId : _pictureIdList)
		{
			reIndexPicture(strPicId);
		}
	}

	/**
	 * Check if the specified picture has its thumbnail and secondary picture
	 * created correctly and also force its re-indexation.
	 * 
	 * @param strPicId
	 *            the id of the {@link IPicture} to check.
	 * @throws CorruptIndexException
	 *             if the index is corrupted.
	 * @throws IOException
	 *             if there is an error while writing the index.
	 */
	public void checkPicture(final String strPicId) throws CorruptIndexException, IOException
	{
		try
		{
			reIndexPicture(strPicId);
			final FsPicture picture = _pictureCache.get(strPicId);
			if (!_processor.hasThumbnail(picture))
			{
				_processor.createThumbnail(picture);
			}
			if (!picture.getImageType().doesKeepFormat() && !_processor.hasDisplayPicture(picture))
			{
				_processor.createDisplayPicture(picture);
			}
		}
		catch (final ExecutionException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Check that all pictures have their thumbnails and secondary pictures
	 * created correctly and also force re-indexation.
	 * 
	 * @throws CorruptIndexException
	 *             if the index is corrupted.
	 * @throws IOException
	 *             if there is an error while writing the index.
	 */
	public void checkAllPictures() throws CorruptIndexException, IOException
	{
		for (final String strPicId : _pictureIdList)
		{
			checkPicture(strPicId);
		}
	}

	/**
	 * Delete the specified {@link IPicture}.
	 * 
	 * @param strPicId
	 *            the id of the {@link IPicture}.
	 * @throws ExecutionException
	 *             if an error occurs while trying to load the {@link IPicture}
	 *             from cache.
	 * @throws SQLException
	 *             if an SQL error occurs while remove the {@link IPicture} from
	 *             the database.
	 * @throws IOException
	 *             if an error occurs while deleting one of the files or
	 *             deleting the {@link IPicture} from the index.
	 */
	public void deletePicture(final String strPicId) throws ExecutionException, SQLException, IOException
	{
		final FsPicture picture = _pictureCache.get(strPicId);
		// delete from index
		synchronized (picture)
		{
			_pictureIndexer.unindexPicture(picture);
		}
		// delete display picture
		if (!picture.getImageType().doesKeepFormat() && _processor.hasDisplayPicture(picture))
		{
			_processor.deleteDisplayPicture(picture);
		}
		// delete thumbnail
		if (_processor.hasThumbnail(picture))
		{
			_processor.deleteThumbnail(picture);
		}
		// delete main picture
		_processor.deleteMainPicture(picture);
		// delete from BDD
		_fileListConnection.removePicture(strPicId);
	}

	/**
	 * {@link IPictureBrowser} on the list of {@ink FsPicture}.
	 * 
	 * @author benobiwan
	 * 
	 */
	private final class PictureIterator extends AbstractIdBasedPictureBrowser<FsPicture>
	{
		/**
		 * Creates a new {@link PictureIterator}.
		 * 
		 * @param query
		 *            the {@link Query} used to create the
		 *            {@link PictureIterator}.
		 * @param idList
		 *            the list of ids of the {@link PictureIterator}.
		 * @param iInitialIndex
		 *            initial index of the {@link IPicture} to display.
		 * @throws ExecutionException
		 *             if an Exception was thrown during the loading of the
		 *             picture.
		 */
		public PictureIterator(final Query query, final List<String> idList, final int iInitialIndex)
				throws ExecutionException
		{
			super(SQLFilePictureBank.this, query, SQLFilePictureBank.this._bus, idList, iInitialIndex);
		}

		@Override
		protected FsPicture getPicture(final String pictureId) throws ExecutionException
		{
			return _pictureCache.get(pictureId);
		}

		@Override
		public SortedSet<ITag> getRecentlyUsed()
		{
			return _tagRepository.getRecentlyUsed();
		}

		@Override
		public void addLastUsed(ITag tag)
		{
			_tagRepository.addLastUsed(tag);
		}
	}

	/**
	 * {@link Runnable} used to update the modified {@link FsPicture}.
	 * 
	 * @author benobiwan
	 * 
	 */
	private final class PictureUpdater implements Runnable
	{
		/**
		 * Boolean used to stop the {@link PictureUpdater}.
		 */
		private volatile boolean _bStop = false;

		/**
		 * Thread used to run the {@link PictureUpdater}.
		 */
		private volatile Thread _workingThread;

		/**
		 * Creates a new {@link PictureUpdater}.
		 */
		public PictureUpdater()
		{
			// nothing to do
		}

		/**
		 * Function used to stop the {@link PictureUpdater}.
		 */
		public void stop()
		{
			_bStop = true;
			if (_workingThread != null)
			{
				synchronized (_workingThread)
				{
					_workingThread.interrupt();
				}
			}
		}

		@Override
		public void run()
		{
			_workingThread = Thread.currentThread();
			while (!_bStop)
			{
				try
				{
					updatePicture(_updateQueue.take(), false);
				}
				catch (final InterruptedException e)
				{
					// nothing to do there
				}
			}
		}
	}

	@Override
	public Set<ITag> getTagSet()
	{
		return _tagRepository.getTagSet();
	}

	@Override
	public ITag getRootTag()
	{
		return _tagRepository.getRootTag();
	}

	@Override
	public ITag getTag(final int iTagId)
	{
		return _tagRepository.getTag(iTagId);
	}

	@Override
	public ITag getTag(final Integer iTagId)
	{
		return _tagRepository.getTag(iTagId);
	}

	@Override
	public ITag getTag(final String strTagName)
	{
		return _tagRepository.getTag(strTagName);
	}

	@Override
	public boolean hasTagNamed(final String strName)
	{
		return _tagRepository.hasTagNamed(strName);
	}

	@Override
	public void addTag(final ITag parent, final String strName, final String strDescription, final boolean bSelectable)
			throws TagAddException
	{
		_tagRepository.addTag(parent, strName, strDescription, bSelectable);
	}

	@Override
	public void addTag(final int iParentId, final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException
	{
		_tagRepository.addTag(iParentId, strName, strDescription, bSelectable);
	}

	@Override
	public void addTag(final String strName, final String strDescription, final boolean bSelectable)
			throws TagAddException
	{
		_tagRepository.addTag(strName, strDescription, bSelectable);
	}

	@Override
	public void editTag(final int iTagId, final ITag parent, final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException
	{
		_tagRepository.editTag(iTagId, parent, strName, strDescription, bSelectable);
	}

	@Override
	public void editTag(final int iTagId, final int iParentId, final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException
	{
		_tagRepository.editTag(iTagId, iParentId, strName, strDescription, bSelectable);
	}

	@Override
	public void removeTag(final int iTagId) throws TagAddException
	{
		_tagRepository.removeTag(iTagId);
	}

	@Override
	public int compareTo(final IPictureBank<?> o)
	{
		return getId() - o.getId();
	}

	@Override
	public SortedSet<ITag> getRecentlyUsed()
	{
		return _tagRepository.getRecentlyUsed();
	}

	@Override
	public void addLastUsed(ITag tag)
	{
		_tagRepository.addLastUsed(tag);
	}
}
