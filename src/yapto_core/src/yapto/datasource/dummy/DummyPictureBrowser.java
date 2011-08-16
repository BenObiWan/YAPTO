package yapto.datasource.dummy;

import java.util.List;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.ITag;
import yapto.datasource.OperationNotSupportedException;

/**
 * Dummy implementation of {@link IPictureBrowser}.
 * 
 * @author benobiwan
 * 
 */
public final class DummyPictureBrowser implements IPictureBrowser
{
	/**
	 * The {@link IPictureList} used as source for this {@link IPictureBrowser}.
	 */
	private IPictureList _sourcePictureList;

	/**
	 * Lock used to protect the access to the source {@link IPictureList}.
	 */
	private final Object _lockSourcePictureList = new Object();

	/**
	 * Creates a new DummyPictureBrowser using the specified
	 * {@link IPictureList} as source.
	 * 
	 * @param sourcePictureList
	 *            the {@link IPictureList} to use as source.
	 */
	public DummyPictureBrowser(IPictureList sourcePictureList)
	{
		synchronized (_lockSourcePictureList)
		{
			_sourcePictureList = sourcePictureList;
		}
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
	public IPictureList filterList(IPictureFilter filter)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPictureList mergeList(IPictureList otherList)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPictureList> getParent() throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ITag> getTagList() throws OperationNotSupportedException
	{
		return _sourcePictureList.getTagList();
	}

	@Override
	public IPicture getCurrentPicture()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPicture getNextPicture()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasNextPicture()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPicture getPreviousPicture()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPreviousPicture()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void changeSourcePictureList(IPictureList source)
	{
		synchronized (_lockSourcePictureList)
		{
			_sourcePictureList = source;
		}
	}

}
