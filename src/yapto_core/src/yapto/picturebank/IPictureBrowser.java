package yapto.picturebank;

import java.util.concurrent.ExecutionException;

import org.apache.lucene.search.Query;

/**
 * An object used to browse pictures from an {@link IPictureBank}.
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
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the picture.
	 */
	IPicture[] nextPictures(int iNbr) throws ExecutionException;

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
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the picture.
	 */
	IPicture[] previousPictures(int iNbr) throws ExecutionException;

	/**
	 * Retrieve the picture whose id are between the specified indexes.
	 * 
	 * @param iBeginIndex
	 *            the index of the first picture to retrieve.
	 * @param iEndIndex
	 *            the index of the last picture to retrieve.
	 * @return an array containing the asked picture in order.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the picture.
	 */
	IPicture[] getPictures(int iBeginIndex, int iEndIndex)
			throws ExecutionException;

	/**
	 * Get the {@link IPictureBank} which created this {@link IPictureBrowser}.
	 * 
	 * @return the {@link IPictureBank} which created this
	 *         {@link IPictureBrowser}.
	 */
	IPictureBank<PICTURE> getPictureBank();

	/**
	 * get the {@link Query} which created this {@link IPictureBrowser}. null if
	 * it isn't filtered.
	 * 
	 * @return the {@link Query} which created this {@link IPictureBrowser}.
	 */
	Query getQuery();
}
