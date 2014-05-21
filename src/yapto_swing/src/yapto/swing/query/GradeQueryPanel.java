package yapto.swing.query;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;

import yapto.picturebank.index.GradeFilteringType;

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
		super(new BorderLayout(5, 5));
		add(_gradeFilteringTypeComboBox, BorderLayout.LINE_START);
		add(_gradeSlider, BorderLayout.CENTER);
		_gradeFilteringTypeComboBox
				.setSelectedItem(GradeFilteringType.GREATER_OR_EQUAL);
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
