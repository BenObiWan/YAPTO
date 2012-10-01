package yapto.datasource.sqlfile.config;

import yapto.datasource.config.IDataSourceConfiguration;
import yapto.datasource.process.IdentifyTask;
import yapto.datasource.sqlfile.IBufferedImageCacheLoaderConfiguration;
import yapto.datasource.sqlfile.SQLFileDataSource;

import com.google.common.cache.CacheLoader;

/**
 * Configuration for an {@link SQLFileDataSource}.
 * 
 * @author benobiwan
 * 
 */
public interface ISQLFileDataSourceConfiguration extends
		IDataSourceConfiguration
{
	/**
	 * Tag of this configuration node.
	 */
	String SQLFILE_DATASOURCE_CONFIGURATION_TAG = "SQLFileDataSource";

	/**
	 * Tag for the database file name.
	 */
	String DATABASE_FILENAME_TAG = "DatabaseFileName";

	/**
	 * Tag for the picture directory.
	 */
	String PICTURE_DIRECTORY_TAG = "PictureDirectory";

	/**
	 * Tag for the thumbnail directory.
	 */
	String THUMBNAILS_DIRECTORY_TAG = "ThumbnailDirectory";

	/**
	 * Tag for the picture directory.
	 */
	String INDEX_DIRECTORY_TAG = "IndexDirectory";

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
	 * Get the file name of the database.
	 * 
	 * @return the file name of the database.
	 */
	String getDatabaseFileName();

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
	 * Get the {@link IBufferedImageCacheLoaderConfiguration} for the thumbnails
	 * {@link CacheLoader}.
	 * 
	 * @return the {@link IBufferedImageCacheLoaderConfiguration} for the
	 *         thumbnails {@link CacheLoader}.
	 */
	IBufferedImageCacheLoaderConfiguration getThumbnailPictureLoaderConfiguration();

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
