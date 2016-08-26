package yapto.picturebank.sqlfile.config;

import javax.management.MBeanServer;

import yapto.picturebank.IPictureBank;
import yapto.picturebank.config.IPictureBankConfiguration;
import yapto.picturebank.tag.ITag;
import common.config.AbstractConfigurationBranch;
import common.config.IConfiguration;
import common.config.InvalidConfigurationException;
import common.config.db.IDatabaseConfiguration;
import common.config.db.SQLiteConfigurationImpl;
import common.config.display.IntegerDisplayType;
import common.config.display.StringDisplayType;
import common.config.leaf.ConfigurationInteger;
import common.config.leaf.ConfigurationString;

/**
 * An implementation of the {@link ISQLFilePictureBankConfiguration} interface.
 * 
 * @author benobiwan
 * 
 */
public final class SQLFilePictureBankConfigurationImpl extends AbstractConfigurationBranch
		implements ISQLFilePictureBankConfiguration
{
	/**
	 * Leaf configuring the {@link IPictureBank} id.
	 */
	private final ConfigurationInteger _leafPictureBankId;

	/**
	 * Leaf configuring the {@link IPictureBank} name.
	 */
	private final ConfigurationString _leafPictureBankName;

	/**
	 * Leaf configuring the base directory for pictures.
	 */
	protected final ConfigurationString _leafPictureDirectory;

	/**
	 * Leaf configuring the base directory for secondary pictures.
	 */
	protected final ConfigurationString _leafSecondaryPictureDirectory;

	/**
	 * Leaf configuring the base directory for thumbnails.
	 */
	protected final ConfigurationString _leafThumbnailsDirectory;

	/**
	 * Leaf configuring the base directory for index.
	 */
	private final ConfigurationString _leafIndexDirectory;

	/**
	 * Leaf configuring the {@link ITag} history size.
	 */
	private final ConfigurationInteger _leafTagHistorySize;

	/**
	 * Short description for the {@link IPictureBank} id.
	 */
	private final static String PICTUREBANK_ID_SHORT_DESC = "PictureBank Id";

	/**
	 * Long description for the {@link IPictureBank} id.
	 */
	private final static String PICTUREBANK_ID_LONG_DESC = "Id of this PictureBank.";

	/**
	 * Invalid message for the {@link IPictureBank} id.
	 */
	private final static String PICTUREBANK_ID_INVALID_MESSAGE = "Invalid Id for this PictureBank.";

	/**
	 * Short description for the {@link IPictureBank} name.
	 */
	private final static String PICTUREBANK_NAME_SHORT_DESC = "PictureBank Name";

	/**
	 * Long description for the {@link IPictureBank} name.
	 */
	private final static String PICTUREBANK_NAME_LONG_DESC = "Name of this PictureBank.";

	/**
	 * Invalid message for the {@link IPictureBank} name.
	 */
	private final static String PICTUREBANK_NAME_INVALID_MESSAGE = "Invalid name for this PictureBank.";

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
	 * Short description for the picture directory.
	 */
	private final static String SECONDARY_PICTURE_DIRECTORY_SHORT_DESC = "Secondary picture directory";

	/**
	 * Long description for the picture directory.
	 */
	private final static String SECONDARY_PICTURE_DIRECTORY_LONG_DESC = "Base directory for the secondary pictures.";

	/**
	 * Invalid message for the picture directory.
	 */
	private final static String SECONDARY_PICTURE_DIRECTORY_INVALID_MESSAGE = "Invalid base directory for the secondary pictures.";

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
	 * Short description for the index directory.
	 */
	private final static String TAG_HISTORY_SHORT_DESC = "Tag history size";

	/**
	 * Long description for the index directory.
	 */
	private final static String TAG_HISTORY_LONG_DESC = "Size of the tag history.";

	/**
	 * Invalid message for the index directory.
	 */
	private final static String TAG_HISTORY_INVALID_MESSAGE = "Invalid size of the tag history.";

	/**
	 * {@link IBufferedImageCacheLoaderConfiguration} for the pictures.
	 */
	private final IBufferedImageCacheLoaderConfiguration _pictureCacheLoaderConfiguration;

	/**
	 * {@link IBufferedImageCacheLoaderConfiguration} for the secondary
	 * pictures.
	 */
	private final IBufferedImageCacheLoaderConfiguration _secondaryPictureCacheLoaderConfiguration;

	/**
	 * {@link IBufferedImageCacheLoaderConfiguration} for the thumbnails.
	 */
	private final IBufferedImageCacheLoaderConfiguration _thumbnailCacheLoaderConfiguration;

	/**
	 * {@link IDatabaseConfiguration} for the connection to the database.
	 */
	private final IDatabaseConfiguration _databaseConfiguration;

	/**
	 * Creates a new SQLFilePictureBankConfigurationImpl using default values
	 * for every elements.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 */
	public SQLFilePictureBankConfigurationImpl(final IConfiguration parent, final MBeanServer mBeanServer)
	{
		super(parent, SQLFILE_PICTUREBANK_CONFIGURATION_TAG, mBeanServer);
		_leafPictureBankId = new ConfigurationInteger(this, PICTUREBANK_ID_TAG, PICTUREBANK_ID_SHORT_DESC,
				PICTUREBANK_ID_LONG_DESC, PICTUREBANK_ID_INVALID_MESSAGE, false, IntegerDisplayType.SPINNER,
				Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		_leafPictureBankName = new ConfigurationString(this, PICTUREBANK_NAME_TAG, PICTUREBANK_NAME_SHORT_DESC,
				PICTUREBANK_NAME_LONG_DESC, PICTUREBANK_NAME_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0,
				"");
		_leafPictureDirectory = new ConfigurationString(this, PICTURE_DIRECTORY_TAG, PICTURE_DIRECTORY_SHORT_DESC,
				PICTURE_DIRECTORY_LONG_DESC, PICTURE_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0,
				"");
		_leafSecondaryPictureDirectory = new ConfigurationString(this, SECONDARY_PICTURE_DIRECTORY_TAG,
				SECONDARY_PICTURE_DIRECTORY_SHORT_DESC, SECONDARY_PICTURE_DIRECTORY_LONG_DESC,
				SECONDARY_PICTURE_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0, "");
		_leafThumbnailsDirectory = new ConfigurationString(this, THUMBNAILS_DIRECTORY_TAG,
				THUMBNAILS_DIRECTORY_SHORT_DESC, THUMBNAILS_DIRECTORY_LONG_DESC, THUMBNAILS_DIRECTORY_INVALID_MESSAGE,
				false, StringDisplayType.TEXTFIELD, 0, "");
		_leafIndexDirectory = new ConfigurationString(this, INDEX_DIRECTORY_TAG, INDEX_DIRECTORY_SHORT_DESC,
				INDEX_DIRECTORY_LONG_DESC, INDEX_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0, "");
		_leafTagHistorySize = new ConfigurationInteger(this, TAG_HISTORY_TAG, TAG_HISTORY_SHORT_DESC,
				TAG_HISTORY_LONG_DESC, TAG_HISTORY_INVALID_MESSAGE, false, IntegerDisplayType.SPINNER,
				Integer.valueOf(0), Integer.valueOf(100), Integer.valueOf(25));
		addLeaf(_leafPictureBankId);
		addLeaf(_leafPictureBankName);
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafSecondaryPictureDirectory);
		addLeaf(_leafIndexDirectory);
		addLeaf(_leafThumbnailsDirectory);
		addLeaf(_leafTagHistorySize);
		_pictureCacheLoaderConfiguration = new PictureLoaderConfigurationImpl();
		_secondaryPictureCacheLoaderConfiguration = new SecondaryPictureLoaderConfigurationImpl();
		_thumbnailCacheLoaderConfiguration = new ThumbnailLoaderConfigurationImpl();
		_databaseConfiguration = new SQLiteConfigurationImpl(this, mBeanServer);
	}

	/**
	 * Creates a new SQLFilePictureBankConfigurationImpl with values coming from
	 * the command line.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param iCommandLinePictureBankId
	 *            the value specified on the command line for the
	 *            {@link IPictureBank} id.
	 * @param strCommandLinePictureBankName
	 *            the value specified on the command line for the
	 *            {@link IPictureBank} name.
	 * @param strCommandLineDatabaseConnection
	 *            the value specified on the command line for the file name of
	 *            the database.
	 * @param strCommandLinePictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for pictures.
	 * @param strCommandLineSecondaryPictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for secondary pictures.
	 * @param strCommandLineThumbnailsDirectory
	 *            the value specified on the command line for the base directory
	 *            for thumbnails.
	 * @param strCommandLineIndexDirectory
	 *            the value specified on the command line for the base directory
	 *            for indexes.
	 * @param iCommandLineTagHistorySize
	 *            the value specified on the command line for the tag history
	 *            size.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFilePictureBankConfigurationImpl(final IConfiguration parent, final MBeanServer mBeanServer,
			final Integer iCommandLinePictureBankId, final String strCommandLinePictureBankName,
			final String strCommandLineDatabaseConnection, final String strCommandLinePictureDirectory,
			final String strCommandLineSecondaryPictureDirectory, final String strCommandLineThumbnailsDirectory,
			final String strCommandLineIndexDirectory, final Integer iCommandLineTagHistorySize)
			throws InvalidConfigurationException
	{
		super(parent, SQLFILE_PICTUREBANK_CONFIGURATION_TAG, mBeanServer);
		_leafPictureBankId = new ConfigurationInteger(this, PICTUREBANK_ID_TAG, PICTUREBANK_ID_SHORT_DESC,
				PICTUREBANK_ID_LONG_DESC, PICTUREBANK_ID_INVALID_MESSAGE, false, IntegerDisplayType.SPINNER,
				Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0), iCommandLinePictureBankId);
		_leafPictureBankName = new ConfigurationString(this, PICTUREBANK_NAME_TAG, PICTUREBANK_NAME_SHORT_DESC,
				PICTUREBANK_NAME_LONG_DESC, PICTUREBANK_NAME_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0, "",
				strCommandLinePictureBankName);
		_leafPictureDirectory = new ConfigurationString(this, PICTURE_DIRECTORY_TAG, PICTURE_DIRECTORY_SHORT_DESC,
				PICTURE_DIRECTORY_LONG_DESC, PICTURE_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0,
				"", strCommandLinePictureDirectory);
		_leafSecondaryPictureDirectory = new ConfigurationString(this, SECONDARY_PICTURE_DIRECTORY_TAG,
				SECONDARY_PICTURE_DIRECTORY_SHORT_DESC, SECONDARY_PICTURE_DIRECTORY_LONG_DESC,
				SECONDARY_PICTURE_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0,
				strCommandLineSecondaryPictureDirectory);
		_leafThumbnailsDirectory = new ConfigurationString(this, THUMBNAILS_DIRECTORY_TAG,
				THUMBNAILS_DIRECTORY_SHORT_DESC, THUMBNAILS_DIRECTORY_LONG_DESC, THUMBNAILS_DIRECTORY_INVALID_MESSAGE,
				false, StringDisplayType.TEXTFIELD, 0, "", strCommandLineThumbnailsDirectory);
		_leafIndexDirectory = new ConfigurationString(this, INDEX_DIRECTORY_TAG, INDEX_DIRECTORY_SHORT_DESC,
				INDEX_DIRECTORY_LONG_DESC, INDEX_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0, "",
				strCommandLineIndexDirectory);
		_leafTagHistorySize = new ConfigurationInteger(this, TAG_HISTORY_TAG, TAG_HISTORY_SHORT_DESC,
				TAG_HISTORY_LONG_DESC, TAG_HISTORY_INVALID_MESSAGE, false, IntegerDisplayType.SPINNER,
				Integer.valueOf(0), Integer.valueOf(100), Integer.valueOf(25), iCommandLineTagHistorySize);
		addLeaf(_leafPictureBankId);
		addLeaf(_leafPictureBankName);
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafIndexDirectory);
		addLeaf(_leafThumbnailsDirectory);
		addLeaf(_leafTagHistorySize);
		_pictureCacheLoaderConfiguration = new PictureLoaderConfigurationImpl();
		_secondaryPictureCacheLoaderConfiguration = new SecondaryPictureLoaderConfigurationImpl();
		_thumbnailCacheLoaderConfiguration = new ThumbnailLoaderConfigurationImpl();
		_databaseConfiguration = new SQLiteConfigurationImpl(this, mBeanServer, strCommandLineDatabaseConnection);
	}

	/**
	 * Creates a new SQLFilePictureBankConfigurationImpl with values coming from
	 * the command line and from a configuration file.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param iCommandLinePictureBankId
	 *            the value specified on the command line for the
	 *            {@link IPictureBank} id.
	 * @param strCommandLinePictureBankName
	 *            the value specified on the command line for the
	 *            {@link IPictureBank} name.
	 * @param strCommandLineDatabaseFileName
	 *            the value specified on the command line for the file name of
	 *            the database.
	 * @param strCommandLinePictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for pictures.
	 * @param strCommandLineSecondaryPictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for secondary pictures.
	 * @param strCommandLineThumbnailsDirectory
	 *            the value specified on the command line for the base directory
	 *            for thumbnails.
	 * @param strCommandLineIndexDirectory
	 *            the value specified on the command line for the base directory
	 * @param iCommandLineTagHistorySize
	 *            the value specified on the command line for the tag history
	 *            size.
	 * @param iConfigurationPictureBankId
	 *            the value specified in the configuration file for the
	 *            {@link IPictureBank} name.
	 * @param strConfigurationPictureBankName
	 *            the value specified in the configuration file for the
	 *            {@link IPictureBank} id.
	 * @param strConfigurationDatabaseFileName
	 *            the value specified in the configuration file for the file
	 *            name of the database.
	 * @param strConfigurationPictureDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for pictures.
	 * @param strConfigurationSecondaryPictureDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for secondary pictures.
	 * @param strConfigurationThumbnailsDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for thumbnails.
	 * @param strConfigurationIndexDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for indexes.
	 * @param iConfigurationTagHistorySize
	 *            the value specified on the configuration file for the tag
	 *            history size.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFilePictureBankConfigurationImpl(final IConfiguration parent, final MBeanServer mBeanServer,
			final Integer iCommandLinePictureBankId, final String strCommandLinePictureBankName,
			final String strCommandLineDatabaseFileName, final String strCommandLinePictureDirectory,
			final String strCommandLineSecondaryPictureDirectory, final String strCommandLineThumbnailsDirectory,
			final String strCommandLineIndexDirectory, final Integer iCommandLineTagHistorySize,
			final Integer iConfigurationPictureBankId, final String strConfigurationPictureBankName,
			final String strConfigurationDatabaseFileName, final String strConfigurationPictureDirectory,
			final String strConfigurationSecondaryPictureDirectory, final String strConfigurationThumbnailsDirectory,
			final String strConfigurationIndexDirectory, final Integer iConfigurationTagHistorySize)
			throws InvalidConfigurationException
	{
		super(parent, SQLFILE_PICTUREBANK_CONFIGURATION_TAG, mBeanServer);
		_leafPictureBankId = new ConfigurationInteger(this, PICTUREBANK_ID_TAG, PICTUREBANK_ID_SHORT_DESC,
				PICTUREBANK_ID_LONG_DESC, PICTUREBANK_ID_INVALID_MESSAGE, false, IntegerDisplayType.SPINNER,
				Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0), iCommandLinePictureBankId);
		_leafPictureBankName = new ConfigurationString(this, PICTUREBANK_NAME_TAG, PICTUREBANK_NAME_SHORT_DESC,
				PICTUREBANK_NAME_LONG_DESC, PICTUREBANK_NAME_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0, "",
				strCommandLinePictureBankName);
		_leafPictureDirectory = new ConfigurationString(this, PICTURE_DIRECTORY_TAG, PICTURE_DIRECTORY_SHORT_DESC,
				PICTURE_DIRECTORY_LONG_DESC, PICTURE_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0,
				"", strCommandLinePictureDirectory);
		_leafSecondaryPictureDirectory = new ConfigurationString(this, SECONDARY_PICTURE_DIRECTORY_TAG,
				SECONDARY_PICTURE_DIRECTORY_SHORT_DESC, SECONDARY_PICTURE_DIRECTORY_LONG_DESC,
				SECONDARY_PICTURE_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0,
				strCommandLineSecondaryPictureDirectory);
		_leafThumbnailsDirectory = new ConfigurationString(this, THUMBNAILS_DIRECTORY_TAG,
				THUMBNAILS_DIRECTORY_SHORT_DESC, THUMBNAILS_DIRECTORY_LONG_DESC, THUMBNAILS_DIRECTORY_INVALID_MESSAGE,
				false, StringDisplayType.TEXTFIELD, 0, "", strCommandLineThumbnailsDirectory);
		_leafIndexDirectory = new ConfigurationString(this, INDEX_DIRECTORY_TAG, INDEX_DIRECTORY_SHORT_DESC,
				INDEX_DIRECTORY_LONG_DESC, INDEX_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0, "",
				strCommandLineIndexDirectory);
		_leafTagHistorySize = new ConfigurationInteger(this, TAG_HISTORY_TAG, TAG_HISTORY_SHORT_DESC,
				TAG_HISTORY_LONG_DESC, TAG_HISTORY_INVALID_MESSAGE, false, IntegerDisplayType.SPINNER,
				Integer.valueOf(0), Integer.valueOf(100), Integer.valueOf(25), iCommandLineTagHistorySize);
		addLeaf(_leafPictureBankId);
		addLeaf(_leafPictureBankName);
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafIndexDirectory);
		addLeaf(_leafThumbnailsDirectory);
		addLeaf(_leafTagHistorySize);
		_pictureCacheLoaderConfiguration = new PictureLoaderConfigurationImpl();
		_secondaryPictureCacheLoaderConfiguration = new SecondaryPictureLoaderConfigurationImpl();
		_thumbnailCacheLoaderConfiguration = new ThumbnailLoaderConfigurationImpl();
		_databaseConfiguration = new SQLiteConfigurationImpl(this, mBeanServer, strCommandLineDatabaseFileName,
				strConfigurationDatabaseFileName);
		_leafPictureBankId.setConfigurationValue(iConfigurationPictureBankId);
		_leafPictureBankName.setConfigurationValue(strConfigurationPictureBankName);
		_leafPictureDirectory.setConfigurationValue(strConfigurationPictureDirectory);
		_leafSecondaryPictureDirectory.setConfigurationValue(strConfigurationSecondaryPictureDirectory);
		_leafIndexDirectory.setConfigurationValue(strConfigurationIndexDirectory);
		_leafThumbnailsDirectory.setConfigurationValue(strConfigurationThumbnailsDirectory);
		_leafTagHistorySize.setConfigurationValue(iConfigurationTagHistorySize);

	}

	@Override
	public String getDescription()
	{
		return "Configuration for the PictureBank based on an SQLite file for the metadatas, and a filesystem for the pictures.";
	}

	@Override
	public String getDatabaseConnection()
	{
		return _databaseConfiguration.getDatabaseConnection();
	}

	@Override
	public int getPictureBankId()
	{
		return _leafPictureBankId.getCurrentValue().intValue();
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

	@Override
	public IBufferedImageCacheLoaderConfiguration getSecondaryPictureLoaderConfiguration()
	{
		return _secondaryPictureCacheLoaderConfiguration;
	}

	@Override
	public String getPictureBankName()
	{
		return _leafPictureBankName.getCurrentValue();
	}

	/**
	 * Implementation of {@link IBufferedImageCacheLoaderConfiguration}.
	 * 
	 * @author benobiwan
	 * 
	 */
	private final class PictureLoaderConfigurationImpl extends AbstractConfigurationBranch
			implements IBufferedImageCacheLoaderConfiguration
	{
		/**
		 * Creates a new PictureLoaderConfigurationImpl.
		 */
		public PictureLoaderConfigurationImpl()
		{
			super(SQLFilePictureBankConfigurationImpl.this, "",
					SQLFilePictureBankConfigurationImpl.this.getMBeanServer());
		}

		@Override
		public String getDescription()
		{
			return "Configuration for the picture loader.";
		}

		@Override
		public String getPictureDirectory()
		{
			return _leafPictureDirectory.getCurrentValue();
		}

		@Override
		public long getCacheSize()
		{
			// TODO to change
			return 10;
		}
	}

	/**
	 * Implementation of {@link IBufferedImageCacheLoaderConfiguration}.
	 * 
	 * @author benobiwan
	 * 
	 */
	private final class SecondaryPictureLoaderConfigurationImpl extends AbstractConfigurationBranch
			implements IBufferedImageCacheLoaderConfiguration
	{
		/**
		 * Creates a new SecondaryPictureLoaderConfigurationImpl.
		 */
		public SecondaryPictureLoaderConfigurationImpl()
		{
			super(SQLFilePictureBankConfigurationImpl.this, "",
					SQLFilePictureBankConfigurationImpl.this.getMBeanServer());
		}

		@Override
		public String getDescription()
		{
			return "Configuration for the secondary picture loader.";
		}

		@Override
		public String getPictureDirectory()
		{
			return _leafSecondaryPictureDirectory.getCurrentValue();
		}

		@Override
		public long getCacheSize()
		{
			// TODO to change
			return 10;
		}
	}

	/**
	 * Implementation of {@link IBufferedImageCacheLoaderConfiguration}.
	 * 
	 * @author benobiwan
	 * 
	 */
	private final class ThumbnailLoaderConfigurationImpl extends AbstractConfigurationBranch
			implements IBufferedImageCacheLoaderConfiguration
	{
		/**
		 * Creates a new ThumbnailLoaderConfigurationImpl.
		 */
		public ThumbnailLoaderConfigurationImpl()
		{
			super(SQLFilePictureBankConfigurationImpl.this, "",
					SQLFilePictureBankConfigurationImpl.this.getMBeanServer());
		}

		@Override
		public String getDescription()
		{
			return "Configuration for the thumbnail loader.";
		}

		@Override
		public String getPictureDirectory()
		{
			return _leafThumbnailsDirectory.getCurrentValue();
		}

		@Override
		public long getCacheSize()
		{
			// TODO to change
			return 50;
		}
	}

	@Override
	public int compareTo(final IPictureBankConfiguration o)
	{
		// TODO handle null
		return getPictureBankId() - o.getPictureBankId();
	}

	@Override
	public int getTagHistorySize()
	{
		return _leafTagHistorySize.getCurrentValue().intValue();
	}
}
