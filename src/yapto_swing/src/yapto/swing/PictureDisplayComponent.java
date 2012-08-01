package yapto.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.PictureChangedEvent;

import com.google.common.eventbus.Subscribe;

/**
 * Component used to display a picture.
 * 
 * @author benobiwan
 * 
 */
public final class PictureDisplayComponent extends JComponent
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 7055585500143733186L;

	/**
	 * The picture to display on this component.
	 */
	private BufferedImage _img;

	/**
	 * The configured type of zoom used to display the picture.
	 */
	private PictureZoomType _zoomType = PictureZoomType.WINDOW_DIMENSION;
	// private PictureZoomType _zoomType = PictureZoomType.REAL_SIZE;

	/**
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link PictureDisplayComponent}.
	 */
	private final IPictureBrowser<? extends IPicture> _pictureIterator;

	/**
	 * Memory of the last scale factor used to scale the image.
	 */
	private Dimension _size;

	/**
	 * Memory of the last scale factor used to scale the image.
	 */
	private double _dScaleFactor;

	/**
	 * The AffineTransform used to scale the image.
	 */
	private AffineTransform _transform;

	/**
	 * Creates a new {@link PictureDisplayComponent}.
	 * 
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public PictureDisplayComponent(
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		_pictureIterator = pictureIterator;
	}

	/**
	 * Load the picture to display.
	 * 
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	public void loadPicture() throws IOException
	{
		final IPicture pic = _pictureIterator.getCurrentPicture();
		if (pic != null)
		{
			_img = pic.getImageData();
			switch (_zoomType)
			{
			case REAL_SIZE:
				setPreferredSize(new Dimension(_img.getWidth(),
						_img.getHeight()));
				break;
			case WINDOW_DIMENSION:
				break;
			case PICTURE_PERCENTAGE:
				break;
			case SPECIFIC_SIZE:
				break;
			case WINDOW_PERCENTAGE:
				break;
			default:
				break;
			}
			repaint();
		}
	}

	@Override
	public void paint(final Graphics g)
	{
		switch (_zoomType)
		{
		case REAL_SIZE:
			g.drawImage(_img, 0, 0, null);
			break;
		case WINDOW_DIMENSION:
			final Graphics2D g2 = (Graphics2D) g;
			changeTransform(getParent().getSize());
			g2.drawImage(_img, _transform, null);
			break;
		case PICTURE_PERCENTAGE:
			break;
		case SPECIFIC_SIZE:
			break;
		case WINDOW_PERCENTAGE:
			break;
		default:
			break;
		}
	}

	/**
	 * Change the AffineTransform to fit the specified size.
	 * 
	 * @param size
	 *            the size to match.
	 */
	private void changeTransform(final Dimension size)
	{
		if (_size == null || _transform == null || !_size.equals(size))
		{
			_size = size;
			final double dScaleFactor = Math.min(
					size.getWidth() / _img.getWidth(),
					size.getHeight() / _img.getHeight());
			changeTransform(dScaleFactor);
		}
	}

	/**
	 * Change the AffineTransform to fit the specified scale factor.
	 * 
	 * @param dScaleFactor
	 *            the scale factor to match.
	 */
	private void changeTransform(final double dScaleFactor)
	{
		if (dScaleFactor != _dScaleFactor || _transform == null
				&& dScaleFactor > 0)
		{
			_dScaleFactor = dScaleFactor;
			_transform = AffineTransform.getScaleInstance(_dScaleFactor,
					_dScaleFactor);
		}
	}

	/**
	 * Method called when the selected picture has changed.
	 * 
	 * @param e
	 *            the event signaling the change of the picture.
	 */
	@Subscribe
	public void handlePictureChanged(
			@SuppressWarnings("unused") final PictureChangedEvent e)
	{
		try
		{
			loadPicture();
		}
		catch (final IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
