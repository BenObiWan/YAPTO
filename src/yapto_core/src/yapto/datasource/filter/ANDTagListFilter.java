package yapto.datasource.filter;

import java.util.SortedSet;
import java.util.TreeSet;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureFilter;
import yapto.datasource.ITag;

/**
 * An {@link IPictureFilter} keeping {@link IPicture} that have all the
 * specified {@link ITag}.
 * 
 * @author benobiwan
 * 
 */
public class ANDTagListFilter implements IPictureFilter
{
	/**
	 * Set of {@link ITag} for this filter.
	 */
	private final SortedSet<ITag> _tagSet = new TreeSet<ITag>();

	/**
	 * Creates a new ANDTagListFilter.
	 * 
	 * @param tags
	 *            the list of {@link ITag} to use.
	 */
	public ANDTagListFilter(final ITag... tags)
	{
		for (final ITag tag : tags)
		{
			_tagSet.add(tag);
		}
	}

	@Override
	public boolean filterPicture(final IPicture picture)
	{
		return picture.getTagList().containsAll(_tagSet);
	}

}
