package yapto.datasource;

import java.util.List;

/**
 * A list of {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public interface IPictureList
{
	/**
	 * Get the number of {@link IPicture} in this {@link IPictureList}.
	 * 
	 * @return the number of {@link IPicture} in this {@link IPictureList}.
	 */
	int getPictureCount();

	/**
	 * Filter the list of {@link IPicture} in this {@link IPictureList} with the
	 * specified {@link IPictureFilter}.
	 * 
	 * @param filter
	 *            the {@link IPictureFilter} to use to filter the list of
	 *            {@link IPicture}.
	 * @return a {@link IPictureList} based on the current {@link IPictureList}
	 *         and the specified {@link IPictureFilter}.
	 */
	IPictureList filterList(IPictureFilter filter);

	/**
	 * Merge this {@link IPictureList} with another {@link IPictureList}.
	 * 
	 * @param otherList
	 *            the {@link IPictureList} to merge with.
	 * @return a {@link IPictureList} containing the {@link IPicture} of the two
	 *         specified list.
	 */
	IPictureList mergeList(IPictureList otherList);

	/**
	 * Get the list of parents of this {@link IPictureList}.
	 * 
	 * @return the list of parents of this {@link IPictureList}.
	 */
	List<IPictureList> getParent();
}
