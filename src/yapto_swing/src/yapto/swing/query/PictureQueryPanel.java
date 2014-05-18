package yapto.swing.query;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureBankList;

public final class PictureQueryPanel extends JPanel implements ActionListener
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -2773568037204858677L;

	/**
	 * Action command for the ok command.
	 */
	private static final String OK_ACTION_COMMAND = "ok";

	/**
	 * Action command for the cancel command.
	 */
	private static final String CANCEL_ACTION_COMMAND = "cancel";

	/**
	 * The {@link PictureBankList} used to load the {@link IPictureBank} used as
	 * source for the {@link IPicture}.
	 */
	private final PictureBankList _bankList;

	/**
	 * Parent {@link JDialog}.
	 */
	private final JDialog _parent;

	private final GradeQueryPanel _gradeQueryPanel = new GradeQueryPanel();

	private final DateQueryPanel _dateQueryPanel = new DateQueryPanel();

	private final FilteringTreeTagPanel _TagQueryPanel;

	/**
	 * Creates a new PictureQueryPanel.
	 * 
	 * @param parent
	 *            parent {@link JDialog}.
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public PictureQueryPanel(final JDialog parent,
			final PictureBankList bankList)
	{
		super(new BorderLayout(5, 5));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		_bankList = bankList;
		_parent = parent;
		final JPanel panelButton = new JPanel(new GridLayout(1, 0, 10, 10));
		final JButton buttonOk = new JButton("ok");
		buttonOk.setActionCommand(OK_ACTION_COMMAND);
		buttonOk.addActionListener(this);
		final JButton buttonCancel = new JButton("cancel");
		buttonCancel.setActionCommand(CANCEL_ACTION_COMMAND);
		buttonCancel.addActionListener(this);
		panelButton.add(buttonOk);
		panelButton.add(buttonCancel);

		final JPanel panelTopSearch = new JPanel(new GridLayout(0, 1));
		panelTopSearch.add(_gradeQueryPanel);
		panelTopSearch.add(_dateQueryPanel);

		_gradeQueryPanel.setBorder(BorderFactory.createTitledBorder("Grade"));
		_dateQueryPanel.setBorder(BorderFactory
				.createTitledBorder("Picture Date"));

		_TagQueryPanel = new FilteringTreeTagPanel(_bankList);

		add(panelTopSearch, BorderLayout.PAGE_START);
		add(_TagQueryPanel, BorderLayout.CENTER);
		add(panelButton, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(final ActionEvent ae)
	{
		switch (ae.getActionCommand())
		{
		case OK_ACTION_COMMAND:
			// TODO change the filter there
			_parent.setVisible(false);
			break;
		case CANCEL_ACTION_COMMAND:
		default:
			_parent.setVisible(false);
			break;
		}
	}

}
