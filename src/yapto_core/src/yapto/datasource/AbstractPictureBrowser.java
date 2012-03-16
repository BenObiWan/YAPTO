package yapto.datasource;

import com.google.common.eventbus.EventBus;

/**
 * Abstract implementation if the {@link IPictureBrowser} interface.
 * 
 * @author benobiwan
 * 
 * @param <PICTURE>
 */
public abstract class AbstractPictureBrowser<PICTURE extends IPicture>
		implements IPictureBrowser<PICTURE>
{
	/**
	 * The picture currently displayed by this {@link AbstractPictureBrowser}.
	 */
	protected PICTURE _currentPicture;

	/**
	 * Lock used to protect the access to the current picture.
	 */
	protected final Object _lock = new Object();

	/**
	 * {@link EventBus} used to signal registered objects of changes in this
	 * {@link AbstractPictureBrowser}.
	 */
	protected final EventBus _bus;

	/**
	 * The source {@link IPictureList} for this {@link IPictureBrowser}.
	 */
	protected final IPictureList<PICTURE> _sourcePictureList;

	/**
	 * Creates a new AbstractPictureBrowser.
	 * 
	 * @param sourcePictureList
	 *            the source {@link IPictureList} for this
	 *            {@link IPictureBrowser}.
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in this {@link AbstractPictureBrowser}.
	 */
	protected AbstractPictureBrowser(
			final IPictureList<PICTURE> sourcePictureList, final EventBus bus)
	{
		_bus = bus;
		_sourcePictureList = sourcePictureList;
	}

	@Override
	public void add(final PICTURE arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void set(final PICTURE arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public IPicture getCurrentPicture()
	{
		synchronized (_lock)
		{
			if (_currentPicture == null)
			{
				_currentPicture = next();
			}
			return _currentPicture;
		}
	}

	@Override
	public void register(final Object object)
	{
		_bus.register(object);
	}

	@Override
	public void unRegister(final Object object)
	{
		_bus.unregister(object);
	}

	@Override
	public IPictureList<PICTURE> getSourcePictureList()
	{
		return _sourcePictureList;
	}
}
