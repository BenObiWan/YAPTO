package yapto.picturebank;

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
	JPG("JPG", "JPEG (Joint Photographic Experts Group JFIF format)"),

	/**
	 * PNG image file.
	 */
	PNG("PNG", "PNG (Portable Network Graphics)"),

	/**
	 * CR2 image file.
	 */
	CR2("CR2", "CR2 (Canon Digital Camera Raw Image Format)"),

	/**
	 * Unknown image file.
	 */
	UNKNOWN("", "");

	/**
	 * Extension of this image format.
	 */
	private final String _strExtension;

	/**
	 * ImageMagick description of this image format.
	 */
	private final String _strImageMagickIdentify;

	/**
	 * Creates a new PictureAddExceptionType.
	 * 
	 * @param strExtension
	 *            extension of this image format.
	 * @param strImageMagickIdentify
	 *            ImageMagick description of this image format.
	 */
	private ImageFormatType(final String strExtension,
			final String strImageMagickIdentify)
	{
		_strExtension = strExtension;
		_strImageMagickIdentify = strImageMagickIdentify;
	}

	/**
	 * Get the extension of this image format.
	 * 
	 * @return the extension of this image format.
	 */
	public String getExtension()
	{
		return _strExtension;
	}

	/**
	 * Get the ImageMagick description of this image format.
	 * 
	 * @return the ImageMagick description of this image format.
	 */
	public String getImageMagickIdentify()
	{
		return _strImageMagickIdentify;
	}
}
