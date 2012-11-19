package yapto.picturebank;

import java.util.Set;

import org.apache.lucene.search.Query;

import yapto.picturebank.tag.ITag;

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
	 * The {@link IPictureBank} for this {@link IPictureBrowser}.
	 */
	protected final IPictureBank<PICTURE> _pictureBank;

	/**
	 * the {@link Query} which created this {@link IPictureBrowser}.
	 */
	protected final Query _query;

	/**
	 * Creates a new AbstractPictureBrowser.
	 * 
	 * @param pictureBank
	 *            the {@link IPictureBank} for this {@link IPictureBrowser}.
	 * @param query
	 *            the {@link Query} which created this {@link IPictureBrowser}.
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in this {@link AbstractPictureBrowser}.
	 */
	protected AbstractPictureBrowser(final IPictureBank<PICTURE> pictureBank,
			final Query query, final EventBus bus)
	{
		_bus = bus;
		_pictureBank = pictureBank;
		_query = query;
	}

	@Override
	public PICTURE getCurrentPicture()
	{
		synchronized (_lock)
		{
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
	public IPictureBank<PICTURE> getPictureBank()
	{
		return _pictureBank;
	}

	@Override
	public Set<ITag> getTagSet()
	{
		return _pictureBank.getTagSet();
	}

	@Override
	public ITag getRootTag()
	{
		return _pictureBank.getRootTag();
	}

	@Override
	public Query getQuery()
	{
		return _query;
	}

	@Override
	public ITag getTag(final int iTagId)
	{
		return _pictureBank.getTag(iTagId);
	}

	@Override
	public ITag getTag(final Integer iTagId)
	{
		return _pictureBank.getTag(iTagId);
	}

	@Override
	public ITag getTag(final String strTagName)
	{
		return _pictureBank.getTag(strTagName);
	}

	@Override
	public boolean hasTagNamed(final String strName)
	{
		return _pictureBank.hasTagNamed(strName);
	}
}
