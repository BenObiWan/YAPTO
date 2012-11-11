package yapto.datasource.sqlfile.config;

import javax.management.MBeanServer;

import yapto.datasource.process.IdentifyTask;
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
	 * Leaf configuring the base directory for thumbnails.
	 */
	protected final ConfigurationString _leafThumbnailsDirectory;

	/**
	 * Leaf configuring the base directory for index.
	 */
	private final ConfigurationString _leafIndexDirectory;

	/**
	 * Leaf configuring the maximum number of {@link IdentifyTask} to run at the
	 * same time.
	 */
	private final ConfigurationInteger _leafMaxIdentifyTask;

	/**
	 * Leaf configuring the maximum number of task other than
	 * {@link IdentifyTask} to run at the same time.
	 */
	private final ConfigurationInteger _leafMaxOtherTask;

	/**
	 * Leaf configuring the minimum number of seconds to wait between picture
	 * modification and saving to the database.
	 */
	private final ConfigurationInteger _leafWaitBeforeWrite;

	/**
	 * Short description for the datasource id.
	 */
	private final static String DATASOURCE_ID_SHORT_DESC = "DataSource Id";

	/**
	 * Long description for the datasource id.
	 */
	private final static String DATASOURCE_ID_LONG_DESC = "Id of this DataSource.";

	/**
	 * Invalid message for the datasource id.
	 */
	private final static String DATASOURCE_ID_INVALID_MESSAGE = "Invalid Id for this DataSource.";

	/**
	 * Short description for the database file name id.
	 */
	private final static String DATASBASE_FILENAME_SHORT_DESC = "Database file name";

	/**
	 * Long description for the database file name id.
	 */
	private final static String DATASBASE_FILENAME_LONG_DESC = "File name for the database.";

	/**
	 * Invalid message for the database file name id.
	 */
	private final static String DATASBASE_FILENAME_INVALID_MESSAGE = "Invalid file name for the database.";

	/**
	 * Short description for the picture directory.
	 */
	private final static String PICTURE_DIRECTORY_SHORT_DESC = "Picture directory";

	/**
	 * Long description for the picture directory.
	 */
	private final static String PICTURE_DIRECTORY_LONG_DESC = "Base directory for the pictures.";

	/**
	 * Invalid message for the picture directory.
	 */
	private final static String PICTURE_DIRECTORY_INVALID_MESSAGE = "Invalid base directory for the pictures.";

	/**
	 * Short description for the thumbnails directory.
	 */
	private final static String THUMBNAILS_DIRECTORY_SHORT_DESC = "Thumbnails directory";

	/**
	 * Long description for the thumbnails directory.
	 */
	private final static String THUMBNAILS_DIRECTORY_LONG_DESC = "Base directory for the thumbnails.";

	/**
	 * Invalid message for the thumbnails directory.
	 */
	private final static String THUMBNAILS_DIRECTORY_INVALID_MESSAGE = "Invalid base directory for the thumbnails.";

	/**
	 * Short description for the index directory.
	 */
	private final static String INDEX_DIRECTORY_SHORT_DESC = "Index directory";

	/**
	 * Long description for the index directory.
	 */
	private final static String INDEX_DIRECTORY_LONG_DESC = "Base directory for the indexes.";

	/**
	 * Invalid message for the index directory.
	 */
	private final static String INDEX_DIRECTORY_INVALID_MESSAGE = "Invalid base directory for the indexes.";

	/**
	 * Short description for the maximum number of identify task.
	 */
	private final static String MAX_IDENTIFY_TASK_SHORT_DESC = "Maximum number of identify task.";

	/**
	 * Long description for the maximum number of identify task.
	 */
	private final static String MAX_IDENTIFY_TASK_LONG_DESC = "Maximum number of concurrent identify task.";

	/**
	 * Invalid message for the maximum number of identify task.
	 */
	private final static String MAX_IDENTIFY_TASK_INVALID_MESSAGE = "Invalid maximum number of identify task.";

	/**
	 * Short description for the maximum number of other task.
	 */
	private final static String MAX_OTHER_TASK_SHORT_DESC = "Maximum number of other task.";

	/**
	 * Long description for the maximum number of other task.
	 */
	private final static String MAX_OTHER_TASK_LONG_DESC = "Maximum number of concurrent task other than  identify task.";

	/**
	 * Invalid message for the maximum number of identify task.
	 */
	private final static String MAX_OTHER_TASK_INVALID_MESSAGE = "Invalid maximum number of other task.";

	/**
	 * Short description for the time waited before writing.
	 */
	private final static String WAIT_BEFORE_WRITE_SHORT_DESC = "Time waited before writing.";

	/**
	 * Long description for the time waited before writing.
	 */
	private final static String WAIT_BEFORE_WRITE_LONG_DESC = "Time waited before writing picture metadata when it's modified.";

	/**
	 * Invalid message for the time waited before writing.
	 */
	private final static String WAIT_BEFORE_WRITE_INVALID_MESSAGE = "Invalid waited before writing.";

	/**
	 * {@link IBufferedImageCacheLoaderConfiguration} for the pictures.
	 */
	private final IBufferedImageCacheLoaderConfiguration _pictureCacheLoaderConfiguration;

	/**
	 * {@link IBufferedImageCacheLoaderConfiguration} for the thumbnails.
	 */
	private final IBufferedImageCacheLoaderConfiguration _thumbnailCacheLoaderConfiguration;

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
		_leafThumbnailsDirectory = new ConfigurationString(parent,
				THUMBNAILS_DIRECTORY_TAG, THUMBNAILS_DIRECTORY_SHORT_DESC,
				THUMBNAILS_DIRECTORY_LONG_DESC,
				THUMBNAILS_DIRECTORY_INVALID_MESSAGE, false,
				StringDisplayType.TEXTFIELD, 0, "");
		_leafIndexDirectory = new ConfigurationString(parent,
				INDEX_DIRECTORY_TAG, INDEX_DIRECTORY_SHORT_DESC,
				INDEX_DIRECTORY_LONG_DESC, INDEX_DIRECTORY_INVALID_MESSAGE,
				false, StringDisplayType.TEXTFIELD, 0, "");
		_leafMaxIdentifyTask = new ConfigurationInteger(this,
				MAX_IDENTIFY_TASK_TAG, MAX_IDENTIFY_TASK_SHORT_DESC,
				MAX_IDENTIFY_TASK_LONG_DESC, MAX_IDENTIFY_TASK_INVALID_MESSAGE,
				false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		_leafMaxOtherTask = new ConfigurationInteger(this, MAX_OTHER_TASK_TAG,
				MAX_OTHER_TASK_SHORT_DESC, MAX_OTHER_TASK_LONG_DESC,
				MAX_OTHER_TASK_INVALID_MESSAGE, false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		_leafWaitBeforeWrite = new ConfigurationInteger(this,
				WAIT_BEFORE_WRITE_TAG, WAIT_BEFORE_WRITE_SHORT_DESC,
				WAIT_BEFORE_WRITE_LONG_DESC, WAIT_BEFORE_WRITE_INVALID_MESSAGE,
				false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		addLeaf(_leafDataSourceId);
		addLeaf(_leafDatabaseFileName);
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafIndexDirectory);
		addLeaf(_leafThumbnailsDirectory);
		addLeaf(_leafMaxIdentifyTask);
		addLeaf(_leafMaxOtherTask);
		addLeaf(_leafWaitBeforeWrite);
		_pictureCacheLoaderConfiguration = new PictureLoaderConfigurationImpl();
		_thumbnailCacheLoaderConfiguration = new ThumbnailLoaderConfigurationImpl();
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
	 * @param strCommandLineThumbnailsDirectory
	 *            the value specified on the command line for the base directory
	 *            for thumbnails.
	 * @param strCommandLineIndexDirectory
	 *            the value specified on the command line for the base directory
	 *            for indexes.
	 * @param iCommandLineMaxIdentifyTask
	 *            the value specified on the command line for the maximum number
	 *            of {@link IdentifyTask} to run at the same time.
	 * @param iCommandLineMaxOtherTask
	 *            the value specified on the command line for the maximum number
	 *            of task other than {@link IdentifyTask} to run at the same
	 *            time.
	 * @param iCommandLineWaitBeforeWrite
	 *            the value specified on the command line for the minimum number
	 *            of seconds to wait between picture modification and saving to
	 *            the database.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFileDataSourceConfigurationImpl(final IConfiguration parent,
			final MBeanServer mBeanServer,
			final Integer iCommandLineDataSourceId,
			final String strCommandLineDatabaseFileName,
			final String strCommandLinePictureDirectory,
			final String strCommandLineThumbnailsDirectory,
			final String strCommandLineIndexDirectory,
			final Integer iCommandLineMaxIdentifyTask,
			final Integer iCommandLineMaxOtherTask,
			final Integer iCommandLineWaitBeforeWrite)
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
		_leafThumbnailsDirectory = new ConfigurationString(parent,
				THUMBNAILS_DIRECTORY_TAG, THUMBNAILS_DIRECTORY_SHORT_DESC,
				THUMBNAILS_DIRECTORY_LONG_DESC,
				THUMBNAILS_DIRECTORY_INVALID_MESSAGE, false,
				StringDisplayType.TEXTFIELD, 0, "",
				strCommandLineThumbnailsDirectory);
		_leafIndexDirectory = new ConfigurationString(parent,
				INDEX_DIRECTORY_TAG, INDEX_DIRECTORY_SHORT_DESC,
				INDEX_DIRECTORY_LONG_DESC, INDEX_DIRECTORY_INVALID_MESSAGE,
				false, StringDisplayType.TEXTFIELD, 0, "",
				strCommandLineIndexDirectory);
		_leafMaxIdentifyTask = new ConfigurationInteger(this,
				MAX_IDENTIFY_TASK_TAG, MAX_IDENTIFY_TASK_SHORT_DESC,
				MAX_IDENTIFY_TASK_LONG_DESC, MAX_IDENTIFY_TASK_INVALID_MESSAGE,
				false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLineMaxIdentifyTask);
		_leafMaxOtherTask = new ConfigurationInteger(this, MAX_OTHER_TASK_TAG,
				MAX_OTHER_TASK_SHORT_DESC, MAX_OTHER_TASK_LONG_DESC,
				MAX_OTHER_TASK_INVALID_MESSAGE, false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLineMaxOtherTask);
		_leafWaitBeforeWrite = new ConfigurationInteger(this,
				WAIT_BEFORE_WRITE_TAG, WAIT_BEFORE_WRITE_SHORT_DESC,
				WAIT_BEFORE_WRITE_LONG_DESC, WAIT_BEFORE_WRITE_INVALID_MESSAGE,
				false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLineWaitBeforeWrite);
		addLeaf(_leafDataSourceId);
		addLeaf(_leafDatabaseFileName);
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafIndexDirectory);
		addLeaf(_leafThumbnailsDirectory);
		addLeaf(_leafMaxIdentifyTask);
		addLeaf(_leafMaxOtherTask);
		addLeaf(_leafWaitBeforeWrite);
		_pictureCacheLoaderConfiguration = new PictureLoaderConfigurationImpl();
		_thumbnailCacheLoaderConfiguration = new ThumbnailLoaderConfigurationImpl();
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
	 * @param strCommandLineThumbnailsDirectory
	 *            the value specified on the command line for the base directory
	 *            for thumbnails.
	 * @param strCommandLineIndexDirectory
	 *            the value specified on the command line for the base directory
	 * @param iCommandLineMaxIdentifyTask
	 *            the value specified on the command line for the maximum number
	 *            of {@link IdentifyTask} to run at the same time.
	 * @param iCommandLineMaxOtherTask
	 *            the value specified on the command line for the maximum number
	 *            of task other than {@link IdentifyTask} to run at the same
	 *            time.
	 * @param iCommandLineWaitBeforeWrite
	 *            the value specified on the command line for the minimum number
	 *            of seconds to wait between picture modification and saving to
	 *            the database.
	 * @param iConfigurationDataSourceId
	 *            the value specified in the configuration file for the
	 *            DataSource id.
	 * @param strConfigurationDatabaseFileName
	 *            the value specified in the configuration file for the file
	 *            name of the database.
	 * @param strConfigurationPictureDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for pictures.
	 * @param strConfigurationThumbnailsDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for thumbnails.
	 * @param strConfigurationIndexDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for indexes.
	 * @param iConfigurationMaxIdentifyTask
	 *            the value specified in the configuration file for the maximum
	 *            number of {@link IdentifyTask} to run at the same time.
	 * @param iConfigurationMaxOtherTask
	 *            the value specified in the configuration file for the maximum
	 *            number of task other than {@link IdentifyTask} to run at the
	 *            same time.
	 * @param iConfigurationWaitBeforeWrite
	 *            the value specified in the configuration file for the minimum
	 *            number of seconds to wait between picture modification and
	 *            saving to the database.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFileDataSourceConfigurationImpl(final IConfiguration parent,
			final MBeanServer mBeanServer,
			final Integer iCommandLineDataSourceId,
			final String strCommandLineDatabaseFileName,
			final String strCommandLinePictureDirectory,
			final String strCommandLineThumbnailsDirectory,
			final String strCommandLineIndexDirectory,
			final Integer iCommandLineMaxIdentifyTask,
			final Integer iCommandLineMaxOtherTask,
			final Integer iCommandLineWaitBeforeWrite,
			final Integer iConfigurationDataSourceId,
			final String strConfigurationDatabaseFileName,
			final String strConfigurationPictureDirectory,
			final String strConfigurationThumbnailsDirectory,
			final String strConfigurationIndexDirectory,
			final Integer iConfigurationMaxIdentifyTask,
			final Integer iConfigurationMaxOtherTask,
			final Integer iConfigurationWaitBeforeWrite)
			throws InvalidConfigurationException
	{
		this(parent, mBeanServer, iCommandLineDataSourceId,
				strCommandLineDatabaseFileName, strCommandLinePictureDirectory,
				strCommandLineThumbnailsDirectory,
				strCommandLineIndexDirectory, iCommandLineMaxIdentifyTask,
				iCommandLineMaxOtherTask, iCommandLineWaitBeforeWrite);
		_leafDataSourceId.setConfigurationValue(iConfigurationDataSourceId);
		_leafDatabaseFileName
				.setConfigurationValue(strConfigurationDatabaseFileName);
		_leafPictureDirectory
				.setConfigurationValue(strConfigurationPictureDirectory);
		_leafIndexDirectory
				.setConfigurationValue(strConfigurationIndexDirectory);
		_leafThumbnailsDirectory
				.setConfigurationValue(strConfigurationThumbnailsDirectory);
		_leafMaxIdentifyTask
				.setConfigurationValue(iConfigurationMaxIdentifyTask);
		_leafMaxOtherTask.setConfigurationValue(iConfigurationMaxOtherTask);
		_leafWaitBeforeWrite
				.setConfigurationValue(iConfigurationWaitBeforeWrite);
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

	@Override
	public IBufferedImageCacheLoaderConfiguration getThumbnailPictureLoaderConfiguration()
	{
		return _thumbnailCacheLoaderConfiguration;
	}

	/**
	 * Implementation of {@link IBufferedImageCacheLoaderConfiguration}.
	 * 
	 * @author benobiwan
	 * 
	 */
	private final class PictureLoaderConfigurationImpl extends
			AbstractConfigurationBranch implements
			IBufferedImageCacheLoaderConfiguration
	{
		/**
		 * Creates a new PictureLoaderConfigurationImpl.
		 */
		public PictureLoaderConfigurationImpl()
		{
			super(SQLFileDataSourceConfigurationImpl.this, "",
					SQLFileDataSourceConfigurationImpl.this.getMBeanServer());
		}

		@Override
		public String getDescription()
		{
			return "";
		}

		@Override
		public String getPictureDirectory()
		{
			return _leafPictureDirectory.getCurrentValue();
		}
	}

	/**
	 * Implementation of {@link IBufferedImageCacheLoaderConfiguration}.
	 * 
	 * @author benobiwan
	 * 
	 */
	private final class ThumbnailLoaderConfigurationImpl extends
			AbstractConfigurationBranch implements
			IBufferedImageCacheLoaderConfiguration
	{
		/**
		 * Creates a new ThumbnailLoaderConfigurationImpl.
		 */
		public ThumbnailLoaderConfigurationImpl()
		{
			super(SQLFileDataSourceConfigurationImpl.this, "",
					SQLFileDataSourceConfigurationImpl.this.getMBeanServer());
		}

		@Override
		public String getDescription()
		{
			return "";
		}

		@Override
		public String getPictureDirectory()
		{
			return _leafThumbnailsDirectory.getCurrentValue();
		}
	}

	@Override
	public int getMaxConcurrentIdentifyTask()
	{
		return _leafMaxIdentifyTask.getCurrentValue().intValue();
	}

	@Override
	public int getMaxConcurrentOtherTask()
	{
		return _leafMaxOtherTask.getCurrentValue().intValue();
	}

	@Override
	public int getWaitBeforeWrite()
	{
		return _leafWaitBeforeWrite.getCurrentValue().intValue();
	}
}
