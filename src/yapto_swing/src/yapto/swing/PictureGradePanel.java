package yapto.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.PictureChangedEvent;

import com.google.common.eventbus.Subscribe;

public final class PictureGradePanel extends JPanel implements ChangeListener
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -5296012830383518783L;

	/**
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link PictureGradePanel}.
	 */
	private final IPictureBrowser<?> _pictureIterator;

	/**
	 * The {@link IPicture} with which to associate the grade.
	 */
	private IPicture _picture;

	/**
	 * Lock protecting access to the {@link IPictureBrowser} and the
	 * {@link IPicture}.
	 */
	private final Object _lock = new Object();

	/**
	 * Slider used to change the grade of the {@link IPicture}.
	 */
	private final JSlider _gradeSlider = new JSlider(0, 5, 0);

	/**
	 * Creates a new PictureGradePanel.
	 * 
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public PictureGradePanel(final IPictureBrowser<?> pictureIterator)
	{
		super(new BorderLayout());
		_pictureIterator = pictureIterator;
		_gradeSlider.addChangeListener(this);
		add(_gradeSlider, BorderLayout.CENTER);
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
			if (_picture != _pictureIterator.getCurrentPicture())
			{
				_picture = _pictureIterator.getCurrentPicture();
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
}
