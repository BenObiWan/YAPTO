package yapto.picturebank;

import yapto.picturebank.config.IPictureBankConfiguration;
import yapto.picturebank.sqlfile.SQLFilePictureBankLoader;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;

import com.google.common.cache.CacheLoader;
import common.config.InvalidConfigurationException;

public final class PictureBankCacheLoader extends
		CacheLoader<IPictureBankConfiguration, IPictureBank<?>>
{
	private final SQLFilePictureBankLoader _sqlFileLoader;

	public PictureBankCacheLoader() throws InvalidConfigurationException
	{
		_sqlFileLoader = new SQLFilePictureBankLoader();
	}

	@Override
	public IPictureBank<?> load(final IPictureBankConfiguration key)
			throws Exception
	{
		if (key instanceof ISQLFilePictureBankConfiguration)
		{
			return _sqlFileLoader
					.getPictureBank((ISQLFilePictureBankConfiguration) key);
		}
		return null;
	}
}
