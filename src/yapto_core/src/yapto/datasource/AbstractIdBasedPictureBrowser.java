package yapto.datasource;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.datasource.sqlfile.FsPicture;

import com.google.common.eventbus.EventBus;

/**
 * Abstract implementation if the {@link IPictureBrowser} interface based on a
 * list of picture ids.
 * 
 * @author benobiwan
 * 
 * @param <PICTURE>
 */
public abstract class AbstractIdBasedPictureBrowser<PICTURE extends IPicture>
		extends AbstractPictureBrowser<PICTURE>
{
	/**
	 * Logger object.
	 */
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractIdBasedPictureBrowser.class);

	/**
	 * {@link List} on the {@link FsPicture} id.
	 */
	protected final List<String> _idList;

	/**
	 * Id of the picture currently selected.
	 */
	protected int _iCurrentId = 0;

	/**
	 * Creates a new AbstractIdBasedPictureBrowser.
	 * 
	 * @param dataSource
	 *            the {@link IDataSource} for this {@link IPictureBrowser}.
	 * @param query
	 *            the {@link Query} which created this {@link IPictureBrowser}.
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in this {@link AbstractPictureBrowser}.
	 * @param idList
	 *            {@link List} containing the ids of the picture contained in
	 *            this AbstractIdBasedPictureBrowser.
	 */
	public AbstractIdBasedPictureBrowser(final IDataSource<PICTURE> dataSource,
			final Query query, final EventBus bus, final List<String> idList)
	{
		super(dataSource, query, bus);
		_idList = idList;
	}

	@Override
	public boolean hasNext()
	{
		synchronized (_lock)
		{
			return _iCurrentId < _idList.size();
		}
	}

	@Override
	public boolean hasPrevious()
	{
		synchronized (_lock)
		{
			return _iCurrentId > 0;
		}
	}

	@Override
	public PICTURE next()
	{
		synchronized (_lock)
		{
			try
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("before next; has previous " + hasPrevious()
							+ " previous id " + previousIndex() + "; has next "
							+ hasNext() + " next id " + nextIndex());
				}
				if (hasNext())
				{
					_iCurrentId++;
					_currentPicture = getPicture(_idList.get(_iCurrentId));
					_bus.post(new PictureChangedEvent());
				}
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("after next; has previous " + hasPrevious()
							+ " previous id " + previousIndex() + "; has next "
							+ hasNext() + " next id " + nextIndex());
				}
				return _currentPicture;
			}
			catch (final ExecutionException e)
			{
				LOGGER.error("can't load next picture.", e);
				return null;
			}
		}
	}

	@Override
	public PICTURE previous()
	{
		synchronized (_lock)
		{
			try
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("before previous; has previous "
							+ hasPrevious() + " previous id " + previousIndex()
							+ "; has next " + hasNext() + " next id "
							+ nextIndex());
				}
				if (hasPrevious())
				{
					_iCurrentId--;
					_currentPicture = getPicture(_idList.get(_iCurrentId));
					_bus.post(new PictureChangedEvent());
				}
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("after previous; has previous "
							+ hasPrevious() + " previous id " + previousIndex()
							+ "; has next " + hasNext() + " next id "
							+ nextIndex());
				}
				return _currentPicture;
			}
			catch (final ExecutionException e)
			{
				LOGGER.error("can't load previous picture.", e);
				return null;
			}
		}
	}

	@Override
	public int nextIndex()
	{
		synchronized (_lock)
		{
			return _iCurrentId + 1;
		}
	}

	@Override
	public int previousIndex()
	{
		synchronized (_lock)
		{
			return _iCurrentId - 1;
		}
	}

	@Override
	public int getCurrentIndex()
	{
		synchronized (_lock)
		{
			return _iCurrentId;
		}
	}

	@Override
	public int getPictureCount()
	{
		return _idList.size();
	}

	@Override
	public IPicture[] nextPictures(final int iNbr) throws ExecutionException
	{
		int iFirstIndex;
		synchronized (_lock)
		{
			iFirstIndex = _iCurrentId + 1;
		}
		return getPictures(iFirstIndex, iFirstIndex + iNbr);
	}

	@Override
	public IPicture[] previousPictures(final int iNbr)
			throws ExecutionException
	{
		int iFirstIndex;
		synchronized (_lock)
		{
			iFirstIndex = _iCurrentId - 1;
		}
		return getPictures(iFirstIndex, iFirstIndex - iNbr);
	}

	@Override
	public IPicture[] getPictures(final int iBeginIndex, final int iEndIndex)
			throws ExecutionException
	{
		boolean bReverseOrder = false;
		int iRealBeginIndex;
		int iRealEndIndex;
		if (iBeginIndex > iEndIndex)
		{
			bReverseOrder = true;
			iRealBeginIndex = iEndIndex;
			iRealEndIndex = iBeginIndex;
		}
		else
		{
			iRealBeginIndex = iBeginIndex;
			iRealEndIndex = iEndIndex;
		}
		if (iRealBeginIndex < 0)
		{
			iRealBeginIndex = 0;
		}
		if (iRealEndIndex > _idList.size())
		{
			iRealEndIndex = _idList.size();
		}
		final List<String> ids = _idList
				.subList(iRealBeginIndex, iRealEndIndex);
		if (bReverseOrder)
		{
			Collections.reverse(ids);
		}
		final IPicture[] array = new IPicture[ids.size()];
		for (int i = 0; i < ids.size(); i++)
		{
			array[i] = getPicture(ids.get(i));
		}
		return array;
	}

	/**
	 * Get the {@link IPicture} with the specified id.
	 * 
	 * @param pictureId
	 *            the id of the picture.
	 * @return the requested {@link IPicture}.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the picture.
	 */
	protected abstract PICTURE getPicture(String pictureId)
			throws ExecutionException;
}
