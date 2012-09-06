package yapto.datasource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import yapto.datasource.tag.Tag;

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
	 * Get the list of {@link Tag}s associated with this {@link IPicture}.
	 * 
	 * @return the list of {@link Tag}s associated with this {@link IPicture}.
	 */
	Set<Tag> getTagSet();

	/**
	 * Get the data of the main image of this {@link IPicture}.
	 * 
	 * @return the data of the main image of this {@link IPicture}.
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	BufferedImage getImageData() throws IOException;

	/**
	 * Get the data of the thumbnail image of this {@link IPicture}.
	 * 
	 * @return the data of the thumbnail image of this {@link IPicture}.
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	BufferedImage getThumbnailData() throws IOException;

	/**
	 * Get the time stamp of the last modification of this {@link IPicture}.
	 * 
	 * @return the time stamp of the last modification of this {@link IPicture}.
	 */
	long getModifiedTimestamp();

	/**
	 * Get the {@link IDataSource} from which this {@link IPicture} is coming.
	 * 
	 * @return the {@link IDataSource} from which this {@link IPicture} is
	 *         coming.
	 */
	IDataSource<? extends IPicture> getDataSource();

	/**
	 * Add a {@link Tag} to this {@link IPicture}.
	 * 
	 * The {@link Tag} must be added to the list of {@link Tag}s known to the
	 * {@link IDataSource}.
	 * 
	 * @param newTag
	 *            the {@link Tag} to add to the picture.
	 */
	void addTag(Tag newTag);

	/**
	 * Remove a {@link Tag} from this {@link IPicture}.
	 * 
	 * @param tag
	 *            the {@link Tag} to remove from the picture.
	 */
	void removeTag(Tag tag);

	/**
	 * Change the {@link List} of {@link Tag}s associated with this picture.
	 * 
	 * @param tags
	 *            the new {@link List} of {@link Tag}s associated with this
	 *            picture.
	 */
	void setTagList(List<Tag> tags);

	/**
	 * Get the grade of this picture.
	 * 
	 * @return the grade of this picture.
	 */
	int getPictureGrade();

	/**
	 * Set the grade of this picture.
	 * 
	 * @param iPictureGrade
	 *            the new grade of this picture.
	 */
	void setPictureGrade(final int iPictureGrade);

	/**
	 * Get the {@link PictureInformation} of this picture.
	 * 
	 * @return the {@link PictureInformation} of this picture.
	 */
	PictureInformation getPictureInformation();
}
