package yapto.picturebank.sqlfile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import yapto.picturebank.IPicture;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Class used to load {@link BufferedImage} from the file system.
 * 
 * @author benobiwan
 * 
 */
public final class ImageLoader
{
	/**
	 * {@link LoadingCache} used to load the {@link BufferedImage}.
	 */
	private final LoadingCache<String, BufferedImage> _mainImageCache;

	/**
	 * {@link LoadingCache} used to load the {@link BufferedImage}.
	 */
	private final LoadingCache<String, BufferedImage> _thumbnailCache;

	/**
	 * Creates a new ImageLoader.
	 * 
	 * @param conf
	 *            the configuration of this {@link ImageLoader}.
	 */
	public ImageLoader(final ISQLFilePictureBankConfiguration conf)
	{
		// image cache
		final CacheLoader<String, BufferedImage> imageLoader = new BufferedImageCacheLoader(
				conf.getMainPictureLoaderConfiguration());
		_mainImageCache = CacheBuilder.newBuilder().build(imageLoader);

		// thumbnail cache
		final CacheLoader<String, BufferedImage> thumbnailLoader = new BufferedImageCacheLoader(
				conf.getThumbnailPictureLoaderConfiguration());
		_thumbnailCache = CacheBuilder.newBuilder().build(thumbnailLoader);
	}

	/**
	 * Get the data of the main image of the specified {@link IPicture}.
	 * 
	 * @param strId
	 *            the id of the image to get.
	 * @return the data of the main image of the specified {@link IPicture}.
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	public BufferedImage getImageData(final String strId) throws IOException
	{
		try
		{
			return _mainImageCache.get(strId);
		}
		catch (final ExecutionException e)
		{
			throw new IOException(e);
		}
	}

	/**
	 * Get the data of the thumbnail image of the specified {@link IPicture}.
	 * 
	 * @param strId
	 *            the id of the image to get.
	 * @return the data of the thumbnail image of the specified {@link IPicture}
	 *         .
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	public BufferedImage getThumbnailData(final String strId)
			throws IOException
	{
		try
		{
			return _thumbnailCache.get(strId);
		}
		catch (final ExecutionException e)
		{
			throw new IOException(e);
		}
	}
}
