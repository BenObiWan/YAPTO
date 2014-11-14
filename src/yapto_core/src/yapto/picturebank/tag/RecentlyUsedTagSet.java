package yapto.picturebank.tag;

import java.util.Collections;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

public final class RecentlyUsedTagSet
{
	private final int _iMaxNumberOfTags;

	private final TreeSet<ITag> _alphabeticTagSet = new TreeSet<>();
	private final LinkedList<ITag> _orderedDeque = new LinkedList<>();
	private final Object _lock = new Object();

	public RecentlyUsedTagSet(final int iMaxNumberOfTags)
	{
		_iMaxNumberOfTags = iMaxNumberOfTags;
	}

	public SortedSet<ITag> getAlphabeticTagSet()
	{
		synchronized (_lock)
		{
			return Collections.unmodifiableSortedSet(_alphabeticTagSet);
		}
	}

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
