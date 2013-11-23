package yapto.picturebank.process;

/**
 * Supported types of image file.
 * 
 * @author benobiwan
 */
public enum ImageFormatType
{
	/**
	 * JPG image file.
	 */
	JPG("jpg"),

	/**
	 * PNG image file.
	 */
	PNG("png");

	/**
	 * Message for this exception type.
	 */
	private final String _strExtension;

	/**
	 * Creates a new PictureAddExceptionType.
	 * 
	 * @param strExtension
	 *            message for this exception type.
	 */
	private ImageFormatType(final String strExtension)
	{
		_strExtension = strExtension;
	}

	/**
	 * Get the message for this exception type.
	 * 
	 * @return the message for this exception type.
	 */
	public String getExtension()
	{
		return _strExtension;
	}
}
