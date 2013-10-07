package yapto.picturebank;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.lucene.search.Query;

public interface IPictureBrowserCreator
{
	/**
	 * Get an {@link IPictureBrowser} browsing all the pictures of this
	 * {@link IPictureBank}.
	 * 
	 * @param strInitialPictureId
	 *            initial {@link IPicture} to display.
	 * @return an {@link IPictureBrowser}.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the initial
	 *             picture.
	 */
	IPictureBrowser<?> getAllPictures(String strInitialPictureId)
			throws ExecutionException;

	/**
	 * Get an {@link IPictureBrowser} browsing a selection of pictures from this
	 * {@link IPictureBank}.
	 * 
	 * @param query
	 *            the {@link Query} used to filter the pictures to select.
	 * @param iLimit
	 *            the maximal number of pictures to select.
	 * @param strInitialPictureId
	 *            initial {@link IPicture} to display.
	 * @return an {@link IPictureBrowser}.
	 * @throws IOException
	 *             if an error occurs during the filtering.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the first
	 *             picture.
	 */
	IPictureBrowser<?> filterPictures(Query query, int iLimit,
			String strInitialPictureId) throws IOException, ExecutionException;

	/**
	 * Get an {@link IPictureBrowser} browsing a random number of pictures from
	 * this {@link IPictureBank}.
	 * 
	 * @param iNbrPicture
	 *            number of pictures to select.
	 * @param strInitialPictureId
	 *            initial {@link IPicture} to display.
	 * @return an {@link IPictureBrowser}.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the first
	 *             picture.
	 */
	IPictureBrowser<?> getRandomPictureList(final int iNbrPicture,
			String strInitialPictureId) throws ExecutionException;
}
