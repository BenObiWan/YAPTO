package yapto.picturebank.sqlfile.config;

import yapto.picturebank.config.IPictureBankConfiguration;
import yapto.picturebank.sqlfile.SQLFilePictureBank;

import com.google.common.cache.CacheLoader;

/**
 * Configuration for an {@link SQLFilePictureBank}.
 * 
 * @author benobiwan
 * 
 */
public interface ISQLFilePictureBankConfiguration extends
		IPictureBankConfiguration
{
	/**
	 * Tag of this configuration node.
	 */
	String SQLFILE_PICTUREBANK_CONFIGURATION_TAG = "SQLFilePictureBank";

	/**
	 * Tag for the database connection String.
	 */
	String DATABASE_CONNECTION_TAG = "DatabaseConnection";

	/**
	 * Tag for the picture directory.
	 */
	String PICTURE_DIRECTORY_TAG = "PictureDirectory";

	/**
	 * Tag for the secondary picture directory.
	 */
	String SECONDARY_PICTURE_DIRECTORY_TAG = "SecondaryPictureDirectory";

	/**
	 * Tag for the thumbnail directory.
	 */
	String THUMBNAILS_DIRECTORY_TAG = "ThumbnailDirectory";

	/**
	 * Tag for the picture directory.
	 */
	String INDEX_DIRECTORY_TAG = "IndexDirectory";

	/**
	 * Get the connection String of the database.
	 * 
	 * @return connection String of the database.
	 */
	String getDatabaseConnection();

	/**
	 * Get the path for the index directory.
	 * 
	 * @return the path for the index directory.
	 */
	String getIndexDirectory();

	/**
	 * Get the {@link IBufferedImageCacheLoaderConfiguration} for the picture
	 * {@link CacheLoader}.
	 * 
	 * @return the {@link IBufferedImageCacheLoaderConfiguration} for the
	 *         picture {@link CacheLoader}.
	 */
	IBufferedImageCacheLoaderConfiguration getMainPictureLoaderConfiguration();

	/**
	 * Get the {@link IBufferedImageCacheLoaderConfiguration} for the secondary
	 * picture {@link CacheLoader}.
	 * 
	 * @return the {@link IBufferedImageCacheLoaderConfiguration} for the
	 *         secondary pictures {@link CacheLoader}.
	 */
	IBufferedImageCacheLoaderConfiguration getSecondaryPictureLoaderConfiguration();

	/**
	 * Get the {@link IBufferedImageCacheLoaderConfiguration} for the thumbnails
	 * {@link CacheLoader}.
	 * 
	 * @return the {@link IBufferedImageCacheLoaderConfiguration} for the
	 *         thumbnails {@link CacheLoader}.
	 */
	IBufferedImageCacheLoaderConfiguration getThumbnailPictureLoaderConfiguration();
}
