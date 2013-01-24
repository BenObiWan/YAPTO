package yapto.swing.query;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * Panel used to select grade for filtering pictures.
 * 
 * @author benobiwan
 * 
 */
public final class GradeQueryPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -4481269118474005283L;

	/**
	 * Slider used to select the grade.
	 */
	private final JSlider _gradeSlider = new JSlider(0, 5, 0);

	/**
	 * Combo box used to select between the different filtering types.
	 */
	private final JComboBox<GradeFilteringType> _gradeFilteringTypeComboBox = new JComboBox<>(
			new GradeFilteringType[] { GradeFilteringType.GREATER_OR_EQUAL,
					GradeFilteringType.EQUAL, GradeFilteringType.LOWER_OR_EQUAL });

	/**
	 * Creates a new GradeQueryPanel.
	 */
	public GradeQueryPanel()
	{
		super(new GridLayout(2, 1));
		add(_gradeFilteringTypeComboBox);
		add(_gradeSlider);
		_gradeSlider.setMajorTickSpacing(1);
		_gradeSlider.setPaintLabels(true);
	}

	/**
	 * Get the selected grade.
	 * 
	 * @return the selected grade.
	 */
	public int getSelectedGrade()
	{
		return _gradeSlider.getValue();
	}

	/**
	 * Get the selected filtering types.
	 * 
	 * @return the selected filtering types.
	 */
	public GradeFilteringType getGradeFilteringType()
	{
		return (GradeFilteringType) _gradeFilteringTypeComboBox
				.getSelectedItem();
	}
}
