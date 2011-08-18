package yapto.swing;

import javax.swing.JFrame;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.dummy.DummyDataSource;
import yapto.datasource.dummy.DummyPictureBrowser;

/**
 * {@link JFrame} used to display an picture and it's information. Main frame of
 * the application and starting class.
 * 
 * @author benobiwan
 * 
 */
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
	 * Main function.
	 * 
	 * @param args
	 *            command line parameters.
	 */
	public static void main(final String[] args)
	{
		final IDataSource dataSource = new DummyDataSource();
		final PictureDisplayFrame main = new PictureDisplayFrame(dataSource);
		main.pack();
		main.setVisible(true);
	}
}
