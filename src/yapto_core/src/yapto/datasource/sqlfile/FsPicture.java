package yapto.datasource.sqlfile;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
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
	 * Path to the file holding the image.
	 */
	private final File _imagePath;

	/**
	 * Set containing all the {@link Tag}s associated with this
	 * {@link FsPicture}.
	 */
	private final Set<Tag> _tagSet = new ConcurrentSkipListSet<Tag>();

	/**
	 * The {@link IDataSource} from which this {@link IPicture} is coming.
	 */
	private final IDataSource _dataSource;

	/**
	 * {@link LoadingCache} used to load the {@link BufferedImage}.
	 */
	private final LoadingCache<File, BufferedImage> _imageCache;

	/**
	 * Creates a new FsPicture.
	 * 
	 * @param imageCache
	 *            the {@link LoadingCache} used to load the
	 *            {@link BufferedImage}.
	 * @param dataSource
	 *            the {@link IDataSource} from which this {@link IPicture} is
	 *            coming.
	 * @param imagePath
	 *            the path to the image file.
	 */
	public FsPicture(final LoadingCache<File, BufferedImage> imageCache,
			final IDataSource dataSource, final File imagePath)
	{
		_imageCache = imageCache;
		_imagePath = imagePath;
		_dataSource = dataSource;
	}

	@Override
	public String getId()
	{
		return _imagePath.toString();
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
			return _imageCache.get(_imagePath);
		}
		catch (final ExecutionException e)
		{
			throw new IOException(e);
		}
	}

	@Override
	public long getTimestamp()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int compareTo(final IPicture otherPicture)
	{
		return getId().compareTo(otherPicture.getId());
	}

	@Override
	public IDataSource getDataSource()
	{
		return _dataSource;
	}

	@Override
	public Dimension getDimension()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHeight()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addTag(final Tag newTag)
	{
		_tagSet.add(newTag);
	}

	@Override
	public void removeTag(final Tag tag)
	{
		_tagSet.remove(tag);
	}
}
