package yapto.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import yapto.datasource.IPictureBrowser;

public final class MainPictureDisplayPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5806024455179560922L;

	private final PictureDisplayComponent _pictureComponent = new PictureDisplayComponent();

	private final PictureInformationPanel _pictureInfoPanel = new PictureInformationPanel();

	/**
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link MainPictureDisplayPanel}.
	 */
	private final IPictureBrowser _pictureBrowser;

	/**
	 * Create a new MainPictureDisplayPanel.
	 * 
	 * @param pictureBrowser
	 *            the {@link IPictureBrowser} to use.
	 */
	public MainPictureDisplayPanel(final IPictureBrowser pictureBrowser)
	{
		super(new BorderLayout());
		_pictureBrowser = pictureBrowser;
		final JScrollPane spPicture = new JScrollPane(_pictureComponent);
		spPicture.setPreferredSize(new Dimension(400, 300));

		add(spPicture, BorderLayout.CENTER);
		add(_pictureInfoPanel, BorderLayout.PAGE_END);
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
}
