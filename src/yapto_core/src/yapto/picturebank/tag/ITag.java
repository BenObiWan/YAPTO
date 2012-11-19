package yapto.picturebank.tag;

import java.util.Set;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;

/**
 * An class describing a tag associated to an {@link IPicture}.
 * 
 * Not selectable {@link ITag}s are aimed at organizing other {@link ITag}s has
 * a Tree.
 * 
 * @author benobiwan
 * 
 */
public interface ITag extends Comparable<ITag>
{
	/**
	 * Get the parent of this {@link ITag}.
	 * 
	 * @return the parent of this {@link ITag}.
	 */
	ITag getParent();

	/**
	 * Get the id of the parent of this {@link ITag}.
	 * 
	 * @return the id of the parent of this {@link ITag}.
	 */
	int getParentId();

	/**
	 * Set the parent of this {@link ITag}.
	 * 
	 * @param iParentTagId
	 *            the id new parent of this {@link ITag}.
	 */
	void setParent(final int iParentTagId);

	/**
	 * Get the name of this {@link ITag}.
	 * 
	 * @return the name of this {@link ITag}.
	 */
	String getName();

	/**
	 * Get the description of this {@link ITag}.
	 * 
	 * @return the description of this {@link ITag}.
	 */
	String getDescription();

	/**
	 * Check whether this {@link ITag} can be selected or not.
	 * 
	 * @return true if this {@link ITag} can be selected.
	 */
	boolean isSelectable();

	/**
	 * Get the children of this {@link ITag}.
	 * 
	 * @return an unmodifiable set of the {@link ITag}s children of this
	 *         {@link ITag}.
	 */
	Set<ITag> getChildren();

	/**
	 * Add a child to this {@link ITag}.
	 * 
	 * @param child
	 *            the child to add.
	 */
	void addChild(final ITag child);

	/**
	 * Remove a child from this {@link ITag}.
	 * 
	 * @param child
	 *            the child to remove.
	 */
	void removeChild(final ITag child);

	/**
	 * Get the id of the {@link IPictureBank}.
	 * 
	 * @return the id of the {@link IPictureBank}.
	 */
	int getPictureBankId();

	/**
	 * Get the id of this {@link ITag}.
	 * 
	 * @return the id of this {@link ITag}.
	 */
	int getTagId();

	/**
	 * Get the id of this {@link ITag} as a String.
	 * 
	 * @return the String representation of the id of this {@link ITag}.
	 */
	String getTagIdAsString();
}
