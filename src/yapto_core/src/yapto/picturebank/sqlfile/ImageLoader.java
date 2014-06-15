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
	 * {@link LoadingCache} used to load the secondary {@link BufferedImage}.
	 */
	private final LoadingCache<String, BufferedImage> _secondaryImageCache;

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
		// main image cache
		final IBufferedImageCacheLoaderConfiguration mainImageCacheConf = conf
				.getMainPictureLoaderConfiguration();
		final CacheLoader<String, BufferedImage> imageLoader = new BufferedImageCacheLoader(
				mainImageCacheConf);
		_mainImageCache = CacheBuilder.newBuilder()
				.maximumSize(mainImageCacheConf.getCacheSize())
				.build(imageLoader);

		// secondary image cache
		final IBufferedImageCacheLoaderConfiguration secondaryImageCacheConf = conf
				.getSecondaryPictureLoaderConfiguration();
		final CacheLoader<String, BufferedImage> secondaryImageLoader = new BufferedImageCacheLoader(
				secondaryImageCacheConf);
		_secondaryImageCache = CacheBuilder.newBuilder()
				.maximumSize(secondaryImageCacheConf.getCacheSize())
				.build(secondaryImageLoader);

		// thumbnail cache
		final IBufferedImageCacheLoaderConfiguration thumbnailImageCacheConf = conf
				.getThumbnailPictureLoaderConfiguration();
		final CacheLoader<String, BufferedImage> thumbnailLoader = new BufferedImageCacheLoader(
				thumbnailImageCacheConf);
		_thumbnailCache = CacheBuilder.newBuilder()
				.maximumSize(thumbnailImageCacheConf.getCacheSize())
				.build(thumbnailLoader);
	}

	/**
	 * Get the data of the main image of the specified {@link IPicture}.
	 * 
	 * @param strFileName
	 *            the file name of the image to get.
	 * @return the data of the main image of the specified {@link IPicture}.
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	public BufferedImage getMainImageData(final String strFileName)
			throws IOException
	{
		try
		{
			return _mainImageCache.get(strFileName);
		}
		catch (final ExecutionException e)
		{
			throw new IOException(e);
		}
	}

	/**
	 * Get the data of a secondary image of the specified {@link IPicture}.
	 * 
	 * @param strFileName
	 *            the file name of the image to get.
	 * @return the data of the secondary image of the specified {@link IPicture}
	 *         .
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	public BufferedImage getSecondaryImageData(final String strFileName)
			throws IOException
	{
		try
		{
			return _secondaryImageCache.get(strFileName);
		}
		catch (final ExecutionException e)
		{
			throw new IOException(e);
		}
	}

	/**
	 * Get the data of the thumbnail image of the specified {@link IPicture}.
	 * 
	 * @param strFileName
	 *            the file name of the image to get.
	 * @return the data of the thumbnail image of the specified {@link IPicture}
	 *         .
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	public BufferedImage getThumbnailData(final String strFileName)
			throws IOException
	{
		try
		{
			return _thumbnailCache.get(strFileName);
		}
		catch (final ExecutionException e)
		{
			throw new IOException(e);
		}
	}
}
