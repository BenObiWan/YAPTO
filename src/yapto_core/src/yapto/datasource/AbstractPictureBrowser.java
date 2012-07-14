package yapto.datasource;

import java.util.Set;

import org.apache.lucene.search.Query;

import yapto.datasource.tag.Tag;

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
	 * The {@link IDataSource} for this {@link IPictureBrowser}.
	 */
	protected final IDataSource<PICTURE> _dataSource;

	/**
	 * the {@link Query} which created this {@link IPictureBrowser}.
	 */
	protected final Query _query;

	/**
	 * Creates a new AbstractPictureBrowser.
	 * 
	 * @param dataSource
	 *            the {@link IDataSource} for this {@link IPictureBrowser}.
	 * @param query
	 *            the {@link Query} which created this {@link IPictureBrowser}.
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in this {@link AbstractPictureBrowser}.
	 */
	protected AbstractPictureBrowser(final IDataSource<PICTURE> dataSource,
			final Query query, final EventBus bus)
	{
		_bus = bus;
		_dataSource = dataSource;
		_query = query;
	}

	@Override
	public PICTURE getCurrentPicture()
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
	public IDataSource<PICTURE> getDataSource()
	{
		return _dataSource;
	}

	@Override
	public Set<Tag> getTagSet() throws OperationNotSupportedException
	{
		return _dataSource.getTagSet();
	}

	@Override
	public Tag getRootTag()
	{
		return _dataSource.getRootTag();
	}

	@Override
	public Query getQuery()
	{
		return _query;
	}
}
