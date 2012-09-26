package yapto.datasource.tag;

import java.util.Set;

/**
 * Interface describing an object used to load {@link ITag}s.
 * 
 * @author benobiwan
 * 
 */
public interface ITagRepository
{
	/**
	 * Get the {@link ITag} with the given id.
	 * 
	 * @param iTagId
	 *            the id of the {@link ITag}.
	 * @return the {@link ITag} with the given id.
	 */
	ITag get(final int iTagId);

	/**
	 * Get the {@link ITag} with the given id.
	 * 
	 * @param iTagId
	 *            the id of the {@link ITag}.
	 * @return the {@link ITag} with the given id.
	 */
	ITag get(final Integer iTagId);

	/**
	 * Get the {@link ITag} with the given name.
	 * 
	 * @param strTagName
	 *            the name of the {@link ITag}.
	 * @return the {@link ITag} with the given name.
	 */
	ITag get(String strTagName);

	/**
	 * Check whether the repository has a {@link ITag} with the give
	 * {@link ITag}.
	 * 
	 * @param strName
	 *            the name to check.
	 * @return true if the repository has a {@link ITag} with the give
	 *         {@link ITag}.
	 */
	boolean hasTagNamed(final String strName);

	/**
	 * Get a Set of all {@link ITag}s in this repository.
	 * 
	 * @return a Set of all {@link ITag}s in this repository.
	 */
	Set<ITag> getTagSet();

	/**
	 * Get the root {@link ITag} of this repository.
	 * 
	 * @return the root {@link ITag} of this repository.
	 */
	ITag getRootTag();
}
