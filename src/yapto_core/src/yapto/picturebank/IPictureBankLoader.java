package yapto.picturebank;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import yapto.picturebank.config.IPictureBankConfiguration;

import common.config.InvalidConfigurationException;

public interface IPictureBankLoader<PICTURE_BANK_CONFIGURATION extends IPictureBankConfiguration, PICTURE_BANK extends IPictureBank>
{
	Set<PICTURE_BANK_CONFIGURATION> getAllConfiguration()
			throws InvalidConfigurationException;

	PICTURE_BANK getPictureBank(final PICTURE_BANK_CONFIGURATION conf)
			throws ClassNotFoundException, SQLException, IOException;
}
