package yapto.datasource;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * An class describing a tag associated to an {@link IPicture}.
 * 
 * Unselectable {@link Tag}s are aimed at organizing other {@link Tag}s has a
 * Tree.
 * 
 * @author benobiwan
 * 
 */
public final class Tag
{
	/**
	 * The parent of this {@link Tag}.
	 */
	private final Tag _parentTag;

	/**
	 * The name of this {@link Tag}.
	 */
	private final String _strName;

	/**
	 * The description of this {@link Tag}.
	 */
	private final String _strDescription;

	/**
	 * Whether or not this {@link Tag} is selectable.
	 */
	private final boolean _bSelectable;

	/**
	 * Set containing the children of this {@link Tag}.
	 */
	private final ConcurrentSkipListSet<Tag> _childrenSet = new ConcurrentSkipListSet<Tag>();

	/**
	 * Creates a new DummyTag.
	 * 
	 * @param parent
	 *            the parent of this {@link Tag}.
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 */
	public Tag(final Tag parent, final String strName,
			final String strDescription, final boolean bSelectable)
	{
		_parentTag = parent;
		_strName = strName;
		_strDescription = strDescription;
		_bSelectable = bSelectable;
		// TODO fill the children set
	}

	/**
	 * Get the parent of this {@link Tag}.
	 * 
	 * @return the parent of this {@link Tag}.
	 */
	public Tag getParent()
	{
		return _parentTag;
	}

	/**
	 * Get the name of this {@link Tag}.
	 * 
	 * @return the name of this {@link Tag}.
	 */
	public String getName()
	{
		return _strName;
	}

	/**
	 * Get the description of this {@link Tag}.
	 * 
	 * @return the description of this {@link Tag}.
	 */
	public String getDescription()
	{
		return _strDescription;
	}

	/**
	 * Check whether this {@link Tag} can be selected or not.
	 * 
	 * @return true if this {@link Tag} can be selected.
	 */
	public boolean isSelectable()
	{
		return _bSelectable;
	}

	/**
	 * Get the children of this {@link Tag}.
	 * 
	 * @return an unmodifiable set of the {@link Tag}s children of this
	 *         {@link Tag}.
	 */
	public Set<Tag> getChildren()
	{
		return Collections.unmodifiableSet(_childrenSet);
	}

	/**
	 * Add a child to this {@link Tag}.
	 * 
	 * @param child
	 *            the child to add.
	 */
	public void addChild(final Tag child)
	{
		_childrenSet.add(child);
	}

	/**
	 * Remove a child from this {@link Tag}.
	 * 
	 * @param child
	 *            the child to remove.
	 */
	public void removeChild(final Tag child)
	{
		_childrenSet.remove(child);
	}
}
