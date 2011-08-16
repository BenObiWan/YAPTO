package yapto.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;

import yapto.datasource.IPictureBrowser;

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
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link PictureDisplayComponent}.
	 */
	private final IPictureBrowser _pictureBrowser;

	/**
	 * Creates a new {@link PictureDisplayComponent}.
	 * 
	 * @param pictureBrowser
	 *            the {@link IPictureBrowser} to use.
	 */
	public PictureDisplayComponent(final IPictureBrowser pictureBrowser)
	{
		_pictureBrowser = pictureBrowser;
	}

	/**
	 * Load the picture to display.
	 * 
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	public void loadPicture() throws IOException
	{
		_img = _pictureBrowser.getCurrentPicture().getImageData();
		setPreferredSize(new Dimension(_img.getWidth(), _img.getHeight()));
	}

	@Override
	public void paint(final Graphics g)
	{
		g.drawImage(_img, 0, 0, null);
	}
}
