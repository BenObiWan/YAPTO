package yapto.datasource.tag;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;

/**
 * An class describing a uneditable tag associated to an {@link IPicture}.
 * 
 * Not selectable {@link ITag}s are aimed at organizing other {@link ITag}s has
 * a Tree.
 * 
 * @author benobiwan
 * 
 */
public final class UneditableTag implements ITag
{
	/**
	 * The parent of this {@link ITag}.
	 */
	private ITag _parentTag;

	/**
	 * Lock to protect access to the parent {@link ITag}.
	 */
	private final Object _parentLock = new Object();

	/**
	 * The name of this {@link ITag}.
	 */
	private final String _strName;

	/**
	 * The description of this {@link ITag}.
	 */
	private final String _strDescription;

	/**
	 * Whether or not this {@link ITag} is selectable.
	 */
	private final boolean _bSelectable;

	/**
	 * Id of the {@link IDataSource}.
	 */
	private final int _iDatasourceId;

	/**
	 * Id of this {@link ITag}.
	 */
	private final int _iTagId;

	/**
	 * Id of this {@link ITag} as a String.
	 */
	private final String _strTagId;

	/**
	 * Set containing the children of this {@link ITag}.
	 */
	private final ConcurrentSkipListSet<ITag> _childrenSet = new ConcurrentSkipListSet<>();

	/**
	 * Creates a new Tag.
	 * 
	 * @param iDatasourceId
	 *            id of the {@link IDataSource}.
	 * @param iTagId
	 *            id of this {@link ITag}.
	 * @param parent
	 *            the parent of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 */
	public UneditableTag(final int iDatasourceId, final int iTagId,
			final ITag parent, final String strName,
			final String strDescription, final boolean bSelectable)
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
	 *            id of this {@link ITag}.
	 * @param parent
	 *            the parent of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 * @param children
	 *            the children of this {@link ITag}.
	 */
	public UneditableTag(final int iDatasourceId, final int iTagId,
			final ITag parent, final String strName,
			final String strDescription, final boolean bSelectable,
			final Set<ITag> children)
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
	 *            id of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 */
	public UneditableTag(final int iDatasourceId, final int iTagId,
			final String strName, final String strDescription,
			final boolean bSelectable)
	{
		this(iDatasourceId, iTagId, null, strName, strDescription, bSelectable);
	}

	/**
	 * Creates a new Tag.
	 * 
	 * @param iDatasourceId
	 *            id of the {@link IDataSource}.
	 * @param iTagId
	 *            id of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 * @param children
	 *            the children of this {@link ITag}.
	 */
	public UneditableTag(final int iDatasourceId, final int iTagId,
			final String strName, final String strDescription,
			final boolean bSelectable, final Set<ITag> children)
	{
		this(iDatasourceId, iTagId, null, strName, strDescription, bSelectable,
				children);
	}

	@Override
	public ITag getParent()
	{
		synchronized (_parentLock)
		{
			return _parentTag;
		}
	}

	@Override
	public void setParent(final ITag parentTag)
	{
		synchronized (_parentLock)
		{
			_parentTag = parentTag;
		}
	}

	@Override
	public String getName()
	{
		return _strName;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public String getDescription()
	{
		return _strDescription;
	}

	@Override
	public boolean isSelectable()
	{
		return _bSelectable;
	}

	@Override
	public Set<ITag> getChildren()
	{
		return Collections.unmodifiableSet(_childrenSet);
	}

	@Override
	public void addChild(final ITag child)
	{
		_childrenSet.add(child);
	}

	@Override
	public void removeChild(final ITag child)
	{
		_childrenSet.remove(child);
	}

	@Override
	public int getDatasourceId()
	{
		return _iDatasourceId;
	}

	@Override
	public int getTagId()
	{
		return _iTagId;
	}

	@Override
	public String getTagIdAsString()
	{
		return _strTagId;
	}

	@Override
	public int compareTo(final ITag arg0)
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
		final ITag other = (ITag) obj;
		if (_bSelectable != other.isSelectable())
		{
			return false;
		}
		if (_iDatasourceId != other.getDatasourceId())
		{
			return false;
		}
		if (_iTagId != other.getTagId())
		{
			return false;
		}
		if (_parentTag == null)
		{
			if (other.getParent() != null)
			{
				return false;
			}
		}
		else if (!_parentTag.equals(other.getParent()))
		{
			return false;
		}
		if (_strDescription == null)
		{
			if (other.getDescription() != null)
			{
				return false;
			}
		}
		else if (!_strDescription.equals(other.getDescription()))
		{
			return false;
		}
		if (_strName == null)
		{
			if (other.getName() != null)
			{
				return false;
			}
		}
		else if (!_strName.equals(other.getName()))
		{
			return false;
		}
		if (_strTagId == null)
		{
			if (other.getTagIdAsString() != null)
			{
				return false;
			}
		}
		else if (!_strTagId.equals(other.getTagIdAsString()))
		{
			return false;
		}
		return true;
	}
}
