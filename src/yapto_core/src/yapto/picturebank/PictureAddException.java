package yapto.picturebank;

/**
 * Thrown when there is a problem during the adding of picture.
 * 
 * @author benobiwan
 */
public final class PictureAddException extends Exception
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 4981175500739738330L;

	/**
	 * Id of the picture concerned by the exception.
	 */
	private final String _strPictureId;

	/**
	 * Type of this exception.
	 */
	private final PictureAddExceptionType _type;

	/**
	 * Creates a new PictureAddException.
	 * 
	 * @param strFsPictureId
	 *            the id of the picture concerned by the exception.
	 * @param type
	 *            the type of this exception.
	 * @param cause
	 *            the cause of this exception.
	 */
	public PictureAddException(final String strFsPictureId,
			final PictureAddExceptionType type, final Throwable cause)
	{
		super(type.getMessage(), cause);
		_strPictureId = strFsPictureId;
		_type = type;
	}

	/**
	 * Creates a new PictureAddException.
	 * 
	 * @param type
	 *            the type of this exception.
	 * @param cause
	 *            the cause of this exception.
	 */
	public PictureAddException(final PictureAddExceptionType type,
			final Throwable cause)
	{
		super(type.getMessage(), cause);
		_strPictureId = null;
		_type = type;
	}

	/**
	 * Creates a new PictureAddException.
	 * 
	 * @param strFsPictureId
	 *            the id of the picture concerned by the exception.
	 * @param type
	 *            the type of this exception.
	 */
	public PictureAddException(final String strFsPictureId,
			final PictureAddExceptionType type)
	{
		super(type.getMessage());
		_strPictureId = strFsPictureId;
		_type = type;
	}

	/**
	 * Creates a new PictureAddException.
	 * 
	 * @param type
	 *            the type of this exception.
	 */
	public PictureAddException(final PictureAddExceptionType type)
	{
		super(type.getMessage());
		_strPictureId = null;
		_type = type;
	}

	/**
	 * Get the id of the picture concerned by the exception.
	 * 
	 * @return the id of the picture concerned by the exception.
	 */
	public String getPictureId()
	{
		return _strPictureId;
	}

	/**
	 * Get the type of this exception.
	 * 
	 * @return the type of this exception.
	 */
	public PictureAddExceptionType getExceptionType()
	{
		return _type;
	}
}
