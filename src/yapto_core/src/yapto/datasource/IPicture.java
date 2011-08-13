package yapto.datasource;

import java.awt.Image;
import java.io.IOException;
import java.util.List;

/**
 * An interface describing a picture.
 * 
 * @author benobiwan
 * 
 */
public interface IPicture extends Comparable<IPicture>
{
	/**
	 * Get the id of this {@link IPicture}.
	 * 
	 * @return the id of this {@link IPicture}.
	 */
	String getId();

	/**
	 * Get the list of {@link ITag}s associated with this {@link IPicture}.
	 * 
	 * @return the list of {@link ITag}s associated with this {@link IPicture}.
	 */
	List<ITag> getTagList();

	/**
	 * Get the data of the main image of this {@link IPicture}.
	 * 
	 * @return the data of the main image of this {@link IPicture}.
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	Image getImageData() throws IOException;

	/**
	 * Get the time stamp of the last modification of this {@link IPicture}.
	 * 
	 * @return the time stamp of the last modification of this {@link IPicture}.
	 */
	long getTimestamp();

	/**
	 * Get the {@link IDataSource} from which this {@link IPicture} is coming.
	 * 
	 * @return the {@link IDataSource} from which this {@link IPicture} is
	 *         coming.
	 */
	IDataSource getDataSource();
}
