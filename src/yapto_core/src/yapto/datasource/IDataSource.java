package yapto.datasource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

import org.apache.lucene.search.Query;

import yapto.datasource.tag.IWritableTagRepository;

/**
 * An interface describing a source for {@link IPicture}.
 * 
 * @author benobiwan
 * @param <PICTURE>
 *            type of {@link IPicture} of this {@link IDataSource}.
 * 
 */
public interface IDataSource<PICTURE extends IPicture> extends
		IPictureList<PICTURE>, IWritableTagRepository
{
	/**
	 * Add an {@link IPicture} to the {@link IDataSource}.
	 * 
	 * @param pictureFile
	 *            the path to the {@link IPicture} to add.
	 * @throws PictureAddException
	 *             if an error occurs during the addition of the
	 *             {@link IPicture}.
	 */
	void addPicture(Path pictureFile) throws PictureAddException;

	/**
	 * Add all {@link IPicture}s from a directory to the {@link IDataSource}.
	 * 
	 * @param pictureDirectory
	 *            the path to the directory.
	 * @return object containing information about every added pictures.
	 * @throws PictureAddException
	 *             if an error occurs during the addition of the
	 *             {@link IPicture}.
	 */
	PictureAddResult addDirectory(Path pictureDirectory)
			throws PictureAddException;

	/**
	 * Create the thumbnail for the specified picture.
	 * 
	 * @param picture
	 *            the picture.
	 */
	void createThumbnail(PICTURE picture);

	/**
	 * Get the id of this {@link IDataSource}.
	 * 
	 * @return the id of this {@link IDataSource}.
	 */
	int getId();

	/**
	 * Close this {@link IDataSource}.
	 */
	void close();

	/**
	 * Get an {@link IPictureBrowser} browsing all the pictures of this
	 * {@link IDataSource}.
	 * 
	 * @return an {@link IPictureBrowser}.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the first
	 *             picture.
	 */
	IPictureBrowser<PICTURE> getAllPictures() throws ExecutionException;

	/**
	 * Get an {@link IPictureBrowser} browsing a selection of pictures from this
	 * {@link IDataSource}.
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
	IPictureBrowser<PICTURE> filterPictures(Query query, int iLimit)
			throws IOException, ExecutionException;
}
