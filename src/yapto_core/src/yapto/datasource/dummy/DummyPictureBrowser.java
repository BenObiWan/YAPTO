package yapto.datasource.dummy;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.Tag;
import yapto.datasource.OperationNotSupportedException;

/**
 * Dummy implementation of {@link IPictureBrowser}.
 * 
 * @author benobiwan
 * 
 */
public final class DummyPictureBrowser implements IPictureBrowser
{
	/**
	 * The {@link IPictureList} used as source for this {@link IPictureBrowser}.
	 */
	private IPictureList _sourcePictureList;

	/**
	 * {@link ListIterator}
	 */
	private ListIterator<IPicture> _pictureIterator;

	/**
	 * The picture currently displayed by this {@link DummyPictureBrowser}.
	 */
	private IPicture _currentPicture;

	/**
	 * Lock used to protect the access to the source {@link IPictureList}.
	 */
	private final Object _lockSourcePictureList = new Object();

	/**
	 * Creates a new DummyPictureBrowser using the specified
	 * {@link IPictureList} as source.
	 * 
	 * @param sourcePictureList
	 *            the {@link IPictureList} to use as source.
	 */
	public DummyPictureBrowser(final IPictureList sourcePictureList)
	{
		changeSourcePictureList(sourcePictureList);
	}

	@Override
	public int getPictureCount() throws OperationNotSupportedException
	{
		synchronized (_lockSourcePictureList)
		{
			return _sourcePictureList.getPictureCount();
		}
	}

	@Override
	public IPictureList filterList(final IPictureFilter filter)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPictureList mergeList(final IPictureList otherList)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPictureList> getParent() throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Tag> getTagSet() throws OperationNotSupportedException
	{
		synchronized (_lockSourcePictureList)
		{
			return _sourcePictureList.getTagSet();
		}
	}

	@Override
	public IPicture getCurrentPicture()
	{
		synchronized (_lockSourcePictureList)
		{
			if (_currentPicture == null)
			{
				_currentPicture = _pictureIterator.next();
			}
			return _currentPicture;
		}
	}

	@Override
	public IPicture getNextPicture()
	{
		synchronized (_lockSourcePictureList)
		{
			_currentPicture = _pictureIterator.next();
			return _currentPicture;
		}
	}

	@Override
	public boolean hasNextPicture()
	{
		synchronized (_lockSourcePictureList)
		{
			return _pictureIterator.hasNext();
		}
	}

	@Override
	public IPicture getPreviousPicture()
	{
		synchronized (_lockSourcePictureList)
		{
			_currentPicture = _pictureIterator.previous();
			return _currentPicture;
		}
	}

	@Override
	public boolean hasPreviousPicture()
	{
		synchronized (_lockSourcePictureList)
		{
			return _pictureIterator.hasPrevious();
		}
	}

	@Override
	public void changeSourcePictureList(final IPictureList source)
	{
		synchronized (_lockSourcePictureList)
		{
			_sourcePictureList = source;
			_pictureIterator = _sourcePictureList.getPictureList()
					.listIterator();
		}
	}

	@Override
	public List<IPicture> getPictureList()
	{
		synchronized (_lockSourcePictureList)
		{
			return _sourcePictureList.getPictureList();
		}
	}

}
