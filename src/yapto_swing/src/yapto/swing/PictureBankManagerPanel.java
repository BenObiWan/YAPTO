package yapto.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.PictureBankListChangedEvent;
import yapto.picturebank.config.IPictureBankConfiguration;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import common.config.InvalidConfigurationException;

public final class PictureBankManagerPanel extends JPanel implements
		ActionListener
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5033785594456871399L;

	/**
	 * Action command for the "add {@link IPictureBank}" action.
	 */
	private static final String ADD_ACTION_COMMAND = "a";

	/**
	 * Action command for the "remove {@link IPictureBank}" action.
	 */
	private static final String REMOVE_ACTION_COMMAND = "r";

	/**
	 * Action command for the "open {@link IPictureBank}s" action.
	 */
	private static final String OPEN_ACTION_COMMAND = "o";

	/**
	 * Action command for the "cancel" action.
	 */
	private static final String CANCEL_ACTION_COMMAND = "c";

	/**
	 * Panel displaying the list of {@link IPictureBank} to load.
	 */
	private final JPanel _panelChooser = new JPanel(new GridLayout(0, 1));

	/**
	 * ButtonGroup used when only one {@link IPictureBank} can be selected.
	 */
	private final ButtonGroup _group = new ButtonGroup();

	/**
	 * CheckBox enabling or disabling the ability to open multiple
	 * {@link IPictureBank}.
	 */
	private final JCheckBox _checkBoxMulti = new JCheckBox("Multiple sources");

	/**
	 * The {@link PictureBankList}.
	 */
	private final PictureBankList _pictureBankList;

	/**
	 * Creates a new {@link PictureBankManagerPanel}.
	 * 
	 * @param pictureBankList
	 *            the {@link PictureBankList} to use.
	 */
	public PictureBankManagerPanel(final PictureBankList pictureBankList)
	{
		super(new BorderLayout(10, 10));
		_pictureBankList = pictureBankList;
		pictureBankList.register(this);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		final JPanel panelBigChooser = new JPanel(new BorderLayout(5, 5));
		panelBigChooser.setBorder(new CompoundBorder(BorderFactory
				.createRaisedBevelBorder(), new EmptyBorder(10, 10, 10, 10)));
		final JScrollPane scrollPaneChooser = new JScrollPane(_panelChooser);
		scrollPaneChooser
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		final JPanel panelButtonsChooser = new JPanel(new GridLayout(1, 0, 10,
				0));
		final JPanel panelBottomChooser = new JPanel(new GridLayout(2, 1, 5, 0));
		panelBottomChooser.add(_checkBoxMulti);
		panelBottomChooser.add(panelButtonsChooser);
		panelBigChooser.add(scrollPaneChooser, BorderLayout.CENTER);
		panelBigChooser.add(panelBottomChooser, BorderLayout.PAGE_END);

		final JButton buttonAdd = new JButton("Add");
		buttonAdd.setActionCommand(ADD_ACTION_COMMAND);
		buttonAdd.addActionListener(this);
		panelButtonsChooser.add(buttonAdd);

		final JButton buttonRemove = new JButton("Remove");
		buttonRemove.setActionCommand(REMOVE_ACTION_COMMAND);
		buttonRemove.addActionListener(this);
		panelButtonsChooser.add(buttonRemove);

		_checkBoxMulti.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				reload();
			}
		});

		final JPanel panelButtons = new JPanel(new GridLayout(1, 0, 10, 0));

		final JButton buttonOpen = new JButton("Open");
		buttonOpen.setActionCommand(OPEN_ACTION_COMMAND);
		buttonOpen.addActionListener(this);
		panelButtons.add(buttonOpen);

		final JButton buttonCancel = new JButton("Cancel");
		buttonCancel.setActionCommand(CANCEL_ACTION_COMMAND);
		buttonCancel.addActionListener(this);
		panelButtons.add(buttonCancel);

		add(panelBigChooser, BorderLayout.CENTER);
		add(panelButtons, BorderLayout.PAGE_END);

		reload();
	}

	/**
	 * Clear the list of registered {@link IPictureBank}.
	 */
	private void clearAll()
	{
		final Enumeration<AbstractButton> list = _group.getElements();
		while (list.hasMoreElements())
		{
			final AbstractButton but = list.nextElement();
			_group.remove(but);
		}
		_panelChooser.removeAll();
	}

	private void addPictureBank(
			final IPictureBankConfiguration pictureBankConfiguration)
	{
		JToggleButton comp;
		if (_checkBoxMulti.isSelected())
		{
			comp = new JCheckBox(pictureBankConfiguration.getPictureBankName());
		}
		else
		{
			comp = new JRadioButton(
					pictureBankConfiguration.getPictureBankName());
			_group.add(comp);
		}
		_panelChooser.add(comp);
		comp.setActionCommand(String.valueOf(pictureBankConfiguration
				.getPictureBankId()));
	}

	protected void reload()
	{
		_panelChooser.setVisible(false);
		clearAll();

		for (final IPictureBankConfiguration conf : _pictureBankList
				.getAllPictureBankConfiguration())
		{
			addPictureBank(conf);
		}

		for (final IPictureBank<?> sel : _pictureBankList
				.getSelectedPictureBank())
		{
			// TODO
		}

		_panelChooser.setVisible(true);
	}

	@Subscribe
	public void handlePictureBankListChangedEvent(
			@SuppressWarnings("unused") final PictureBankListChangedEvent ev)
	{
		reload();
	}

	@Override
	public void actionPerformed(final ActionEvent ae)
	{
		System.out.println(ae.getActionCommand());
		// TODO Auto-generated method stub

	}

	public static void main(final String[] args)
			throws InvalidConfigurationException
	{
		final JFrame main = new JFrame("test");
		final EventBus _bus = new EventBus();
		final PictureBankList pictureBankList = new PictureBankList(_bus);

		main.setContentPane(new PictureBankManagerPanel(pictureBankList));
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.pack();
		main.setVisible(true);
	}
}
