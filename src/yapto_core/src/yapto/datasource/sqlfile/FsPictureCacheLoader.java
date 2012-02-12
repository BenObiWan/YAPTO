package yapto.datasource.sqlfile;

import yapto.datasource.sqlfile.config.FsPictureCacheLoaderConfiguration;

import com.google.common.cache.CacheLoader;

/**
 * A {@link CacheLoader} loading {@link FsPicture} from the file system.
 * 
 * @author benobiwan
 * 
 */
public final class FsPictureCacheLoader extends CacheLoader<String, FsPicture>
{
	/**
	 * The configuration of this FsPictureCacheLoader.
	 */
	private final FsPictureCacheLoaderConfiguration _conf;

	/**
	 * Creates a new FsPictureCacheLoader.
	 * 
	 * @param conf
	 *            the configuration for this FsPictureCacheLoader.
	 */
	public FsPictureCacheLoader(final FsPictureCacheLoaderConfiguration conf)
	{
		_conf = conf;
	}

	@Override
	public FsPicture load(final String key) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
}
