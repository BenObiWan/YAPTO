package yapto.picturebank.sqlfile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.ImageFormatType;
import yapto.picturebank.PictureInformation;
import yapto.picturebank.tag.ITag;

/**
 * Implementation of the {@link IPicture} interface.
 * 
 * @author benobiwan
 * 
 */
public final class FsPicture implements IPicture
{
	/**
	 * Lock protecting access to this object attributes.
	 */
	private static final Object _lock = new Object();

	/**
	 * The id of the picture.
	 */
	private final String _strId;

	/**
	 * The id of the picture with extension.
	 */
	private final String _strIdWithExt;

	private final String _strIdJPG;

	/**
	 * Set containing all the {@link ITag}s associated with this
	 * {@link FsPicture}.
	 */
	private final Set<ITag> _tagSet = new ConcurrentSkipListSet<>();

	/**
	 * The {@link IPictureBank} from which this {@link IPicture} is coming.
	 */
	private final SQLFilePictureBank _pictureBank;

	/**
	 * {@link ImageLoader} used to load the {@link BufferedImage}.
	 */
	private final ImageLoader _imageLoader;

	/**
	 * The timestamp of the last modification of this picture.
	 */
	private long _lModifiedTimestamp;

	/**
	 * The timestamp of the addition of this picture to the {@link IPictureBank}
	 * .
	 */
	private final long _lAddingTimestamp;

	/**
	 * The grade of this picture.
	 */
	private int _iPictureGrade;

	/**
	 * Boolean telling whether this FsPicture has been modified or not.
	 */
	private boolean _bModified = false;

	/**
	 * The {@link PictureInformation} of this picture.
	 */
	private PictureInformation _pictureInformation;

	/**
	 * Creates a new FsPicture.
	 * 
	 * @param imageLoader
	 *            the {@link ImageLoader} used to load the {@link BufferedImage}
	 *            .
	 * @param pictureBank
	 *            the {@link IPictureBank} from which this {@link IPicture} is
	 *            coming.
	 * @param strId
	 *            the id of the picture.
	 * @param lModifiedTimestamp
	 *            the timestamp of the last modification of this picture.
	 * @param lAddingTimestamp
	 *            the timestamp of the addition of this picture to the
	 *            {@link IPictureBank}.
	 * @param pictureInformation
	 *            the {@link PictureInformation} of this picture.
	 */
	public FsPicture(final ImageLoader imageLoader,
			final SQLFilePictureBank pictureBank, final String strId,
			final long lModifiedTimestamp, final long lAddingTimestamp,
			final PictureInformation pictureInformation)
	{
		_strId = strId;
		_strIdWithExt = _strId + '.' + pictureInformation.getExtension();
		_strIdJPG = _strId + '.' + ImageFormatType.JPG.getExtension();
		_imageLoader = imageLoader;
		_pictureBank = pictureBank;
		_lAddingTimestamp = lAddingTimestamp;
		synchronized (_lock)
		{
			_lModifiedTimestamp = lModifiedTimestamp;
			_pictureInformation = pictureInformation;
		}
	}

	/**
	 * Creates a new FsPicture.
	 * 
	 * @param imageLoader
	 *            the {@link ImageLoader} used to load the {@link BufferedImage}
	 *            .
	 * @param pictureBank
	 *            the {@link IPictureBank} from which this {@link IPicture} is
	 *            coming.
	 * @param strId
	 *            the id of the picture.
	 * @param lModifiedTimestamp
	 *            the timestamp of the last modification of this picture.
	 * @param lAddingTimestamp
	 *            the timestamp of the addition of this picture to the
	 *            {@link IPictureBank}.
	 * @param iPictureGrade
	 *            the new grade of this picture.
	 * @param pictureInformation
	 *            the {@link PictureInformation} of this picture.
	 * @param tagList
	 *            list of {@link ITag}s.
	 */
	public FsPicture(final ImageLoader imageLoader,
			final SQLFilePictureBank pictureBank, final String strId,
			final long lModifiedTimestamp, final long lAddingTimestamp,
			final int iPictureGrade,
			final PictureInformation pictureInformation,
			final List<ITag> tagList)
	{
		this(imageLoader, pictureBank, strId, lModifiedTimestamp,
				lAddingTimestamp, pictureInformation);
		_tagSet.addAll(tagList);
		synchronized (_lock)
		{
			_iPictureGrade = iPictureGrade;
		}
	}

