package yapto.datasource.sqlfile;

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
	 * The {@link SQLFileDataSource}.
	 */
	private final SQLFileDataSource _dataSource;

	/**
	 * Creates a new FsPictureRemovalListener.
	 * 
	 * @param dataSource
	 *            the {@link SQLFileDataSource}.
	 */
	public FsPictureRemovalListener(final SQLFileDataSource dataSource)
	{
		_dataSource = dataSource;
	}

	@Override
	public void onRemoval(
			final RemovalNotification<String, FsPicture> notification)
	{
		_dataSource.updatePicture(notification.getValue(), true);
	}
}
