package yapto.picturebank;

import java.util.List;

/**
 * List of picture Ids plus index of the selected picture.
 * 
 * @author benobiwan
 */
public final class PictureListWithIndex
{
	/**
	 * List of picture Ids.
	 */
	private final List<String> _pictureIdList;

	/**
	 * Index of the current picture.
	 */
	private final int _iIndex;

	/**
	 * Create a new picture list with index.
	 * 
	 * @param pictureIdList
	 *            list of picture Ids.
	 * @param iIndex
	 *            index of the current picture.
	 */
	public PictureListWithIndex(final List<String> pictureIdList,
			final int iIndex)
	{
		_pictureIdList = pictureIdList;
		_iIndex = iIndex;
	}

	/**
	 * Get the list of picture Ids.
	 * 
	 * @return the list of picture Ids.
	 */
	public List<String> getPictureIdList()
	{
		return _pictureIdList;
	}

	/**
	 * Get the index of the currently selected picture.
	 * 
	 * @return the index of the currently selected picture.
	 */
	public int getIndex()
	{
		return _iIndex;
	}
}
