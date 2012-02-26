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
	 * Object holding the connection to the database and the prepared
	 * statements.
	 */
	private final SQLFileListConnection _fileListConnection;

	/**
	 * Creates a new FsPictureCacheLoader.
	 * 
	 * @param conf
	 *            the configuration for this FsPictureCacheLoader.
	 * @param fileListConnection
	 *            object holding the connection to the database and the prepared
	 *            statements.
	 */
	public FsPictureCacheLoader(final FsPictureCacheLoaderConfiguration conf,
			final SQLFileListConnection fileListConnection)
	{
		_conf = conf;
		_fileListConnection = fileListConnection;
	}

	@Override
	public FsPicture load(final String key) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
}
