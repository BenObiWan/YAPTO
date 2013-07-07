package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.tag.ITag;
import yapto.picturebank.tag.TagRepositoryChangedEvent;

import com.google.common.eventbus.Subscribe;

/**
 * Implementation of {@link AbstractTreeTagPanel} where multiple {@link ITag}s
 * can be selected.
 * 
 * @author benobiwan
 * 
 */
public class InitialTreeTagPanel extends AbstractTreeTagPanel
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
	public InitialTreeTagPanel(final PictureBankList bankList)
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

	@Override
	@Subscribe
	public void handleTagRepositoryChangedEvent(
			final TagRepositoryChangedEvent ev)
	{
		super.handleTagRepositoryChangedEvent(ev);
	}

	/**
	 * Get the list of checked {@link ITag}s.
	 * 
	 * @return the list of checked {@link ITag}s.
	 */
	public List<ITag> getCheckedTags()
	{
		final TreePath[] pathList = _tagTree.getCheckingPaths();
		final List<ITag> tagList = new LinkedList<>();
		for (final TreePath path : pathList)
		{
			final Object value = path.getLastPathComponent();
			final Object userObject = ((DefaultMutableTreeNode) value)
					.getUserObject();
			if (userObject != null && userObject instanceof ITag)
			{
				tagList.add((ITag) userObject);
			}
		}
		return tagList;
	}
}