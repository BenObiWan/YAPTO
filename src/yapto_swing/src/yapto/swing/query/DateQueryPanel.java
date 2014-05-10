package yapto.swing.query;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class DateQueryPanel extends JPanel
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

	public DateQueryPanel()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get the selected filtering types.
	 * 
	 * @return the selected filtering types.
	 */
	public DateFilteringType getGradeFilteringType()
	{
		return (DateFilteringType) _dateFilteringTypeComboBox.getSelectedItem();
	}

	// long getStartingDate()
	// {
	//
	// }
	//
	// long getEndingDate()
	// {
	//
	// }
}
