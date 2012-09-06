package yapto.swing;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.tag.Tag;

/**
 * Panel displaying the list of {@link Tag} that can be associated with an
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
	 * {@link JTree} used to display all the {@link Tag}s.
	 */
	private final JTree _tagTree;

	/**
	 * Root node of the JTree.
	 */
	private final DefaultMutableTreeNode _rootNode;

	/**
	 * Creates a new TagEditorPanel.
	 * 
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public TreeTagEditorPanel(
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		super(pictureIterator);
		_rootNode = new DefaultMutableTreeNode();
		_tagTree = new JTree(_rootNode);
		final JScrollPane scrollPane = new JScrollPane(_tagTree);
		add(scrollPane, BorderLayout.CENTER);
		updateAvailableTags();
		changePicture();
	}

	@Override
	public void updateAvailableTags()
	{
		synchronized (_lock)
		{
			_rootNode.removeAllChildren();
			if (_pictureIterator != null)
			{
				final Tag rootTag = _pictureIterator.getRootTag();
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
	 * {@link Tag}.
	 * 
	 * @param tag
	 *            the {@link Tag}.
	 * @return the created {@link MutableTreeNode}.
	 */
	private static MutableTreeNode createTreeNode(final Tag tag)
	{
		final MutableTreeNode node = new DefaultMutableTreeNode(tag.getName());
		return node;
	}

	/**
	 * Creates all the children {@link MutableTreeNode} according to the
	 * children of the {@link Tag}. Calls itself recursively to build the entire
	 * TreeNode.
	 * 
	 * @param parentTag
	 *            the parent {@link Tag}.
	 * @param parentNode
	 *            the parent {@link MutableTreeNode}.
	 */
	private void populateChildren(final Tag parentTag,
			final MutableTreeNode parentNode)
	{
		for (final Tag t : parentTag.getChildren())
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
	public void selectAppropriateTags()
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
}
