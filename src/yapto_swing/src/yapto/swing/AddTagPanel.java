package yapto.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.tag.ITag;
import yapto.datasource.tag.TagAddException;

/**
 * Panel used to enter all the information of a new {@link ITag} and create it.
 * 
 * @author benobiwan
 * 
 */
public final class AddTagPanel extends JPanel implements ActionListener
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 1479318453431238470L;

	/**
	 * Action command for the create tag action.
	 */
	private static final String CREATE_ACTION_COMMAND = "cr";

	/**
	 * Action command for the cancel action.
	 */
	private static final String CANCEL_ACTION_COMMAND = "ca";

	/**
	 * Title for the error dialog box.
	 */
	private static final String ERROR_DIALOG_TITLE = "Tag add error";

	/**
	 * Text field used to enter the name of the new {@link ITag}.
	 */
	private final JTextField _tagNameField = new JTextField();

	/**
	 * Check box used to specify if the new {@link ITag} is selectable or not.
	 */
	private final JCheckBox _tagSelectableField = new JCheckBox();

	/**
	 * Text area used to enter the description of the new {@link ITag}.
	 */
	private final JTextArea _tagDescriptionField = new JTextArea();

	/**
	 * Combo box used to specify the parent of the new {@link ITag}.
	 */
	private final AbstractTreeTagPanel _tagParent;

	/**
	 * Parent {@link JDialog} of this Panel.
	 */
	private final JDialog _parent;

	/**
	 * The {@link IPictureBrowser} where to create tags.
	 */
	private final IPictureBrowser<?> _pictureBrowser;

	/**
	 * Creates a new AddTagPanel.
	 * 
	 * @param parent
	 *            parent {@link JDialog} of this Panel.
	 * @param pictureBrowser
	 *            the {@link IPictureBrowser} where to create tags.
	 */
	public AddTagPanel(final JDialog parent,
			final IPictureBrowser<? extends IPicture> pictureBrowser)
	{
		super(new BorderLayout(5, 5));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		_parent = parent;
		_pictureBrowser = pictureBrowser;
		_tagParent = new ParentingTreeTagPanel(pictureBrowser);

		final JPanel panelHead = new JPanel(new GridLayout(2, 2, 5, 5));
		panelHead.add(new JLabel("Name "));
		panelHead.add(_tagNameField);
		panelHead.add(new JLabel("Selectable "));
		panelHead.add(_tagSelectableField);
		_tagSelectableField.setSelected(true);

		final JPanel panelParent = new JPanel(new BorderLayout());
		panelParent.add(new JLabel("Parent :"), BorderLayout.PAGE_START);
		panelParent.add(_tagParent, BorderLayout.CENTER);

		final JPanel panelDescription = new JPanel(new BorderLayout());
		panelDescription.add(new JLabel("Description :"),
				BorderLayout.PAGE_START);
		final JScrollPane scrollPane = new JScrollPane(_tagDescriptionField);
		panelDescription.add(scrollPane, BorderLayout.CENTER);

		final JPanel panelCenter = new JPanel(new GridLayout(2, 1, 5, 5));
		panelCenter.add(panelParent);
		panelCenter.add(panelDescription);

		final JPanel panelButton = new JPanel(new GridLayout(1, 2, 10, 10));
		final JButton buttonCreate = new JButton("Create");
		buttonCreate.setActionCommand(CREATE_ACTION_COMMAND);
		buttonCreate.addActionListener(this);
		final JButton buttonCancel = new JButton("Cancel");
		buttonCancel.setActionCommand(CANCEL_ACTION_COMMAND);
		buttonCancel.addActionListener(this);
		panelButton.add(buttonCreate);
		panelButton.add(buttonCancel);

		add(panelHead, BorderLayout.PAGE_START);
		add(panelCenter, BorderLayout.CENTER);
		add(panelButton, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		if (CREATE_ACTION_COMMAND.equals(e.getActionCommand()))
		{
			final boolean bSelectable = _tagSelectableField.isSelected();
			final String strName = _tagNameField.getText();
			final String strDescription = _tagDescriptionField.getText();
			// _tagParent
			try
			{
				_pictureBrowser.getDataSource().addTag(strName, strDescription,
						bSelectable);
				_parent.setVisible(false);
			}
			catch (final TagAddException ex)
			{
				JOptionPane.showMessageDialog(_parent, ex.getExceptionType()
						.getMessage(), ERROR_DIALOG_TITLE,
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (CANCEL_ACTION_COMMAND.equals(e.getActionCommand()))
		{
			_parent.setVisible(false);
		}
	}
}
