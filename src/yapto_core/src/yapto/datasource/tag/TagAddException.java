package yapto.datasource.tag;

/**
 * Thrown when there is a problem during the adding of tag.
 * 
 * @author benobiwan
 */
public final class TagAddException extends Exception
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 4981175500739738330L;

	/**
	 * Name of the tag concerned by the exception.
	 */
	private final String _strTagName;

	/**
	 * Type of this exception.
	 */
	private final TagAddExceptionType _type;

	/**
	 * Creates a new TagAddException.
	 * 
	 * @param strTagName
	 *            the name of the tag concerned by the exception.
	 * @param type
	 *            the type of this exception.
	 * @param cause
	 *            the cause of this exception.
	 */
	public TagAddException(final String strTagName,
			final TagAddExceptionType type, final Throwable cause)
	{
		super(type.getMessage(), cause);
		_strTagName = strTagName;
		_type = type;
	}

	/**
	 * Creates a new TagAddException.
	 * 
	 * @param type
	 *            the type of this exception.
	 * @param cause
	 *            the cause of this exception.
	 */
	public TagAddException(final TagAddExceptionType type, final Throwable cause)
	{
		super(type.getMessage(), cause);
		_strTagName = null;
		_type = type;
	}

	/**
	 * Creates a new TagAddException.
	 * 
	 * @param strTagName
	 *            the name of the tag concerned by the exception.
	 * @param type
	 *            the type of this exception.
	 */
	public TagAddException(final String strTagName,
			final TagAddExceptionType type)
	{
		super(type.getMessage());
		_strTagName = strTagName;
		_type = type;
	}

	/**
	 * Creates a new TagAddException.
	 * 
	 * @param type
	 *            the type of this exception.
	 */
	public TagAddException(final TagAddExceptionType type)
	{
		super(type.getMessage());
		_strTagName = null;
		_type = type;
	}

	/**
	 * Get the name of the tag concerned by the exception.
	 * 
	 * @return the name of the tag concerned by the exception.
	 */
	public String getTagName()
	{
		return _strTagName;
	}

	/**
	 * Get the type of this exception.
	 * 
	 * @return the type of this exception.
	 */
	public TagAddExceptionType getExceptionType()
	{
		return _type;
	}
}
