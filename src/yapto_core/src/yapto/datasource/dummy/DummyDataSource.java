package yapto.datasource.dummy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.Tag;
import yapto.datasource.OperationNotSupportedException;

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

	private final List<IPicture> _listPicture = new CopyOnWriteArrayList<IPicture>();

	/**
	 * List of {@link Tag} available on this {@link IDataSource}.
	 */
	private final List<Tag> _listTags = new CopyOnWriteArrayList<Tag>();

	/**
	 * Creates a new DummyDataSource.
	 */
	public DummyDataSource()
	{
		final String[] fileList = { "/tmp/picture1.jpg", "/tmp/picture2.jpg" };

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
	public List<Tag> getTagList()
	{
		return _listTags;
	}

	@Override
	public List<IPicture> getPictureList()
	{
		return _listPicture;
	}
}
