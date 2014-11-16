package yapto.picturebank.tag;

import java.util.Collections;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Object that keep track of the recently used {@link ITag}s.
 * 
 * @author benobiwan
 *
 */
public final class RecentlyUsedTagSet
{
	/**
	 * Maximum number of {@link ITag}s in kept in history.
	 */
	private final int _iMaxNumberOfTags;

	/**
	 * Set to keep the {@link ITag}s in alphabetic order.
	 */
	private final TreeSet<ITag> _alphabeticTagSet = new TreeSet<>();

	/**
	 * List of the {@link ITag}s order with the latest used first.
	 */
	private final LinkedList<ITag> _orderedDeque = new LinkedList<>();

	/**
	 * Lock object to protect access to the list of {@link ITag}s.
	 */
	private final Object _lock = new Object();

	/**
	 * Create a new RecentlyUsedTagSet.
	 * 
	 * @param iMaxNumberOfTags
	 *            maximum number of {@link ITag}s in this RecentlyUsedTagSet.
	 */
	public RecentlyUsedTagSet(final int iMaxNumberOfTags)
	{
		_iMaxNumberOfTags = iMaxNumberOfTags;
	}

	/**
	 * Get the recently used {@link ITag}s ordered alphabetically.
	 * 
	 * @return the recently used {@link ITag}s ordered alphabetically.
	 */
	public SortedSet<ITag> getAlphabeticTagSet()
	{
		synchronized (_lock)
		{
			return Collections.unmodifiableSortedSet(_alphabeticTagSet);
		}
	}

	/**
	 * Mark an {@link ITag} as being the latest used {@link ITag}.
	 * 
	 * @param tag
	 *            the new latest used {@link ITag}.
	 */
	public void addLastUsed(final ITag tag)
	{
		synchronized (_lock)
		{
			if (_alphabeticTagSet.contains(tag))
			{
				// check if it's not already the first tag
				if (!tag.equals(_orderedDeque.peek()))
				{
					_orderedDeque.removeFirstOccurrence(tag);
					_orderedDeque.push(tag);
				}
			}
			else
			{
				// check if we need to remove a tag
				if (_alphabeticTagSet.size() == _iMaxNumberOfTags)
				{
					final ITag rem = _orderedDeque.pollLast();
					_alphabeticTagSet.remove(rem);
				}
				_alphabeticTagSet.add(tag);
				_orderedDeque.push(tag);
			}
		}
	}
}
