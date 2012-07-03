package yapto.datasource.sqlfile;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.tag.Tag;

import com.google.common.cache.LoadingCache;

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
	 * The original name of the file.
	 */
	private final String _strOriginalFileName;

	/**
	 * Set containing all the {@link Tag}s associated with this
	 * {@link FsPicture}.
	 */
	private final Set<Tag> _tagSet = new ConcurrentSkipListSet<>();

	/**
	 * The {@link IDataSource} from which this {@link IPicture} is coming.
	 */
	private final IDataSource<FsPicture> _dataSource;

	/**
	 * {@link LoadingCache} used to load the {@link BufferedImage}.
	 */
	private final LoadingCache<String, BufferedImage> _imageCache;

	/**
	 * Dimension of the picture.
	 */
	private final Dimension _pictureDimension;

	/**
	 * The timestamp of the last modification of this picture.
	 */
	private long _lModifiedTimestamp;

	/**
	 * The timestamp of the creation of this picture.
	 */
	private final long _lCreationTimestamp;

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
	 * Creates a new FsPicture.
	 * 
	 * @param imageCache
	 *            the {@link LoadingCache} used to load the
	 *            {@link BufferedImage}.
	 * @param dataSource
	 *            the {@link IDataSource} from which this {@link IPicture} is
	 *            coming.
	 * @param strId
	 *            the id of the picture.
	 * @param strOrigInalFileName
	 *            original file name.
	 * @param iWidth
	 *            the width of the picture.
	 * @param iHeight
	 *            the height of the picture.
	 * @param lModifiedTimestamp
	 *            the timestamp of the last modification of this picture.
	 * @param lCreationTimestamp
	 *            the timestamp of the creation of this picture.
	 * @param lAddingTimestamp
	 *            the timestamp of the addition of this picture to the
	 *            {@link IDataSource}.
	 */
	public FsPicture(final LoadingCache<String, BufferedImage> imageCache,
			final IDataSource<FsPicture> dataSource, final String strId,
			final String strOrigInalFileName, final int iWidth,
			final int iHeight, final long lModifiedTimestamp,
			final long lCreationTimestamp, final long lAddingTimestamp)
	{
		_strId = strId;
		_strOriginalFileName = strOrigInalFileName;
		_imageCache = imageCache;
		_dataSource = dataSource;
		_pictureDimension = new Dimension(iWidth, iHeight);
		_lCreationTimestamp = lCreationTimestamp;
		_lAddingTimestamp = lAddingTimestamp;
		synchronized (this)
		{
			_lModifiedTimestamp = lModifiedTimestamp;
		}
	}

	/**
	 * Creates a new FsPicture.
	 * 
	 * @param imageCache
	 *            the {@link LoadingCache} used to load the
	 *            {@link BufferedImage}.
	 * @param dataSource
	 *            the {@link IDataSource} from which this {@link IPicture} is
	 *            coming.
	 * @param strId
	 *            the id of the picture.
	 * @param strOriginalFileName
	 *            original file name.
	 * @param iWidth
	 *            the width of the picture.
	 * @param iHeight
	 *            the height of the picture.
	 * @param lModifiedTimestamp
	 *            the timestamp of the last modification of this picture.
	 * @param lCreationTimestamp
	 *            the timestamp of the creation of this picture.
	 * @param lAddingTimestamp
	 *            the timestamp of the addition of this picture to the
	 *            {@link IDataSource}.
	 * @param iPictureGrade
	 *            the new grade of this picture.
	 * @param tagList
	 *            list of {@link Tag}s.
	 */
	public FsPicture(final LoadingCache<String, BufferedImage> imageCache,
			final IDataSource<FsPicture> dataSource, final String strId,
			final String strOriginalFileName, final int iWidth,
			final int iHeight, final long lModifiedTimestamp,
			final long lCreationTimestamp, final long lAddingTimestamp,
			final int iPictureGrade, final List<Tag> tagList)
	{
		this(imageCache, dataSource, strId, strOriginalFileName, iWidth,
				iHeight, lModifiedTimestamp, lCreationTimestamp,
				lAddingTimestamp);
		_tagSet.addAll(tagList);
		synchronized (this)
		{
			_iPictureGrade = iPictureGrade;
		}
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
		try
		{
			return _imageCache.get(_strId);
		}
		catch (final ExecutionException e)
		{
			throw new IOException(e);
		}
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
	public Dimension getDimension()
	{
		return _pictureDimension;
	}

	@Override
	public int getHeight()
	{
		return (int) _pictureDimension.getHeight();
	}

	@Override
	public int getWidth()
	{
		return (int) _pictureDimension.getWidth();
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
	 * Change the modified status of this {@link FsPicture}.
	 * 
	 * @param bModified
	 *            the new modified status of this {@link FsPicture}.
	 */
	public void setModified(final boolean bModified)
	{
		synchronized (this)
		{
			_bModified = bModified;
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
			}
		}
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
}
