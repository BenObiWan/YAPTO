package yapto.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.IPictureBrowser;
import yapto.picturebank.PictureBankList;

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
	 * Logger object.
	 */
	protected static transient final Logger LOGGER = LoggerFactory
			.getLogger(MainPictureDisplayPanel.class);

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
	 * The {@link PictureBankList} used to load the {@link IPictureBank} used as
	 * source for the {@link IPicture}.
	 */
	protected final PictureBankList _bankList;

	/**
	 * Create a new MainPictureDisplayPanel.
	 * 
	 * @param parent
	 *            parent {@link Frame}.
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 * @throws ExecutionException
	 *             TODO
	 */
	public MainPictureDisplayPanel(final Frame parent,
			final PictureBankList bankList) throws ExecutionException
	{
		super(new BorderLayout());
		_bankList = bankList;
		_pictureBrowser = _bankList.getRandomPictureList(40);
		_pictureComponent = new PictureDisplayComponent(_bankList);
		_pictureInfoPanel = new PictureInformationPanel(parent, _pictureBrowser);
		_pictureBrowser.register(_pictureInfoPanel);
		_pictureBrowserPanel = new PictureBrowserPanel(_bankList);

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
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			LOGGER.error(e.getMessage(), e);
		}
	}
}
