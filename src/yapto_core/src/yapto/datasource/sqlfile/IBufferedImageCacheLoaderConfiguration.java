package yapto.datasource.sqlfile;

import common.config.IConfiguration;
import common.config.IConfigurationBranch;

/**
 * {@link IConfiguration} for {@link BufferedImageCacheLoader}.
 * 
 * @author benobiwan
 * 
 */
public interface IBufferedImageCacheLoaderConfiguration extends
		IConfigurationBranch
{
	/**
	 * Get the path for the picture directory.
	 * 
	 * @return the path for the picture directory.
	 */
	String getPictureDirectory();
}
