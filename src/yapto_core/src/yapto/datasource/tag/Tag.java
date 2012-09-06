package yapto.datasource.tag;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;

/**
 * An class describing a tag associated to an {@link IPicture}.
 * 
 * Unselectable {@link Tag}s are aimed at organizing other {@link Tag}s has a
 * Tree.
 * 
 * @author benobiwan
 * 
 */
public final class Tag implements Comparable<Tag>
{
	/**
	 * The parent of this {@link Tag}.
	 */
	private Tag _parentTag;

	/**
	 * Lock to protect access to the parent {@link Tag}.
	 */
	private final Object _parentLock = new Object();

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
	 * Id of the {@link IDataSource}.
	 */
	private final int _iDatasourceId;

	/**
	 * Id of this {@link Tag}.
	 */
	private final int _iTagId;

	/**
	 * Id of this {@link Tag} as a String.
	 */
	private final String _strTagId;

	/**
	 * Set containing the children of this {@link Tag}.
	 */
	private final ConcurrentSkipListSet<Tag> _childrenSet = new ConcurrentSkipListSet<>();

	/**
	 * Creates a new Tag.
	 * 
	 * @param iDatasourceId
	 *            id of the {@link IDataSource}.
	 * @param iTagId
	 *            id of this {@link Tag}.
	 * @param parent
	 *            the parent of this {@link Tag}.
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 */
	public Tag(final int iDatasourceId, final int iTagId, final Tag parent,
			final String strName, final String strDescription,
			final boolean bSelectable)
	{
		_iDatasourceId = iDatasourceId;
		_iTagId = iTagId;
		_strTagId = String.valueOf(_iTagId);
		synchronized (_parentLock)
		{
			_parentTag = parent;
		}
		_strName = strName;
		_strDescription = strDescription;
		_bSelectable = bSelectable;
	}

	/**
	 * Creates a new Tag.
	 * 
	 * @param iDatasourceId
	 *            id of the {@link IDataSource}.
	 * @param iTagId
	 *            id of this {@link Tag}.
	 * @param parent
	 *            the parent of this {@link Tag}.
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 * @param children
	 *            the children of this {@link Tag}.
	 */
	public Tag(final int iDatasourceId, final int iTagId, final Tag parent,
			final String strName, final String strDescription,
			final boolean bSelectable, final Set<Tag> children)
	{
		this(iDatasourceId, iTagId, parent, strName, strDescription,
				bSelectable);
		_childrenSet.addAll(children);
	}

	/**
	 * Creates a new Tag.
	 * 
	 * @param iDatasourceId
	 *            id of the {@link IDataSource}.
	 * @param iTagId
	 *            id of this {@link Tag}.
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 */
	public Tag(final int iDatasourceId, final int iTagId, final String strName,
			final String strDescription, final boolean bSelectable)
	{
		this(iDatasourceId, iTagId, null, strName, strDescription, bSelectable);
	}

	/**
	 * Creates a new Tag.
	 * 
	 * @param iDatasourceId
	 *            id of the {@link IDataSource}.
	 * @param iTagId
	 *            id of this {@link Tag}.
	 * @param strName
	 *            the name of this {@link Tag}.
	 * @param strDescription
	 *            the description of this {@link Tag}.
	 * @param bSelectable
	 *            whether or not this {@link Tag} is selectable.
	 * @param children
	 *            the children of this {@link Tag}.
	 */
	public Tag(final int iDatasourceId, final int iTagId, final String strName,
			final String strDescription, final boolean bSelectable,
			final Set<Tag> children)
	{
		this(iDatasourceId, iTagId, null, strName, strDescription, bSelectable,
				children);
	}

	/**
	 * Get the parent of this {@link Tag}.
	 * 
	 * @return the parent of this {@link Tag}.
	 */
	public Tag getParent()
	{
		synchronized (_parentLock)
		{
			return _parentTag;
		}
	}

	/**
	 * Set the parent of this {@link Tag}.
	 * 
	 * @param parentTag
	 *            the new parent of this {@link Tag}.
	 */
	public void setParent(final Tag parentTag)
	{
		synchronized (_parentLock)
		{
			_parentTag = parentTag;
		}
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

	@Override
	public String toString()
	{
		return getName();
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

	/**
	 * Get the id of the {@link IDataSource}.
	 * 
	 * @return the id of the {@link IDataSource}.
	 */
	public int getDatasourceId()
	{
		return _iDatasourceId;
	}

	/**
	 * Get the id of this {@link Tag}.
	 * 
	 * @return the id of this {@link Tag}.
	 */
	public int getTagId()
	{
		return _iTagId;
	}

	/**
	 * Get the id of this {@link Tag} as a String.
	 * 
	 * @return the String representation of the id of this {@link Tag}.
	 */
	public String getTagIdAsString()
	{
		return _strTagId;
	}

	@Override
	public int compareTo(final Tag arg0)
	{
		int iComp = 0;
		iComp += _iDatasourceId;
		iComp -= arg0.getDatasourceId();
		if (iComp == 0)
		{
			iComp += _iTagId;
			iComp -= arg0.getTagId();
			if (iComp == 0)
			{
				iComp = _strName.compareTo(arg0.getName());
				if (iComp == 0)
				{
					iComp = _strDescription.compareTo(arg0.getDescription());
					if (iComp == 0)
					{
						if (_bSelectable == arg0.isSelectable())
						{
							synchronized (_parentLock)
							{
								if (_parentTag != null
										&& arg0.getParent() != null)
								{
									iComp = _parentTag.getName().compareTo(
											arg0.getParent().getName());
								}
								else
								{
									iComp = 0;
								}
							}
						}
						else if (_bSelectable)
						{
							iComp = 1;
						}
						else
						{
							iComp = -1;
						}
					}
				}
			}
		}
		return iComp;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (_bSelectable ? 1231 : 1237);
		result = prime * result + _iDatasourceId;
		result = prime * result + _iTagId;
		result = prime * result
				+ ((_parentTag == null) ? 0 : _parentTag.hashCode());
		result = prime * result
				+ ((_strDescription == null) ? 0 : _strDescription.hashCode());
		result = prime * result
				+ ((_strName == null) ? 0 : _strName.hashCode());
		result = prime * result
				+ ((_strTagId == null) ? 0 : _strTagId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Tag other = (Tag) obj;
		if (_bSelectable != other._bSelectable)
		{
			return false;
		}
		if (_iDatasourceId != other._iDatasourceId)
		{
			return false;
		}
		if (_iTagId != other._iTagId)
		{
			return false;
		}
		if (_parentTag == null)
		{
			if (other._parentTag != null)
			{
				return false;
			}
		}
		else if (!_parentTag.equals(other._parentTag))
		{
			return false;
		}
		if (_strDescription == null)
		{
			if (other._strDescription != null)
			{
				return false;
			}
		}
		else if (!_strDescription.equals(other._strDescription))
		{
			return false;
		}
		if (_strName == null)
		{
			if (other._strName != null)
			{
				return false;
			}
		}
		else if (!_strName.equals(other._strName))
		{
			return false;
		}
		if (_strTagId == null)
		{
			if (other._strTagId != null)
			{
				return false;
			}
		}
		else if (!_strTagId.equals(other._strTagId))
		{
			return false;
		}
		return true;
	}

}
