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
	 * Get the id of this {@link IDataSource}.
	 * 
	 * @return the id of this {@link IDataSource}.
	 */
	int getDataSourceId();
}
