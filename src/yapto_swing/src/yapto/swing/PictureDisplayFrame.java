package yapto.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PictureDisplayFrame extends JFrame
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -4401831166047624407L;

	private final PictureDisplayComponent _pictureComponent = new PictureDisplayComponent();

	public PictureDisplayFrame()
	{
		super("yapto");
		final JPanel imagePane = new JPanel(new BorderLayout());
		setContentPane(imagePane);
		_pictureComponent.setPreferredSize(new Dimension(400, 300));
		imagePane.add(_pictureComponent, BorderLayout.CENTER);
		try
		{
			_pictureComponent.loadPicture();
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void drawPicture() throws IOException
	{
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException
	{
		final PictureDisplayFrame main = new PictureDisplayFrame();
		main.pack();
		main.setVisible(true);
		main.drawPicture();
	}
}
