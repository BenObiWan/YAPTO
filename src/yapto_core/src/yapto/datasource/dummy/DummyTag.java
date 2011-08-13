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
	 * Creates a new DummyTag.
	 * 
	 * @param parent
	 *            the parent of this {@link ITag}.
	 * @param strName
	 *            the name of this {@link ITag}.
	 * @param strDescription
	 *            the description of this {@link ITag}.
	 */
	public DummyTag(final ITag parent, final String strName,
			final String strDescription)
	{
		_parentTag = parent;
		_strName = strName;
		_strDescription = strDescription;
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
}
