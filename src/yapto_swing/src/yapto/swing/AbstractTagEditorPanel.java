package yapto.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.IPictureList;
import yapto.datasource.PictureChangedEvent;
import yapto.datasource.tag.Tag;

import com.google.common.eventbus.Subscribe;

/**
 * Panel displaying the list of {@link Tag} that can be associated with an
 * {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public abstract class AbstractTagEditorPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -5913543215027452909L;

	/**
	 * The {@link IPicture} with which to associate the tags.
	 */
	protected IPicture _picture;

	/**
	 * Lock protecting access to the {@link IPictureList} and the
	 * {@link IPicture}.
	 */
	protected final Object _lock = new Object();

	/**
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link AbstractTagEditorPanel}.
	 */
	protected IPictureBrowser<? extends IPicture> _pictureIterator;

	/**
	 * Creates a new AbstractTagEditorPanel.
	 * 
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public AbstractTagEditorPanel(
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		super(new BorderLayout());
		_pictureIterator = pictureIterator;
	}

	/**
	 * Select the {@link Tag}s appropriate to the selected {@link IPicture}.
	 */
	protected abstract void selectAppropriateTags();

	/**
	 * Update the list of available {@link Tag}s according to those available in
	 * the {@link IPictureList}.
	 */
	protected abstract void updateAvailableTags();

	/**
	 * Save the {@link Tag}s associated to an {@link IPicture}.
	 */
	protected abstract void savePictureTags();

	/**
	 * Change the {@link IPicture} which {@link Tag}s can be changed on this
	 * {@link TreeTagEditorPanel}.
	 */
	public final void changePicture()
	{
		synchronized (_lock)
		{
			if (_picture != _pictureIterator.getCurrentPicture())
			{
				_picture = _pictureIterator.getCurrentPicture();
				selectAppropriateTags();
			}
		}
	}

	/**
	 * Update the list of available {@link Tag}s according the {@link Tag}s of
	 * the specified {@link IPictureList}.
	 * 
	 * @param pictureIterator
	 *            the new {@link IPictureBrowser}.
	 */
	public final void changePictureList(
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		synchronized (_lock)
		{
			if (_pictureIterator != pictureIterator)
			{
				_pictureIterator = pictureIterator;
				updateAvailableTags();
			}
		}
	}

	/**
	 * Method called when the selected picture has changed.
	 * 
	 * @param e
	 *            the event signaling the change of the picture.
	 */
	@Subscribe
	public void handlePictureChanged(
			@SuppressWarnings("unused") final PictureChangedEvent e)
	{
		changePicture();
	}
}