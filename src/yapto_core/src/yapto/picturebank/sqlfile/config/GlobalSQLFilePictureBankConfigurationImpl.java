package yapto.picturebank.sqlfile.config;

import javax.management.MBeanServer;

import yapto.picturebank.process.IdentifyTask;
import yapto.picturebank.sqlfile.SQLFilePictureBank;

import common.config.AbstractConfigurationBranch;
import common.config.IConfiguration;
import common.config.InvalidConfigurationException;
import common.config.display.IntegerDisplayType;
import common.config.leaf.ConfigurationInteger;

/**
 * Configuration common to every {@link SQLFilePictureBank}.
 * 
 * @author benobiwan
 * 
 */
public final class GlobalSQLFilePictureBankConfigurationImpl extends
		AbstractConfigurationBranch implements
		IGlobalSQLFilePictureBankConfiguration
{
	/**
	 * Leaf configuring the maximum number of {@link IdentifyTask} to run at the
	 * same time.
	 */
	private final ConfigurationInteger _leafMaxIdentifyTask;

	/**
	 * Leaf configuring the maximum number of task other than
	 * {@link IdentifyTask} to run at the same time.
	 */
	private final ConfigurationInteger _leafMaxOtherTask;

	/**
	 * Leaf configuring the minimum number of seconds to wait between picture
	 * modification and saving to the database.
	 */
	private final ConfigurationInteger _leafWaitBeforeWrite;

	/**
	 * Leaf configuring the minimum number of images to preload.
	 */
	private final ConfigurationInteger _leafNumberOfImagesToPreload;

	/**
	 * Short description for the maximum number of identify task.
	 */
	private final static String MAX_IDENTIFY_TASK_SHORT_DESC = "Maximum number of identify task.";

	/**
	 * Long description for the maximum number of identify task.
	 */
	private final static String MAX_IDENTIFY_TASK_LONG_DESC = "Maximum number of concurrent identify task.";

	/**
	 * Invalid message for the maximum number of identify task.
	 */
	private final static String MAX_IDENTIFY_TASK_INVALID_MESSAGE = "Invalid maximum number of identify task.";

	/**
	 * Short description for the maximum number of other task.
	 */
	private final static String MAX_OTHER_TASK_SHORT_DESC = "Maximum number of other task.";

	/**
	 * Long description for the maximum number of other task.
	 */
	private final static String MAX_OTHER_TASK_LONG_DESC = "Maximum number of concurrent task other than  identify task.";

	/**
	 * Invalid message for the maximum number of identify task.
	 */
	private final static String MAX_OTHER_TASK_INVALID_MESSAGE = "Invalid maximum number of other task.";

	/**
	 * Short description for the time waited before writing.
	 */
	private final static String WAIT_BEFORE_WRITE_SHORT_DESC = "Time waited before writing.";

	/**
	 * Long description for the time waited before writing.
	 */
	private final static String WAIT_BEFORE_WRITE_LONG_DESC = "Time waited before writing picture metadata when it's modified.";

	/**
	 * Invalid message for the time waited before writing.
	 */
	private final static String WAIT_BEFORE_WRITE_INVALID_MESSAGE = "Invalid waited before writing.";

	/**
	 * Short description for the number of images to preload.
	 */
	private final static String PRELOAD_IMAGES_COUNT_SHORT_DESC = "Number of images to preload.";

	/**
	 * Long description for the number of images to preload.
	 */
	private final static String PRELOAD_IMAGES_COUNT_LONG_DESC = "Number of images to preload.";

	/**
	 * Invalid message for the number of images to preload.
	 */
	private final static String PRELOAD_IMAGES_COUNT_INVALID_MESSAGE = "Invalid number of images to preload.";

	/**
	 * Creates a new GlobalSQLFilePictureBankConfigurationImpl using default
	 * values.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 */
	public GlobalSQLFilePictureBankConfigurationImpl(
			final IConfiguration parent, final MBeanServer mBeanServer)
	{
		super(parent, GLOBAL_SQLFILE_PICTUREBANK_CONFIGURATION_TAG, mBeanServer);
		_leafMaxIdentifyTask = new ConfigurationInteger(this,
				MAX_IDENTIFY_TASK_TAG, MAX_IDENTIFY_TASK_SHORT_DESC,
				MAX_IDENTIFY_TASK_LONG_DESC, MAX_IDENTIFY_TASK_INVALID_MESSAGE,
				false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		_leafMaxOtherTask = new ConfigurationInteger(this, MAX_OTHER_TASK_TAG,
				MAX_OTHER_TASK_SHORT_DESC, MAX_OTHER_TASK_LONG_DESC,
				MAX_OTHER_TASK_INVALID_MESSAGE, false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		_leafWaitBeforeWrite = new ConfigurationInteger(this,
				WAIT_BEFORE_WRITE_TAG, WAIT_BEFORE_WRITE_SHORT_DESC,
				WAIT_BEFORE_WRITE_LONG_DESC, WAIT_BEFORE_WRITE_INVALID_MESSAGE,
				false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		_leafNumberOfImagesToPreload = new ConfigurationInteger(this,
				PRELOAD_IMAGES_COUNT_TAG, PRELOAD_IMAGES_COUNT_SHORT_DESC,
				PRELOAD_IMAGES_COUNT_LONG_DESC,
				PRELOAD_IMAGES_COUNT_INVALID_MESSAGE, false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0));
		addLeaf(_leafMaxIdentifyTask);
		addLeaf(_leafMaxOtherTask);
		addLeaf(_leafWaitBeforeWrite);
		addLeaf(_leafNumberOfImagesToPreload);
	}

	/**
	 * Creates a new GlobalSQLFilePictureBankConfigurationImpl with values
	 * specified on the command line.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param iCommandLineMaxIdentifyTask
	 *            the value specified on the command line for the maximum number
	 *            of {@link IdentifyTask} to run at the same time.
	 * @param iCommandLineMaxOtherTask
	 *            the value specified on the command line for the maximum number
	 *            of task other than {@link IdentifyTask} to run at the same
	 *            time.
	 * @param iCommandLineWaitBeforeWrite
	 *            the value specified on the command line for the minimum number
	 *            of seconds to wait between picture modification and saving to
	 *            the database.
	 * @param iCommandLineNumberOfImagesToPreload
	 *            the value specified on the command line for the number of
	 *            images to preload.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public GlobalSQLFilePictureBankConfigurationImpl(
			final IConfiguration parent, final MBeanServer mBeanServer,
			final Integer iCommandLineMaxIdentifyTask,
			final Integer iCommandLineMaxOtherTask,
			final Integer iCommandLineWaitBeforeWrite,
			final Integer iCommandLineNumberOfImagesToPreload)
			throws InvalidConfigurationException
	{
		super(parent, GLOBAL_SQLFILE_PICTUREBANK_CONFIGURATION_TAG, mBeanServer);
		_leafMaxIdentifyTask = new ConfigurationInteger(this,
				MAX_IDENTIFY_TASK_TAG, MAX_IDENTIFY_TASK_SHORT_DESC,
				MAX_IDENTIFY_TASK_LONG_DESC, MAX_IDENTIFY_TASK_INVALID_MESSAGE,
				false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLineMaxIdentifyTask);
		_leafMaxOtherTask = new ConfigurationInteger(this, MAX_OTHER_TASK_TAG,
				MAX_OTHER_TASK_SHORT_DESC, MAX_OTHER_TASK_LONG_DESC,
				MAX_OTHER_TASK_INVALID_MESSAGE, false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLineMaxOtherTask);
		_leafWaitBeforeWrite = new ConfigurationInteger(this,
				WAIT_BEFORE_WRITE_TAG, WAIT_BEFORE_WRITE_SHORT_DESC,
				WAIT_BEFORE_WRITE_LONG_DESC, WAIT_BEFORE_WRITE_INVALID_MESSAGE,
				false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLineWaitBeforeWrite);
		_leafNumberOfImagesToPreload = new ConfigurationInteger(this,
				PRELOAD_IMAGES_COUNT_TAG, PRELOAD_IMAGES_COUNT_SHORT_DESC,
				PRELOAD_IMAGES_COUNT_LONG_DESC,
				PRELOAD_IMAGES_COUNT_INVALID_MESSAGE, false,
				IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0),
				iCommandLineNumberOfImagesToPreload);
		addLeaf(_leafMaxIdentifyTask);
		addLeaf(_leafMaxOtherTask);
		addLeaf(_leafWaitBeforeWrite);
		addLeaf(_leafNumberOfImagesToPreload);
	}

	/**
	 * Creates a new GlobalSQLFilePictureBankConfigurationImpl with values
	 * specified on the command line and in a configuration file.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param iCommandLineMaxIdentifyTask
	 *            the value specified on the command line for the maximum number
	 *            of {@link IdentifyTask} to run at the same time.
	 * @param iCommandLineMaxOtherTask
	 *            the value specified on the command line for the maximum number
	 *            of task other than {@link IdentifyTask} to run at the same
	 *            time.
	 * @param iCommandLineWaitBeforeWrite
	 *            the value specified on the command line for the minimum number
	 *            of seconds to wait between picture modification and saving to
	 *            the database.
	 * @param iCommandLineNumberOfImageToPreload
	 *            the value specified on the command line for the number of
	 *            images to preload.
	 * @param iConfigurationMaxIdentifyTask
	 *            the value specified in the configuration file for the maximum
	 *            number of {@link IdentifyTask} to run at the same time.
	 * @param iConfigurationMaxOtherTask
	 *            the value specified in the configuration file for the maximum
	 *            number of task other than {@link IdentifyTask} to run at the
	 *            same time.
	 * @param iConfigurationWaitBeforeWrite
	 *            the value specified in the configuration file for the minimum
	 *            number of seconds to wait between picture modification and
	 *            saving to the database.
	 * @param iConfigurationNumberOfImageToPreload
	 *            the value specified on the configuration file for the number
	 *            of images to preload.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public GlobalSQLFilePictureBankConfigurationImpl(
			final IConfiguration parent, final MBeanServer mBeanServer,
			final Integer iCommandLineMaxIdentifyTask,
			final Integer iCommandLineMaxOtherTask,
			final Integer iCommandLineWaitBeforeWrite,
			final Integer iCommandLineNumberOfImageToPreload,
			final Integer iConfigurationMaxIdentifyTask,
			final Integer iConfigurationMaxOtherTask,
			final Integer iConfigurationWaitBeforeWrite,
			final Integer iConfigurationNumberOfImageToPreload)
			throws InvalidConfigurationException
	{
		this(parent, mBeanServer, iCommandLineMaxIdentifyTask,
				iCommandLineMaxOtherTask, iCommandLineWaitBeforeWrite,
				iCommandLineNumberOfImageToPreload);
		_leafMaxIdentifyTask
				.setConfigurationValue(iConfigurationMaxIdentifyTask);
		_leafMaxOtherTask.setConfigurationValue(iConfigurationMaxOtherTask);
		_leafWaitBeforeWrite
				.setConfigurationValue(iConfigurationWaitBeforeWrite);
		_leafNumberOfImagesToPreload
				.setConfigurationValue(iConfigurationNumberOfImageToPreload);
	}

	@Override
	public int getMaxConcurrentIdentifyTask()
	{
		return _leafMaxIdentifyTask.getCurrentValue().intValue();
	}

	@Override
	public int getMaxConcurrentOtherTask()
	{
		return _leafMaxOtherTask.getCurrentValue().intValue();
	}

	@Override
	public int getWaitBeforeWrite()
	{
		return _leafWaitBeforeWrite.getCurrentValue().intValue();
	}

	@Override
	public int getPreloadImageCount()
	{
		return _leafNumberOfImagesToPreload.getCurrentValue().intValue();
	}

	@Override
	public String getDescription()
	{
		return "Configuration common to every SQLFilePictureBank.";
	}
}
