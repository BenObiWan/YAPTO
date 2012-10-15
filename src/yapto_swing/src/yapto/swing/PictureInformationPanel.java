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
	 * {@link PictureInformationPanel}.
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
	 * The {@link PictureGradePanel} used to view and modify the grade of the
	 * current picture.
	 */
	private final PictureGradePanel _gradePanel;

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
		_tagPanel = new TreeTagEditorPanel(parent, pictureIterator);
		_gradePanel = new PictureGradePanel(pictureIterator);
		_pictureIterator.register(_tagPanel);
		_pictureIterator.register(_gradePanel);
		add(_tagPanel, BorderLayout.CENTER);
		add(_gradePanel, BorderLayout.PAGE_END);
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
