package yapto.swing;

import javax.swing.JPanel;
import javax.swing.JTree;

import yapto.datasource.IPicture;
import yapto.datasource.ITag;

/**
 * Panel displaying the information of an {@link IPicture}.
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
	 * {@link IPicture} from which the information displayed originate.
	 */
	private IPicture _displayedPicture;

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
	 */
	public PictureInformationPanel()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * Change the {@link IPicture} whose information are displayed.
	 * 
	 * @param picture
	 *            the new {@link IPicture} to handle.
	 */
	public void setDisplayedPicture(final IPicture picture)
	{
		synchronized (_lockPicture)
		{
			_displayedPicture = picture;
		}
	}
}
