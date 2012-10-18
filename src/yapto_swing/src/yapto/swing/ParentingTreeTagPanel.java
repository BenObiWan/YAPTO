package yapto.swing;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.tag.ITag;

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

	/**
	 * Change the currently selected tag.
	 * 
	 * @param iTagId
	 *            id of the tag to select.
	 */
	public void setSelectedTag(final int iTagId)
	{

	}

	/**
	 * Get the id of the currently selected tag.
	 * 
	 * @return the id of the currently selected tag.
	 */
	public int getSelectedTagId()
	{
		TreePath path = _tagTree.getSelectionPath();
		if (path != null)
		{
			Object sel = path.getLastPathComponent();
			if (sel != null && sel instanceof DefaultMutableTreeNode)
			{
				final Object userObject = ((DefaultMutableTreeNode) sel)
						.getUserObject();
				if (userObject != null && userObject instanceof ITag)
				{
					return ((ITag) userObject).getTagId();
				}
			}
		}
		return 0;
	}
}
