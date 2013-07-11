package yapto.picturebank.sqlfile;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import yapto.picturebank.IPictureBankLoader;
import yapto.picturebank.sqlfile.config.GlobalSQLFilePictureBankConfigurationImpl;
import yapto.picturebank.sqlfile.config.IGlobalSQLFilePictureBankConfiguration;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;
import yapto.picturebank.sqlfile.config.SQLFilePictureBankConfigurationImpl;

import com.google.common.eventbus.EventBus;
import common.config.InvalidConfigurationException;

/**
 * Loader for {@link SQLFilePictureBank}.
 * 
 * @author benobiwan
 * 
 */
public final class SQLFilePictureBankLoader
		implements
		IPictureBankLoader<ISQLFilePictureBankConfiguration, SQLFilePictureBank>
{
	/**
	 * Configuration common to every SQLFilePictureBank.
	 */
	private final IGlobalSQLFilePictureBankConfiguration _globalConf;

	/**
	 * Creates a new {@link SQLFilePictureBankLoader}.
	 * 
	 * @throws InvalidConfigurationException
	 *             the {IGlobalSQLFilePictureBankConfiguration} is invalid.
	 */
	public SQLFilePictureBankLoader() throws InvalidConfigurationException
	{
		_globalConf = new GlobalSQLFilePictureBankConfigurationImpl(null,
				ManagementFactory.getPlatformMBeanServer(), Integer.valueOf(4),
				Integer.valueOf(4), Integer.valueOf(3), Integer.valueOf(4));
	}

	@Override
	public Set<ISQLFilePictureBankConfiguration> getAllConfiguration()
			throws InvalidConfigurationException
	{
		final Set<ISQLFilePictureBankConfiguration> confSet = new HashSet<>();
		confSet.add(new SQLFilePictureBankConfigurationImpl(null,
				ManagementFactory.getPlatformMBeanServer(), Integer.valueOf(1),
				"local", "/home/benobiwan/images/photoDB/photoDB.sqlite",
				"/home/benobiwan/images/photoDB/photos/",
				"/home/benobiwan/images/photoDB/thumbnails/",
				"/home/benobiwan/images/photoDB/index/"));
		return Collections.unmodifiableSet(confSet);
	}

	@Override
	public SQLFilePictureBank getPictureBank(
			final ISQLFilePictureBankConfiguration conf)
			throws ClassNotFoundException, SQLException, IOException
	{
		final EventBus bus = new EventBus();
		return new SQLFilePictureBank(_globalConf, conf, bus);
	}
}
