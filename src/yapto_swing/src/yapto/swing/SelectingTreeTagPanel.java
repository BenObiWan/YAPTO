package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingEvent;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingListener;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import javax.swing.tree.DefaultMutableTreeNode;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.tag.ITag;

public class SelectingTreeTagPanel extends AbstractTreeTagPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5743908692336875510L;

	/**
	 * Creates a new SelectingTreeTagPanel.
	 * 
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public SelectingTreeTagPanel(
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		super(pictureIterator);
		_tagTree.getCheckingModel().setCheckingMode(CheckingMode.SIMPLE);
		_tagTree.addTreeCheckingListener(new TreeCheckingListener()
		{
			@Override
			public void valueChanged(final TreeCheckingEvent e)
			{
				final Object value = e.getPath().getLastPathComponent();
				final Object userObject = ((DefaultMutableTreeNode) value)
						.getUserObject();
				if (userObject != null && userObject instanceof ITag)
				{
					final ITag tag = (ITag) userObject;
					final IPicture pic = _pictureIterator.getCurrentPicture();
					if (e.isCheckedPath())
					{
						pic.addTag(tag);
					}
					else
					{
						pic.removeTag(tag);
					}
				}
			}
		});
	}
}
