package yapto.datasource.tag;

import java.util.Set;

/**
 * Interface describing an object used to load {@link Tag}s.
 * 
 * @author benobiwan
 * 
 */
public interface ITagRepository
{
	/**
	 * Get the {@link Tag} with the given id.
	 * 
	 * @param iTagId
	 *            the id of the {@link Tag}.
	 * @return the {@link Tag} with the given id.
	 */
	Tag get(final int iTagId);

	/**
	 * Get the {@link Tag} with the given id.
	 * 
	 * @param iTagId
	 *            the id of the {@link Tag}.
	 * @return the {@link Tag} with the given id.
	 */
	Tag get(final Integer iTagId);

	/**
	 * Get the {@link Tag} with the given name.
	 * 
	 * @param strTagName
	 *            the name of the {@link Tag}.
	 * @return the {@link Tag} with the given name.
	 */
	Tag get(String strTagName);

	/**
	 * Check whether the repository has a {@link Tag} with the give {@link Tag}.
	 * 
	 * @param strName
	 *            the name to check.
	 * @return true if the repository has a {@link Tag} with the give
	 *         {@link Tag}.
	 */
	boolean hasTagNamed(final String strName);

	/**
	 * Get a Set of all {@link Tag}s in this repository.
	 * 
	 * @return a Set of all {@link Tag}s in this repository.
	 */
	Set<Tag> getTagSet();

	/**
	 * Get the root {@link Tag} of this repository.
	 * 
	 * @return the root {@link Tag} of this repository.
	 */
	Tag getRootTag();
}
