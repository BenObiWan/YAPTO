package yapto.datasource;

import org.apache.lucene.search.Query;

/**
 * An object used to browse pictures from an {@link IDataSource}.
 * 
 * @author benobiwan
 * @param <PICTURE>
 *            type of {@link IPicture} of this {@link IPictureBrowser}.
 */
public interface IPictureBrowser<PICTURE extends IPicture> extends
		IPictureList<PICTURE>
{
	/**
	 * Get the current {@link IPicture}.
	 * 
	 * @return the current {@link IPicture}.
	 */
	PICTURE getCurrentPicture();

	/**
	 * Get the index of the current picture.
	 * 
	 * @return the index of the current picture.
	 */
	int getCurrentIndex();

	/**
	 * Returns true if this IPictureBrowser has more elements when traversing
	 * the list in the forward direction.
	 * 
	 * @return true if this IPictureBrowser has more elements when traversing
	 *         the list in the forward direction.
	 */
	boolean hasNext();

	/**
	 * Returns the next element in the list and advances the cursor position.
	 * 
	 * @return the next element in the list.
	 */
	PICTURE next();

	/**
	 * Returns the index of the next element.
	 * 
	 * @return the index of the next element.
	 */
	int nextIndex();

	/**
	 * Retrieve the pictures directly after the current picture, without
	 * changing the current picture.
	 * 
	 * @param iNbr
	 *            the number of pictures to retrieve.
	 * @return an array containing the asked picture in order.
	 */
	PICTURE[] nextPictures(int iNbr);

	/**
	 * Returns true if this IPictureBrowser has more elements when traversing
	 * the list in the reverse direction.
	 * 
	 * @return true if this IPictureBrowser has more elements when traversing
	 *         the list in the reverse direction.
	 */
	boolean hasPrevious();

	/**
	 * Returns the next previous in the list and advances the cursor position.
	 * 
	 * @return the next previous in the list.
	 */
	PICTURE previous();

	/**
	 * Returns the index of the previous element.
	 * 
	 * @return the index of the previous element.
	 */
	int previousIndex();

	/**
	 * Retrieve the pictures directly before the current picture, without
	 * changing the current picture.
	 * 
	 * @param iNbr
	 *            the number of pictures to retrieve.
	 * @return an array containing the asked picture in order.
	 */
	PICTURE[] previousPictures(int iNbr);

	/**
	 * Retrieve the picture whose id are between the specified indexes.
	 * 
	 * @param iBeginIndex
	 *            the index of the first picture to retrieve.
	 * @param iEndIndex
	 *            the index of the last picture to retrieve.
	 * @return an array containing the asked picture in order.
	 */
	PICTURE[] getPictures(int iBeginIndex, int iEndIndex);

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
	 * Get the {@link IDataSource} which created this {@link IPictureBrowser}.
	 * 
	 * @return the {@link IDataSource} which created this
	 *         {@link IPictureBrowser}.
	 */
	IDataSource<PICTURE> getDataSource();

	/**
	 * get the {@link Query} which created this {@link IPictureBrowser}. null if
	 * it isn't filtered.
	 * 
	 * @return the {@link Query} which created this {@link IPictureBrowser}.
	 */
	Query getQuery();
}
