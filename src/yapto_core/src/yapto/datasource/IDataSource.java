package yapto.datasource;

import java.io.File;
import java.io.IOException;

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
	 * Add a {@link Tag} to the list of {@link Tag}s know to this
	 * {@link IDataSource}.
	 * 
	 * @param newTag
	 *            the {@link Tag} to add.
	 */
	void addTag(Tag newTag);

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
	 */
	IPictureBrowser<PICTURE> getAllPictures();

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
	 */
	IPictureBrowser<PICTURE> filterPictures(Query query, int iLimit)
			throws IOException;
}
