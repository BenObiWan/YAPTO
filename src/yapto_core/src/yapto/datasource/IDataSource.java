package yapto.datasource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
	 */
	void addPicture(File picturePath) throws FileNotFoundException, IOException;
}
