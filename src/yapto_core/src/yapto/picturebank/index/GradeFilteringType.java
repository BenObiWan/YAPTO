package yapto.picturebank.index;

/**
 * Type of grade filtering for use in {@link QueryBuilder}.
 * 
 * @author benobiwan
 * 
 */
public enum GradeFilteringType
{
	/**
	 * Grade must be equal to the selected value.
	 */
	EQUAL("Equal"),

	/**
	 * Grade must be greater or equal than the selected value.
	 */
	GREATER_OR_EQUAL("Greater or equal"),

	/**
	 * Grade must be lower or equal than the selected value.
	 */
	LOWER_OR_EQUAL("Lower or equal");

	/**
	 * Description of this grade filtering type.
	 */
	private final String _strDescription;

	/**
	 * Creates a new GradeFilteringType.
	 * 
	 * @param strDescription
	 *            description for this grade filtering type.
	 */
	private GradeFilteringType(final String strDescription)
	{
		_strDescription = strDescription;
	}

	/**
	 * Get the description for this grade filtering type.
	 * 
	 * @return the description for this grade filtering type.
	 */
	public String getDescription()
	{
		return _strDescription;
	}

	@Override
	public String toString()
	{
		return getDescription();
	}
}
