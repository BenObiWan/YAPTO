package yapto.picturebank.sqlfile.config;

import javax.management.MBeanServer;

import common.config.IConfiguration;
import common.config.InvalidConfigurationException;
import common.config.db.IDatabaseConfiguration;
import common.config.db.SQLiteConfigurationImpl;
import yapto.picturebank.IPictureBank;

/**
 * An implementation of the {@link ISQLFilePictureBankConfiguration} interface
 * using a SQLite database.
 * 
 * @author benobiwan
 * 
 */
public final class SQLFilePictureBankConfigurationSQLite extends AbstractSQLFilePictureBankConfiguration
{

	/**
	 * {@link IDatabaseConfiguration} for the connection to the database.
	 */
	private final IDatabaseConfiguration _databaseConfiguration;

	/**
	 * Creates a new AbstractSQLFilePictureBankConfiguration using default
	 * values for every elements.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 */
	public SQLFilePictureBankConfigurationSQLite(IConfiguration parent, MBeanServer mBeanServer)
	{
		super(parent, mBeanServer);
		_databaseConfiguration = new SQLiteConfigurationImpl(this, mBeanServer);
	}

	/**
	 * Creates a new SQLFilePictureBankConfigurationSQLite with values coming
	 * from the command line.
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
	 * @param iCommandLinePictureCacheSize
	 *            the value specified on the command line for the cache size for
	 *            pictures.
	 * @param strCommandLineSecondaryPictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for secondary pictures.
	 * @param iCommandLineSecondaryPictureCacheSize
	 *            the value specified on the command line for the cache size for
	 *            secondary pictures.
	 * @param strCommandLineThumbnailsDirectory
	 *            the value specified on the command line for the base directory
	 *            for thumbnails.
	 * @param iCommandLineThumbnailSize
	 *            the value specified on the command line for the cache size for
	 *            thumbnails.
	 * @param strCommandLineIndexDirectory
	 *            the value specified on the command line for the base directory
	 *            for indexes.
	 * @param iCommandLineTagHistorySize
	 *            the value specified on the command line for the tag history
	 *            size.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFilePictureBankConfigurationSQLite(IConfiguration parent, MBeanServer mBeanServer,
			Integer iCommandLinePictureBankId, String strCommandLinePictureBankName,
			String strCommandLineDatabaseConnection, String strCommandLinePictureDirectory,
			int iCommandLinePictureCacheSize, String strCommandLineSecondaryPictureDirectory,
			int iCommandLineSecondaryPictureCacheSize, String strCommandLineThumbnailsDirectory,
			int iCommandLineThumbnailSize, String strCommandLineIndexDirectory, Integer iCommandLineTagHistorySize)
			throws InvalidConfigurationException
	{
		super(parent, mBeanServer, iCommandLinePictureBankId, strCommandLinePictureBankName,
				strCommandLinePictureDirectory, iCommandLinePictureCacheSize, strCommandLineSecondaryPictureDirectory,
				iCommandLineSecondaryPictureCacheSize, strCommandLineThumbnailsDirectory, iCommandLineThumbnailSize,
				strCommandLineIndexDirectory, iCommandLineTagHistorySize);
		_databaseConfiguration = new SQLiteConfigurationImpl(this, mBeanServer, strCommandLineDatabaseConnection);
	}

	/**
	 * Creates a new AbstractSQLFilePictureBankConfiguration with values coming
	 * from the command line and from a configuration file.
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
	 * @param iCommandLinePictureCacheSize
	 *            the value specified on the command line for the cache size for
	 *            pictures.
	 * @param strCommandLineSecondaryPictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for secondary pictures.
	 * @param iCommandLineSecondaryPictureCacheSize
	 *            the value specified on the command line for the cache size for
	 *            secondary pictures.
	 * @param strCommandLineThumbnailsDirectory
	 *            the value specified on the command line for the base directory
	 *            for thumbnails.
	 * @param iCommandLineThumbnailSize
	 *            the value specified on the command line for the cache size for
	 *            thumbnails.
	 * @param strCommandLineIndexDirectory
	 *            the value specified on the command line for the base directory
	 *            for indexes.
	 * @param iCommandLineTagHistorySize
	 *            the value specified on the command line for the tag history
	 *            size.
	 * @param iConfigurationPictureBankId
	 *            the value specified in the configuration file for the
	 *            {@link IPictureBank} name.
	 * @param strConfigurationPictureBankName
	 *            the value specified in the configuration file for the
	 *            {@link IPictureBank} id.
	 * @param strConfigurationDatabaseConnection
	 *            the value specified in the configuration file for the file
	 *            name of the database.
	 * @param strConfigurationPictureDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for pictures.
	 * @param iConfigurationPictureCacheSize
	 *            the value specified in the configuration file for the cache
	 *            size for pictures.
	 * @param strConfigurationSecondaryPictureDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for secondary pictures.
	 * @param iConfigurationSecondaryPictureCacheSize
	 *            the value specified in the configuration file for the cache
	 *            size for secondary pictures.
	 * @param strConfigurationThumbnailsDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for thumbnails.
	 * @param iConfigurationThumbnailCacheSize
	 *            the value specified in the configuration file for the cache
	 *            size for thumbnails.
	 * @param strConfigurationIndexDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for indexes.
	 * @param iConfigurationTagHistorySize
	 *            the value specified on the configuration file for the tag
	 *            history size.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public SQLFilePictureBankConfigurationSQLite(IConfiguration parent, MBeanServer mBeanServer,
			Integer iCommandLinePictureBankId, String strCommandLinePictureBankName,
			String strCommandLineDatabaseConnection, String strCommandLinePictureDirectory,
			int iCommandLinePictureCacheSize, String strCommandLineSecondaryPictureDirectory,
			int iCommandLineSecondaryPictureCacheSize, String strCommandLineThumbnailsDirectory,
			int iCommandLineThumbnailSize, String strCommandLineIndexDirectory, Integer iCommandLineTagHistorySize,
			Integer iConfigurationPictureBankId, String strConfigurationPictureBankName,
			String strConfigurationDatabaseConnection, String strConfigurationPictureDirectory,
			int iConfigurationPictureCacheSize, String strConfigurationSecondaryPictureDirectory,
			int iConfigurationSecondaryPictureCacheSize, String strConfigurationThumbnailsDirectory,
			int iConfigurationThumbnailCacheSize, String strConfigurationIndexDirectory,
			Integer iConfigurationTagHistorySize) throws InvalidConfigurationException
	{
		super(parent, mBeanServer, iCommandLinePictureBankId, strCommandLinePictureBankName,
				strCommandLinePictureDirectory, iCommandLinePictureCacheSize, strCommandLineSecondaryPictureDirectory,
				iCommandLineSecondaryPictureCacheSize, strCommandLineThumbnailsDirectory, iCommandLineThumbnailSize,
				strCommandLineIndexDirectory, iCommandLineTagHistorySize, iConfigurationPictureBankId,
				strConfigurationPictureBankName, strConfigurationPictureDirectory, iConfigurationPictureCacheSize,
				strConfigurationSecondaryPictureDirectory, iConfigurationSecondaryPictureCacheSize,
				strConfigurationThumbnailsDirectory, iConfigurationThumbnailCacheSize, strConfigurationIndexDirectory,
				iConfigurationTagHistorySize);
		_databaseConfiguration = new SQLiteConfigurationImpl(this, mBeanServer, strCommandLineDatabaseConnection,
				strConfigurationDatabaseConnection);
	}

	@Override
	public String getDatabaseConnection()
	{
		return _databaseConfiguration.getDatabaseConnection();
	}

	@Override
	public String getDescription()
	{
		return "Configuration for the PictureBank based on an SQLite file for the metadatas, and a filesystem for the pictures.";
	}

	@Override
	public IDatabaseConfiguration getDatabaseConfiguration()
	{
		return _databaseConfiguration;
	}
}
