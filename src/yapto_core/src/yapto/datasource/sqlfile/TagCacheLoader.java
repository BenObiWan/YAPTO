package yapto.datasource.sqlfile;

import java.sql.ResultSet;

import yapto.datasource.sqlfile.config.BufferedImageCacheLoaderConfiguration;
import yapto.datasource.tag.Tag;

import com.google.common.cache.CacheLoader;

/**
 * A {@link CacheLoader} loading {@link Tag} from a database.
 * 
 * @author benobiwan
 * 
 */
public final class TagCacheLoader extends CacheLoader<Integer, Tag>
{
	/**
	 * The configuration of this FsPictureCacheLoader.
	 */
	private final BufferedImageCacheLoaderConfiguration _conf;

	/**
	 * Object holding the connection to the database and the prepared
	 * statements.
	 */
	private final SQLFileListConnection _fileListConnection;

	/**
	 * Creates a new TagCacheLoader.
	 * 
	 * @param conf
	 *            the configuration for this FsPictureCacheLoader.
	 * @param fileListConnection
	 *            object holding the connection to the database and the prepared
	 *            statements.
	 */
	public TagCacheLoader(final BufferedImageCacheLoaderConfiguration conf,
			final SQLFileListConnection fileListConnection)
	{
		_conf = conf;
		_fileListConnection = fileListConnection;
	}

	@Override
	public Tag load(final Integer key) throws Exception
	{
		ResultSet res = null;
		try
		{
			res = _fileListConnection.loadTag(key.intValue());
			return new Tag(
					_conf.getDataSourceId(),
					res.getInt(SQLFileListConnection.TAG_ID_COLUMN_NAME),
					res.getString(SQLFileListConnection.TAG_NAME_COLUMN_NAME),
					res.getString(SQLFileListConnection.TAG_DESCRIPTION_COLUMN_NAME),
					res.getBoolean(SQLFileListConnection.TAG_SELECTABLE_COLUMN_NAME));
		}
		finally
		{
			if (res != null)
			{
				res.close();
			}
		}
	}
}
