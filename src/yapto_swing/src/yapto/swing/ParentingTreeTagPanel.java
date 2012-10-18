package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;

public class ParentingTreeTagPanel extends AbstractTreeTagPanel
{

	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 4592506607245089459L;

	/**
	 * Creates a new ParentingTreeTagPanel.
	 * 
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public ParentingTreeTagPanel(
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		super(pictureIterator);
		_tagTree.getCheckingModel().setCheckingMode(CheckingMode.SINGLE);
	}

}
