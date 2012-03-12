package yapto.datasource.dummy;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.OperationNotSupportedException;
import yapto.datasource.PictureChangedEvent;
import yapto.datasource.tag.Tag;

import com.google.common.eventbus.EventBus;

/**
 * Dummy implementation of {@link IPictureBrowser}.
 * 
 * @author benobiwan
 * 
 */
public final class DummyPictureBrowser implements IPictureBrowser<DummyPicture>
{
	/**
	 * Logger object.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DummyPictureBrowser.class);

	/**
	 * The {@link IPictureList} used as source for this {@link IPictureBrowser}.
	 */
	private IPictureList<? extends IPicture> _sourcePictureList;

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
	 * {@link EventBus} used to signal registered objects of changes in this
	 * {@link DummyPictureBrowser}.
	 */
	private final EventBus _bus;

	/**
	 * Creates a new DummyPictureBrowser using the specified
	 * {@link IPictureList} as source.
	 * 
	 * @param sourcePictureList
	 *            the {@link IPictureList} to use as source.
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in this {@link DummyPictureBrowser}.
	 */
	public DummyPictureBrowser(
			final IPictureList<DummyPicture> sourcePictureList,
			final EventBus bus)
	{
		_bus = bus;
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
	public IPictureList<DummyPicture> filterList(final IPictureFilter filter)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPictureList<? extends IPicture> mergeList(
			final IPictureList<IPicture> otherList)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPictureList<DummyPicture>> getParent()
			throws OperationNotSupportedException
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
				_pictureIterator.next();
				_currentPicture = _pictureIterator.previous();
				_bus.post(new PictureChangedEvent());
			}
			return _currentPicture;
		}
	}

	@Override
	public IPicture getNextPicture()
	{
		synchronized (_lockSourcePictureList)
		{
			if (_pictureIterator.hasNext())
			{
				if (!_pictureIterator.hasPrevious())
				{
					_pictureIterator.next();
				}
				_currentPicture = _pictureIterator.next();
				_bus.post(new PictureChangedEvent());
			}
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
			if (_pictureIterator.hasPrevious())
			{
				if (!_pictureIterator.hasNext())
				{
					_pictureIterator.previous();
				}
				_currentPicture = _pictureIterator.previous();
				_bus.post(new PictureChangedEvent());
			}
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
	public void changeSourcePictureList(
			final IPictureList<? extends IPicture> source)
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

	@Override
	public Tag getRootTag()
	{
		return _sourcePictureList.getRootTag();
	}

	@Override
	public void register(final Object object)
	{
		_bus.register(object);
	}

	@Override
	public ListIterator<DummyPicture> getPictureIterator()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
