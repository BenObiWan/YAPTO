package yapto.datasource.sqlfile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.OperationNotSupportedException;
import yapto.datasource.sqlfile.config.FsPictureCacheLoaderConfiguration;
import yapto.datasource.sqlfile.config.ISQLFileDataSourceConfiguration;
import yapto.datasource.tag.Tag;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * {@link IDataSource} using an SQLite file to stock the meta-informations, and
 * a standard filesystem for the pictures.
 * 
 * @author benobiwan
 * 
 */
public class SQLFileDataSource implements IDataSource
{
	/**
	 * Logger object.
	 */
	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(SQLFileDataSource.class);

	/**
	 * Name for the 'tag' table.
	 */
	private static final String TAG_TABLE_NAME = "tag";

	/**
	 * Name for the 'id' column of the 'tag' table.
	 */
	private static final String TAG_ID_COLUMN_NAME = "id";

	/**
	 * Name for the 'name' column of the 'tag' table.
	 */
	private static final String TAG_NAME_COLUMN_NAME = "name";

	/**
	 * Name for the 'description' column of the 'tag' table.
	 */
	private static final String TAG_DESCRIPTION_COLUMN_NAME = "description";

	/**
	 * Name for the 'parentId' column of the 'tag' table.
	 */
	private static final String TAG_PARENT_ID_COLUMN_NAME = "parentId";

	/**
	 * Name for the 'selectable' column of the 'tag' table.
	 */
	private static final String TAG_SELECTABLE_COLUMN_NAME = "selectable";

	/**
	 * Name for the 'picture' table.
	 */
	private static final String PICTURE_TABLE_NAME = "picture";

	/**
	 * Name for the 'picture_tag' table.
	 */
	private static final String PICTURE_TAG_TABLE_NAME = "picture_tag";

	/**
	 * Name for the 'tagId' column of the 'picture_tag' table.
	 */
	private static final String PICTURE_TAG_TAG_ID_COLUMN_NAME = "tagId";

	/**
	 * Name for the 'pictureId' column of the 'picture_tag' table.
	 */
	private static final String PICTURE_TAG_PICTURE_ID_COLUMN_NAME = "pictureId";

	/**
	 * Connection to the database.
	 */
	private final Connection _connection;

	/**
	 * Statement to insert a {@link Tag}.
	 */
	private final PreparedStatement _psInsertTag;

	/**
	 * Statement to count the number of {@link IPicture}s having a given
	 * {@link Tag}.
	 */
	private final PreparedStatement _countPicturesByTag;

	/**
	 * Statement to count the total number of {@link IPicture}s in this
	 * {@link SQLFileDataSource}.
	 */
	private final PreparedStatement _countPictures;

	/**
	 * Statement to select all the {@link IPicture}s having a given {@link Tag}.
	 */
	private final PreparedStatement _selectPicturesByTag;

	/**
	 * Set containing all the {@link Tag}s.
	 */
	private final Set<Tag> _tagSet = new TreeSet<Tag>();

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
	private final LoadingCache<String, FsPicture> _pictureCache;

	/**
	 * {@link LoadingCache} used to load the {@link BufferedImage}.
	 */
	private final LoadingCache<File, BufferedImage> _imageCache;

