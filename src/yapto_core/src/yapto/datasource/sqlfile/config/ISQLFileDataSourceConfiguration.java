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
