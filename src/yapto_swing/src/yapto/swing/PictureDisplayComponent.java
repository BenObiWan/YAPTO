package yapto.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PictureDisplayComponent extends Component
{
	private BufferedImage _img;

	public PictureDisplayComponent()
	{
	}

	public void loadPicture() throws IOException
	{
		_img = ImageIO.read(new File("/tmp/picture.jpg"));
	}

	public void paint(Graphics g)
	{
		g.drawImage(_img, 0, 0, null);
	}
}
