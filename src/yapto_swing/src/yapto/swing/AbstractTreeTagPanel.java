package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.IPictureBrowser;
import yapto.picturebank.IPictureList;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.PictureBrowserChangedEvent;
import yapto.picturebank.tag.ITag;
import yapto.picturebank.tag.TagRepositoryChangedEvent;

import com.google.common.eventbus.Subscribe;

/**
 * Abstract panel displaying a {@link CheckboxTree} for {@link ITag} selection.
 * 
 * @author benobiwan
 * 
 */
public abstract class AbstractTreeTagPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 2501581601065249093L;

	/**
	 * {@link CheckboxTree} used to display all the {@link ITag}s.
	 */
	protected final CheckboxTree _tagTree;

	/**
	 * Root node of the {@link CheckboxTree}.
	 */
	private final DefaultMutableTreeNode _rootNode;

	/**
	 * {@link TreeModel} used by the {@link CheckboxTree}.
	 */
	private DefaultTreeModel _treeModel;

	/**
	 * The {@link PictureBankList} used to load the {@link IPictureBank} used as
	 * source for the {@link IPicture}.
	 */
	protected final PictureBankList _bankList;

	/**
	 * Lock protecting access to the {@link IPictureBrowser} and the
	 * {@link IPicture}.
	 */
	protected final Object _lock = new Object();

	/**
	 * The {@link IPictureBrowser} used to get the tag list and the current
	 * {@link IPicture} on this {@link AbstractTreeTagPanel}.
	 */
	protected IPictureBrowser<? extends IPicture> _pictureBrowser;

	/**
	 * {@link HashMap} linking tag id and their {@link TreePath}.
	 */
	protected HashMap<Integer, TreePath> _treePathMap = new HashMap<>();

	/**
	 * Creates a new AbstractTreeTagPanel.
	 * 
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public AbstractTreeTagPanel(final PictureBankList bankList)
	{
		super(new BorderLayout());
		_bankList = bankList;
		synchronized (_lock)
		{
			// TODO improve this
			_rootNode = new DefaultMutableTreeNode(_bankList
					.getSelectedPictureBank().first().getRootTag());
		}
		_tagTree = new CheckboxTree(_rootNode);
		_tagTree.setCellRenderer(new ToolTipCheckboxTreeCellRenderer());
		final TreeModel model = _tagTree.getModel();
		if (model instanceof DefaultTreeModel)
		{
			_treeModel = (DefaultTreeModel) model;
		}
		else
		{
			_treeModel = new DefaultTreeModel(_rootNode);
			_tagTree.setModel(_treeModel);
		}
		ToolTipManager.sharedInstance().registerComponent(_tagTree);
		final JScrollPane scrollPane = new JScrollPane(_tagTree);
		add(scrollPane, BorderLayout.CENTER);
		changePictureBrowser();
	}

	/**
	 * Update the list of available {@link ITag}s according to those available
	 * in the {@link IPictureList}.
	 */
	public void updateAvailableTags()
	{
		synchronized (_lock)
		{
			_rootNode.removeAllChildren();
			_treePathMap.clear();
			if (_pictureBrowser != null)
			{
				final ITag rootTag = _pictureBrowser.getRootTag();
				final TreePath rootTreePath = new TreePath(_rootNode);
				_treePathMap.put(Integer.valueOf(0), rootTreePath);
				populateChildren(rootTag, _rootNode, rootTreePath);
				_treeModel.reload();
				expandAll();
			}
		}
	}

	/**
	 * Creates the {@link MutableTreeNode} corresponding to the given
	 * {@link ITag}.
	 * 
	 * @param tag
	 *            the {@link ITag}.
	 * @return the created {@link MutableTreeNode}.
	 */
	private static MutableTreeNode createTreeNode(final ITag tag)
	{
		final MutableTreeNode node = new DefaultMutableTreeNode(tag);
		return node;
	}

	/**
	 * Creates all the children {@link MutableTreeNode} according to the
	 * children of the {@link ITag}. Calls itself recursively to build the
	 * entire TreeNode.
	 * 
	 * @param parentTag
	 *            the parent {@link ITag}.
	 * @param parentNode
	 *            the parent {@link MutableTreeNode}.
	 * @param parentTreePath
	 *            {@link TreePath} of the parent.
	 */
	private void populateChildren(final ITag parentTag,
			final MutableTreeNode parentNode, final TreePath parentTreePath)
	{
		for (final ITag t : parentTag.getChildren())
		{
			final MutableTreeNode node = createTreeNode(t);
			final TreePath treePath = parentTreePath.pathByAddingChild(node);
			_treePathMap.put(Integer.valueOf(t.getTagId()), treePath);
			populateChildren(t, node, treePath);
			parentNode.insert(node, parentNode.getChildCount());
		}
	}

	/**
	 * Expand all nodes of the {@link JTree}.
	 */
	private void expandAll()
	{
		int row = 0;
		while (row < _tagTree.getRowCount())
		{
			_tagTree.expandRow(row);
			row++;
		}
	}

	/**
	 * Method called when the list of {@link ITag}s has been modified.
	 * 
	 * @param ev
	 *            the event signaling the change in the {@link ITag} list.
	 */
	@Subscribe
	public void handleTagRepositoryChangedEvent(
			@SuppressWarnings("unused") final TagRepositoryChangedEvent ev)
	{
		updateAvailableTags();
	}

	/**
	 * Handle a {@link PictureBrowserChangedEvent} by changing the current
	 * {@link IPictureBrowser}.
	 * 
	 * @param ev
	 *            the event to handle.
	 */
	@Subscribe
	public void handlePictureBrowserChangedEvent(
			@SuppressWarnings("unused") final PictureBrowserChangedEvent ev)
	{
		changePictureBrowser();
	}

	/**
	 * Change the {@link IPictureBrowser}.
	 */
	public void changePictureBrowser()
	{
		synchronized (_lock)
		{
			_pictureBrowser = _bankList.getLastSelectPictureBrowser();
			if (_pictureBrowser != null)
			{
				updateAvailableTags();
			}
		}
	}
}
