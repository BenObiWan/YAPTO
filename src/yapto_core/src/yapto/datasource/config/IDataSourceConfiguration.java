package yapto.datasource.config;

import yapto.datasource.IDataSource;

import common.config.IConfigurationBranch;

/**
 * Configuration for an {@link IDataSource}.
 * 
 * @author benobiwan
 * 
 */
public interface IDataSourceConfiguration extends IConfigurationBranch
{
	/**
	 * Tag for the DataSource id.
	 */
	String DATASOURCE_ID_TAG = "DataSourceId";

	/**
	 * Get the id of this {@link IDataSource}.
	 * 
	 * @return the id of this {@link IDataSource}.
	 */
	int getDataSourceId();
}
