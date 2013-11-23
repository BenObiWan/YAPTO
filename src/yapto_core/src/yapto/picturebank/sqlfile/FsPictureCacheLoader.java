package yapto.picturebank.sqlfile;

import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.util.LinkedList;

import yapto.picturebank.IPictureBank;
import yapto.picturebank.tag.ITag;
import yapto.picturebank.tag.ITagRepository;

import com.google.common.cache.CacheLoader;

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
	 * {@link ITagRepository} used to load {@link ITag}s.
	 */
	private final ITagRepository _tagRepository;

	/**
	 * {@link IPictureBank} of this FsPictureCacheLoader.
	 */
	private final SQLFilePictureBank _pictureBank;

	/**
	 * Creates a new FsPictureCacheLoader.
	 * 
	 * @param fileListConnection
	 *            object holding the connection to the database and the prepared
	 *            statements.
	 * @param imageLoader
	 *            {@link ImageLoader} used to load the {@link BufferedImage}.
	 * @param tagRepository
	 *            {@link ITagRepository} used to load {@link ITag}s.
	 * @param pictureBank
	 *            {@link IPictureBank} of this FsPictureCacheLoader.
	 */
	public FsPictureCacheLoader(final SQLFileListConnection fileListConnection,
			final ImageLoader imageLoader, final ITagRepository tagRepository,
			final SQLFilePictureBank pictureBank)
	{
		_fileListConnection = fileListConnection;
		_imageLoader = imageLoader;
		_tagRepository = tagRepository;
		_pictureBank = pictureBank;
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
				final LinkedList<ITag> tagList = new LinkedList<>();
				for (final Integer tagId : tagIds)
				{
					tagList.add(_tagRepository.getTag(tagId));
				}
				return new FsPicture(
						_imageLoader,
						_pictureBank,
						key,
						pictureRes
								.getLong(SQLFileListConnection.PICTURE_MODIFIED_TIMESTAMP_COLUMN_NAME),
						pictureRes
								.getLong(SQLFileListConnection.PICTURE_ADDING_TIMESTAMP_COLUMN_NAME),
						pictureRes
								.getInt(SQLFileListConnection.PICTURE_GRADE_COLUMN_NAME),
						pictureRes
								.getString(SQLFileListConnection.PICTURE_ORIGINAL_NAME),
						pictureRes
								.getInt(SQLFileListConnection.PICTURE_WIDTH_COLUMN_NAME),
						pictureRes
								.getInt(SQLFileListConnection.PICTURE_HEIGTH_COLUMN_NAME),
						pictureRes
								.getLong(SQLFileListConnection.PICTURE_CREATION_TIMESTAMP_COLUMN_NAME),
						pictureRes
								.getInt(SQLFileListConnection.PICTURE_ORIENTATION_COLUMN_NAME),
						pictureRes
								.getString(SQLFileListConnection.PICTURE_MAKE_COLUMN_NAME),
						pictureRes
								.getString(SQLFileListConnection.PICTURE_MODEL_COLUMN_NAME),
						pictureRes
								.getString(SQLFileListConnection.PICTURE_EXPOSURE_COLUMN_NAME),
						pictureRes
								.getString(SQLFileListConnection.PICTURE_RELATIVE_APERTURE_COLUMN_NAME),
						pictureRes
								.getString(SQLFileListConnection.PICTURE_FOCAL_LENGTH_COLUMN_NAME),
						pictureRes
								.getString(SQLFileListConnection.PICTURE_FORMAT_COLUMN_NAME),
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