	/**
	 * Create a new FsPicture and the associated {@link PictureInformation}.
	 * 
	 * @param imageLoader
	 *            the {@link ImageLoader} used to load the {@link BufferedImage}
	 *            .
	 * @param pictureBank
	 *            the {@link IPictureBank} from which this {@link IPicture} is
	 *            coming.
	 * @param strId
	 *            the id of the picture.
	 * @param lModifiedTimestamp
	 *            the timestamp of the last modification of this picture.
	 * @param lAddingTimestamp
	 *            the timestamp of the addition of this picture to the
	 *            {@link IPictureBank}.
	 * @param iPictureGrade
	 *            the new grade of this picture.
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
	 * @param tagList
	 *            list of {@link ITag}s.
	 */
	public FsPicture(final ImageLoader imageLoader,
			final SQLFilePictureBank pictureBank, final String strId,
			final long lModifiedTimestamp, final long lAddingTimestamp,
			final int iPictureGrade, final String strOriginalFileName,
			final int iWidth, final int iHeight, final long lCreationTimestamp,
			final int iOrientation, final String strMake,
			final String strModel, final String strExposureTime,
			final String strRelativeAperture, final String strFocalLength,
			final String strFileType, final List<ITag> tagList)
	{
		this(imageLoader, pictureBank, strId, lModifiedTimestamp,
				lAddingTimestamp, iPictureGrade, new PictureInformation(
						strOriginalFileName, iWidth, iHeight,
						lCreationTimestamp, iOrientation, strMake, strModel,
						strExposureTime, strRelativeAperture, strFocalLength,
						strFileType), tagList);
	}

	@Override
	public String getId()
	{
		return _strId;
	}

	@Override
	public String getIdWithExt()
	{
		return _strIdWithExt;
	}

	@Override
	public Set<ITag> getTagSet()
	{
		return Collections.unmodifiableSet(_tagSet);
	}

	@Override
	public BufferedImage getImageData() throws IOException
	{
		if (isDiplayable())
		{
			return _imageLoader.getMainImageData(_strIdWithExt);
		}
		return _imageLoader.getSecondaryImageData(_strIdJPG);
	}

	@Override
	public BufferedImage getThumbnailData() throws IOException
	{
		return _imageLoader.getThumbnailData(_strId);
	}

	@Override
	public long getModifiedTimestamp()
	{
		synchronized (_lock)
		{
			return _lModifiedTimestamp;
		}
	}

	@Override
	public int compareTo(final IPicture otherPicture)
	{
		return getId().compareTo(otherPicture.getId());
	}

	@Override
	public IPictureBank<FsPicture> getPictureBank()
	{
		return _pictureBank;
	}

	@Override
	public void addTag(final ITag newTag)
	{
		if (!_tagSet.contains(newTag))
		{
			synchronized (_lock)
			{
				_lModifiedTimestamp = System.currentTimeMillis();
				_bModified = true;
				_pictureBank.setPictureForUpdating(this);
			}
			_tagSet.add(newTag);
		}
	}

	@Override
	public void removeTag(final ITag tag)
	{
		if (_tagSet.contains(tag))
		{
			synchronized (_lock)
			{
				_lModifiedTimestamp = System.currentTimeMillis();
				_bModified = true;
				_pictureBank.setPictureForUpdating(this);
			}
			_tagSet.remove(tag);
		}
	}

	@Override
	public void setTagList(final List<ITag> tags)
	{
		if (_tagSet.retainAll(tags) | _tagSet.addAll(tags))
		{
			synchronized (_lock)
			{
				_lModifiedTimestamp = System.currentTimeMillis();
				_bModified = true;
				_pictureBank.setPictureForUpdating(this);
			}
		}
	}

	/**
	 * Check whether this FsPicture has been modified or not.
	 * 
	 * @return true if this FsPicture has been modified.
	 */
	public boolean hasBeenModified()
	{
		synchronized (_lock)
		{
			return _bModified;
		}
	}

	/**
	 * Change the modified status of this {@link FsPicture} to unmodified.
	 */
	public void unsetModified()
	{
		synchronized (_lock)
		{
			_bModified = false;
		}
	}

	@Override
	public int getPictureGrade()
	{
		synchronized (_lock)
		{
			return _iPictureGrade;
		}
	}

	@Override
	public void setPictureGrade(final int iPictureGrade)
	{
		synchronized (_lock)
		{
			if (_iPictureGrade != iPictureGrade)
			{
				_lModifiedTimestamp = System.currentTimeMillis();
				_bModified = true;
				_iPictureGrade = iPictureGrade;
				_pictureBank.setPictureForUpdating(this);
			}
		}
	}

	/**
	 * Get the timestamp of the addition of this picture to the
	 * {@link IPictureBank}.
	 * 
	 * @return the timestamp of the addition of this picture to the
	 *         {@link IPictureBank}.
	 */
	public long getAddingTimestamp()
	{
		return _lAddingTimestamp;
	}

	@Override
	public PictureInformation getPictureInformation()
	{
		return _pictureInformation;
	}

	@Override
	public boolean isDiplayable()
	{
		synchronized (_lock)
		{
			return (_pictureInformation != null)
					&& (_pictureInformation.getImageFormat().doesKeepFormat());
		}
	}

	@Override
	public ImageFormatType getImageType()
	{
		synchronized (_lock)
		{
			return _pictureInformation.getImageFormat();
		}
	}
}
