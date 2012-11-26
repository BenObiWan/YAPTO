package yapto.picturebank.sqlfile.config;

import yapto.picturebank.process.IdentifyTask;
import yapto.picturebank.sqlfile.SQLFilePictureBank;

/**
 * Configuration common to every {@link SQLFilePictureBank}.
 * 
 * @author benobiwan
 * 
 */
public interface IGlobalSQLFilePictureBankConfiguration
{
	/**
	 * Tag of this configuration node.
	 */
	String GLOBAL_SQLFILE_PICTUREBANK_CONFIGURATION_TAG = "GlobalSQLFilePictureBank";

	/**
	 * Tag for the maximum number of {@link IdentifyTask} to run at the same
	 * time.
	 */
	String MAX_IDENTIFY_TASK_TAG = "MaxIdentifyTask";

	/**
	 * Tag for the maximum number of task other than {@link IdentifyTask} to run
	 * at the same time.
	 */
	String MAX_OTHER_TASK_TAG = "MaxOtherTask";

	/**
	 * Tag for the minimum number of seconds to wait between picture
	 * modification and saving to the database.
	 */
	String WAIT_BEFORE_WRITE_TAG = "WaitBeforeWrite";

	/**
	 * Get the maximum number of {@link IdentifyTask} to run at the same time.
	 * 
	 * @return the maximum number of {@link IdentifyTask} to run at the same
	 *         time.
	 */
	int getMaxConcurrentIdentifyTask();

	/**
	 * Get the maximum number of task other than {@link IdentifyTask} to run at
	 * the same time.
	 * 
	 * @return the maximum number of task other than {@link IdentifyTask} to run
	 *         at the same time.
	 */
	int getMaxConcurrentOtherTask();

	/**
	 * Get the minimum number of seconds to wait between picture modification
	 * and saving to the database.
	 * 
	 * @return the minimum number of seconds to wait between picture
	 *         modification and saving to the database.
	 */
	int getWaitBeforeWrite();
}
