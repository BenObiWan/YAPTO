package yapto.datasource.dummy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import yapto.datasource.IPicture;
import yapto.datasource.ITag;

/**
 * Dummy implementation of the {@link IPicture} interface.
 * 
 * @author benobiwan
 * 
 */
public final class DummyPicture implements IPicture
{
	/**
	 * Image displayed by this {@link DummyPicture}.
	 */
	private BufferedImage _img;

	/**
	 * Lock protecting the access to the image.
	 */
	private final Object _lockImage = new Object();

	/**
	 * Creates a new DummyPicture.
	 */
	public DummyPicture()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ITag> getTagList()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getImageData() throws IOException
	{
		synchronized (_lockImage)
		{
			if (_img == null)
			{
				_img = ImageIO.read(new File("/tmp/picture.jpg"));
			}
			return _img;
		}
	}

	@Override
	public long getTimestamp()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
