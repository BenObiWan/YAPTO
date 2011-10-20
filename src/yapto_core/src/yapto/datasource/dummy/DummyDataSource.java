package yapto.datasource.dummy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.OperationNotSupportedException;
import yapto.datasource.Tag;

/**
 * Dummy implementation of the {@link IDataSource} interface.
 * 
 * @author benobiwan
 * 
 */
public final class DummyDataSource implements IDataSource
{
	/**
	 * Map containing all the picture and their id.
	 */
	private final ConcurrentSkipListMap<String, IPicture> _mapPicture = new ConcurrentSkipListMap<String, IPicture>();

	/**
	 * List of {@link IPicture} of this {@link DummyDataSource}.
	 */
	private final List<IPicture> _listPicture = new CopyOnWriteArrayList<IPicture>();

	/**
	 * Set of all the {@link Tag}s available on this {@link IDataSource}.
	 */
	private final Set<Tag> _tagSet = new ConcurrentSkipListSet<Tag>();

	/**
	 * The root {@link Tag} of this {@link DummyDataSource}.
	 */
	private final Tag _rootTag;

	/**
	 * Creates a new DummyDataSource.
	 */
	public DummyDataSource()
	{
		final String[] fileList = { "/tmp/picture1.jpg", "/tmp/picture2.jpg" };
		_rootTag = new Tag(null, "root", "", false);

		for (final String file : fileList)
		{
			try
			{
				addPicture(new File(file));
			}
			catch (final FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (final IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getPictureCount()
	{
		return _mapPicture.size();
	}

	@Override
	public IPictureList filterList(final IPictureFilter filter)
			throws OperationNotSupportedException
	{
		throw new OperationNotSupportedException();
	}

	@Override
	public IPictureList mergeList(final IPictureList otherList)
			throws OperationNotSupportedException
	{
		throw new OperationNotSupportedException();
	}

	@Override
	public List<IPictureList> getParent() throws OperationNotSupportedException
	{
		throw new OperationNotSupportedException();
	}

	@Override
	public void addPicture(final File picturePath)
			throws FileNotFoundException, IOException
	{
		final DummyPicture pict = new DummyPicture(this, picturePath);
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
		return _listPicture;
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
}
