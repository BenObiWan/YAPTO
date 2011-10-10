package yapto.swing;

import javax.swing.JPanel;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureList;
import yapto.datasource.ITag;

/**
 * Panel displaying the list of {@link ITag} that can be associated with an
 * {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public final class TagEditorPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5669141686850523799L;

	/**
	 * The {@link IPictureList} used to load the list of available {@link ITag}
	 * s.
	 */
	private IPictureList _pictureList;

	/**
	 * The {@link IPicture} with which to associate the tags.
	 */
	private IPicture _picture;

	/**
	 * Lock protecting access to the {@link IPictureList} and the
	 * {@link IPicture}.
	 */
	private final Object _lock = new Object();

	/**
	 * Creates a new TagEditorPanel.
	 * 
	 * @param pictureList
	 *            the {@link IPictureList} from which {@link ITag}s can be
	 *            selected.
	 * @param picture
	 *            the picture from which to edit the {@link ITag}s.
	 */
	public TagEditorPanel(final IPictureList pictureList, final IPicture picture)
	{
		changePictureList(pictureList);
		changePicture(picture);
	}

	/**
	 * Update the list of available {@link ITag}s according the {@link ITag}s of
	 * the specified {@link IPictureList}.
	 * 
	 * @param pictureList
	 *            the new {@link IPictureList}.
	 */
	public void changePictureList(final IPictureList pictureList)
	{
		synchronized (_lock)
		{
			if (_pictureList != pictureList)
			{
				_pictureList = pictureList;
				if (_pictureList == null)
				{
					removeAllAvailableTags();
				}
				else
				{
					updateAvailableTags();
					if (_picture != null)
					{
						selectAppropriateTags();
					}
				}
			}
		}
	}

	/**
	 * Change the {@link IPicture} which {@link ITag}s can be changed on this
	 * {@link TagEditorPanel}.
	 * 
	 * @param picture
	 *            the new {@link IPicture}.
	 */
	public void changePicture(final IPicture picture)
	{
		synchronized (_lock)
		{
			if (_picture != picture)
			{
				_picture = picture;
				if (_pictureList != null)
				{
					unselectAllTags();
					if (_picture != null)
					{
						selectAppropriateTags();
					}
				}
			}
		}
	}

	/**
	 * Remove all the available {@link ITag}s.
	 */
	private void removeAllAvailableTags()
	{

	}

	/**
	 * Update the list of available {@link ITag}s according to those available
	 * in the {@link IPictureList}.
	 */
	private void updateAvailableTags()
	{

	}

	/**
	 * Unselect all {@link ITag}s.
	 */
	private void unselectAllTags()
	{

	}

	/**
	 * Select the {@link ITag}s appropriate to the selected {@link IPicture}.
	 */
	private void selectAppropriateTags()
	{

	}
}
