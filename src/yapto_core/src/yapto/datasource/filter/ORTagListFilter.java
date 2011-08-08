package yapto.datasource.filter;

import java.util.SortedSet;
import java.util.TreeSet;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureFilter;
import yapto.datasource.ITag;

/**
 * An {@link IPictureFilter} keeping {@link IPicture} that have at least one of
 * the specified {@link ITag}.
 * 
 * @author benobiwan
 * 
 */
public class ORTagListFilter implements IPictureFilter
{
	/**
	 * List of {@link ITag} for this filter.
	 */
	private final SortedSet<ITag> _tagSet = new TreeSet<ITag>();

	/**
	 * Creates a new ORTagListFilter.
	 * 
	 * @param tags
	 *            the list of {@link ITag} to use.
	 */
	public ORTagListFilter(final ITag... tags)
	{
		for (final ITag tag : tags)
		{
			_tagSet.add(tag);
		}
	}

	@Override
	public boolean filterPicture(final IPicture picture)
	{
		for (final ITag tag : picture.getTagList())
		{
			if (_tagSet.contains(tag))
			{
				return true;
			}
		}
		return false;
	}

}
