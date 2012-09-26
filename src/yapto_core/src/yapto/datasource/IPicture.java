package yapto.datasource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import yapto.datasource.tag.ITag;

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
	Set<ITag> getTagSet();

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
	 * Add a {@link ITag} to this {@link IPicture}.
	 * 
	 * The {@link ITag} must be added to the list of {@link ITag}s known to the
	 * {@link IDataSource}.
	 * 
	 * @param newTag
	 *            the {@link ITag} to add to the picture.
	 */
	void addTag(ITag newTag);

	/**
	 * Remove a {@link ITag} from this {@link IPicture}.
	 * 
	 * @param tag
	 *            the {@link ITag} to remove from the picture.
	 */
	void removeTag(ITag tag);

	/**
	 * Change the {@link List} of {@link ITag}s associated with this picture.
	 * 
	 * @param tags
	 *            the new {@link List} of {@link ITag}s associated with this
	 *            picture.
	 */
	void setTagList(List<ITag> tags);

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
