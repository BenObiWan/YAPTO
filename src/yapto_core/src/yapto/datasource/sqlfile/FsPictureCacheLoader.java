package yapto.datasource.sqlfile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.util.LinkedList;

import yapto.datasource.IDataSource;
import yapto.datasource.sqlfile.config.FsPictureCacheLoaderConfiguration;
import yapto.datasource.tag.Tag;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * A {@link CacheLoader} loading {@link FsPicture} from the file system.
 * 
 * @author benobiwan
 * 
 */
public final class FsPictureCacheLoader extends CacheLoader<String, FsPicture>
{
	/**
	 * The configuration of this FsPictureCacheLoader.
	 */
	private final FsPictureCacheLoaderConfiguration _conf;

	/**
	 * Object holding the connection to the database and the prepared
	 * statements.
	 */
	private final SQLFileListConnection _fileListConnection;

	/**
	 * {@link LoadingCache} used to load the {@link BufferedImage}.
	 */
	private final LoadingCache<File, BufferedImage> _imageCache;

	/**
	 * {@link LoadingCache} used to load the {@link Tag}.
	 */
	private final LoadingCache<Integer, Tag> _tagCache;

	/**
	 * {@link IDataSource} of this FsPictureCacheLoader.
	 */
	private final IDataSource _dataSource;

	/**
	 * Creates a new FsPictureCacheLoader.
	 * 
	 * @param conf
	 *            the configuration for this FsPictureCacheLoader.
	 * @param fileListConnection
	 *            object holding the connection to the database and the prepared
	 *            statements.
	 * @param imageCache
	 *            {@link LoadingCache} used to load the {@link BufferedImage}.
	 * @param tagCache
	 *            {@link LoadingCache} used to load the {@link Tag}.
	 * @param dataSource
	 *            {@link IDataSource} of this FsPictureCacheLoader.
	 */
	public FsPictureCacheLoader(final FsPictureCacheLoaderConfiguration conf,
			final SQLFileListConnection fileListConnection,
			final LoadingCache<File, BufferedImage> imageCache,
			final LoadingCache<Integer, Tag> tagCache,
			final IDataSource dataSource)
	{
		_conf = conf;
		_fileListConnection = fileListConnection;
		_imageCache = imageCache;
		_tagCache = tagCache;
		_dataSource = dataSource;
	}

	@Override
	public FsPicture load(final String key) throws Exception
	{
		final ResultSet pictureRes = _fileListConnection.loadPicture(key);
		if (pictureRes != null)
		{
			final Integer[] tagIds = _fileListConnection.loadTagsOfPicture(key);
			final LinkedList<Tag> tagList = new LinkedList<Tag>();
			for (final Integer tagId : tagIds)
			{
				tagList.add(_tagCache.get(tagId));
			}
			return new FsPicture(
					_imageCache,
					key,
					_dataSource,
					new File(
							pictureRes
									.getString(SQLFileListConnection.PICTURE_PATH_COLUMN_NAME)),
					pictureRes
							.getInt(SQLFileListConnection.PICTURE_WIDTH_COLUMN_NAME),
					pictureRes
							.getInt(SQLFileListConnection.PICTURE_HEIGTH_COLUMN_NAME),
					pictureRes
							.getLong(SQLFileListConnection.PICTURE_TIMESTAMP_COLUMN_NAME),
					pictureRes
							.getInt(SQLFileListConnection.PICTURE_GRADE_COLUMN_NAME),
					tagList);
		}
		return null;
	}
}
