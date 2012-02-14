package yapto.datasource.sqlfile;

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
		CacheLoader<File, BufferedImage>
{
	@Override
	public BufferedImage load(final File key) throws Exception
	{
		return ImageIO.read(key);
	}
}
