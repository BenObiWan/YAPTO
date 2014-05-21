package yapto.picturebank.index;

/**
 * Type of date filtering for use in {@link QueryBuilder}.
 * 
 * @author benobiwan
 * 
 */
public enum DateFilteringType
{
	/**
	 * Date must be between the two selected values.
	 */
	INTERVAL("Interval"),

	/**
	 * Date must be greater or equal than the selected value.
	 */
	GREATER_OR_EQUAL("Greater or equal"),

	/**
	 * Date must be lower or equal than the selected value.
	 */
	LOWER_OR_EQUAL("Lower or equal");

	/**
	 * Description of this date filtering type.
	 */
	private final String _strDescription;

	/**
	 * Creates a new DateFilteringType.
	 * 
	 * @param strDescription
	 *            description for this date filtering type.
	 */
	private DateFilteringType(final String strDescription)
	{
		_strDescription = strDescription;
	}

	/**
	 * Get the description for this date filtering type.
	 * 
	 * @return the description for this date filtering type.
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
