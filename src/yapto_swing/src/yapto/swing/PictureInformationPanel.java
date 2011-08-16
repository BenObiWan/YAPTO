package yapto.swing;

import javax.swing.JPanel;
import javax.swing.JTree;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.ITag;

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
	 * Tree used to display the list of {@link ITag}s.
	 */
	private final JTree _tagTree = new JTree();

	/**
	 * Creates a new PictureInformationPanel.
	 * 
	 * @param pictureBrowser
	 *            the {@link IPictureBrowser} to use.
	 */
	public PictureInformationPanel(final IPictureBrowser pictureBrowser)
	{
		_pictureBrowser = pictureBrowser;
	}
}
