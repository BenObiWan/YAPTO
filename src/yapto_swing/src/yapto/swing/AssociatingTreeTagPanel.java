package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingEvent;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingListener;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

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
 * Used to choose to associate {@link ITag}s to an {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public class AssociatingTreeTagPanel extends AbstractTreeTagPanel implements
		ActionListener
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5743908692336875510L;

	/**
	 * Action command for the add tag action.
	 */
	private static final String ADD_ACTION_COMMAND = "add";

	/**
	 * Action command for the edit tag action.
	 */
	private static final String EDIT_ACTION_COMMAND = "edi";

	/**
	 * Action command for the delete tag action.
	 */
	private static final String DELETE_ACTION_COMMAND = "del";

	/**
	 * Boolean signaling the changing of {@link IPicture}.
	 */
	protected boolean _bChangingPicture = false;

	/**
	 * Lock protecting access to _bChangingPicture boolean.
	 */
	protected final Object _lockChangingPicture = new Object();

	/**
	 * Dialog for tag creation.
	 */
	private final JDialog _dialogCreateTag;

	/**
	 * Panel used for tag creation.
	 */
	private final AddTagPanel _addTagPanel;

	/**
	 * Creates a new SelectingTreeTagPanel.
	 * 
	 * @param parent
	 *            parent {@link Frame}.
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public AssociatingTreeTagPanel(final Frame parent,
			final PictureBankList bankList)
	{
		super(bankList);
		_tagTree.getCheckingModel().setCheckingMode(CheckingMode.SIMPLE);
		_tagTree.addTreeCheckingListener(new PictureTagUpdateTreeCheckingListener());

		_dialogCreateTag = new JDialog(parent, "Create tag", true);
		_addTagPanel = new AddTagPanel(_dialogCreateTag, _bankList);
		_dialogCreateTag.setContentPane(_addTagPanel);

		// Contextual menu
		final JPopupMenu popup = new JPopupMenu();
		final JMenuItem addTagItem = new JMenuItem("Add tag");
		addTagItem.addActionListener(this);
		addTagItem.setActionCommand(ADD_ACTION_COMMAND);
		popup.add(addTagItem);
		final JMenuItem editTagItem = new JMenuItem("Edit tag");
		editTagItem.addActionListener(this);
		editTagItem.setActionCommand(EDIT_ACTION_COMMAND);
		popup.add(editTagItem);
		final JMenuItem deleteTagItem = new JMenuItem("Delete tag");
		deleteTagItem.addActionListener(this);
		deleteTagItem.setActionCommand(DELETE_ACTION_COMMAND);
		popup.add(deleteTagItem);
		_tagTree.setComponentPopupMenu(popup);
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
		final IPicture pic = _pictureBrowser.getCurrentPicture();
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

	@Override
	@Subscribe
	public void handleTagRepositoryChangedEvent(
			final TagRepositoryChangedEvent ev)
	{
		super.handleTagRepositoryChangedEvent(ev);
		setSelectedTags();
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		if (ADD_ACTION_COMMAND.equals(e.getActionCommand()))
		{
			_addTagPanel.initialize(false, null);
			_dialogCreateTag.pack();
			_dialogCreateTag.setVisible(true);
		}
		else if (EDIT_ACTION_COMMAND.equals(e.getActionCommand()))
		{
			// TODO implement edit tag.
		}
		else if (DELETE_ACTION_COMMAND.equals(e.getActionCommand()))
		{
			// TODO implement delete tag.
		}
	}

	/**
	 * Internal class implementing {@link TreeCheckingListener} used to update
	 * the {@link ITag}s associated to the {@link IPicture}.
	 * 
	 * @author benobiwan
	 * 
	 */
	protected final class PictureTagUpdateTreeCheckingListener implements
			TreeCheckingListener
	{
		/**
		 * Creates a new PictureTagUpdateTreeCheckingListener.
		 */
		public PictureTagUpdateTreeCheckingListener()
		{
			// nothing
		}

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
						final IPicture pic = _pictureBrowser
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
	}
}
