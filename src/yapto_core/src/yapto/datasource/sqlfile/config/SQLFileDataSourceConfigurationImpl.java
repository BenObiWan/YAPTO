package yapto.datasource.sqlfile.config;

import javax.management.MBeanServer;

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
	private final ConfigurationString _leafPictureDirectory;

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
				"DataSource Id", "Id of this DataSource.",
				"Invalid Id for this DataSource.", false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		_leafDatabaseFileName = new ConfigurationString(parent,
				DATABASE_FILENAME_TAG, "Database file name",
				"File name for the database.", "File name for the database.",
				false, StringDisplayType.TEXTFIELD, 0, "");
		_leafPictureDirectory = new ConfigurationString(parent,
				PICTURE_DIRECTORY_TAG, "Picture directory",
				"Base directory for the pictures.",
				"Base directory for the pictures.", false,
				StringDisplayType.TEXTFIELD, 0, "");
		addLeaf(_leafDataSourceId);
		addLeaf(_leafDatabaseFileName);
		addLeaf(_leafPictureDirectory);
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
			final String strCommandLinePictureDirectory)
			throws InvalidConfigurationException
	{
		super(parent, SQLFILE_DATASOURCE_CONFIGURATION_TAG, mBeanServer);
		_leafDataSourceId = new ConfigurationInteger(this, DATASOURCE_ID_TAG,
				"DataSource Id", "Id of this DataSource.",
				"Invalid Id for this DataSource.", false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLineDataSourceId);
		_leafDatabaseFileName = new ConfigurationString(parent,
				DATABASE_FILENAME_TAG, "Database file name",
				"File name for the database.", "File name for the database.",
				false, StringDisplayType.TEXTFIELD, 0, "",
				strCommandLineDatabaseFileName);
		_leafPictureDirectory = new ConfigurationString(parent,
				PICTURE_DIRECTORY_TAG, "Picture directory",
				"Base directory for the pictures.",
				"Base directory for the pictures.", false,
				StringDisplayType.TEXTFIELD, 0, "",
				strCommandLinePictureDirectory);
		addLeaf(_leafDataSourceId);
		addLeaf(_leafDatabaseFileName);
		addLeaf(_leafPictureDirectory);
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
			final Integer iConfigurationDataSourceId,
			final String strConfigurationDatabaseFileName,
			final String strConfigurationPictureDirectory)
			throws InvalidConfigurationException
	{
		this(parent, mBeanServer, iCommandLineDataSourceId,
				strCommandLineDatabaseFileName, strCommandLinePictureDirectory);
		_leafDataSourceId.setConfigurationValue(iConfigurationDataSourceId);
		_leafDatabaseFileName
				.setConfigurationValue(strConfigurationDatabaseFileName);
		_leafPictureDirectory
				.setConfigurationValue(strConfigurationPictureDirectory);
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
	public String getPictureDirectory()
	{
		return _leafPictureDirectory.getCurrentValue();
	}

	@Override
	public int getDataSourceId()
	{
		return _leafDataSourceId.getCurrentValue().intValue();
	}
}
