package yapto.swing;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Logger object.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PictureInformationPanel.class);

	/**
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link PictureDisplayComponent}.
	 */
	private final IPictureBrowser<?> _pictureIterator;

	/**
	 * Object used to control the access on the displayed picture.
	 */
	private final Object _lockPicture = new Object();

	/**
	 * The {@link AbstractTagEditorPanel} used to view and modify the tags
	 * associated with the current picture.
	 */
	private final AbstractTagEditorPanel _tagPanel;

	/**
	 * Creates a new PictureInformationPanel.
	 * 
	 * @param parent
	 *            parent {@link Frame}.
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public PictureInformationPanel(final Frame parent,
			final IPictureBrowser<?> pictureIterator)
	{
		super(new BorderLayout());
		_pictureIterator = pictureIterator;
		_tagPanel = new ListTagEditorPanel(parent, pictureIterator);
		_pictureIterator.register(_tagPanel);
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
		LOGGER.info("PictureInformationPanel pict changed");
	}
}
