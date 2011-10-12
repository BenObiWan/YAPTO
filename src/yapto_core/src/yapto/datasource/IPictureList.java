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
	 * @throws OperationNotSupportedException
	 *             if this operation isn't supported by this
	 *             {@link IPictureList}.
	 */
	int getPictureCount() throws OperationNotSupportedException;

	/**
	 * Filter the list of {@link IPicture} in this {@link IPictureList} with the
	 * specified {@link IPictureFilter}.
	 * 
	 * @param filter
	 *            the {@link IPictureFilter} to use to filter the list of
	 *            {@link IPicture}.
	 * @return a {@link IPictureList} based on the current {@link IPictureList}
	 *         and the specified {@link IPictureFilter}.
	 * @throws OperationNotSupportedException
	 *             if this operation isn't supported by this
	 *             {@link IPictureList}.
	 */
	IPictureList filterList(IPictureFilter filter)
			throws OperationNotSupportedException;

	/**
	 * Merge this {@link IPictureList} with another {@link IPictureList}.
	 * 
	 * @param otherList
	 *            the {@link IPictureList} to merge with.
	 * @return a {@link IPictureList} containing the {@link IPicture} of the two
	 *         specified list.
	 * @throws OperationNotSupportedException
	 *             if this operation isn't supported by this
	 *             {@link IPictureList}.
	 */
	IPictureList mergeList(IPictureList otherList)
			throws OperationNotSupportedException;

	/**
	 * Get the list of parents of this {@link IPictureList}.
	 * 
	 * @return the list of parents of this {@link IPictureList}.
	 * @throws OperationNotSupportedException
	 *             if this operation isn't supported by this
	 *             {@link IPictureList}.
	 */
	List<IPictureList> getParent() throws OperationNotSupportedException;

	/**
	 * Get the list of {@link Tag}s associated with {@link IPicture} in this
	 * {@link IPictureList}.
	 * 
	 * @return the list of {@link Tag}s associated with {@link IPicture} in
	 *         this {@link IPictureList}.
	 * @throws OperationNotSupportedException
	 *             if this operation isn't supported by this
	 *             {@link IPictureList}.
	 */
	List<Tag> getTagList() throws OperationNotSupportedException;

	/**
	 * Get the {@link List} of all {@link IPicture}.
	 * 
	 * @return the {@link List} of all {@link IPicture}.
	 */
	List<IPicture> getPictureList();
}
