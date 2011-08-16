package yapto.swing;

import java.io.IOException;

import javax.swing.JFrame;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.dummy.DummyDataSource;
import yapto.datasource.dummy.DummyPictureBrowser;

public final class PictureDisplayFrame extends JFrame
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -4401831166047624407L;

	/**
	 * Creates a new PictureDisplayFrame.
	 * 
	 * @param dataSource
	 *            the {@link IDataSource} used as source for the
	 *            {@link IPicture}.
	 */
	public PictureDisplayFrame(final IDataSource dataSource)
	{
		super("yapto");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		final MainPictureDisplayPanel contentPane = new MainPictureDisplayPanel(
				new DummyPictureBrowser(dataSource));
		setContentPane(contentPane);
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException
	{
		final IDataSource dataSource = new DummyDataSource();
		final PictureDisplayFrame main = new PictureDisplayFrame(dataSource);
		main.pack();
		main.setVisible(true);
	}
}
