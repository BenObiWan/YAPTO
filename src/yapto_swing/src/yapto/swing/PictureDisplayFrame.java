package yapto.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.Executors;

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

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.dummy.DummyDataSource;
import yapto.datasource.dummy.DummyPicture;

import com.google.common.eventbus.AsyncEventBus;

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
	 * Creates a new PictureDisplayFrame.
	 * 
	 * @param pictureBrowser
	 *            the {@link IPictureBrowser} used as source for the
	 *            {@link IPicture}.
	 */
	public PictureDisplayFrame(final IPictureBrowser<?> pictureBrowser)
	{
		super("yapto");
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
				// TODO do this better
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

		final MainPictureDisplayPanel contentPane = new MainPictureDisplayPanel(
				pictureBrowser);

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
			break;
		case ADD_DIRECTORY_ACTION_COMMAND:
			break;
		case QUIT_ACTION_COMMAND:
			stop();
			break;
		default:
			LOGGER.error("Action command " + ae.getActionCommand()
					+ " unknown.");
			break;
		}
	}

	/**
	 * Main function.
	 * 
	 * @param args
	 *            command line parameters.
	 */
	public static void main(final String[] args)
	{
		BasicConfigurator.configure();

		final IDataSource<DummyPicture> dataSource = new DummyDataSource(
				new AsyncEventBus(Executors.newFixedThreadPool(10)));

		final IPictureBrowser<?> pictureBrowser = dataSource
				.getPictureIterator();
		final PictureDisplayFrame main = new PictureDisplayFrame(pictureBrowser);
		main.pack();
		main.setVisible(true);
	}
}
