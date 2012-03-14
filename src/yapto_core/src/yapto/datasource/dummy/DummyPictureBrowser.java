package yapto.datasource.dummy;

import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.datasource.AbstractPictureBrowser;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.IPictureList;
import yapto.datasource.PictureChangedEvent;

import com.google.common.eventbus.EventBus;

/**
 * Dummy implementation of {@link IPictureBrowser}.
 * 
 * @author benobiwan
 * 
 */
public final class DummyPictureBrowser extends
		AbstractPictureBrowser<DummyPicture>
{
	/**
	 * Logger object.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DummyPictureBrowser.class);

	/**
	 * {@link ListIterator}
	 */
	private ListIterator<DummyPicture> _pictureIterator;

	/**
	 * Creates a new DummyPictureBrowser using the specified
	 * {@link IPictureList} as source.
	 * 
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in this {@link DummyPictureBrowser}.
	 */
	public DummyPictureBrowser(final EventBus bus)
	{
		super(bus);
	}

	@Override
	public DummyPicture next()
	{
		synchronized (_lock)
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
	public boolean hasNext()
	{
		return _pictureIterator.hasNext();
	}

	@Override
	public DummyPicture previous()
	{
		synchronized (_lock)
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
	public boolean hasPrevious()
	{
		return _pictureIterator.hasPrevious();
	}

	@Override
	public int nextIndex()
	{
		return _pictureIterator.nextIndex();
	}

	@Override
	public int previousIndex()
	{
		return _pictureIterator.previousIndex();
	}
}
