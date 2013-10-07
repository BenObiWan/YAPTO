package yapto.picturebank;

import java.util.List;

public final class PictureListWithIndex
{
	private final List<String> _list;

	private final int _iIndex;

	public PictureListWithIndex(final List<String> list, final int iIndex)
	{
		_list = list;
		_iIndex = iIndex;
	}

	public List<String> getPictureList()
	{
		return _list;
	}

	public int getIndex()
	{
		return _iIndex;
	}
}
