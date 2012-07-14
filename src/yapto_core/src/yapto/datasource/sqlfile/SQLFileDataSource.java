package yapto.datasource.sqlfile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.datasource.AbstractIdBasedPictureBrowser;
import yapto.datasource.IDataSource;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.OperationNotSupportedException;
import yapto.datasource.PictureAddException;
import yapto.datasource.PictureAddExceptionType;
import yapto.datasource.index.PictureIndexer;
import yapto.datasource.sqlfile.config.ISQLFileDataSourceConfiguration;
import yapto.datasource.tag.Tag;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.eventbus.EventBus;

/**
 * {@link IDataSource} using an SQLite file to stock the meta-informations, and
 * a standard filesystem for the pictures.
 * 
 * @author benobiwan
 * 
 */
public class SQLFileDataSource implements IDataSource<FsPicture>
{
	/**
	 * Logger object.
	 */
	protected static transient final Logger LOGGER = LoggerFactory
			.getLogger(SQLFileDataSource.class);

	/**
	 * Set containing all the {@link Tag}s.
	 */
	private final Set<Tag> _tagSet = new TreeSet<>();

	/**
	 * List of all picture id.
	 */
	protected final List<String> _pictureIdList = new Vector<>();

	/**
	 * Map containing all the {@link Tag}s.
	 */
	private final Map<Integer, Tag> _tagMap = new TreeMap<>();

	/**
	 * Configuration for this {@link SQLFileDataSource}.
	 */
	private final ISQLFileDataSourceConfiguration _conf;

	/**
	 * {@link LoadingCache} used to load the {@link FsPicture}.
	 */
	protected final LoadingCache<String, FsPicture> _pictureCache;

	/**
	 * {@link LoadingCache} used to load the {@link BufferedImage}.
	 */
	private final LoadingCache<String, BufferedImage> _imageCache;

	/**
	 * {@link LoadingCache} used to load the {@link Tag}.
	 */
	private final LoadingCache<Integer, Tag> _tagCache;

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
	private final PictureIndexer _indexer;

	/**
	 * Queue holding all the {@link FsPicture} to update.
	 */
	protected final BlockingQueue<FsPicture> _updateQueue = new LinkedBlockingQueue<>();

	/**
	 * {@link Runnable} used to update modified {@link FsPicture}.
	 */
	private final PictureUpdater _updater;

