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
	 * Path to the file holding the image.
	 */
	private final String _strPath;

	/**
	 * Lock protecting the access to the image.
	 */
	private final Object _lockImage = new Object();

	/**
	 * Creates a new DummyPicture.
	 * 
	 * @param strPath
	 *            the path to the image file.
	 */
	public DummyPicture(final String strPath)
	{
		_strPath = strPath;
	}

	@Override
	public String getId()
	{
		return _strPath;
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
				_img = ImageIO.read(new File(_strPath));
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

	@Override
	public int compareTo(final IPicture otherPicture)
	{
		return getId().compareTo(otherPicture.getId());
	}

}
