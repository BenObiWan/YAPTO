package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.tag.ITag;

/**
 * Panel displaying the list of {@link ITag} that can be associated with an
 * {@link IPicture} using a {@link JTree}.
 * 
 * @author benobiwan
 * 
 */
public final class TreeTagEditorPanel extends AbstractTagEditorPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5669141686850523799L;

	/**
	 * {@link CheckboxTree} used to display all the {@link ITag}s.
	 */
	private final CheckboxTree _tagTree;

	/**
	 * Root node of the JTree.
	 */
	private final DefaultMutableTreeNode _rootNode;

	/**
	 * Creates a new TagEditorPanel.
	 * 
	 * @param parent
	 *            parent {@link Frame}.
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public TreeTagEditorPanel(final Frame parent,
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		super(parent, pictureIterator);
		_rootNode = new DefaultMutableTreeNode();
		_tagTree = new CheckboxTree(_rootNode);
		_tagTree.getCheckingModel().setCheckingMode(CheckingMode.SIMPLE);
		final JScrollPane scrollPane = new JScrollPane(_tagTree);
		add(scrollPane, BorderLayout.CENTER);
		updateAvailableTags();
		changePicture();
	}

	@Override
	protected void updateAvailableTags()
	{
		synchronized (_lock)
		{
			_rootNode.removeAllChildren();
			if (_pictureIterator != null)
			{
				final ITag rootTag = _pictureIterator.getRootTag();
				populateChildren(rootTag, _rootNode);
				expandAll();
				if (_picture != null)
				{
					selectAppropriateTags();
				}
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
		final MutableTreeNode node = new DefaultMutableTreeNode(tag.getName());
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
	 */
	private void populateChildren(final ITag parentTag,
			final MutableTreeNode parentNode)
	{
		for (final ITag t : parentTag.getChildren())
		{
			final MutableTreeNode node = createTreeNode(t);
			populateChildren(t, node);
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

	@Override
	protected void selectAppropriateTags()
	{
		synchronized (_lock)
		{
			if (_pictureIterator != null)
			{
				// TODO unselect all tags
				if (_picture != null)
				{
					// TODO select appropriate tags
				}
			}
		}
	}

	@Override
	protected void savePictureTags()
	{
		// TODO Auto-generated method stub

	}
}
