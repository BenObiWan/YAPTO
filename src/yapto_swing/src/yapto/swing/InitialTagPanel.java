package yapto.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.tag.ITag;

/**
 * Implementation of {@link AbstractTreeTagPanel} where multiple {@link ITag}s
 * can be selected.
 * 
 * Used to choose to associate {@link ITag}s to an {@link IPicture} when adding
 * it.
 * 
 * @author benobiwan
 * 
 */
public final class InitialTagPanel extends JPanel implements ActionListener
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -4860591879198822564L;

	/**
	 * Action command for the cancel action.
	 */
	private static final String CANCEL_ACTION_COMMAND = "ca";

	/**
	 * Action command for the select tag action.
	 */
	private static final String SELECT_TAG_ACTION_COMMAND = "se";

	/**
	 * Action command for the add tag action.
	 */
	private static final String ADD_TAG_ACTION_COMMAND = "ad";

	/**
	 * Tree used to specify the list of {@link ITag}s which will be associated
	 * with the {@link IPicture}(s).
	 */
	private final InitialTreeTagPanel _tagInit;

	/**
	 * Parent {@link JDialog} of this Panel.
	 */
	private final JDialog _parent;

	/**
	 * The {@link PictureBankList} used to load the {@link IPictureBank} used as
	 * source for the {@link IPicture}.
	 */
	private final PictureBankList _bankList;

	/**
	 * Lock protecting access to the _bAddSelected boolean.
	 */
	private final Object _lock = new Object();

	/**
	 * Boolean telling whether the tag needs to be added or not.
	 */
	private boolean _bAddSelected = false;

	/**
	 * Dialog for tag creation.
	 */
	private final JDialog _dialogCreateTag;

	/**
	 * Panel used for tag creation.
	 */
	private final AddTagPanel _addTagPanel;

	/**
	 * Creates a new InitialTagPanel.
	 * 
	 * @param parent
	 *            parent {@link JDialog}.
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public InitialTagPanel(final JDialog parent, final PictureBankList bankList)
	{
		super(new BorderLayout(5, 5));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		_parent = parent;
		_bankList = bankList;
		_tagInit = new InitialTreeTagPanel(_bankList);
		// dialog for adding new tag
		_dialogCreateTag = new JDialog(parent, "Create tag", true);
		_addTagPanel = new AddTagPanel(_dialogCreateTag, _bankList);
		_dialogCreateTag.setContentPane(_addTagPanel);
		// panel holding the buttons
		final JPanel panelButton = new JPanel(new GridLayout(1, 3, 10, 10));
		final JButton buttonSelTag = new JButton("Select Tags");
		buttonSelTag.setActionCommand(SELECT_TAG_ACTION_COMMAND);
		buttonSelTag.addActionListener(this);
		final JButton buttonAddTag = new JButton("Add new Tag");
		buttonAddTag.setActionCommand(ADD_TAG_ACTION_COMMAND);
		buttonAddTag.addActionListener(this);
		final JButton buttonCancel = new JButton("Cancel");
		buttonCancel.setActionCommand(CANCEL_ACTION_COMMAND);
		buttonCancel.addActionListener(this);
		panelButton.add(buttonSelTag);
		panelButton.add(buttonAddTag);
		panelButton.add(buttonCancel);
		add(_tagInit, BorderLayout.CENTER);
		add(panelButton, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		if (SELECT_TAG_ACTION_COMMAND.equals(e.getActionCommand()))
		{
			synchronized (_lock)
			{
				_bAddSelected = true;
			}
			_parent.setVisible(false);
		}
		else if (CANCEL_ACTION_COMMAND.equals(e.getActionCommand()))
		{
			_parent.setVisible(false);
		}
		else if (ADD_TAG_ACTION_COMMAND.equals(e.getActionCommand()))
		{
			_addTagPanel.initialize(false, null);
			_dialogCreateTag.pack();
			_dialogCreateTag.setVisible(true);
		}
	}

	/**
	 * Clear checking of all the {@link ITag}s.
	 */
	public void clearSelection()
	{
		synchronized (_lock)
		{
			_bAddSelected = false;
		}
		_tagInit.unsetSelectedTags();
	}

	/**
	 * Get the list of checked {@link ITag}s.
	 * 
	 * @return the list of checked {@link ITag}s.
	 */
	public List<ITag> getCheckedTags()
	{
		synchronized (_lock)
		{
			if (_bAddSelected)
			{
				return _tagInit.getCheckedTags();
			}
			return null;
		}
	}
}
