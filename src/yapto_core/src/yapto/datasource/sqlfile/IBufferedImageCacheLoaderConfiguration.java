package yapto.datasource.sqlfile;

import common.config.IConfigurationBranch;

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
