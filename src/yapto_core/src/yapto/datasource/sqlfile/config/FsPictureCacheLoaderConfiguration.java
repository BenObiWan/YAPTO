package yapto.datasource.sqlfile.config;

import yapto.datasource.config.IDataSourceConfiguration;
import yapto.datasource.sqlfile.FsPictureCacheLoader;

/**
 * Configuration for an {@link FsPictureCacheLoader}.
 * 
 * @author benobiwan
 * 
 */
public interface FsPictureCacheLoaderConfiguration extends
		IDataSourceConfiguration
{
	/**
	 * Get the path for the picture directory.
	 * 
	 * @return the path for the picture directory.
	 */
	String getPictureDirectory();
}
