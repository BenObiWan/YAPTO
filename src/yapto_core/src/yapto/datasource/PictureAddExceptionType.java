package yapto.datasource;

/**
 * Enum describing the different types of {@link PictureAddException}.
 * 
 * @author benobiwan
 */
public enum PictureAddExceptionType
{
	/**
	 * The image file is absent.
	 */
	FILE_NOT_FOUND(""),

	/**
	 * The specified path is not a file.
	 */
	NOT_A_FILE(""),

	/**
	 * Can't read the specified file.
	 */
	CAN_T_READ(""),

	/**
	 * Specified MessageDigest algorithm doesn't exists.
	 */
	NO_SUCH_ALGORITHM(""),

	/**
	 * Destination file already exists.
	 */
	FILE_ALREADY_EXISTS(""),

	/**
	 * IO error during the copy.
	 */
	COPY_ERROR(""),

	/**
	 * IO error during the reading of the file.
	 */
	IO_ERROR(""),

	/**
	 * SQL error during the insertion of the picture in the database.
	 */
	SQL_INSERT_ERROR(""),

	/**
	 * The lucene index is corrupted.
	 */
	CORRUPT_INDEX_ERROR(""),

	/**
	 * IO error during the indexation.
	 */
	INDEX_ERROR(""),

	/**
	 * Error during the execution of identify on the picture.
	 */
	IDENTIFY_EXECUTION_ERROR("");

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
