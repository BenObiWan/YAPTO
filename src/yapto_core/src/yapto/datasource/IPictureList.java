package yapto.datasource;

import yapto.datasource.tag.ITagRepository;

/**
 * A list of {@link IPicture}.
 * 
 * @author benobiwan
 * @param <PICTURE>
 *            type of {@link IPicture} of this {@link IPictureList}.
 */
public interface IPictureList<PICTURE extends IPicture> extends ITagRepository
{
	/**
	 * Get the number of {@link IPicture} in this {@link IPictureList}.
	 * 
	 * @return the number of {@link IPicture} in this {@link IPictureList}.
	 */
	int getPictureCount();
}
