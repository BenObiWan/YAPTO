package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.tag.ITag;

/**
 * Implementation of {@link AbstractTreeTagPanel} where only one {@link ITag}
 * can be selected.
 * 
 * Used to choose the parent for an {@link ITag} in {@link AddTagPanel}.
 * 
 * @author benobiwan
 * 
 */
public final class ParentingTreeTagPanel extends AbstractTreeTagPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 4592506607245089459L;

	/**
	 * Creates a new ParentingTreeTagPanel.
	 * 
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public ParentingTreeTagPanel(final PictureBankList bankList)
	{
		super(bankList);
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
		_tagTree.setCheckingPath(_treePathMap.get(Integer.valueOf(iTagId)));
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
