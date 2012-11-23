package yapto.picturebank;

import yapto.picturebank.config.IPictureBankConfiguration;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * {@link RemovalListener} which closes the {@link IPictureBank} on removal.
 * 
 * @author benobiwan
 * 
 */
public final class PictureBankRemovalListener implements
		RemovalListener<IPictureBankConfiguration, IPictureBank<?>>
{
	@Override
	public void onRemoval(
			final RemovalNotification<IPictureBankConfiguration, IPictureBank<?>> notification)
	{
		notification.getValue().close();
	}
}
