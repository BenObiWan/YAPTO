package yapto.swing.query;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureBankList;

/**
 * Panel for creating a query to select which pictures are displayed.
 * 
 * @author benobiwan
 * 
 */
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

	/**
	 * Checkbox to enable or disable the grade panel.
	 */
	private final JCheckBox _cbGrade = new JCheckBox();

	/**
	 * Panel for selecting the grade part of the query.
	 */
	private final GradeQueryPanel _gradeQueryPanel = new GradeQueryPanel();

	/**
	 * Checkbox to enable or disable the date panel.
	 */
	private final JCheckBox _cbDate = new JCheckBox();

	/**
	 * Panel for selecting the date part of the query.
	 */
	private final DateQueryPanel _dateQueryPanel = new DateQueryPanel();

	/**
	 * Checkbox to enable or disable the tag panel.
	 */
	private final JCheckBox _cbTag = new JCheckBox();

	/**
	 * Panel for selecting the tag part of the query.
	 */
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

		final JPanel panelGrade = new JPanel(new BorderLayout());
		final JPanel panelGradeCb = new JPanel(new BorderLayout());
		panelGradeCb.add(_cbGrade, BorderLayout.PAGE_START);
		panelGrade.add(panelGradeCb, BorderLayout.LINE_START);
		panelGrade.add(_gradeQueryPanel, BorderLayout.CENTER);
		panelTopSearch.add(panelGrade);

		final JPanel panelDate = new JPanel(new BorderLayout());
		final JPanel panelDateCb = new JPanel(new BorderLayout());
		panelDateCb.add(_cbDate, BorderLayout.PAGE_START);
		panelDate.add(panelDateCb, BorderLayout.LINE_START);
		panelDate.add(_dateQueryPanel, BorderLayout.CENTER);
		panelTopSearch.add(panelDate);

		_gradeQueryPanel.setBorder(BorderFactory.createTitledBorder("Grade"));
		_dateQueryPanel.setBorder(BorderFactory
				.createTitledBorder("Picture Date"));

		_TagQueryPanel = new FilteringTreeTagPanel(_bankList);
		final JPanel panelTag = new JPanel(new BorderLayout());
		final JPanel panelTagCb = new JPanel(new BorderLayout());
		panelTagCb.add(_cbTag, BorderLayout.PAGE_START);
		panelTag.add(panelTagCb, BorderLayout.LINE_START);
		panelTag.add(_TagQueryPanel, BorderLayout.CENTER);
		panelTopSearch.add(panelTag);

		add(panelTopSearch, BorderLayout.PAGE_START);
		add(panelTag, BorderLayout.CENTER);
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
