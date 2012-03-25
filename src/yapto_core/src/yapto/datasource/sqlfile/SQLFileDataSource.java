package yapto.datasource.sqlfile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.datasource.AbstractIdBasedPictureBrowser;
import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.OperationNotSupportedException;
import yapto.datasource.sqlfile.config.BufferedImageCacheLoaderConfiguration;
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
	private final Set<Tag> _tagSet = new TreeSet<Tag>();

	/**
	 * List of all picture id.
	 */
	protected final List<String> _pictureIdList = new Vector<String>();

	/**
	 * Map containing all the {@link Tag}s.
	 */
	private final Map<Integer, Tag> _tagMap = new TreeMap<Integer, Tag>();

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
	 * Creates a new SQLFileDataSource.
	 * 
	 * @param conf
	 *            configuration for this {@link SQLFileDataSource}.
	 * @param cacheLoaderConf
	 *            configuration for the
	 *            {@link BufferedImageCacheLoaderConfiguration}.
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
			final BufferedImageCacheLoaderConfiguration cacheLoaderConf,
			final EventBus bus) throws SQLException, ClassNotFoundException,
			IOException
	{
		_bus = bus;
		_conf = conf;
		_fileListConnection = new SQLFileListConnection(_conf);

		// tag cache
		final CacheLoader<Integer, Tag> tagLoader = new TagCacheLoader(
				cacheLoaderConf, _fileListConnection);
		_tagCache = CacheBuilder.newBuilder().build(tagLoader);

		// image cache
		final CacheLoader<String, BufferedImage> imageLoader = new BufferedImageCacheLoader(
				cacheLoaderConf);
		_imageCache = CacheBuilder.newBuilder().build(imageLoader);

		// picture cache
		final CacheLoader<String, FsPicture> pictureLoader = new FsPictureCacheLoader(
				_fileListConnection, _imageCache, _tagCache, this);
		final RemovalListener<String, FsPicture> pictureListener = new FsPictureRemovalListener(
				_fileListConnection);
		_pictureCache = CacheBuilder.newBuilder()
				.removalListener(pictureListener).build(pictureLoader);

		if (!checkAndCreateDirectories())
		{
			throw new IOException(
					"Error creating the required picture directories : "
							+ _conf.getPictureDirectory());
		}
		_fileListConnection.createTables();
		loadTags();
	}

	@Override
	public int getPictureCount() throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IPictureList<FsPicture> filterList(final IPictureFilter filter)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPictureList<IPicture> mergeList(
			final IPictureList<IPicture> otherList)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPictureList<FsPicture>> getParent()
			throws OperationNotSupportedException
	{
		// no parent for a datasource.
		return null;
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
	public List<IPicture> getPictureList()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPicture(final File picturePath)
			throws OperationNotSupportedException, FileNotFoundException,
			IOException
	{
		// TODO Auto-generated method stub

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
		final ResultSet resLoad = _fileListConnection.loadTagList();
		Tag tag = null;
		Tag parentTag = null;
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
			tag = new Tag(getId(), iTagId, strName, strDescription, bSelectable);
			_tagSet.add(tag);
			_tagMap.put(Integer.valueOf(iTagId), tag);
		}
		final ResultSet resParent = _fileListConnection.loadParents();
		while (resParent.next())
		{
			final Integer iId = Integer.valueOf(resLoad
					.getInt(SQLFileListConnection.TAG_ID_COLUMN_NAME));
			final Integer iParentId = Integer.valueOf(resLoad
					.getInt(SQLFileListConnection.TAG_PARENT_ID_COLUMN_NAME));
			tag = _tagMap.get(iId);
			parentTag = _tagMap.get(iParentId);
			if (tag != null && parentTag != null)
			{
				tag.setParent(parentTag);
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
		final File fBaseDirectory = new File(_conf.getPictureDirectory());
		boolean bRes = checkDirectory(fBaseDirectory);
		if (bRes)
		{
			for (int i = 0; i < 256; i++)
			{
				bRes &= checkDirectory(new File(fBaseDirectory,
						Integer.toHexString(i)));
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
	private boolean checkDirectory(final File fDirectory)
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
		_pictureCache.invalidateAll();
	}

	@Override
	public IPictureBrowser<FsPicture> getPictureIterator()
	{
		return new PictureIterator();
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
		 */
		public PictureIterator()
		{
			super(SQLFileDataSource.this, SQLFileDataSource.this._bus,
					_pictureIdList.listIterator());
		}

		@Override
		protected FsPicture getPicture(final String pictureId)
				throws ExecutionException
		{
			return _pictureCache.get(_idIterator.previous());
		}
	}
}
