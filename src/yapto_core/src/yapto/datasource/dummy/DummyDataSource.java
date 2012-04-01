package yapto.datasource.dummy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.datasource.AbstractPictureBrowser;
import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.OperationNotSupportedException;
import yapto.datasource.PictureChangedEvent;
import yapto.datasource.tag.Tag;

import com.google.common.eventbus.EventBus;

/**
 * Dummy implementation of the {@link IDataSource} interface.
 * 
 * @author benobiwan
 * 
 */
public final class DummyDataSource implements IDataSource<DummyPicture>
{
	/**
	 * Logger object.
	 */
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(DummyDataSource.class);

	/**
	 * Map containing all the picture and their id.
	 */
	private final ConcurrentSkipListMap<String, IPicture> _mapPicture = new ConcurrentSkipListMap<String, IPicture>();

	/**
	 * List of {@link IPicture} of this {@link DummyDataSource}.
	 */
	protected final List<DummyPicture> _listPicture = new CopyOnWriteArrayList<DummyPicture>();

	/**
	 * Set of all the {@link Tag}s available on this {@link IDataSource}.
	 */
	private final Set<Tag> _tagSet = new ConcurrentSkipListSet<Tag>();

	/**
	 * The root {@link Tag} of this {@link DummyDataSource}.
	 */
	private final Tag _rootTag;

	/**
	 * {@link EventBus} used to signal registered objects of changes in the
	 * {@link IPictureBrowser}.
	 */
	protected final EventBus _bus;

	/**
	 * Creates a new DummyDataSource.
	 * 
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in the {@link IPictureBrowser}.
	 */
	public DummyDataSource(final EventBus bus)
	{
		_bus = bus;
		final String[] fileList = { "/tmp/picture1.jpg", "/tmp/picture2.jpg",
				"/tmp/picture3.jpg", "/tmp/picture4.jpg" };
		int iId = 0;
		_rootTag = new Tag(getId(), iId++, null, "root", "", false);
		final Tag child1 = new Tag(getId(), iId++, _rootTag, "node1",
				"node1 desc", false);
		_rootTag.addChild(child1);
		final Tag child11 = new Tag(getId(), iId++, child1, "node1.1",
				"node1.1 desc", true);
		child1.addChild(child11);
		final Tag child12 = new Tag(getId(), iId++, child1, "node1.2",
				"node1.2 desc", true);
		child1.addChild(child12);

		final Tag child121 = new Tag(getId(), iId++, child1, "node1.2.1",
				"node1.2 desc", true);
		child12.addChild(child121);
		final Tag child122 = new Tag(getId(), iId++, child1, "node1.2.2",
				"node1.2 desc", true);
		child12.addChild(child122);

		final Tag child2 = new Tag(getId(), iId++, _rootTag, "node2",
				"node2 desc", true);
		_rootTag.addChild(child2);

		for (final String file : fileList)
		{
			try
			{
				addPicture(new File(file));
			}
			catch (final FileNotFoundException e)
			{
				LOGGER.error(e.getLocalizedMessage(), e);
			}
			catch (final IOException e)
			{
				LOGGER.error(e.getLocalizedMessage(), e);
			}
		}
	}

	@Override
	public int getPictureCount()
	{
		return _mapPicture.size();
	}

	@Override
	public IPictureList<DummyPicture> filterList(final IPictureFilter filter)
			throws OperationNotSupportedException
	{
		throw new OperationNotSupportedException();
	}

	@Override
	public IPictureList<? extends IPicture> mergeList(
			final IPictureList<IPicture> otherList)
			throws OperationNotSupportedException
	{
		throw new OperationNotSupportedException();
	}

	@Override
	public List<IPictureList<DummyPicture>> getParent()
			throws OperationNotSupportedException
	{
		throw new OperationNotSupportedException();
	}

	@Override
	public void addPicture(final File pictureFile)
			throws FileNotFoundException, IOException
	{
		final DummyPicture pict = new DummyPicture(this, pictureFile);
		_mapPicture.put(pict.getId(), pict);
		_listPicture.add(pict);
	}

	@Override
	public Set<Tag> getTagSet()
	{
		return Collections.unmodifiableSet(_tagSet);
	}

	@Override
	public List<IPicture> getPictureList()
	{
		return null;
	}

	@Override
	public void addTag(final Tag newTag)
	{
		_tagSet.add(newTag);
	}

	@Override
	public Tag getRootTag()
	{
		return _rootTag;
	}

	@Override
	public int getId()
	{
		return 1;
	}

	@Override
	public void close()
	{
		// nothing to do
	}

	@Override
	public IPictureBrowser<DummyPicture> getPictureIterator()
	{
		return new DummyPictureBrowser();
	}

	/**
	 * Dummy implementation of {@link IPictureBrowser}.
	 * 
	 * @author benobiwan
	 * 
	 */
	private final class DummyPictureBrowser extends
			AbstractPictureBrowser<DummyPicture>
	{
		/**
		 * {@link ListIterator}
		 */
		private final ListIterator<DummyPicture> _pictureIterator;

		/**
		 * Creates a new DummyPictureBrowser using the specified
		 * {@link IPictureList} as source.
		 */
		public DummyPictureBrowser()
		{
			super(DummyDataSource.this, DummyDataSource.this._bus);
			_pictureIterator = _listPicture.listIterator();
		}

		@Override
		public DummyPicture next()
		{
			synchronized (_lock)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("before next");
					LOGGER.debug("has previous " + hasPrevious()
							+ " previous id " + previousIndex());
					LOGGER.debug("has next " + hasNext() + " next id "
							+ nextIndex());
				}
				if (_pictureIterator.hasNext())
				{
					_currentPicture = _pictureIterator.next();
					_bus.post(new PictureChangedEvent());
				}
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("after next");
					LOGGER.debug("has previous " + hasPrevious()
							+ " previous id " + previousIndex());
					LOGGER.debug("has next " + hasNext() + " next id "
							+ nextIndex());
				}
				return _currentPicture;
			}
		}

		@Override
		public boolean hasNext()
		{
			synchronized (_lock)
			{
				return _pictureIterator.hasNext();
			}
		}

		@Override
		public DummyPicture previous()
		{
			synchronized (_lock)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("before previous");
					LOGGER.debug("has previous " + hasPrevious()
							+ " previous id " + previousIndex());
					LOGGER.debug("has next " + hasNext() + " next id "
							+ nextIndex());
				}
				if (_pictureIterator.hasPrevious())
				{
					_pictureIterator.previous();
					_pictureIterator.previous();
					_currentPicture = _pictureIterator.next();
					_bus.post(new PictureChangedEvent());
				}
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("after previous");
					LOGGER.debug("has previous " + hasPrevious()
							+ " previous id " + previousIndex());
					LOGGER.debug("has next " + hasNext() + " next id "
							+ nextIndex());
				}
				return _currentPicture;
			}
		}

		@Override
		public boolean hasPrevious()
		{
			synchronized (_lock)
			{
				if (_pictureIterator.hasPrevious())
				{
					return _pictureIterator.previousIndex() != 0;
				}
				return false;
			}
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

}
