package yapto.datasource.sqlfile.config;

import yapto.datasource.config.IDataSourceConfiguration;
import yapto.datasource.sqlfile.SQLFileDataSource;

/**
 * Configuration for an {@link SQLFileDataSource}.
 * 
 * @author benobiwan
 * 
 */
public interface ISQLFileDataSourceConfiguration extends
		IDataSourceConfiguration
{
	/**
	 * Tag of this configuration node.
	 */
	String SQLFILE_DATASOURCE_CONFIGURATION_TAG = "SQLFileDataSource";

	/**
	 * Tag for the database file name.
	 */
	String DATABASE_FILENAME_TAG = "DatabaseFileName";

	/**
	 * Tag for the picture directory.
	 */
	String PICTURE_DIRECTORY_TAG = "PictureDirectory";

	/**
	 * Get the file name of the database.
	 * 
	 * @return the file name of the database.
	 */
	String getDatabaseFileName();

	/**
	 * Get the path for the picture directory.
	 * 
	 * @return the path for the picture directory.
	 */
	String getPictureDirectory();
}
