package yapto.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.IPictureBrowser;
import yapto.picturebank.IPictureList;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.PictureBrowserChangedEvent;
import yapto.picturebank.PictureChangedEvent;
import yapto.picturebank.tag.ITag;

import com.google.common.eventbus.Subscribe;

/**
 * Panel displaying the list of {@link ITag} that can be associated with an
 * {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public abstract class AbstractTagEditorPanel extends JPanel implements
		ActionListener
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -5913543215027452909L;

	/**
	 * The {@link PictureBankList} used to load the {@link IPictureBank} used as
	 * source for the {@link IPicture}.
	 */
	private final PictureBankList _bankList;

	/**
	 * The {@link IPicture} with which to associate the tags.
	 */
	protected IPicture _picture;

	/**
	 * Lock protecting access to the {@link IPictureBrowser} and the
	 * {@link IPicture}.
	 */
	protected final Object _lock = new Object();

	/**
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link AbstractTagEditorPanel}.
	 */
	protected IPictureBrowser<? extends IPicture> _pictureBrowser;

	/**
	 * Dialog for tag creation.
	 */
	private final JDialog _dialogCreateTag;

	/**
	 * Panel used for tag creation.
	 */
	private final AddTagPanel _addTagPanel;

	/**
	 * Creates a new AbstractTagEditorPanel.
	 * 
	 * @param parent
	 *            parent {@link Frame}.
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public AbstractTagEditorPanel(final Frame parent,
			final PictureBankList bankList)
	{
		super(new BorderLayout());
		_bankList = bankList;
		_bankList.register(this);
		final JButton buttonAddTag = new JButton("Add tag");
		buttonAddTag.addActionListener(this);
		final JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.add(buttonAddTag, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);
		_dialogCreateTag = new JDialog(parent, "Create tag", true);
		_addTagPanel = new AddTagPanel(_dialogCreateTag, _bankList);
		_dialogCreateTag.setContentPane(_addTagPanel);
	}

	/**
	 * Select the {@link ITag}s appropriate to the selected {@link IPicture}.
	 */
	protected abstract void selectAppropriateTags();

	/**
	 * Update the list of available {@link ITag}s according to those available
	 * in the {@link IPictureList}.
	 */
	protected abstract void updateAvailableTags();

	/**
	 * Save the {@link ITag}s associated to an {@link IPicture}.
	 */
	protected abstract void savePictureTags();

	/**
	 * Change the {@link IPicture} which {@link ITag}s can be changed on this
	 * {@link TreeTagEditorPanel}.
	 */
	public final void changePicture()
	{
		synchronized (_lock)
		{
			if (_picture != _pictureBrowser.getCurrentPicture())
			{
				_picture = _pictureBrowser.getCurrentPicture();
				selectAppropriateTags();
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

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_addTagPanel.initialize(false, null);
		_dialogCreateTag.pack();
		_dialogCreateTag.setVisible(true);
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
			if (_pictureBrowser != null)
			{
				_pictureBrowser.unRegister(this);
			}
			_pictureBrowser = _bankList.getLastSelectPictureBrowser();
			if (_pictureBrowser != null)
			{
				_pictureBrowser.register(this);
				updateAvailableTags();
			}
		}
	}
}