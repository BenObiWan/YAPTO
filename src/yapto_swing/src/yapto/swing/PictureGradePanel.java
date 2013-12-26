package yapto.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.IPictureBrowser;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.PictureBrowserChangedEvent;
import yapto.picturebank.PictureChangedEvent;

import com.google.common.eventbus.Subscribe;

/**
 * Panel used to set and display the grade of an {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public final class PictureGradePanel extends JPanel implements ChangeListener
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -5296012830383518783L;

	/**
	 * The {@link PictureBankList} used to load the {@link IPictureBank} used as
	 * source for the {@link IPicture}.
	 */
	private final PictureBankList _bankList;

	/**
	 * Lock protecting access to the {@link IPictureBrowser} and the
	 * {@link IPicture}.
	 */
	private final Object _lock = new Object();

	/**
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link PictureGradePanel}.
	 */
	private IPictureBrowser<?> _pictureBrowser;

	/**
	 * The {@link IPicture} with which to associate the grade.
	 */
	private IPicture _picture;

	/**
	 * Slider used to change the grade of the {@link IPicture}.
	 */
	private final JSlider _gradeSlider = new JSlider(0, 5, 0);

	/**
	 * Creates a new PictureGradePanel.
	 * 
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public PictureGradePanel(final PictureBankList bankList)
	{
		super(new BorderLayout());
		_bankList = bankList;
		changePictureBrowser();
		_bankList.register(this);
		_gradeSlider.addChangeListener(this);
		add(_gradeSlider, BorderLayout.CENTER);
		_gradeSlider.setMajorTickSpacing(1);
		_gradeSlider.setPaintLabels(true);
		changePicture();
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

	/**
	 * Change the {@link IPicture} which grade can be changed on this
	 * {@link PictureGradePanel}.
	 */
	public final void changePicture()
	{
		synchronized (_lock)
		{
			if (_picture != _pictureBrowser.getCurrentPicture())
			{
				_picture = _pictureBrowser.getCurrentPicture();
				if (_picture != null)
				{
					_gradeSlider.setValue(_picture.getPictureGrade());
				}
				else
				{
					_gradeSlider.setValue(0);
				}
			}
		}
	}

	@Override
	public void stateChanged(final ChangeEvent e)
	{
		synchronized (_lock)
		{
			if (_picture != null
					&& _picture.getPictureGrade() != _gradeSlider.getValue())
			{
				_picture.setPictureGrade(_gradeSlider.getValue());
			}
		}
	}

	/**
	 * Handle a {@link PictureBrowserChangedEvent} by changing the current
	 * {@link IPictureBrowser}.
	 * 
	 * @param ev
	 *            the event to handle.
	 */
	@Subscribe
	public void handlePictureBrowserChangedEvent(
			@SuppressWarnings("unused") final PictureBrowserChangedEvent ev)
	{
		changePictureBrowser();
	}

	/**
	 * Change the {@link IPictureBrowser}.
	 */
	public void changePictureBrowser()
	{
		synchronized (_lock)
		{
			_pictureBrowser = _bankList.getLastSelectPictureBrowser();
		}
		changePicture();
	}
}
