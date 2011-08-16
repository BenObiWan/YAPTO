package yapto.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

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
	 * Creates a new {@link PictureDisplayComponent}.
	 */
	public PictureDisplayComponent()
	{
	}

	/**
	 * Load the picture to display.
	 * 
	 * @throws IOException
	 *             if an error occurs during reading.
	 */
	public void loadPicture() throws IOException
	{
		_img = ImageIO.read(new File("/tmp/picture.jpg"));
		setPreferredSize(new Dimension(_img.getWidth(), _img.getHeight()));
	}

	@Override
	public void paint(final Graphics g)
	{
		g.drawImage(_img, 0, 0, null);
	}
}
