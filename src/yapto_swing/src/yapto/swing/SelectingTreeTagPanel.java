package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingEvent;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingListener;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import javax.swing.tree.DefaultMutableTreeNode;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.tag.ITag;

/**
 * Implementation of {@link AbstractTreeTagPanel} where multiple {@link ITag}s
 * can be selected.
 * 
 * Used to choose to associate {@link ITag}s to an {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public class SelectingTreeTagPanel extends AbstractTreeTagPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5743908692336875510L;

	/**
	 * Boolean signaling the changing of {@link IPicture}.
	 */
	protected boolean _bChangingPicture = false;

	/**
	 * Lock protecting access to _bChangingPicture boolean.
	 */
	protected final Object _lockChangingPicture = new Object();

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
					synchronized (_lockChangingPicture)
					{
						if (!_bChangingPicture)
						{
							final ITag tag = (ITag) userObject;
							final IPicture pic = _pictureIterator
									.getCurrentPicture();
							if (pic != null)
							{
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
					}
				}
			}
		});
	}

	/**
	 * Clear checking of all the {@link ITag}s.
	 */
	public void unsetSelectedTags()
	{
		synchronized (_lockChangingPicture)
		{
			_bChangingPicture = true;
		}
		_tagTree.clearChecking();
		synchronized (_lockChangingPicture)
		{
			_bChangingPicture = false;
		}
	}

	/**
	 * Set checking of all the {@link ITag}s according to the selected
	 * {@link IPicture}.
	 */
	public void setSelectedTags()
	{
		synchronized (_lockChangingPicture)
		{
			_bChangingPicture = true;
		}
		final IPicture pic = _pictureIterator.getCurrentPicture();
		if (pic != null)
		{
			for (final ITag tag : pic.getTagSet())
			{
				_tagTree.addCheckingPath(_treePathMap.get(Integer.valueOf(tag
						.getTagId())));
			}
		}
		synchronized (_lockChangingPicture)
		{
			_bChangingPicture = false;
		}
	}
}
