package yapto.picturebank.sqlfile;

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
	 * The {@link SQLFilePictureBank}.
	 */
	private final SQLFilePictureBank _pictureBank;

	/**
	 * Creates a new FsPictureRemovalListener.
	 * 
	 * @param pictureBank
	 *            the {@link SQLFilePictureBank}.
	 */
	public FsPictureRemovalListener(final SQLFilePictureBank pictureBank)
	{
		_pictureBank = pictureBank;
	}

	@Override
	public void onRemoval(
			final RemovalNotification<String, FsPicture> notification)
	{
		_pictureBank.updatePicture(notification.getValue(), true);
	}
}
