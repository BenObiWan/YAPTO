package yapto.datasource;

/**
 * An {@link IPictureList} which enable to browse the picture in order.
 * 
 * @author benobiwan
 * @param <PICTURE>
 *            type of {@link IPicture} of this {@link IPictureList}.
 */
public interface IPictureBrowser<PICTURE extends IPicture> extends
		IPictureList<PICTURE>
{
	/**
	 * Get the current {@link IPicture}.
	 * 
	 * @return the current {@link IPicture}.
	 */
	IPicture getCurrentPicture();

	/**
	 * Get the next {@link IPicture}, and change the current accordingly. Does
	 * nothing if there is no next {@link IPicture}.
	 * 
	 * @return the next {@link IPicture}.
	 */
	IPicture getNextPicture();

	/**
	 * Check whether there is a following {@link IPicture}.
	 * 
	 * @return true if there is a following {@link IPicture}.
	 */
	boolean hasNextPicture();

	/**
	 * Get the previous {@link IPicture}, and change the current accordingly.
	 * Does nothing if there is no next {@link IPicture}. *
	 * 
	 * @return the previous {@link IPicture}.
	 */
	IPicture getPreviousPicture();

	/**
	 * Check whether there is a preceding {@link IPicture}.
	 * 
	 * @return true if there is a preceding {@link IPicture}.
	 */
	boolean hasPreviousPicture();

	/**
	 * Change the {@link IPictureList} used as source for this
	 * {@link IPictureBrowser}.
	 * 
	 * @param source
	 *            the new {@link IPictureList} to use as source.
	 */
	void changeSourcePictureList(IPictureList<?> source);

	/**
	 * Register an object to the listen for change in this
	 * {@link IPictureBrowser}.
	 * 
	 * @param object
	 *            the object to register.
	 */
	void register(Object object);
}
