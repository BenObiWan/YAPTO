package yapto.datasource.filter;

import java.util.SortedSet;
import java.util.TreeSet;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureFilter;
import yapto.datasource.Tag;

/**
 * An {@link IPictureFilter} keeping {@link IPicture} that have all the
 * specified {@link Tag}.
 * 
 * @author benobiwan
 * 
 */
public class ANDTagListFilter implements IPictureFilter
{
	/**
	 * Set of {@link Tag} for this filter.
	 */
	private final SortedSet<Tag> _tagSet = new TreeSet<Tag>();

	/**
	 * Creates a new ANDTagListFilter.
	 * 
	 * @param tags
	 *            the list of {@link Tag} to use.
	 */
	public ANDTagListFilter(final Tag... tags)
	{
		for (final Tag tag : tags)
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
