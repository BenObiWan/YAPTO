package yapto.datasource.dummy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.ITag;
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

	/**
	 * Creates a new DummyDataSource.
	 */
	public DummyDataSource()
	{
		// TODO Auto-generated constructor stub
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
		DummyPicture pict = new DummyPicture(this, picturePath);
		_mapPicture.put(pict.getId(), pict);
	}

	@Override
	public List<ITag> getTagList() throws OperationNotSupportedException
	{
		throw new OperationNotSupportedException();
	}
}
