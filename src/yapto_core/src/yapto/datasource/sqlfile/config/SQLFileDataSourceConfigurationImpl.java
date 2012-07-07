package yapto.datasource.sqlfile.config;

import javax.management.MBeanServer;

import yapto.datasource.sqlfile.IBufferedImageCacheLoaderConfiguration;

import common.config.AbstractConfigurationBranch;
import common.config.IConfiguration;
import common.config.InvalidConfigurationException;
import common.config.display.IntegerDisplayType;
import common.config.display.StringDisplayType;
import common.config.leaf.ConfigurationInteger;
import common.config.leaf.ConfigurationString;

/**
 * An implementation of the {@link ISQLFileDataSourceConfiguration} interface.
 * 
 * @author benobiwan
 * 
 */
public class SQLFileDataSourceConfigurationImpl extends
		AbstractConfigurationBranch implements ISQLFileDataSourceConfiguration
{
	/**
	 * Leaf configuring the DataSource id.
	 */
	private final ConfigurationInteger _leafDataSourceId;

	/**
	 * Leaf configuring the file name for the database.
	 */
	private final ConfigurationString _leafDatabaseFileName;

	/**
	 * Leaf configuring the base directory for pictures.
	 */
	protected final ConfigurationString _leafPictureDirectory;

	/**
	 * Leaf configuring the base directory for index.
	 */
	private final ConfigurationString _leafIndexDirectory;

	private final static String DATASOURCE_ID_SHORT_DESC = "DataSource Id";
	private final static String DATASOURCE_ID_LONG_DESC = "Id of this DataSource.";
	private final static String DATASOURCE_ID_INVALID_MESSAGE = "Invalid Id for this DataSource.";
	private final static String DATASBASE_FILENAME_SHORT_DESC = "Database file name";
	private final static String DATASBASE_FILENAME_LONG_DESC = "File name for the database.";
	private final static String DATASBASE_FILENAME_INVALID_MESSAGE = "Invalid file name for the database.";
	private final static String PICTURE_DIRECTORY_SHORT_DESC = "Picture directory";
	private final static String PICTURE_DIRECTORY_LONG_DESC = "Base directory for the pictures.";
	private final static String PICTURE_DIRECTORY_INVALID_MESSAGE = "Invalid base directory for the pictures.";
	private final static String INDEX_DIRECTORY_SHORT_DESC = "Index directory";
	private final static String INDEX_DIRECTORY_LONG_DESC = "Base directory for the indexes.";
	private final static String INDEX_DIRECTORY_INVALID_MESSAGE = "Invalid base directory for the indexes.";

	private final IBufferedImageCacheLoaderConfiguration _pictureCacheLoaderConfiguration;

	/**
	 * Creates a new SQLFileDataSourceConfigurationImpl using default values for
	 * every elements.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 */
	public SQLFileDataSourceConfigurationImpl(final IConfiguration parent,
			final MBeanServer mBeanServer)
	{
		super(parent, SQLFILE_DATASOURCE_CONFIGURATION_TAG, mBeanServer);

		_leafDataSourceId = new ConfigurationInteger(this, DATASOURCE_ID_TAG,
				DATASOURCE_ID_SHORT_DESC, DATASOURCE_ID_LONG_DESC,
				DATASOURCE_ID_INVALID_MESSAGE, false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		_leafDatabaseFileName = new ConfigurationString(parent,
				DATABASE_FILENAME_TAG, DATASBASE_FILENAME_SHORT_DESC,
				DATASBASE_FILENAME_LONG_DESC,
				DATASBASE_FILENAME_INVALID_MESSAGE, false,
				StringDisplayType.TEXTFIELD, 0, "");
		_leafPictureDirectory = new ConfigurationString(parent,
				PICTURE_DIRECTORY_TAG, PICTURE_DIRECTORY_SHORT_DESC,
				PICTURE_DIRECTORY_LONG_DESC, PICTURE_DIRECTORY_INVALID_MESSAGE,
				false, StringDisplayType.TEXTFIELD, 0, "");
		_leafIndexDirectory = new ConfigurationString(parent,
				INDEX_DIRECTORY_TAG, INDEX_DIRECTORY_SHORT_DESC,
				INDEX_DIRECTORY_LONG_DESC, INDEX_DIRECTORY_INVALID_MESSAGE,
				false, StringDisplayType.TEXTFIELD, 0, "");
		addLeaf(_leafDataSourceId);
		addLeaf(_leafDatabaseFileName);
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafIndexDirectory);
		_pictureCacheLoaderConfiguration = new IPictureLoaderConfigurationImpl(
				this, mBeanServer);
	}

	/**
	 * Creates a new SQLFileDataSourceConfigurationImpl using default values for
	 * every elements.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param iCommandLineDataSourceId
	 *            the value specified on the command line for the DataSource id.
	 * @param strCommandLineDatabaseFileName
	 *            the value specified on the command line for the file name of
	 *            the database.
	 * @param strCommandLinePictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for pictures.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFileDataSourceConfigurationImpl(final IConfiguration parent,
			final MBeanServer mBeanServer,
			final Integer iCommandLineDataSourceId,
			final String strCommandLineDatabaseFileName,
			final String strCommandLinePictureDirectory,
			final String strCommandLineIndexDirectory)
			throws InvalidConfigurationException
	{
		super(parent, SQLFILE_DATASOURCE_CONFIGURATION_TAG, mBeanServer);
		_leafDataSourceId = new ConfigurationInteger(this, DATASOURCE_ID_TAG,
				DATASOURCE_ID_SHORT_DESC, DATASOURCE_ID_LONG_DESC,
				DATASOURCE_ID_INVALID_MESSAGE, false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLineDataSourceId);
		_leafDatabaseFileName = new ConfigurationString(parent,
				DATABASE_FILENAME_TAG, DATASBASE_FILENAME_SHORT_DESC,
				DATASBASE_FILENAME_LONG_DESC,
				DATASBASE_FILENAME_INVALID_MESSAGE, false,
				StringDisplayType.TEXTFIELD, 0, "",
				strCommandLineDatabaseFileName);
		_leafPictureDirectory = new ConfigurationString(parent,
				PICTURE_DIRECTORY_TAG, PICTURE_DIRECTORY_SHORT_DESC,
				PICTURE_DIRECTORY_LONG_DESC, PICTURE_DIRECTORY_INVALID_MESSAGE,
				false, StringDisplayType.TEXTFIELD, 0, "",
				strCommandLinePictureDirectory);
		_leafIndexDirectory = new ConfigurationString(parent,
				INDEX_DIRECTORY_TAG, INDEX_DIRECTORY_SHORT_DESC,
				INDEX_DIRECTORY_LONG_DESC, INDEX_DIRECTORY_INVALID_MESSAGE,
				false, StringDisplayType.TEXTFIELD, 0, "",
				strCommandLineIndexDirectory);
		addLeaf(_leafDataSourceId);
		addLeaf(_leafDatabaseFileName);
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafIndexDirectory);
		_pictureCacheLoaderConfiguration = new IPictureLoaderConfigurationImpl(
				this, mBeanServer);
	}

	/**
	 * Creates a new SQLFileDataSourceConfigurationImpl using default values for
	 * every elements.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param iCommandLineDataSourceId
	 *            the value specified on the command line for the DataSource id.
	 * @param strCommandLineDatabaseFileName
	 *            the value specified on the command line for the file name of
	 *            the database.
	 * @param strCommandLinePictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for pictures.
	 * @param iConfigurationDataSourceId
	 *            the value specified in the configuration file for the
	 *            DataSource id.
	 * @param strConfigurationDatabaseFileName
	 *            the value specified in the configuration file for the file
	 *            name of the database.
	 * @param strConfigurationPictureDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for pictures.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFileDataSourceConfigurationImpl(final IConfiguration parent,
			final MBeanServer mBeanServer,
			final Integer iCommandLineDataSourceId,
			final String strCommandLineDatabaseFileName,
			final String strCommandLinePictureDirectory,
			final String strCommandLineIndexDirectory,
			final Integer iConfigurationDataSourceId,
			final String strConfigurationDatabaseFileName,
			final String strConfigurationPictureDirectory,
			final String strConfigurationIndexDirectory)
			throws InvalidConfigurationException
	{
		this(parent, mBeanServer, iCommandLineDataSourceId,
				strCommandLineDatabaseFileName, strCommandLinePictureDirectory,
				strCommandLineIndexDirectory);
		_leafDataSourceId.setConfigurationValue(iConfigurationDataSourceId);
		_leafDatabaseFileName
				.setConfigurationValue(strConfigurationDatabaseFileName);
		_leafPictureDirectory
				.setConfigurationValue(strConfigurationPictureDirectory);
		_leafIndexDirectory
				.setConfigurationValue(strConfigurationIndexDirectory);
	}

	@Override
	public String getDescription()
	{
		return "Configuration for the DataSource based on an SQLite file for the metadatas, and a filesystem for the pictures.";
	}

	@Override
	public String getDatabaseFileName()
	{
		return _leafDatabaseFileName.getCurrentValue();
	}

	@Override
	public int getDataSourceId()
	{
		return _leafDataSourceId.getCurrentValue().intValue();
	}

	@Override
	public String getIndexDirectory()
	{
		return _leafIndexDirectory.getCurrentValue();
	}

	@Override
	public IBufferedImageCacheLoaderConfiguration getMainPictureLoaderConfiguration()
	{
		return _pictureCacheLoaderConfiguration;
	}

	private final class IPictureLoaderConfigurationImpl extends
			AbstractConfigurationBranch implements
			IBufferedImageCacheLoaderConfiguration
	{
		public IPictureLoaderConfigurationImpl(final IConfiguration parent,
				final MBeanServer mBeanServer)
		{
			super(parent, "", mBeanServer);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getDescription()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPictureDirectory()
		{
			return _leafPictureDirectory.getCurrentValue();
		}
	}

	private final class IThumbnailLoaderConfigurationImpl extends
			AbstractConfigurationBranch implements
			IBufferedImageCacheLoaderConfiguration
	{
		public IThumbnailLoaderConfigurationImpl(final IConfiguration parent,
				final MBeanServer mBeanServer)
		{
			super(parent, "", mBeanServer);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getDescription()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPictureDirectory()
		{
			return _leafPictureDirectory.getCurrentValue();
		}
	}
}
