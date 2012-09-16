package yapto.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;

import javax.swing.JPanel;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;

/**
 * Panel displaying an {@link IPicture} and its information.
 * 
 * @author benobiwan
 * 
 */
public final class MainPictureDisplayPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5806024455179560922L;

	/**
	 * {@link PictureDisplayComponent} used to display the current
	 * {@link IPicture}.
	 */
	private final PictureDisplayComponent _pictureComponent;

	/**
	 * {@link PictureInformationPanel} used to display informations about the
	 * current {@link IPicture}.
	 */
	private final PictureInformationPanel _pictureInfoPanel;

	/**
	 * The {@link PictureBrowserPanel} used to control the
	 * {@link IPictureBrowser}.
	 */
	private final PictureBrowserPanel _pictureBrowserPanel;

	/**
	 * The {@link IPictureBrowser} used to display picture on this
	 * {@link MainPictureDisplayPanel}.
	 */
	private final IPictureBrowser<?> _pictureBrowser;

	/**
	 * Create a new MainPictureDisplayPanel.
	 * 
	 * @param parent
	 *            parent {@link Frame}.
	 * @param pictureBrowser
	 *            the {@link IPictureBrowser} to use.
	 */
	public MainPictureDisplayPanel(final Frame parent,
			final IPictureBrowser<?> pictureBrowser)
	{
		super(new BorderLayout());
		_pictureBrowser = pictureBrowser;
		_pictureComponent = new PictureDisplayComponent(_pictureBrowser);
		_pictureBrowser.register(_pictureComponent);
		_pictureInfoPanel = new PictureInformationPanel(parent, _pictureBrowser);
		_pictureBrowser.register(_pictureInfoPanel);
		_pictureBrowserPanel = new PictureBrowserPanel(_pictureBrowser);
		_pictureBrowser.register(_pictureBrowserPanel);

		_pictureComponent.setPreferredSize(new Dimension(400, 300));

		add(_pictureComponent, BorderLayout.CENTER);
		add(_pictureInfoPanel, BorderLayout.LINE_END);
		add(_pictureBrowserPanel, BorderLayout.PAGE_END);
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
