package yapto.swing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.IPictureBrowser;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.PictureBrowserChangedEvent;
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
	 * The {@link PictureBankList} used to load the {@link IPictureBank} used as
	 * source for the {@link IPicture}.
	 */
	private final PictureBankList _bankList;

	/**
	 * The {@link IPictureBrowser} which is controlled by this
	 * {@link PictureBrowserPanel}.
	 */
	protected IPictureBrowser<? extends IPicture> _pictureBrowser;

	/**
	 * Lock protecting access to the {@link IPictureBrowser}.
	 */
	protected final Object _lockBrowser = new Object();

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
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public PictureBrowserPanel(final PictureBankList bankList)
	{
		super(new GridLayout(1, 0, 10, 10));
		_bankList = bankList;
		_bankList.register(this);
		_jbPrevious = new JButton("<");
		_jbPrevious.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent arg0)
			{
				synchronized (_lockBrowser)
				{
					_pictureBrowser.previous();
				}
			}
		});
		add(_jbPrevious);

		_jbNext = new JButton(">");
		_jbNext.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent arg0)
			{
				_pictureBrowser.next();
			}
		});
		add(_jbNext);
		changePictureBrowser();
	}

	/**
	 * Update the enable/disable state of the {@link JButton}s according to the
	 * state of the {@link IPictureBrowser}
	 */
	private void updateButtonEnableState()
	{
		synchronized (_lockBrowser)
		{
			_jbPrevious.setEnabled(_pictureBrowser.hasPrevious());
			_jbNext.setEnabled(_pictureBrowser.hasNext());
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
		updateButtonEnableState();
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
		synchronized (_lockBrowser)
		{
			if (_pictureBrowser != null)
			{
				_pictureBrowser.unRegister(this);
			}
			_pictureBrowser = _bankList.getLastSelectPictureBrowser();
			if (_pictureBrowser != null)
			{
				_pictureBrowser.register(this);
			}
		}
		updateButtonEnableState();
	}
}
