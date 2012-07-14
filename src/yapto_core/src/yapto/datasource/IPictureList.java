package yapto.datasource;

import java.util.Set;

import yapto.datasource.tag.Tag;

/**
 * A list of {@link IPicture}.
 * 
 * @author benobiwan
 * @param <PICTURE>
 *            type of {@link IPicture} of this {@link IPictureList}.
 */
public interface IPictureList<PICTURE extends IPicture>
{
	/**
	 * Get the number of {@link IPicture} in this {@link IPictureList}.
	 * 
	 * @return the number of {@link IPicture} in this {@link IPictureList}.
	 */
	int getPictureCount();

	/**
	 * Get the list of {@link Tag}s associated with {@link IPicture} in this
	 * {@link IPictureList}.
	 * 
	 * @return the list of {@link Tag}s associated with {@link IPicture} in this
	 *         {@link IPictureList}.
	 * @throws OperationNotSupportedException
	 *             if this operation isn't supported by this
	 *             {@link IPictureList}.
	 */
	Set<Tag> getTagSet() throws OperationNotSupportedException;

	/**
	 * Get the root {@link Tag} associated with {@link IPicture} in this
	 * {@link IPictureList}.
	 * 
	 * @return the root {@link Tag}.
	 */
	Tag getRootTag();
}
