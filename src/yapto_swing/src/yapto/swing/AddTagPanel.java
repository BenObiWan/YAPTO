package yapto.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import yapto.datasource.tag.Tag;

public class AddTagPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 1479318453431238470L;

	/**
	 * Text field used to enter the name of the new {@link Tag}.
	 */
	private final JTextField _tagNameField = new JTextField();

	/**
	 * Check box used to specify if the new {@link Tag} is selectable or not.
	 */
	private final JCheckBox _tagSelectableField = new JCheckBox("Selectable");

	/**
	 * Text area used to enter the description of the new {@link Tag}.
	 */
	private final JTextArea _tagDescriptionField = new JTextArea();

	/**
	 * Combo box used to specify the parent of the new {@link Tag}.
	 */
	private final JComboBox _tagParent = new JComboBox();

	/**
	 * Creates a new AddTagPanel.
	 */
	public AddTagPanel()
	{
		super(new BorderLayout(5, 5));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		final JPanel panelHead = new JPanel(new GridLayout(3, 1, 5, 5));

		final JPanel panelName = new JPanel(new BorderLayout());
		panelName.add(new JLabel("Name "), BorderLayout.LINE_START);
		panelName.add(_tagNameField, BorderLayout.CENTER);
		panelHead.add(panelName);
		panelHead.add(_tagSelectableField);
		panelHead.add(_tagParent);

		final JPanel panelButton = new JPanel(new GridLayout(1, 2, 5, 5));
		panelButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		final JButton buttonCreate = new JButton("Create");
		final JButton buttonCancel = new JButton("Cancel");
		panelButton.add(buttonCreate);
		panelButton.add(buttonCancel);

		add(panelHead, BorderLayout.PAGE_START);
		add(_tagDescriptionField, BorderLayout.CENTER);
		add(panelButton, BorderLayout.PAGE_END);
	}

}
