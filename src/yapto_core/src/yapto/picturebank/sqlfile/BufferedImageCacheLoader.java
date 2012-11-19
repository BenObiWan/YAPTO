package yapto.picturebank.sqlfile;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.google.common.cache.CacheLoader;

/**
 * A {@link CacheLoader} loading {@link BufferedImage} from the file system.
 * 
 * @author benobiwan
 * 
 */
public final class BufferedImageCacheLoader extends
		CacheLoader<String, BufferedImage>
{
	/**
	 * The configuration of this BufferedImageCacheLoader.
	 */
	private final IBufferedImageCacheLoaderConfiguration _cacheLoaderConf;

	/**
	 * Creates a new BufferedImageCacheLoader.
	 * 
	 * @param cacheLoaderConf
	 *            the configuration of this BufferedImageCacheLoader.
	 */
	public BufferedImageCacheLoader(
			final IBufferedImageCacheLoaderConfiguration cacheLoaderConf)
	{
		_cacheLoaderConf = cacheLoaderConf;
	}

	@Override
	public BufferedImage load(final String key) throws Exception
	{
		final char[] subdir = new char[3];
		key.getChars(0, 2, subdir, 0);
		subdir[2] = File.separatorChar;
		final File imagePath = new File(_cacheLoaderConf.getPictureDirectory(),
				new String(subdir) + key);
		return ImageIO.read(imagePath);
	}
}
