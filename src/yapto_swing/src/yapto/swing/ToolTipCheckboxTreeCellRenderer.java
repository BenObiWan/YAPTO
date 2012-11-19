package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTreeCellRenderer;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.DefaultCheckboxTreeCellRenderer;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import yapto.picturebank.tag.ITag;

/**
 * {@link CheckboxTreeCellRenderer} that display {ITag} description has tool
 * tip.
 * 
 * @author benobiwan
 * 
 */
public final class ToolTipCheckboxTreeCellRenderer extends
		DefaultCheckboxTreeCellRenderer
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 7224495502642573920L;

	@Override
	public Component getTreeCellRendererComponent(final JTree tree,
			final Object value, final boolean sel, final boolean expanded,
			final boolean leaf, final int row, final boolean hasFocus)
	{

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		if (value != null && value instanceof DefaultMutableTreeNode)
		{
			final Object userObject = ((DefaultMutableTreeNode) value)
					.getUserObject();
			if (userObject != null && userObject instanceof ITag)
			{
				setToolTipText(((ITag) userObject).getDescription());
			}
			else
			{
				setToolTipText(null);
			}
		}
		else
		{
			setToolTipText(null);
		}
		return this;
	}
}