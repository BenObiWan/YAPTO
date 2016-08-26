package yapto.picturebank.sqlfile.config;

import javax.management.MBeanServer;

import common.config.AbstractConfigurationBranch;
import common.config.IConfiguration;
import common.config.InvalidConfigurationException;
import common.config.display.IntegerDisplayType;
import common.config.display.StringDisplayType;
import common.config.leaf.ConfigurationInteger;
import common.config.leaf.ConfigurationString;

/**
 * An implementation of the {@link IBufferedImageCacheLoaderConfiguration}
 * interface.
 * 
 * @author benobiwan
 *
 */
public final class BufferedImageCacheLoaderConfigurationImpl extends AbstractConfigurationBranch
		implements IBufferedImageCacheLoaderConfiguration
{

	/**
	 * Leaf configuring the base directory for pictures storage.
	 */
	private final ConfigurationString _leafPictureDirectory;

	/**
	 * Leaf configuring the cache size.
	 */
	private final ConfigurationInteger _leafCacheSize;

	/**
	 * Short description for the pictures directory.
	 */
	private final static String PICTURE_DIRECTORY_SHORT_DESC = "Pictures directory";

	/**
	 * Long description for the pictures directory.
	 */
	private final static String PICTURE_DIRECTORY_LONG_DESC = "Base directory for the pictures.";

	/**
	 * Invalid message for the pictures directory.
	 */
	private final static String PICTURE_DIRECTORY_INVALID_MESSAGE = "Invalid base directory for the pictures.";

	/**
	 * Short description for the pictures directory.
	 */
	private final static String CACHE_SIZE_SHORT_DESC = "Cache size";

	/**
	 * Long description for the pictures directory.
	 */
	private final static String CACHE_SIZE_LONG_DESC = "Cache size for the pictures.";

	/**
	 * Invalid message for the pictures directory.
	 */
	private final static String CACHE_SIZE_INVALID_MESSAGE = "Invalid cache size value.";

	/**
	 * Creates a new BufferedImageCacheLoaderConfigurationImpl using default
	 * values for every elements.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param strCacheLoaderName
	 *            name of this cache loader.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 */
	public BufferedImageCacheLoaderConfigurationImpl(final IConfiguration parent, final String strCacheLoaderName,
			final MBeanServer mBeanServer)
	{
		super(parent, strCacheLoaderName, mBeanServer);
		_leafPictureDirectory = new ConfigurationString(this, PICTURE_DIRECTORY_TAG, PICTURE_DIRECTORY_SHORT_DESC,
				PICTURE_DIRECTORY_LONG_DESC, PICTURE_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0,
				"");
		_leafCacheSize = new ConfigurationInteger(this, CACHE_SIZE_TAG, CACHE_SIZE_SHORT_DESC, CACHE_SIZE_LONG_DESC,
				CACHE_SIZE_INVALID_MESSAGE, false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(10));
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafCacheSize);
	}

	/**
	 * Creates a new BufferedImageCacheLoaderConfigurationImpl with values
	 * coming from the command line.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param strCacheLoaderName
	 *            name of this cache loader.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param strCommandLinePictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for pictures.
	 * @param iCommandLineCacheSize
	 *            the value specified on the command line for the cache size.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public BufferedImageCacheLoaderConfigurationImpl(final IConfiguration parent, final String strCacheLoaderName,
			final MBeanServer mBeanServer, final String strCommandLinePictureDirectory, final int iCommandLineCacheSize)
			throws InvalidConfigurationException
	{
		super(parent, strCacheLoaderName, mBeanServer);
		_leafPictureDirectory = new ConfigurationString(this, PICTURE_DIRECTORY_TAG, PICTURE_DIRECTORY_SHORT_DESC,
				PICTURE_DIRECTORY_LONG_DESC, PICTURE_DIRECTORY_INVALID_MESSAGE, false, StringDisplayType.TEXTFIELD, 0,
				"", strCommandLinePictureDirectory);
		_leafCacheSize = new ConfigurationInteger(this, CACHE_SIZE_TAG, CACHE_SIZE_SHORT_DESC, CACHE_SIZE_LONG_DESC,
				CACHE_SIZE_INVALID_MESSAGE, false, IntegerDisplayType.SPINNER, Integer.valueOf(0),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(10), Integer.valueOf(iCommandLineCacheSize));
		addLeaf(_leafPictureDirectory);
		addLeaf(_leafCacheSize);
	}

	/**
	 * Creates a new BufferedImageCacheLoaderConfigurationImpl with values
	 * coming from the command line and from a configuration file.
	 * 
	 * @param parent
	 *            the parent configuration.
	 * @param strCacheLoaderName
	 *            name of this cache loader.
	 * @param mBeanServer
	 *            the {@link MBeanServer} to use.
	 * @param strCommandLinePictureDirectory
	 *            the value specified on the command line for the base directory
	 *            for pictures.
	 * @param iCommandLineCacheSize
	 *            the value specified on the command line for the cache size.
	 * @param strConfigurationPictureDirectory
	 *            the value specified in the configuration file for the base
	 *            directory for pictures.
	 * @param iConfigurationCacheSize
	 *            the value specified in the configuration file for the cache
	 *            size.
	 * @throws InvalidConfigurationException
	 *             one of the given value is invalid.
	 */
	public BufferedImageCacheLoaderConfigurationImpl(final IConfiguration parent, final String strCacheLoaderName,
			final MBeanServer mBeanServer, final String strCommandLinePictureDirectory, final int iCommandLineCacheSize,
			final String strConfigurationPictureDirectory, final int iConfigurationCacheSize)
			throws InvalidConfigurationException
	{
		this(parent, strCacheLoaderName, mBeanServer, strCommandLinePictureDirectory, iCommandLineCacheSize);
		_leafPictureDirectory.setConfigurationValue(strConfigurationPictureDirectory);
		_leafCacheSize.setConfigurationValue(Integer.valueOf(iConfigurationCacheSize));
	}

	@Override
	public String getDescription()
	{
		return "Picture loader configuration";
	}

	@Override
	public String getPictureDirectory()
	{
		return _leafPictureDirectory.getCurrentValue();
	}

	@Override
	public long getCacheSize()
	{
		return _leafCacheSize.getCurrentValue().intValue();
	}
}
