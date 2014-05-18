package yapto.swing.query;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.tag.ITag;
import yapto.swing.AbstractTreeTagPanel;

/**
 * Implementation of {@link AbstractTreeTagPanel} where multiple {@link ITag}s
 * can be selected.
 * 
 * Used to filter {@link IPicture}s to display by {@link ITag}s.
 * 
 * @author benobiwan
 * 
 */
public final class FilteringTreeTagPanel extends AbstractTreeTagPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5743908692336875510L;

	/**
	 * Creates a new SelectingTreeTagPanel.
	 * 
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public FilteringTreeTagPanel(final PictureBankList bankList)
	{
		super(bankList);
		_tagTree.getCheckingModel().setCheckingMode(CheckingMode.SIMPLE);
	}

	/**
	 * Clear checking of all the {@link ITag}s.
	 */
	public void unsetSelectedTags()
	{
		_tagTree.clearChecking();
	}

	/**
	 * Get the ids of the selected {@link ITag}s.
	 * 
	 * @return the ids of the selected {@link ITag}s.
	 */
	public int[] getSelectedTagIds()
	{
		final TreePath[] paths = _tagTree.getCheckingPaths();
		final int[] tagIds = new int[paths.length];
		int i = 0;
		for (final TreePath path : paths)
		{
			if (path != null)
			{
				final Object sel = path.getLastPathComponent();
				if (sel != null && sel instanceof DefaultMutableTreeNode)
				{
					final Object userObject = ((DefaultMutableTreeNode) sel)
							.getUserObject();
					if (userObject != null && userObject instanceof ITag)
					{
						tagIds[i] = ((ITag) userObject).getTagId();
						i++;
					}
				}
			}
		}
		return tagIds;
	}

	/**
	 * Get the selected {@link ITag}s.
	 * 
	 * @return the selected {@link ITag}s.
	 */
	public ITag[] getSelectedTags()
	{
		final TreePath[] paths = _tagTree.getCheckingPaths();
		final ITag[] tags = new ITag[paths.length];
		int i = 0;
		for (final TreePath path : paths)
		{
			if (path != null)
			{
				final Object sel = path.getLastPathComponent();
				if (sel != null && sel instanceof DefaultMutableTreeNode)
				{
					final Object userObject = ((DefaultMutableTreeNode) sel)
							.getUserObject();
					if (userObject != null && userObject instanceof ITag)
					{
						tags[i] = (ITag) userObject;
						i++;
					}
				}
			}
		}
		return tags;
	}
}