	/**
	 * Creates a new SQLFileDataSource.
	 * 
	 * @param conf
	 *            configuration for this {@link SQLFileDataSource}.
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
	public SQLFileDataSource(final ISQLFileDataSourceConfiguration conf,
			final EventBus bus) throws SQLException, ClassNotFoundException,
			IOException
	{
		_bus = bus;
		_conf = conf;
		_fileListConnection = new SQLFileListConnection(_conf);
		_indexer = new PictureIndexer(_conf);

		// tag cache
		final CacheLoader<Integer, Tag> tagLoader = new TagCacheLoader(_conf,
				_fileListConnection);
		_tagCache = CacheBuilder.newBuilder().build(tagLoader);

		// image cache
		final CacheLoader<String, BufferedImage> imageLoader = new BufferedImageCacheLoader(
				_conf.getMainPictureLoaderConfiguration());
		_imageCache = CacheBuilder.newBuilder().build(imageLoader);

		// picture cache
		final CacheLoader<String, FsPicture> pictureLoader = new FsPictureCacheLoader(
				_fileListConnection, _imageCache, _tagCache, this);
		final RemovalListener<String, FsPicture> pictureListener = new FsPictureRemovalListener(
				this);
		_pictureCache = CacheBuilder.newBuilder()
				.removalListener(pictureListener).build(pictureLoader);

		if (!checkAndCreateDirectories())
		{
			throw new IOException(
					"Error creating the required picture directories : "
							+ _conf.getMainPictureLoaderConfiguration()
									.getPictureDirectory());
		}
		_fileListConnection.createTables();
		loadTags();
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
	public Set<Tag> getTagSet() throws OperationNotSupportedException
	{
		return Collections.unmodifiableSet(_tagSet);
	}

	@Override
	public Tag getRootTag()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPicture(final File pictureFile) throws PictureAddException
	{
		if (!pictureFile.canRead())
		{
			throw new PictureAddException(PictureAddExceptionType.CAN_T_READ);
		}
		if (!pictureFile.isFile())
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
			throw new PictureAddException(
					PictureAddExceptionType.NO_SUCH_ALGORITHM, e);
		}
		try
		{
			FileInputStream stream = null;
			try
			{
				stream = new FileInputStream(pictureFile);
				final byte[] dataBytes = new byte[4096];

				int byteRead = 0;
				while ((byteRead = stream.read(dataBytes)) != -1)
				{
					mdSha256.update(dataBytes, 0, byteRead);
				}
			}
			catch (final FileNotFoundException e)
			{
				throw new PictureAddException(
						PictureAddExceptionType.FILE_NOT_FOUND, e);
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
			sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16)
					.substring(1).toUpperCase());
		}

		final String strPictureId = sb.toString();

		// check if already present
		if (_pictureIdList.contains(strPictureId))
		{
			throw new PictureAddException(strPictureId,
					PictureAddExceptionType.FILE_ALREADY_EXISTS);
		}

		// calc width and height
		final int iWidth = 0;
		final int iHeight = 0;
		final long lCreationTimestamp = System.currentTimeMillis();
		// copy file
		final Path destPath = FileSystems.getDefault()
				.getPath(
						_conf.getMainPictureLoaderConfiguration()
								.getPictureDirectory(),
						strPictureId.substring(0, 2), strPictureId);
		try
		{
			Files.copy(pictureFile.toPath(), destPath);
		}
		catch (final FileAlreadyExistsException e)
		{
			throw new PictureAddException(strPictureId,
					PictureAddExceptionType.FILE_ALREADY_EXISTS, e);
		}
		catch (final IOException e)
		{
			throw new PictureAddException(strPictureId,
					PictureAddExceptionType.COPY_ERROR, e);
		}
		final FsPicture picture = new FsPicture(_imageCache, this,
				strPictureId, pictureFile.getName(), iWidth, iHeight,
				lAddedTimestamp, lCreationTimestamp, lAddedTimestamp);
		try
		{
			_fileListConnection.insertPicture(picture);
			_indexer.indexPicture(picture);
		}
		catch (final SQLException e)
		{
			throw new PictureAddException(strPictureId,
					PictureAddExceptionType.SQL_INSERT_ERROR, e);
		}
		catch (final CorruptIndexException e)
		{
			throw new PictureAddException(strPictureId,
					PictureAddExceptionType.CORRUPT_INDEX_ERROR, e);
		}
		catch (final IOException e)
		{
			throw new PictureAddException(strPictureId,
					PictureAddExceptionType.INDEX_ERROR, e);
		}
		_pictureIdList.add(strPictureId);
	}

	@Override
	public void addTag(final Tag newTag)
	{
		final Integer tagId = Integer.valueOf(newTag.getTagId());
		if (!_tagMap.containsKey(tagId))
		{
			_tagSet.add(newTag);
			_tagMap.put(tagId, newTag);
			try
			{
				_fileListConnection.saveTagToDatabase(newTag);
			}
			catch (final SQLException e)
			{
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public int getId()
	{
		return _conf.getDataSourceId();
	}

	/**
	 * Load {@link Tag}s from the database.
	 * 
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	private void loadTags() throws SQLException
	{
		ResultSet resLoad = null;
		ResultSet resParent = null;
		Tag tag = null;
		Tag parentTag = null;
		try
		{
			resLoad = _fileListConnection.loadTagList();
			while (resLoad.next())
			{
				final int iTagId = resLoad
						.getInt(SQLFileListConnection.TAG_ID_COLUMN_NAME);
				final String strName = resLoad
						.getString(SQLFileListConnection.TAG_NAME_COLUMN_NAME);
				final String strDescription = resLoad
						.getString(SQLFileListConnection.TAG_DESCRIPTION_COLUMN_NAME);
				final boolean bSelectable = resLoad
						.getBoolean(SQLFileListConnection.TAG_SELECTABLE_COLUMN_NAME);
				tag = new Tag(getId(), iTagId, strName, strDescription,
						bSelectable);
				_tagSet.add(tag);
				_tagMap.put(Integer.valueOf(iTagId), tag);
			}
		}
		finally
		{
			if (resLoad != null)
			{
				resLoad.close();
			}
		}
		try
		{
			resParent = _fileListConnection.loadParents();
			while (resParent.next())
			{
				final Integer iId = Integer.valueOf(resParent
						.getInt(SQLFileListConnection.TAG_ID_COLUMN_NAME));
				final Integer iParentId = Integer
						.valueOf(resParent
								.getInt(SQLFileListConnection.TAG_PARENT_ID_COLUMN_NAME));
				tag = _tagMap.get(iId);
				parentTag = _tagMap.get(iParentId);
				if (tag != null && parentTag != null)
				{
					tag.setParent(parentTag);
				}
			}
		}
		finally
		{
			if (resParent != null)
			{
				resParent.close();
			}
		}
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
		bRes &= checkDirectory(new File(_conf.getIndexDirectory()));
		final File fPictureBaseDirectory = new File(_conf
				.getMainPictureLoaderConfiguration().getPictureDirectory());
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
				bRes &= checkDirectory(new File(fPictureBaseDirectory,
						strFileName));
			}
		}
		final File fThumbnailBaseDirectory = new File(_conf
				.getThumbnailPictureLoaderConfiguration().getPictureDirectory());
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
				bRes &= checkDirectory(new File(fThumbnailBaseDirectory,
						strFileName));
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
		try
		{
			_indexer.close();
		}
		catch (final IOException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public IPictureBrowser<FsPicture> getAllPictures()
			throws ExecutionException
	{
		return new PictureIterator(null, _pictureIdList);
	}

	@Override
	public IPictureBrowser<FsPicture> filterPictures(final Query query,
			final int iLimit) throws IOException, ExecutionException
	{
		final List<String> list = _indexer.searchPicture(query, iLimit);
		return new PictureIterator(query, list);
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
			final String strId = resLoad
					.getString(SQLFileListConnection.PICTURE_ID_COLUMN_NAME);
			_pictureIdList.add(strId);
		}
	}

	/**
	 * Update the picture in the database and the index.
	 * 
	 * @param picture
	 *            the picture to update.
	 */
	public void updatePicture(final FsPicture picture)
	{
		synchronized (picture)
		{
			if (picture.hasBeenModified())
			{
				try
				{
					_fileListConnection.updatePicture(picture);
					_indexer.indexPicture(picture);
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
	 * {@link IPictureBrowser} on the list of {@ink FsPicture}.
	 * 
	 * @author benobiwan
	 * 
	 */
	private final class PictureIterator extends
			AbstractIdBasedPictureBrowser<FsPicture>
	{
		/**
		 * Creates a new {@link PictureIterator}.
		 * 
		 * @param query
		 *            the {@link Query} used to create the
		 *            {@link PictureIterator}.
		 * @param idList
		 *            the list of ids of the {@link PictureIterator}.
		 * @throws ExecutionException
		 *             if an Exception was thrown during the loading of the
		 *             picture.
		 */
		public PictureIterator(final Query query, final List<String> idList)
				throws ExecutionException
		{
			super(SQLFileDataSource.this, query, SQLFileDataSource.this._bus,
					idList);
		}

		@Override
		protected FsPicture getPicture(final String pictureId)
				throws ExecutionException
		{
			return _pictureCache.get(pictureId);
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
					updatePicture(_updateQueue.take());
				}
				catch (final InterruptedException e)
				{
					// nothing to do there
				}
			}
		}
	}
}
