package yapto.datasource.dummy;

import yapto.datasource.ITag;

/**
 * Dummy implementation of the {@link ITag} interface.
 * 
 * @author benobiwan
 * 
 */
public final class DummyTag implements ITag
{
	/**
	 * The parent of this {@link ITag}.
	 */
	private final ITag _parentTag;

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
	 * Creates a new DummyTag.
	 * 
	 * @param parent
	 *            the parent of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 * @param bSelectable
	 *            whether or not this {@link ITag} is selectable.
	 */
	public DummyTag(final ITag parent, final String strName,
			final String strDescription, final boolean bSelectable)
	{
		_parentTag = parent;
		_strName = strName;
		_strDescription = strDescription;
		_bSelectable = bSelectable;
	}

	@Override
	public ITag getParent()
	{
		return _parentTag;
	}

	@Override
	public String getName()
	{
		return _strName;
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
}
