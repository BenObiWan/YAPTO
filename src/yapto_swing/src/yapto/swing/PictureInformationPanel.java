package yapto.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.PictureChangedEvent;

import com.google.common.eventbus.Subscribe;

/**
 * Panel displaying the informations of an {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public final class PictureInformationPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -7799432118702150952L;

	/**
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link PictureDisplayComponent}.
	 */
	private final IPictureBrowser _pictureBrowser;

	/**
	 * Object used to control the access on the displayed picture.
	 */
	private final Object _lockPicture = new Object();

	/**
	 * The {@link TagEditorPanel} used to view and modify the tags associated
	 * with the current picture.
	 */
	private final TagEditorPanel _tagPanel;

	/**
	 * Creates a new PictureInformationPanel.
	 * 
	 * @param pictureBrowser
	 *            the {@link IPictureBrowser} to use.
	 */
	public PictureInformationPanel(final IPictureBrowser pictureBrowser)
	{
		super(new BorderLayout());
		_pictureBrowser = pictureBrowser;
		_tagPanel = new TagEditorPanel(_pictureBrowser,
				_pictureBrowser.getCurrentPicture());
		add(_tagPanel, BorderLayout.CENTER);
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
		System.out.println("PictureInformationPanel pict changed");
	}
}
