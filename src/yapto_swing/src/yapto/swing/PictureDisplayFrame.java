package yapto.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureAddException;
import yapto.picturebank.PictureAddExceptionType;
import yapto.picturebank.PictureBankList;
import yapto.picturebank.tag.ITag;
import yapto.swing.query.PictureQueryPanel;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import common.config.InvalidConfigurationException;

/**
 * {@link JFrame} used to display an picture and it's information. Main frame of
 * the application and starting class.
 * 
 * @author benobiwan
 * 
 */
public final class PictureDisplayFrame extends JFrame implements ActionListener
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -4401831166047624407L;

	/**
	 * Logger object.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PictureDisplayFrame.class);

	/**
	 * Action command for the add picture command.
	 */
	private static final String ADD_PICTURE_ACTION_COMMAND = "addPicture";

	/**
	 * Action command for the add directory command.
	 */
	private static final String ADD_DIRECTORY_ACTION_COMMAND = "addDirectory";

	/**
	 * Action command for the quit command.
	 */
	private static final String QUIT_ACTION_COMMAND = "quit";

	/**
	 * Action command for the new filter command.
	 */
	private static final String NEW_FILTER_ACTION_COMMAND = "newFilter";

	/**
	 * Action command for the all pictures command.
	 */
	private static final String ALL_PICTURES_ACTION_COMMAND = "allPictures";

	/**
	 * Action command for the random pictures command.
	 */
	private static final String RANDOM_PICTURES_ACTION_COMMAND = "randomPictures";

	/**
	 * {@link JFileChooser} used to select an unique file to add.
	 */
	private final JFileChooser _individualPictureChooser = new JFileChooser();

	/**
	 * {@link JFileChooser} used to select a directory to add.
	 */
	private final JFileChooser _directoryChooser = new JFileChooser();

	/**
	 * Panel for choosing initial tags.
	 */
	private final InitialTagPanel _initialTagPanel;

	/**
	 * Dialog for choosing initial tags.
	 */
	private final JDialog _dialogInitTag;

	/**
	 * The {@link PictureBankList} used to load the {@link IPictureBank} used as
	 * source for the {@link IPicture}.
	 */
	protected final PictureBankList _bankList;

	/**
	 * {@link JDialog} used to filter the picture displayed.
	 */
	private final JDialog _queryDialog;

	/**
	 * Creates a new PictureDisplayFrame.
	 * 
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public PictureDisplayFrame(final PictureBankList bankList)
	{
		super("yapto");
		_bankList = bankList;

		_directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		_dialogInitTag = new JDialog(this, "Initial tags", true);
		_initialTagPanel = new InitialTagPanel(_dialogInitTag, _bankList);
		_dialogInitTag.setContentPane(_initialTagPanel);

		_queryDialog = new JDialog(this, "Filter pictures", true);
		_queryDialog.setContentPane(new PictureQueryPanel(_queryDialog,
				_bankList));

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener()
		{

			@Override
			public void windowClosing(final WindowEvent e)
			{
				stop();
			}

			@Override
			public void windowClosed(final WindowEvent e)
			{
				_bankList.unselectAll();
				System.exit(0);
			}

			@Override
			public void windowOpened(final WindowEvent e)
			{
				// nothing to do
			}

			@Override
			public void windowIconified(final WindowEvent e)
			{
				// nothing to do
			}

			@Override
			public void windowDeiconified(final WindowEvent e)
			{
				// nothing to do
			}

			@Override
			public void windowActivated(final WindowEvent e)
			{
				// nothing to do
			}

			@Override
			public void windowDeactivated(final WindowEvent e)
			{
				// nothing to do
			}
		});

		MainPictureDisplayPanel contentPane = null;
		contentPane = new MainPictureDisplayPanel(this, _bankList);
		setJMenuBar(createMenuBar());
		setContentPane(contentPane);
	}

	/**
	 * Create the menu bar.
	 * 
	 * @return the menu bar for this application.
	 */
	private JMenuBar createMenuBar()
	{
		final JMenuBar menuBar = new JMenuBar();

		final JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		JMenuItem menuItem = new JMenuItem("add picture");
		menuItem.setActionCommand(ADD_PICTURE_ACTION_COMMAND);
		menuItem.addActionListener(this);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("add directory");
		menuItem.setActionCommand(ADD_DIRECTORY_ACTION_COMMAND);
		menuItem.addActionListener(this);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Quit");
		menuItem.setMnemonic(KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.ALT_MASK));
		menuItem.setActionCommand(QUIT_ACTION_COMMAND);
		menuItem.addActionListener(this);
		fileMenu.add(menuItem);

		final JMenu filterMenu = new JMenu("Filter");
		filterMenu.setMnemonic(KeyEvent.VK_I);
		menuBar.add(filterMenu);

		menuItem = new JMenuItem("all pictures");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ALL_PICTURES_ACTION_COMMAND);
		menuItem.addActionListener(this);
		filterMenu.add(menuItem);

		menuItem = new JMenuItem("random pictures");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(RANDOM_PICTURES_ACTION_COMMAND);
		menuItem.addActionListener(this);
		filterMenu.add(menuItem);

		menuItem = new JMenuItem("new filter");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(NEW_FILTER_ACTION_COMMAND);
		menuItem.addActionListener(this);
		filterMenu.add(menuItem);

		return menuBar;
	}

	/**
	 * Stop the application.
	 */
	public void stop()
	{
		final int iAnswer = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to exit?", "Exit",
				JOptionPane.YES_NO_OPTION);
		switch (iAnswer)
		{
		case JOptionPane.YES_OPTION:
			dispose();
			break;
		case JOptionPane.NO_OPTION:
		case JOptionPane.CLOSED_OPTION:
		default:
			break;
		}
	}

	@Override
	public void actionPerformed(final ActionEvent ae)
	{
		switch (ae.getActionCommand())
		{
		case ADD_PICTURE_ACTION_COMMAND:
			if (_individualPictureChooser
					.showOpenDialog(PictureDisplayFrame.this) == JFileChooser.APPROVE_OPTION)
			{
				final File file = _individualPictureChooser.getSelectedFile();
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("Opening: " + file.getName() + ".");
				}
				// Select the list of tags to add to the picture.
				_initialTagPanel.clearSelection();
				_dialogInitTag.pack();
				_dialogInitTag.setVisible(true);
				final List<ITag> tagList = _initialTagPanel.getCheckedTags();
				if (tagList != null)
				{
					try
					{
						// TODO choose to which IPictureBank the picture is
						// added when more than one is opened.
						final SortedSet<IPictureBank<?>> selectedBankSet = _bankList
								.getSelectedPictureBank();
						if (selectedBankSet != null
								&& !selectedBankSet.isEmpty())
						{
							selectedBankSet.first().addPicture(file.toPath(),
									tagList);
						}
						else
						{
							throw new PictureAddException(
									PictureAddExceptionType.NO_OPEN_PICTUREBANK);
						}
					}
					catch (final PictureAddException e)
					{
						switch (e.getExceptionType())
						{
						case FILE_ALREADY_EXISTS:
							final String strId = e.getPictureId();
							if (strId != null)
							{
								// TODO add a dialog to compare the two
								// pictures.
								logException(e);
							}
							else
							{
								logException(e);
							}
							break;
						default:
							logException(e);
						}
					}
				}
			}
			break;
		case ADD_DIRECTORY_ACTION_COMMAND:
			if (_directoryChooser.showOpenDialog(PictureDisplayFrame.this) == JFileChooser.APPROVE_OPTION)
			{
				final File file = _directoryChooser.getSelectedFile();
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("Opening directory: " + file.getName() + ".");
				}
				// Select the list of tags to add to the picture.
				_initialTagPanel.clearSelection();
				_dialogInitTag.pack();
				_dialogInitTag.setVisible(true);
				final List<ITag> tagList = _initialTagPanel.getCheckedTags();
				if (tagList != null)
				{
					try
					{
						// TODO choose to which IPictureBank the pictures are
						// added when more than one is opened.
						final SortedSet<IPictureBank<?>> selectedBankSet = _bankList
								.getSelectedPictureBank();
						if (selectedBankSet != null
								&& !selectedBankSet.isEmpty())
						{
							selectedBankSet.first().addDirectory(file.toPath(),
									tagList);
							// TODO handle return object
						}
						else
						{
							throw new PictureAddException(
									PictureAddExceptionType.NO_OPEN_PICTUREBANK);
						}
					}
					catch (final PictureAddException e)
					{
						logException(e);
					}
				}
			}
			break;
		case NEW_FILTER_ACTION_COMMAND:
			_queryDialog.pack();
			_queryDialog.setVisible(true);
			break;
		case ALL_PICTURES_ACTION_COMMAND:
			if (JOptionPane
					.showConfirmDialog(
							this,
							"Are you sure you want to clear existing filter and select all pictures?",
							"all pictures", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
			{
				try
				{
					_bankList.getAllPictures();
				}
				catch (final ExecutionException e)
				{
					logException(e);
				}
			}
			break;
		case RANDOM_PICTURES_ACTION_COMMAND:
			final String input = JOptionPane.showInputDialog(this,
					"Enter number of pictures to select:", "random pictures",
					JOptionPane.QUESTION_MESSAGE);
			if (input != null)
			{
				try
				{
					_bankList.getRandomPictureList(Integer.parseInt(input));
				}
				catch (final NumberFormatException e)
				{
					logException("You must enter an integer.", e);
				}
				catch (final ExecutionException | IllegalArgumentException e)
				{
					logException(e);
				}
			}
			break;
		case QUIT_ACTION_COMMAND:
			stop();
			break;
		default:
			logError("Action command " + ae.getActionCommand() + " unknown.");
			break;
		}
	}

	/**
	 * Method used to log an error and display it on a dialog.
	 * 
	 * @param strError
	 *            the error to display.
	 */
	private void logError(final String strError)
	{
		JOptionPane.showMessageDialog(this, strError, "Error",
				JOptionPane.ERROR_MESSAGE);
		LOGGER.error(strError);
	}

	/**
	 * Method used to log an exception and display it's message on a dialog.
	 * 
	 * @param e
	 *            the exception to log.
	 */
	private void logException(final Exception e)
	{
		JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
				JOptionPane.ERROR_MESSAGE);
		LOGGER.error(e.getMessage(), e);
	}

	/**
	 * Method used to log an exception and display a custom message on a dialog.
	 * 
	 * @param strError
	 *            error message to display.
	 * @param e
	 *            the exception to log.
	 */
	private void logException(final String strError, final Exception e)
	{
		JOptionPane.showMessageDialog(this, strError, "Error",
				JOptionPane.ERROR_MESSAGE);
		LOGGER.error(strError, e);
	}

	/**
	 * Main function.
	 * 
	 * @param args
	 *            command line parameters.
	 * @throws InvalidConfigurationException
	 *             the configuration of the {@link PictureBankList} is invalid.
	 * @throws ExecutionException
	 *             if an Exception was thrown during the loading of the first
	 *             picture.
	 */
	public static void main(final String[] args)
			throws InvalidConfigurationException, ExecutionException
	{
		BasicConfigurator.configure();

		final EventBus bus = new AsyncEventBus(Executors.newFixedThreadPool(10));
		final PictureBankList bankList = new PictureBankList(bus);

		// TODO to remove
		bankList.selectPictureBankById(1);
		bankList.getRandomPictureList(40);

		final PictureDisplayFrame main = new PictureDisplayFrame(bankList);
		main.pack();
		main.setVisible(true);
	}
}
