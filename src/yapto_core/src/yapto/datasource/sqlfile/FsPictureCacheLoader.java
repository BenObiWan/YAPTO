package yapto.datasource.sqlfile;

import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.util.LinkedList;

import yapto.datasource.IDataSource;
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
	 * Object holding the connection to the database and the prepared
	 * statements.
	 */
	private final SQLFileListConnection _fileListConnection;

	/**
	 * {@link ImageLoader} used to load the {@link BufferedImage}.
	 */
	private final ImageLoader _imageLoader;

	/**
	 * {@link LoadingCache} used to load the {@link Tag}.
	 */
	private final LoadingCache<Integer, Tag> _tagCache;

	/**
	 * {@link IDataSource} of this FsPictureCacheLoader.
	 */
	private final SQLFileDataSource _dataSource;

	/**
	 * Creates a new FsPictureCacheLoader.
	 * 
	 * @param fileListConnection
	 *            object holding the connection to the database and the prepared
	 *            statements.
	 * @param imageLoader
	 *            {@link ImageLoader} used to load the {@link BufferedImage}.
	 * @param tagCache
	 *            {@link LoadingCache} used to load the {@link Tag}.
	 * @param dataSource
	 *            {@link IDataSource} of this FsPictureCacheLoader.
	 */
	public FsPictureCacheLoader(final SQLFileListConnection fileListConnection,
			final ImageLoader imageLoader,
			final LoadingCache<Integer, Tag> tagCache,
			final SQLFileDataSource dataSource)
	{
		_fileListConnection = fileListConnection;
		_imageLoader = imageLoader;
		_tagCache = tagCache;
		_dataSource = dataSource;
	}

	@Override
	public FsPicture load(final String key) throws Exception
	{
		ResultSet pictureRes = null;
		try
		{
			pictureRes = _fileListConnection.loadPicture(key);
			if (pictureRes != null)
			{
				final Integer[] tagIds = _fileListConnection
						.loadTagsOfPicture(key);
				final LinkedList<Tag> tagList = new LinkedList<>();
				for (final Integer tagId : tagIds)
				{
					tagList.add(_tagCache.get(tagId));
				}
				return new FsPicture(
						_imageLoader,
						_dataSource,
						key,
						pictureRes
								.getString(SQLFileListConnection.PICTURE_ORIGINAL_NAME),
						pictureRes
								.getInt(SQLFileListConnection.PICTURE_WIDTH_COLUMN_NAME),
						pictureRes
								.getInt(SQLFileListConnection.PICTURE_HEIGTH_COLUMN_NAME),
						pictureRes
								.getLong(SQLFileListConnection.PICTURE_MODIFIED_TIMESTAMP_COLUMN_NAME),
						pictureRes
								.getLong(SQLFileListConnection.PICTURE_CREATION_TIMESTAMP_COLUMN_NAME),
						pictureRes
								.getLong(SQLFileListConnection.PICTURE_ADDING_TIMESTAMP_COLUMN_NAME),
						pictureRes
								.getInt(SQLFileListConnection.PICTURE_GRADE_COLUMN_NAME),
						tagList);
			}
			return null;
		}
		finally
		{
			if (pictureRes != null)
			{
				pictureRes.close();
			}
		}
	}
}
