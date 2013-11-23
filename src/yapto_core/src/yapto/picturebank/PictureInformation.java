package yapto.picturebank;

import java.awt.Dimension;

/**
 * Object holding all the meta information of a picture.
 * 
 * @author benobiwan
 * 
 */
public final class PictureInformation
{
	/**
	 * The original name of the file.
	 */
	private final String _strOriginalFileName;

	/**
	 * Dimension of the picture.
	 */
	private final Dimension _pictureDimension;

	/**
	 * The timestamp of the creation of this picture.
	 */
	private final long _lCreationTimestamp;

	/**
	 * Orientation of this picture.
	 */
	private final int _iOrientation;

	/**
	 * 'Make' exif information of this picture.
	 */
	private final String _strMake;

	/**
	 * 'Model' exif information of this picture.
	 */
	private final String _strModel;

	/**
	 * 'exposure' exif information of this picture.
	 */
	private final String _strExposureTime;

	/**
	 * 'relative aperture' exif information of this picture.
	 */
	private final String _strRelativeAperture;

	/**
	 * 'focal length' exif information of this picture.
	 */
	private final String _strFocalLength;

	/**
	 * 'format' information of this picture.
	 */
	private final String _strFileType;

	/**
	 * Creates a new PictureInformation.
	 * 
	 * @param strOriginalFileName
	 *            original file name.
	 * @param iWidth
	 *            the width of the picture.
	 * @param iHeight
	 *            the height of the picture.
	 * @param lCreationTimestamp
	 *            the timestamp of the creation of this picture.
	 * @param iOrientation
	 *            the orientation of this picture.
	 * @param strMake
	 *            the 'Make' exif information of this picture.
	 * @param strModel
	 *            the 'Model' exif information of this picture.
	 * @param strExposureTime
	 *            the 'exposure' exif information of this picture.
	 * @param strRelativeAperture
	 *            the 'relative aperture' exif information of this picture.
	 * @param strFocalLength
	 *            the 'focal length' exif information of this picture.
	 * @param strFileType
	 *            the 'format' information of this picture.
	 */
	public PictureInformation(final String strOriginalFileName,
			final int iWidth, final int iHeight, final long lCreationTimestamp,
			final int iOrientation, final String strMake,
			final String strModel, final String strExposureTime,
			final String strRelativeAperture, final String strFocalLength,
			final String strFileType)
	{
		super();
		_strOriginalFileName = strOriginalFileName;
		_pictureDimension = new Dimension(iWidth, iHeight);
		_lCreationTimestamp = lCreationTimestamp;
		_iOrientation = iOrientation;
		_strMake = strMake;
		_strModel = strModel;
		_strExposureTime = strExposureTime;
		_strRelativeAperture = strRelativeAperture;
		_strFocalLength = strFocalLength;
		_strFileType = strFileType;
	}

	/**
	 * Get the orientation of this picture.
	 * 
	 * @return the orientation of this picture.
	 */
	public int getOrientation()
	{
		return _iOrientation;
	}

	/**
	 * Get the 'Make' exif information of this picture.
	 * 
	 * @return the 'Make' exif information of this picture.
	 */
	public String getMake()
	{
		return _strMake;
	}

	/**
	 * Get the 'Model' exif information of this picture.
	 * 
	 * @return the 'Model' exif information of this picture.
	 */
	public String getModel()
	{
		return _strModel;
	}

	/**
	 * Get the original name of the file.
	 * 
	 * @return the original name of the file.
	 */
	public String getOriginalFileName()
	{
		return _strOriginalFileName;
	}

	/**
	 * Get the timestamp of the creation of this picture.
	 * 
	 * @return the timestamp of the creation of this picture.
	 */
	public long getCreationTimestamp()
	{
		return _lCreationTimestamp;
	}

	/**
	 * Get the {@link Dimension} of this {@link IPicture}.
	 * 
	 * @return the {@link Dimension} of this {@link IPicture}.
	 */
	public Dimension getDimension()
	{
		return _pictureDimension;
	}

	/**
	 * Get the height of this {@link IPicture}.
	 * 
	 * @return the height of this {@link IPicture}.
	 */
	public int getHeight()
	{
		return (int) _pictureDimension.getHeight();
	}

	/**
	 * Get the width of this {@link IPicture}.
	 * 
	 * @return the width of this {@link IPicture}.
	 */
	public int getWidth()
	{
		return (int) _pictureDimension.getWidth();
	}

	/**
	 * Get the 'exposure' exif information of this picture.
	 * 
	 * @return the 'exposure' exif information of this picture.
	 */
	public String getExposureTime()
	{
		return _strExposureTime;
	}

	/**
	 * Get the 'relative aperture' exif information of this picture.
	 * 
	 * @return the 'relative aperture' exif information of this picture.
	 */
	public String getRelativeAperture()
	{
		return _strRelativeAperture;
	}

	/**
	 * Get the 'focal length' exif information of this picture.
	 * 
	 * @return the 'focal length' exif information of this picture.
	 */
	public String getFocalLength()
	{
		return _strFocalLength;
	}

	/**
	 * Get the 'format' information of this picture.
	 * 
	 * @return the 'format' information of this picture.
	 */
	public String getFormat()
	{
		return _strFileType;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("original file name: ");
		sb.append(_strOriginalFileName);
		sb.append("\ncreation timestamp: ");
		sb.append(_lCreationTimestamp);
		sb.append("\nWidth: ");
		sb.append(getWidth());
		sb.append("\nHeigth: ");
		sb.append(getHeight());
		sb.append("\nOrientation: ");
		sb.append(_iOrientation);
		sb.append("\nMake: ");
		sb.append(_strMake);
		sb.append("\nModel: ");
		sb.append(_strModel);
		sb.append("\nExposure time: ");
		sb.append(_strExposureTime);
		sb.append("\nReltaive aperture: ");
		sb.append(_strRelativeAperture);
		sb.append("\nFocal length: ");
		sb.append(_strFocalLength);
		sb.append("\nFormat: ");
		sb.append(_strFileType);
		return sb.toString();
	}
}
