package yapto.picturebank;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import yapto.picturebank.config.IPictureBankConfiguration;

import common.config.InvalidConfigurationException;

/**
 * Interface describing a loader for {@link IPictureBank}.
 * 
 * @author benobiwan
 * 
 * @param <PICTURE_BANK_CONFIGURATION>
 *            type of configuration for the type of {@link IPictureBank} loaded
 *            by this class.
 * @param <PICTURE_BANK>
 *            type of {@link IPictureBank} loaded by this class.
 */
public interface IPictureBankLoader<PICTURE_BANK_CONFIGURATION extends IPictureBankConfiguration, PICTURE_BANK extends IPictureBank<?>>
{
	/**
	 * Load all the {@link IPictureBankConfiguration} corresponding to this
	 * loader.
	 * 
	 * @return the list of {@link IPictureBankConfiguration} corresponding to
	 *         this loader.
	 * @throws InvalidConfigurationException
	 *             if one of the {@link IPictureBankConfiguration} can't be
	 *             opened.
	 */
	Set<PICTURE_BANK_CONFIGURATION> getAllConfiguration()
			throws InvalidConfigurationException;

	/**
	 * Load the {@link IPictureBank} corresponding to the given configuration.
	 * TODO : better Exception throwing.
	 * 
	 * @param conf
	 *            configuration of the {@link IPictureBank} to load.
	 * @return the loaded {@link IPictureBank}.
	 * @throws SQLException
	 *             if an SQL error occurred during the connection to the
	 *             database.
	 * @throws ClassNotFoundException
	 *             if the database driver class can't be found.
	 * @throws IOException
	 *             if there is an error in creating the required picture
	 *             directories.
	 */
	PICTURE_BANK getPictureBank(final PICTURE_BANK_CONFIGURATION conf)
			throws ClassNotFoundException, SQLException, IOException;
}
