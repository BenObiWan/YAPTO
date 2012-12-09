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
	 * @return an {@link IPictureBrowser}.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the first
	 *             picture.
	 */
	IPictureBrowser<?> getAllPictures() throws ExecutionException;

	/**
	 * Get an {@link IPictureBrowser} browsing a selection of pictures from this
	 * {@link IPictureBank}.
	 * 
	 * @param query
	 *            the {@link Query} used to filter the pictures to select.
	 * @param iLimit
	 *            the maximal number of pictures to select.
	 * 
	 * @return an {@link IPictureBrowser}.
	 * @throws IOException
	 *             if an error occurs during the filtering.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the first
	 *             picture.
	 */
	IPictureBrowser<?> filterPictures(Query query, int iLimit)
			throws IOException, ExecutionException;

	/**
	 * Get an {@link IPictureBrowser} browsing a random number of pictures from
	 * this {@link IPictureBank}.
	 * 
	 * @param iNbrPicture
	 *            number of pictures to select.
	 * @return an {@link IPictureBrowser}.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the first
	 *             picture.
	 */
	IPictureBrowser<?> getRandomPictureList(final int iNbrPicture)
			throws ExecutionException;
}
