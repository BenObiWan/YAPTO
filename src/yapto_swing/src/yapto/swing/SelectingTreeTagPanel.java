package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;

public class SelectingTreeTagPanel extends AbstractTreeTagPanel
{

	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5743908692336875510L;

	/**
	 * Creates a new SelectingTreeTagPanel.
	 * 
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public SelectingTreeTagPanel(
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		super(pictureIterator);
		_tagTree.getCheckingModel().setCheckingMode(CheckingMode.SIMPLE);
	}

}
