package yapto.datasource.dummy;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.Tag;

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
	private final File _imagePath;

	/**
	 * Lock protecting the access to the image.
	 */
	private final Object _lockImage = new Object();

	/**
	 * The {@link IDataSource} from which this {@link IPicture} is coming.
	 */
	private final IDataSource _dataSource;

	/**
	 * Creates a new DummyPicture.
	 * 
	 * @param dataSource
	 *            the {@link IDataSource} from which this {@link IPicture} is
	 *            coming.
	 * 
	 * @param imagePath
	 *            the path to the image file.
	 */
	public DummyPicture(final IDataSource dataSource, final File imagePath)
	{
		_imagePath = imagePath;
		_dataSource = dataSource;
	}

	@Override
	public String getId()
	{
		return _imagePath.toString();
	}

	@Override
	public List<Tag> getTagList()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedImage getImageData() throws IOException
	{
		synchronized (_lockImage)
		{
			if (_img == null)
			{
				_img = ImageIO.read(_imagePath);
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
}
