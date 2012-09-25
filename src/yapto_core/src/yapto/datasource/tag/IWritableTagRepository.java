package yapto.datasource.tag;

/**
 * Interface describing an object used to load and save {@link Tag}s.
 * 
 * @author benobiwan
 * 
 */
public interface IWritableTagRepository extends ITagRepository
{
	/**
	 * Add a new {@link Tag} to the list of {@link Tag}s know to this
	 * {@link IWritableTagRepository}.
	 * 
	 * @param parent
	 *            the parent of this {@link Tag}.
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link Tag}.
	 */
	void addTag(final Tag parent, final String strName,
			final String strDescription, final boolean bSelectable)
			throws TagAddException;

	/**
	 * Add a new {@link Tag} to the list of {@link Tag}s know to this
	 * {@link IWritableTagRepository}.
	 * 
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link Tag}.
	 */
	void addTag(final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException;

	/**
	 * Edit the specified {@link Tag} of this {@link IWritableTagRepository}.
	 * 
	 * @param iTagId
	 *            the id of the {@link Tag} to edit.
	 * @param parent
	 *            the parent of this {@link Tag}.
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 * @throws TagAddException
	 *             if an error occurs during the addition of the {@link Tag}.
	 */
	void editTag(final int iTagId, final Tag parent, final String strName,
			final String strDescription, final boolean bSelectable)
			throws TagAddException;
}
