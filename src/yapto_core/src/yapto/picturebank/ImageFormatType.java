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
	JPG("JPG", "JPEG (Joint Photographic Experts Group JFIF format)", true),

	/**
	 * PNG image file.
	 */
	PNG("PNG", "PNG (Portable Network Graphics)", true),

	/**
	 * CR2 image file.
	 */
	CR2("CR2", "CR2 (Canon Digital Camera Raw Image Format)", false),

	/**
	 * Unknown image file.
	 */
	UNKNOWN("", "", false);

	/**
	 * Extension of this image format.
	 */
	private final String _strExtension;

	/**
	 * ImageMagick description of this image format.
	 */
	private final String _strImageMagickIdentify;

	/**
	 * Whether to keep secondary images and thumbnails in this file format or
	 * not.
	 */
	private final boolean _bKeepFormat;

	/**
	 * Creates a new PictureAddExceptionType.
	 * 
	 * @param strExtension
	 *            extension of this image format.
	 * @param strImageMagickIdentify
	 *            ImageMagick description of this image format.
	 * @param bKeepFormat
	 *            whether to keep secondary images and thumbnails in this file
	 *            format or not.
	 */
	private ImageFormatType(final String strExtension,
			final String strImageMagickIdentify, boolean bKeepFormat)
	{
		_strExtension = strExtension;
		_strImageMagickIdentify = strImageMagickIdentify;
		_bKeepFormat = bKeepFormat;
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

	/**
	 * Get whether this format will be kept for the secondary images and
	 * thumbnails.
	 * 
	 * @return true if this format will be kept for the secondary images and
	 *         thumbnails.
	 */
	public boolean doesKeepFormat()
	{
		return _bKeepFormat;
	}
}
