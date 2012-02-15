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
