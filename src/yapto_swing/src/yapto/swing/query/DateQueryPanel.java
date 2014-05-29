package yapto.swing.query;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXDatePicker;

import yapto.picturebank.index.DateFilteringType;

/**
 * Panel to select the date as part of creating a query.
 * 
 * @author benobiwan
 * 
 */
public final class DateQueryPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 659009991436717474L;

	/**
	 * Combo box used to select between the different filtering types.
	 */
	private final JComboBox<DateFilteringType> _dateFilteringTypeComboBox = new JComboBox<>(
			new DateFilteringType[] { DateFilteringType.GREATER_OR_EQUAL,
					DateFilteringType.INTERVAL,
					DateFilteringType.LOWER_OR_EQUAL });

	/**
	 * Date picker to choose the start date.
	 */
	private final JXDatePicker _startDatePicker = new JXDatePicker();

	/**
	 * Date picker to choose the end date.
	 */
	private final JXDatePicker _endDatePicker = new JXDatePicker();

	/**
	 * Creates a new DateQueryPanel.
	 */
	public DateQueryPanel()
	{
		super(new BorderLayout(5, 5));
		_dateFilteringTypeComboBox.setSelectedItem(DateFilteringType.INTERVAL);
		final JPanel startPanel = new JPanel(new BorderLayout(5, 5));
		startPanel.add(new JLabel("Start"), BorderLayout.LINE_START);
		startPanel.add(_startDatePicker, BorderLayout.CENTER);
		final JPanel endPanel = new JPanel(new BorderLayout(5, 5));
		endPanel.add(new JLabel("End"), BorderLayout.LINE_START);
		endPanel.add(_endDatePicker, BorderLayout.CENTER);
		final JPanel datePanel = new JPanel(new GridLayout(1, 2, 5, 5));
		datePanel.add(startPanel);
		datePanel.add(endPanel);

		add(_dateFilteringTypeComboBox, BorderLayout.LINE_START);
		add(datePanel, BorderLayout.CENTER);

	}

	/**
	 * Get the selected filtering types.
	 * 
	 * @return the selected filtering types.
	 */
	public DateFilteringType getDateFilteringType()
	{
		return (DateFilteringType) _dateFilteringTypeComboBox.getSelectedItem();
	}

	/**
	 * Get the start date.
	 * 
	 * @return the start date.
	 */
	long getStartingDate()
	{
		return _startDatePicker.getDate().getTime();
	}

	/**
	 * Get the end date.
	 * 
	 * @return the end date.
	 */
	long getEndingDate()
	{
		return _endDatePicker.getDate().getTime();
	}
}
