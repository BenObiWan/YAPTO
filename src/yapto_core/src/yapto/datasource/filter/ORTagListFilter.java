package yapto.datasource.filter;

import java.util.SortedSet;
import java.util.TreeSet;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureFilter;
import yapto.datasource.tag.Tag;

/**
 * An {@link IPictureFilter} keeping {@link IPicture} that have at least one of
 * the specified {@link Tag}.
 * 
 * @author benobiwan
 * 
 */
public class ORTagListFilter implements IPictureFilter
{
	/**
	 * List of {@link Tag} for this filter.
	 */
	private final SortedSet<Tag> _tagSet = new TreeSet<Tag>();

	/**
	 * Creates a new ORTagListFilter.
	 * 
	 * @param tags
	 *            the list of {@link Tag} to use.
	 */
	public ORTagListFilter(final Tag... tags)
	{
		for (final Tag tag : tags)
		{
			_tagSet.add(tag);
		}
	}

	@Override
	public boolean filterPicture(final IPicture picture)
	{
		for (final Tag tag : picture.getTagSet())
		{
			if (_tagSet.contains(tag))
			{
				return true;
			}
		}
		return false;
	}

}
