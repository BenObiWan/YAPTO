package yapto.picturebank.sqlfile.config;

import common.config.IConfiguration;
import common.config.IConfigurationBranch;
import yapto.picturebank.sqlfile.BufferedImageCacheLoader;

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
	 * Tag for the picture directory.
	 */
	String PICTURE_DIRECTORY_TAG = "PictureDirectory";
	
	/**
	 * Tag for the cache size.
	 */
	String CACHE_SIZE_TAG = "CacheSizer";
	
	/**
	 * Get the path for the picture directory.
	 * 
	 * @return the path for the picture directory.
	 */
	String getPictureDirectory();

	/**
	 * Size of the picture cache.
	 * 
	 * @return the size of the picture cache.
	 */
	long getCacheSize();
}
