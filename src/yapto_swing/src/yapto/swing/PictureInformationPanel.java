package yapto.swing;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.PictureChangedEvent;

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
	 * The {@link PictureBankList} used to load the {@link IPictureBank} used as
	 * source for the {@link IPicture}.
	 */
	private final PictureBankList _bankList;

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
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public PictureInformationPanel(final Frame parent,
			final PictureBankList bankList)
	{
		super(new BorderLayout());
		_bankList = bankList;
		_tagPanel = new TreeTagEditorPanel(parent, _bankList);
		_gradePanel = new PictureGradePanel(_bankList);
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
