package yapto.picturebank.sqlfile;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import yapto.picturebank.sqlfile.config.GlobalSQLFilePictureBankConfigurationImpl;
import yapto.picturebank.sqlfile.config.IGlobalSQLFilePictureBankConfiguration;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;
import yapto.picturebank.sqlfile.config.SQLFilePictureBankConfigurationImpl;

import com.google.common.eventbus.EventBus;
import common.config.InvalidConfigurationException;

public final class SQLFilePictureBankLoader
{
	/**
	 * Set of individual SQLFilePictureBank configuration.
	 */
	private final Set<ISQLFilePictureBankConfiguration> _confSet = new HashSet<>();

	/**
	 * Configuration common to every SQLFilePictureBank.
	 */
	private final IGlobalSQLFilePictureBankConfiguration _globalConf;

	public SQLFilePictureBankLoader() throws InvalidConfigurationException
	{
		_globalConf = new GlobalSQLFilePictureBankConfigurationImpl(null,
				ManagementFactory.getPlatformMBeanServer(), Integer.valueOf(4),
				Integer.valueOf(4), Integer.valueOf(3));

		_confSet.add(new SQLFilePictureBankConfigurationImpl(null,
				ManagementFactory.getPlatformMBeanServer(), Integer.valueOf(1),
				"/home/benobiwan/images/photoDB/photoDB.sqlite",
				"/home/benobiwan/images/photoDB/photos/",
				"/home/benobiwan/images/photoDB/thumbnails/",
				"/home/benobiwan/images/photoDB/index/"));
	}

	public Set<ISQLFilePictureBankConfiguration> getAllConfiguration()
	{
		return Collections.unmodifiableSet(_confSet);
	}

	public SQLFilePictureBank getPictureBank(
			final ISQLFilePictureBankConfiguration conf)
			throws ClassNotFoundException, SQLException, IOException
	{
		final EventBus bus = new EventBus();
		return new SQLFilePictureBank(_globalConf, conf, bus);
	}
}
