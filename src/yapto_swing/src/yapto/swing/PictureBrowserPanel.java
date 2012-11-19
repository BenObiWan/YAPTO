package yapto.swing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBrowser;
import yapto.picturebank.PictureChangedEvent;

import com.google.common.eventbus.Subscribe;

/**
 * Panel used to control the selected image of an {@link IPictureBrowser}.
 * 
 * @author benobiwan
 * 
 */
public final class PictureBrowserPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 8383818741917881197L;

	/**
	 * The {@link IPictureBrowser} which is controlled by this
	 * {@link PictureBrowserPanel}.
	 */
	protected final IPictureBrowser<? extends IPicture> _pictureIterator;

	/**
	 * {@link JButton} used to select the previous picture.
	 */
	private final JButton _jbPrevious;

	/**
	 * {@link JButton} used to select the next picture.
	 */
	private final JButton _jbNext;

	/**
	 * Creates a new PictureBrowserPanel.
	 * 
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public PictureBrowserPanel(
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		super(new GridLayout(1, 0, 10, 10));
		_pictureIterator = pictureIterator;
		_jbPrevious = new JButton("<");
		_jbPrevious.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent arg0)
			{
				_pictureIterator.previous();
			}
		});
		add(_jbPrevious);

		_jbNext = new JButton(">");
		_jbNext.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent arg0)
			{
				_pictureIterator.next();
			}
		});
		add(_jbNext);
		updateButtonEnableState();
	}

	/**
	 * Update the enable/disable state of the {@link JButton}s according to the
	 * state of the {@link IPictureBrowser}
	 */
	private void updateButtonEnableState()
	{
		_jbPrevious.setEnabled(_pictureIterator.hasPrevious());
		_jbNext.setEnabled(_pictureIterator.hasNext());
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
		updateButtonEnableState();
	}
}
