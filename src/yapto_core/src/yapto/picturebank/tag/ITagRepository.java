package yapto.picturebank.tag;

import java.util.Set;
import java.util.SortedSet;

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
	ITag getTag(final int iTagId);

	/**
	 * Get the {@link ITag} with the given id.
	 * 
	 * @param iTagId
	 *            the id of the {@link ITag}.
	 * @return the {@link ITag} with the given id.
	 */
	ITag getTag(final Integer iTagId);

	/**
	 * Get the {@link ITag} with the given name.
	 * 
	 * @param strTagName
	 *            the name of the {@link ITag}.
	 * @return the {@link ITag} with the given name.
	 */
	ITag getTag(String strTagName);

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

	/**
	 * Get SortedSet of recently used {@link ITag}.
	 * 
	 * @return SortedSet of recently used {@link ITag}.
	 */
	SortedSet<ITag> getRecentlyUsed();

	/**
	 * Mark an {@link ITag} as being the last used {@link ITag}.
	 * 
	 * @param tag
	 *            the last used {@link ITag}.
	 */
	void addLastUsed(final ITag tag);
}
