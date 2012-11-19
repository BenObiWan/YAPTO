package yapto.picturebank.sqlfile.config;

import javax.management.MBeanServer;

import yapto.picturebank.IPictureBank;
import yapto.picturebank.sqlfile.IBufferedImageCacheLoaderConfiguration;

import common.config.AbstractConfigurationBranch;
import common.config.IConfiguration;
import common.config.InvalidConfigurationException;
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
public final class SQLFilePictureBankConfigurationImpl extends
		AbstractConfigurationBranch implements ISQLFilePictureBankConfiguration
{
	/**
	 * Leaf configuring the {@link IPictureBank} id.
	 */
	private final ConfigurationInteger _leafPictureBankId;

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
	 * {@link IBufferedImageCacheLoaderConfiguration} for the pictures.
	 */
	private final IBufferedImageCacheLoaderConfiguration _pictureCacheLoaderConfiguration;

	/**
	 * {@link IBufferedImageCacheLoaderConfiguration} for the thumbnails.
	 */
	private final IBufferedImageCacheLoaderConfiguration _thumbnailCacheLoaderConfiguration;

	/**
	 * Creates a new SQLFilePictureBankConfigurationImpl using default values
	 * for every elements.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 */
	public SQLFilePictureBankConfigurationImpl(final IConfiguration parent,
			final MBeanServer mBeanServer)
	{
		super(parent, SQLFILE_PICTUREBANK_CONFIGURATION_TAG, mBeanServer);
		_leafPictureBankId = new ConfigurationInteger(this, PICTUREBANK_ID_TAG,
				PICTUREBANK_ID_SHORT_DESC, PICTUREBANK_ID_LONG_DESC,
				PICTUREBANK_ID_INVALID_MESSAGE, false,
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
		addLeaf(_leafPictureBankId);
		addLeaf(_leafDatabaseFileName);
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafIndexDirectory);
		addLeaf(_leafThumbnailsDirectory);
		_pictureCacheLoaderConfiguration = new PictureLoaderConfigurationImpl();
		_thumbnailCacheLoaderConfiguration = new ThumbnailLoaderConfigurationImpl();
	}

	/**
	 * Creates a new SQLFilePictureBankConfigurationImpl using default values
	 * for every elements.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param iCommandLinePictureBankId
	 *            the value specified on the command line for the
	 *            {@link IPictureBank} id.
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
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFilePictureBankConfigurationImpl(final IConfiguration parent,
			final MBeanServer mBeanServer,
			final Integer iCommandLinePictureBankId,
			final String strCommandLineDatabaseFileName,
			final String strCommandLinePictureDirectory,
			final String strCommandLineThumbnailsDirectory,
			final String strCommandLineIndexDirectory)
			throws InvalidConfigurationException
	{
		super(parent, SQLFILE_PICTUREBANK_CONFIGURATION_TAG, mBeanServer);
		_leafPictureBankId = new ConfigurationInteger(this, PICTUREBANK_ID_TAG,
				PICTUREBANK_ID_SHORT_DESC, PICTUREBANK_ID_LONG_DESC,
				PICTUREBANK_ID_INVALID_MESSAGE, false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLinePictureBankId);
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
		addLeaf(_leafPictureBankId);
		addLeaf(_leafDatabaseFileName);
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafIndexDirectory);
		addLeaf(_leafThumbnailsDirectory);
		_pictureCacheLoaderConfiguration = new PictureLoaderConfigurationImpl();
		_thumbnailCacheLoaderConfiguration = new ThumbnailLoaderConfigurationImpl();
	}

	/**
	 * Creates a new SQLFilePictureBankConfigurationImpl using default values
	 * for every elements.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param iCommandLinePictureBankId
	 *            the value specified on the command line for the
	 *            {@link IPictureBank} id.
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
	 * @param iConfigurationPictureBankId
	 *            the value specified in the configuration file for the
	 *            {@link IPictureBank} id.
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
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFilePictureBankConfigurationImpl(final IConfiguration parent,
			final MBeanServer mBeanServer,
			final Integer iCommandLinePictureBankId,
			final String strCommandLineDatabaseFileName,
			final String strCommandLinePictureDirectory,
			final String strCommandLineThumbnailsDirectory,
			final String strCommandLineIndexDirectory,
			final Integer iConfigurationPictureBankId,
			final String strConfigurationDatabaseFileName,
			final String strConfigurationPictureDirectory,
			final String strConfigurationThumbnailsDirectory,
			final String strConfigurationIndexDirectory)
			throws InvalidConfigurationException
	{
		this(parent, mBeanServer, iCommandLinePictureBankId,
				strCommandLineDatabaseFileName, strCommandLinePictureDirectory,
				strCommandLineThumbnailsDirectory, strCommandLineIndexDirectory);
		_leafPictureBankId.setConfigurationValue(iConfigurationPictureBankId);
		_leafDatabaseFileName
				.setConfigurationValue(strConfigurationDatabaseFileName);
		_leafPictureDirectory
				.setConfigurationValue(strConfigurationPictureDirectory);
		_leafIndexDirectory
				.setConfigurationValue(strConfigurationIndexDirectory);
		_leafThumbnailsDirectory
				.setConfigurationValue(strConfigurationThumbnailsDirectory);

	}

	@Override
	public String getDescription()
	{
		return "Configuration for the PictureBank based on an SQLite file for the metadatas, and a filesystem for the pictures.";
	}

	@Override
	public String getDatabaseFileName()
	{
		return _leafDatabaseFileName.getCurrentValue();
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
			super(SQLFilePictureBankConfigurationImpl.this, "",
					SQLFilePictureBankConfigurationImpl.this.getMBeanServer());
		}

		@Override
		public String getDescription()
		{
			// TODO
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
			super(SQLFilePictureBankConfigurationImpl.this, "",
					SQLFilePictureBankConfigurationImpl.this.getMBeanServer());
		}

		@Override
		public String getDescription()
		{
			// TODO
			return "";
		}

		@Override
		public String getPictureDirectory()
		{
			return _leafThumbnailsDirectory.getCurrentValue();
		}
	}
}
