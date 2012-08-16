package yapto.datasource.sqlfile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.PictureInformation;
import yapto.datasource.tag.Tag;

/**
 * Implementation of the {@link IPicture} interface.
 * 
 * @author benobiwan
 * 
 */
public final class FsPicture implements IPicture
{
	/**
	 * The id of the picture.
	 */
	private final String _strId;

	/**
	 * Set containing all the {@link Tag}s associated with this
	 * {@link FsPicture}.
	 */
	private final Set<Tag> _tagSet = new ConcurrentSkipListSet<>();

	/**
	 * The {@link IDataSource} from which this {@link IPicture} is coming.
	 */
	private final SQLFileDataSource _dataSource;

	/**
	 * {@link ImageLoader} used to load the {@link BufferedImage}.
	 */
	private final ImageLoader _imageLoader;

	/**
	 * The timestamp of the last modification of this picture.
	 */
	private long _lModifiedTimestamp;

	/**
	 * The timestamp of the addition of this picture to the {@link IDataSource}.
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
	 * @param dataSource
	 *            the {@link IDataSource} from which this {@link IPicture} is
	 *            coming.
	 * @param strId
	 *            the id of the picture.
	 * @param lModifiedTimestamp
	 *            the timestamp of the last modification of this picture.
	 * @param lAddingTimestamp
	 *            the timestamp of the addition of this picture to the
	 *            {@link IDataSource}.
	 * @param pictureInformation
	 *            the {@link PictureInformation} of this picture.
	 */
	public FsPicture(final ImageLoader imageLoader,
			final SQLFileDataSource dataSource, final String strId,
			final long lModifiedTimestamp, final long lAddingTimestamp,
			final PictureInformation pictureInformation)
	{
		_strId = strId;
		_imageLoader = imageLoader;
		_dataSource = dataSource;
		_lAddingTimestamp = lAddingTimestamp;
		synchronized (this)
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
	 * @param dataSource
	 *            the {@link IDataSource} from which this {@link IPicture} is
	 *            coming.
	 * @param strId
	 *            the id of the picture.
	 * @param lModifiedTimestamp
	 *            the timestamp of the last modification of this picture.
	 * @param lAddingTimestamp
	 *            the timestamp of the addition of this picture to the
	 *            {@link IDataSource}.
	 * @param iPictureGrade
	 *            the new grade of this picture.
	 * @param pictureInformation
	 *            the {@link PictureInformation} of this picture.
	 * @param tagList
	 *            list of {@link Tag}s.
	 */
	public FsPicture(final ImageLoader imageLoader,
			final SQLFileDataSource dataSource, final String strId,
			final long lModifiedTimestamp, final long lAddingTimestamp,
			final int iPictureGrade,
			final PictureInformation pictureInformation, final List<Tag> tagList)
	{
		this(imageLoader, dataSource, strId, lModifiedTimestamp,
				lAddingTimestamp, pictureInformation);
		_tagSet.addAll(tagList);
		synchronized (this)
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
	 * @param dataSource
	 *            the {@link IDataSource} from which this {@link IPicture} is
	 *            coming.
	 * @param strId
	 *            the id of the picture.
	 * @param lModifiedTimestamp
	 *            the timestamp of the last modification of this picture.
	 * @param lAddingTimestamp
	 *            the timestamp of the addition of this picture to the
	 *            {@link IDataSource}.
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
	 * @param tagList
	 *            list of {@link Tag}s.
	 */
	public FsPicture(final ImageLoader imageLoader,
			final SQLFileDataSource dataSource, final String strId,
			final long lModifiedTimestamp, final long lAddingTimestamp,
			final int iPictureGrade, final String strOriginalFileName,
			final int iWidth, final int iHeight, final long lCreationTimestamp,
			final int iOrientation, final String strMake,
			final String strModel, final String strExposureTime,
			final String strRelativeAperture, final String strFocalLength,
			final List<Tag> tagList)
	{
		this(imageLoader, dataSource, strId, lModifiedTimestamp,
				lAddingTimestamp, iPictureGrade, new PictureInformation(
						strOriginalFileName, iWidth, iHeight,
						lCreationTimestamp, iOrientation, strMake, strModel,
						strExposureTime, strRelativeAperture, strFocalLength),
				tagList);
	}

	@Override
	public String getId()
	{
		return _strId;
	}

	@Override
	public Set<Tag> getTagSet()
	{
		return Collections.unmodifiableSet(_tagSet);
	}

	@Override
	public BufferedImage getImageData() throws IOException
	{
		return _imageLoader.getImageData(_strId);
	}

	@Override
	public BufferedImage getThumbnailData() throws IOException
	{
		return _imageLoader.getThumbnailData(_strId);
	}

	@Override
	public long getModifiedTimestamp()
	{
		synchronized (this)
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
	public IDataSource<FsPicture> getDataSource()
	{
		return _dataSource;
	}

	@Override
	public void addTag(final Tag newTag)
	{
		if (!_tagSet.contains(newTag))
		{
			synchronized (this)
			{
				_lModifiedTimestamp = System.currentTimeMillis();
				_bModified = true;
				_dataSource.setPictureForUpdating(this);
			}
			_tagSet.add(newTag);
		}
	}

	@Override
	public void removeTag(final Tag tag)
	{
		synchronized (this)
		{
			_lModifiedTimestamp = System.currentTimeMillis();
			_bModified = true;
			_dataSource.setPictureForUpdating(this);
		}
		_tagSet.remove(tag);
	}

	/**
	 * Check whether this FsPicture has been modified or not.
	 * 
	 * @return true if this FsPicture has been modified.
	 */
	public boolean hasBeenModified()
	{
		synchronized (this)
		{
			return _bModified;
		}
	}

	/**
	 * Change the modified status of this {@link FsPicture} to unmodified.
	 */
	public void unsetModified()
	{
		synchronized (this)
		{
			_bModified = false;
		}
	}

	@Override
	public int getPictureGrade()
	{
		synchronized (this)
		{
			return _iPictureGrade;
		}
	}

	@Override
	public void setPictureGrade(final int iPictureGrade)
	{
		synchronized (this)
		{
			if (_iPictureGrade != iPictureGrade)
			{
				_lModifiedTimestamp = System.currentTimeMillis();
				_bModified = true;
				_iPictureGrade = iPictureGrade;
				_dataSource.setPictureForUpdating(this);
			}
		}
	}

	/**
	 * Get the timestamp of the addition of this picture to the
	 * {@link IDataSource}.
	 * 
	 * @return the timestamp of the addition of this picture to the
	 *         {@link IDataSource}.
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
}
