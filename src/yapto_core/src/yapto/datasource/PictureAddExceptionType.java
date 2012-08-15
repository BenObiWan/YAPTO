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
	FILE_NOT_FOUND("The image file is absent."),

	/**
	 * The specified path is not a file.
	 */
	NOT_A_FILE("The specified path is not a file."),

	/**
	 * Can't read the specified file.
	 */
	CAN_T_READ("Can't read the specified file."),

	/**
	 * Specified MessageDigest algorithm doesn't exists.
	 */
	NO_SUCH_ALGORITHM("Specified MessageDigest algorithm doesn't exists."),

	/**
	 * Destination file already exists.
	 */
	FILE_ALREADY_EXISTS("Destination file already exists."),

	/**
	 * IO error during the copy.
	 */
	COPY_ERROR("IO error during the copy."),

	/**
	 * IO error during the reading of the file.
	 */
	IO_ERROR("IO error during the reading of the file."),

	/**
	 * SQL error during the insertion of the picture in the database.
	 */
	SQL_INSERT_ERROR(
			"SQL error during the insertion of the picture in the database."),

	/**
	 * The lucene index is corrupted.
	 */
	CORRUPT_INDEX_ERROR("The lucene index is corrupted."),

	/**
	 * IO error during the indexation.
	 */
	INDEX_ERROR("IO error during the indexation."),

	/**
	 * Error during the execution of identify on the picture.
	 */
	IDENTIFY_EXECUTION_ERROR(
			"Error during the execution of identify on the picture.");

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
