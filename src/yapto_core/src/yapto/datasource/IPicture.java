package yapto.datasource;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
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
	BufferedImage getImageData() throws IOException;

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

	/**
	 * Get the {@link Dimension} of this {@link IPicture}.
	 * 
	 * @return the {@link Dimension} of this {@link IPicture}.
	 */
	Dimension getDimension();

	/**
	 * Get the height of this {@link IPicture}.
	 * 
	 * @return the height of this {@link IPicture}.
	 */
	int getHeight();

	/**
	 * Get the width of this {@link IPicture}.
	 * 
	 * @return the width of this {@link IPicture}.
	 */
	int getWidth();
}
