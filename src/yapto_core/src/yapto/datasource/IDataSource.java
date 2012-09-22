package yapto.datasource;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.lucene.search.Query;

import yapto.datasource.tag.Tag;

/**
 * An interface describing a source for {@link IPicture}.
 * 
 * @author benobiwan
 * @param <PICTURE>
 *            type of {@link IPicture} of this {@link IDataSource}.
 * 
 */
public interface IDataSource<PICTURE extends IPicture> extends
		IPictureList<PICTURE>
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
	void addPicture(File pictureFile) throws PictureAddException;

	/**
	 * Add a new {@link Tag} to the list of {@link Tag}s know to this
	 * {@link IDataSource}.
	 * 
	 * @param parent
	 *            the parent of this {@link Tag}.
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link Tag}.
	 */
	void addTag(final Tag parent, final String strName,
			final String strDescription, final boolean bSelectable)
			throws TagAddException;

	/**
	 * Add a new {@link Tag} to the list of {@link Tag}s know to this
	 * {@link IDataSource}.
	 * 
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link Tag}.
	 */
	void addTag(final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException;

	/**
	 * Check whether the datasource has a {@link Tag} with the give {@link Tag}.
	 * 
	 * @param strName
	 *            the name to check.
	 * @return true if the datasource has a {@link Tag} with the give
	 *         {@link Tag}.
	 */
	boolean hasTagNamed(final String strName);

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
