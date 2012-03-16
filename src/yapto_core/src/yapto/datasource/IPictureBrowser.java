package yapto.datasource;

import java.util.ListIterator;

/**
 * A {@link ListIterator} which enable to browse the picture in order.
 * 
 * @author benobiwan
 * @param <PICTURE>
 *            type of {@link IPicture} of this {@link IPictureList}.
 */
public interface IPictureBrowser<PICTURE extends IPicture> extends
		ListIterator<PICTURE>
{
	/**
	 * Get the current {@link IPicture}.
	 * 
	 * @return the current {@link IPicture}.
	 */
	IPicture getCurrentPicture();

	/**
	 * Register an object to the listen for change in this
	 * {@link IPictureBrowser}.
	 * 
	 * @param object
	 *            the object to register.
	 */
	void register(Object object);

	/**
	 * Unregister an object to the listen for change in this
	 * {@link IPictureBrowser}.
	 * 
	 * @param object
	 *            the object to unregister.
	 */
	void unRegister(Object object);

	/**
	 * Get the source {@link IPictureList} for this {@link IPictureBrowser}.
	 * 
	 * @return the source {@link IPictureList} for this {@link IPictureBrowser}.
	 */
	IPictureList<PICTURE> getSourcePictureList();
}