	/**
	 * Creates a new SQLFileDataSource.
	 * 
	 * @param conf
	 *            configuration for this {@link SQLFileDataSource}.
	 * @param cacheLoaderConf
	 *            configuration for the
	 *            {@link FsPictureCacheLoaderConfiguration}.
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
			final FsPictureCacheLoaderConfiguration cacheLoaderConf)
			throws SQLException, ClassNotFoundException, IOException
	{
		_conf = conf;
		Class.forName("org.sqlite.JDBC");
		final CacheLoader<String, FsPicture> pictureLoader = new FsPictureCacheLoader(
				cacheLoaderConf);
		final CacheLoader<File, BufferedImage> imageLoader = new BufferedImageCacheLoader();
		_pictureCache = CacheBuilder.newBuilder().build(pictureLoader);
		_imageCache = CacheBuilder.newBuilder().build(imageLoader);

		_connection = DriverManager.getConnection("jdbc:sqlite:"
				+ _conf.getDatabaseFileName());
		_psInsertTag = _connection.prepareStatement("insert into "
				+ TAG_TABLE_NAME + " (" + TAG_ID_COLUMN_NAME + ", "
				+ TAG_NAME_COLUMN_NAME + ", " + TAG_DESCRIPTION_COLUMN_NAME
				+ ", " + TAG_PARENT_ID_COLUMN_NAME + ", "
				+ TAG_SELECTABLE_COLUMN_NAME + ") values(?, ?, ?, ?, ?)");
		_countPicturesByTag = _connection.prepareStatement("select count("
				+ PICTURE_TAG_PICTURE_ID_COLUMN_NAME + ") FROM "
				+ PICTURE_TAG_TABLE_NAME + " WHERE "
				+ PICTURE_TAG_TAG_ID_COLUMN_NAME + "=?");
		_selectPicturesByTag = _connection.prepareStatement("select "
				+ PICTURE_TAG_PICTURE_ID_COLUMN_NAME + " FROM "
				+ PICTURE_TAG_TABLE_NAME + " WHERE "
				+ PICTURE_TAG_TAG_ID_COLUMN_NAME + "=?");
		_countPictures = _connection.prepareStatement("select count(" + "TODO"
				+ ") FROM " + PICTURE_TABLE_NAME);

		if (!checkAndCreateDirectories())
		{
			throw new IOException(
					"Error creating the required picture directories : "
							+ _conf.getPictureDirectory());
		}
		createTables();
		loadTags();
	}

	@Override
	public int getPictureCount() throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IPictureList filterList(final IPictureFilter filter)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPictureList mergeList(final IPictureList otherList)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPictureList> getParent() throws OperationNotSupportedException
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
				saveTagToDatabase(newTag);
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
	 * Create the tables of this database.
	 * 
	 * @throws SQLException
	 *             if an SQL error occurred during the creation of the tables.
	 */
	private void createTables() throws SQLException
	{
		final Statement statement = _connection.createStatement();
		// Tag table
		statement.executeUpdate("create table " + TAG_TABLE_NAME
				+ " if not exists (" + TAG_ID_COLUMN_NAME + " integer, "
				+ TAG_NAME_COLUMN_NAME + " text, "
				+ TAG_DESCRIPTION_COLUMN_NAME + " text, "
				+ TAG_PARENT_ID_COLUMN_NAME + " integer, "
				+ TAG_SELECTABLE_COLUMN_NAME + " boolean)");
		// picture table

		// picture_tag table
		statement.executeUpdate("create table " + PICTURE_TAG_TABLE_NAME
				+ " if not exists (" + PICTURE_TAG_TAG_ID_COLUMN_NAME
				+ " integer, " + PICTURE_TAG_PICTURE_ID_COLUMN_NAME
				+ " integer)");
	}

	/**
	 * Save the given {@link Tag} to the database.
	 * 
	 * @param tag
	 *            the {@link Tag} to save.
	 * @throws SQLException
	 *             if an SQL error occurred during the saving of the {@link Tag}
	 *             .
	 */
	private void saveTagToDatabase(final Tag tag) throws SQLException
	{
		_psInsertTag.setInt(1, tag.getTagId());
		_psInsertTag.setString(2, tag.getName());
		_psInsertTag.setString(3, tag.getDescription());
		if (tag.getParent() == null)
		{
			_psInsertTag.setInt(4, -1);
		}
		else
		{
			_psInsertTag.setInt(4, tag.getParent().getTagId());
		}
		_psInsertTag.setBoolean(5, tag.isSelectable());
		_psInsertTag.executeUpdate();
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
		final Statement statementLoad = _connection.createStatement();
		final ResultSet resLoad = statementLoad.executeQuery("select "
				+ TAG_ID_COLUMN_NAME + ", " + TAG_NAME_COLUMN_NAME + ", "
				+ TAG_DESCRIPTION_COLUMN_NAME + ", "
				+ TAG_SELECTABLE_COLUMN_NAME + " from " + TAG_TABLE_NAME);
		Tag tag = null;
		Tag parentTag = null;
		while (resLoad.next())
		{
			final int iTagId = resLoad.getInt(TAG_ID_COLUMN_NAME);
			final String strName = resLoad.getString(TAG_NAME_COLUMN_NAME);
			final String strDescription = resLoad
					.getString(TAG_DESCRIPTION_COLUMN_NAME);
			final boolean bSelectable = resLoad
					.getBoolean(TAG_SELECTABLE_COLUMN_NAME);
			tag = new Tag(getId(), iTagId, strName, strDescription, bSelectable);
			_tagSet.add(tag);
			_tagMap.put(Integer.valueOf(iTagId), tag);
		}
		final Statement statementParent = _connection.createStatement();
		final ResultSet resParent = statementParent.executeQuery("select "
				+ TAG_ID_COLUMN_NAME + ", " + TAG_PARENT_ID_COLUMN_NAME
				+ " from " + TAG_TABLE_NAME);
		while (resParent.next())
		{
			final Integer iId = Integer.valueOf(resLoad
					.getInt(TAG_ID_COLUMN_NAME));
			final Integer iParentId = Integer.valueOf(resLoad
					.getInt(TAG_PARENT_ID_COLUMN_NAME));
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
}
