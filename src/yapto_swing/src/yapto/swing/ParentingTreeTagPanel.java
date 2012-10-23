package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

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
		final TreePath path = _treePathMap.get(Integer.valueOf(iTagId));
		_tagTree.setCheckingPath(path);
	}

	/**
	 * Get the id of the currently selected tag.
	 * 
	 * @return the id of the currently selected tag.
	 */
	public int getSelectedTagId()
	{
		final TreePath path = _tagTree.getSelectionPath();
		if (path != null)
		{
			final Object sel = path.getLastPathComponent();
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
