package yapto.datasource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import yapto.datasource.tag.Tag;

/**
 * An interface describing a source for {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public interface IDataSource extends IPictureList
{
	/**
	 * Add an {@link IPicture} to the {@link IDataSource}.
	 * 
	 * @param picturePath
	 *            the path to the {@link IPicture} to add.
	 * @throws FileNotFoundException
	 *             if the file isn't found.
	 * @throws IOException
	 *             if an IO error occurs during the calculation of the checksum.
	 * @throws OperationNotSupportedException
	 *             if this operation isn't supported by this {@link IDataSource}
	 *             .
	 */
	void addPicture(File picturePath) throws OperationNotSupportedException,
			FileNotFoundException, IOException;

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
}
