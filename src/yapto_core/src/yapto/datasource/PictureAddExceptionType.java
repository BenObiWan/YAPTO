package yapto.datasource;

public enum PictureAddExceptionType
{
	FILE_NOT_FOUND(""),

	NOT_A_FILE(""),

	CAN_T_READ(""),

	NO_SUCH_ALGORITHM(""),

	FILE_ALREADY_EXISTS(""),

	DIRECTORY_NOT_EMPTY(""),

	COPY_ERROR(""),

	IO_ERROR(""),

	SQL_INSERT_ERROR(""),

	CORRUPT_INDEX_ERROR(""),

	INDEX_ERROR("");

	/**
	 * Message for this exception type.
	 */
	private final String _strMessage;

	/**
	 * Creates a new PictureAddExceptionType;
	 * 
	 * @param strMessage
	 *            message for this exception type.
	 */
	private PictureAddExceptionType(final String strMessage)
	{
		_strMessage = strMessage;
	}

	/**
	 * Get the message for this exception type.
	 * 
	 * @return the message for this exception type.
	 */
	public String getMessage()
	{
		return _strMessage;
	}
}
