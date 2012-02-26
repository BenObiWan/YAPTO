package yapto.datasource.sqlfile;

import yapto.datasource.sqlfile.config.FsPictureCacheLoaderConfiguration;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * A {@link RemovalListener} used to save the modifications of an FsPicture when
 * it is removed from the cache.
 * 
 * @author benobiwan
 * 
 */
public final class FsPictureRemovalListener implements
		RemovalListener<String, FsPicture>
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
	 * 
	 * @param conf
	 *            the configuration for this FsPictureCacheLoader.
	 * @param fileListConnection
	 *            object holding the connection to the database and the prepared
	 *            statements.
	 */
	public FsPictureRemovalListener(
			final FsPictureCacheLoaderConfiguration conf,
			final SQLFileListConnection fileListConnection)
	{
		_conf = conf;
		_fileListConnection = fileListConnection;
	}

	@Override
	public void onRemoval(
			final RemovalNotification<String, FsPicture> notification)
	{
		if (notification.getValue().hasBeenModified())
		{
			// TODO save modifications
		}
	}
}
