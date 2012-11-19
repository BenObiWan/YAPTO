package yapto.picturebank.tag;

/**
 * Interface describing an object used to load and save {@link ITag}s.
 * 
 * @author benobiwan
 * 
 */
public interface IWritableTagRepository extends ITagRepository
{
	/**
	 * Add a new {@link ITag} to the list of {@link ITag}s know to this
	 * {@link IWritableTagRepository}.
	 * 
	 * @param iParentId
	 *            id of the parent of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link ITag}.
	 */
	void addTag(final int iParentId, final String strName,
			final String strDescription, final boolean bSelectable)
			throws TagAddException;

	/**
	 * Add a new {@link ITag} to the list of {@link ITag}s know to this
	 * {@link IWritableTagRepository}.
	 * 
	 * @param parent
	 *            the parent of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link ITag}.
	 */
	void addTag(final ITag parent, final String strName,
			final String strDescription, final boolean bSelectable)
			throws TagAddException;

	/**
	 * Add a new {@link ITag} to the list of {@link ITag}s know to this
	 * {@link IWritableTagRepository}.
	 * 
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link ITag}.
	 */
	void addTag(final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException;

	/**
	 * Edit the specified {@link ITag} of this {@link IWritableTagRepository}.
	 * 
	 * @param iTagId
	 *            the id of the {@link ITag} to edit.
	 * @param iParentId
	 *            id of the parent of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link ITag}.
	 */
	void editTag(final int iTagId, final int iParentId, final String strName,
			final String strDescription, final boolean bSelectable)
			throws TagAddException;

	/**
	 * Edit the specified {@link ITag} of this {@link IWritableTagRepository}.
	 * 
	 * @param iTagId
	 *            the id of the {@link ITag} to edit.
	 * @param parent
	 *            the parent of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link ITag}.
	 */
	void editTag(final int iTagId, final ITag parent, final String strName,
			final String strDescription, final boolean bSelectable)
			throws TagAddException;

	/**
	 * Remove the specified {@link ITag} from this
	 * {@link IWritableTagRepository}.
	 * 
	 * @param iTagId
	 *            the id of the {@link ITag} to remove.
	 * @throws TagAddException
	 *             if an error occurs during the removal of the {@link ITag}.
	 */
	void removeTag(final int iTagId) throws TagAddException;
}
