package yapto.picturebank.sqlfile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import common.config.db.IDatabaseConfiguration;
import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureInformation;
import yapto.picturebank.tag.ITag;

/**
 * Object holding the connection to the database and the prepared statements.
 * 
 * @author benobiwan
 * 
 */
public final class SQLFileListConnection
{
	/**
	 * Configuration for this database.
	 */
	private final IDatabaseConfiguration _dbConf;

	// tag table
	/**
	 * Name for the 'tag' table.
	 */
	public static final String TAG_TABLE_NAME = "tag";

	/**
	 * Name for the 'id' column of the 'tag' table.
	 */
	public static final String TAG_ID_COLUMN_NAME = "id";

	/**
	 * Name for the 'name' column of the 'tag' table.
	 */
	public static final String TAG_NAME_COLUMN_NAME = "name";

	/**
	 * Name for the 'description' column of the 'tag' table.
	 */
	public static final String TAG_DESCRIPTION_COLUMN_NAME = "description";

	/**
	 * Name for the 'parentId' column of the 'tag' table.
	 */
	public static final String TAG_PARENT_ID_COLUMN_NAME = "parentId";

	/**
	 * Name for the 'selectable' column of the 'tag' table.
	 */
	public static final String TAG_SELECTABLE_COLUMN_NAME = "selectable";

	// picture table
	/**
	 * Name for the 'picture' table.
	 */
	public static final String PICTURE_TABLE_NAME = "picture";

	/**
	 * Name for the 'id' column of the 'picture' table.
	 */
	public static final String PICTURE_ID_COLUMN_NAME = "id";

	/**
	 * Name for the 'grade' column of the 'picture' table.
	 */
	public static final String PICTURE_GRADE_COLUMN_NAME = "grade";

	/**
	 * Name for the 'modified_timestamp' column of the 'picture' table.
	 */
	public static final String PICTURE_MODIFIED_TIMESTAMP_COLUMN_NAME = "modified_timestamp";

	/**
	 * Name for the 'adding_timestamp' column of the 'picture' table.
	 */
	public static final String PICTURE_ADDING_TIMESTAMP_COLUMN_NAME = "adding_timestamp";

	// picture table, PictureInformation section
	/**
	 * Name for the'original_name' column of the 'picture' table.
	 */
	public static final String PICTURE_ORIGINAL_NAME = "original_name";

	/**
	 * Name for the 'width' column of the 'picture' table.
	 */
	public static final String PICTURE_WIDTH_COLUMN_NAME = "width";

	/**
	 * Name for the 'mark' column of the 'picture' table.
	 */
	public static final String PICTURE_HEIGTH_COLUMN_NAME = "height";

	/**
	 * Name for the 'creation_timestamp' column of the 'picture' table.
	 */
	public static final String PICTURE_CREATION_TIMESTAMP_COLUMN_NAME = "creation_timestamp";

	/**
	 * Name for the 'orientation' column of the 'picture' table.
	 */
	public static final String PICTURE_ORIENTATION_COLUMN_NAME = "orientation";

	/**
	 * Name for the 'make' column of the 'picture' table.
	 */
	public static final String PICTURE_MAKE_COLUMN_NAME = "make";

	/**
	 * Name for the 'model' column of the 'picture' table.
	 */
	public static final String PICTURE_MODEL_COLUMN_NAME = "model";

	/**
	 * Name for the 'exposure' column of the 'picture' table.
	 */
	public static final String PICTURE_EXPOSURE_COLUMN_NAME = "exposure";

	/**
	 * Name for the 'relative aperture' column of the 'picture' table.
	 */
	public static final String PICTURE_RELATIVE_APERTURE_COLUMN_NAME = "relative_aperture";

	/**
	 * Name for the 'focal length' column of the 'picture' table.
	 */
	public static final String PICTURE_FOCAL_LENGTH_COLUMN_NAME = "focal_length";

	/**
	 * Name for the 'format' column of the 'picture' table.
	 */
	public static final String PICTURE_FORMAT_COLUMN_NAME = "format";

	// picture_tag table
	/**
	 * Name for the 'picture_tag' table.
	 */
	public static final String PICTURE_TAG_TABLE_NAME = "picture_tag";

	/**
	 * Name for the 'tagId' column of the 'picture_tag' table.
	 */
	public static final String PICTURE_TAG_TAG_ID_COLUMN_NAME = "tagId";

	/**
	 * Name for the 'pictureId' column of the 'picture_tag' table.
	 */
	public static final String PICTURE_TAG_PICTURE_ID_COLUMN_NAME = "pictureId";

	/**
	 * Connection to the database.
	 */
	private final Connection _connection;

	/**
	 * Statement to insert a {@link ITag}.
	 */
	private final PreparedStatement _psInsertTag;

	/**
	 * Statement to edit a {@link ITag}.
	 */
	private final PreparedStatement _psEditTag;

	/**
	 * Statement to count the number of {@link IPicture}s having a given
	 * {@link ITag}.
	 */
	private final PreparedStatement _psCountPicturesByTag;

	/**
	 * Statement to count the total number of {@link IPicture}s in this
	 * {@link SQLFilePictureBank}.
	 */
	private final PreparedStatement _psCountPictures;

	/**
	 * Statement to select all the {@link IPicture}s having a given {@link ITag}
	 * .
	 */
	private final PreparedStatement _psSelectPicturesByTag;

	/**
	 * Statement to insert an {@link IPicture} in the database.
	 */
	private final PreparedStatement _psInsertPicture;

	/**
	 * Statement to update the mark and the timestamp of the {@link IPicture}.
	 */
	private final PreparedStatement _psUpdatePictureMarkAndTimestamp;

	/**
	 * Statement to insert a {@link ITag} for an {@link IPicture}.
	 */
	private final PreparedStatement _psInsertTagForPicture;

	/**
	 * Statement to remove all the {@link ITag}s of an {@link IPicture}.
	 */
	private final PreparedStatement _psRemoveTagsForPicture;

	/**
	 * Statement to load a picture.
	 */
	private final PreparedStatement _psLoadPicture;

	/**
	 * Statement to load all the {@link ITag}s of an {@link IPicture}.
	 */
	private final PreparedStatement _psLoadTagsOfPicture;

	/**
	 * Statement to load a {@link ITag}.
	 */
	private final PreparedStatement _psLoadTag;

	/**
	 * Statement to load the list of {@link IPicture} names.
	 */
	private final PreparedStatement _psListPicture;

	/**
	 * Statement to remove a {@link ITag}.
	 */
	private final PreparedStatement _psRemoveTag;

	/**
	 * Statement to remove a {@link ITag} from all the {@link IPicture}s.
	 */
	private final PreparedStatement _psRemoveTagFromAllPictures;

	/**
	 * Statement to remove all {@link ITag} associated to an {@link IPicture}.
	 */
	private final PreparedStatement _psRemoveAllTagsForPicture;

	/**
	 * Statement to remove a {@link IPicture}.
	 */
	private final PreparedStatement _psRemovePicture;

	/**
	 * creates a new SQLFileListConnection.
	 * 
	 * @throws SQLException
	 *             if an SQL error occurred during the connection to the
	 *             database.
	 * @throws ClassNotFoundException
	 *             if the database driver class can't be found.
	 */
	public SQLFileListConnection(final IDatabaseConfiguration dbConf) throws ClassNotFoundException, SQLException
	{
		_dbConf = dbConf;
		Class.forName(_dbConf.getDatabaseDriver());
		_connection = DriverManager.getConnection(_dbConf.getDatabaseConnection());
		createTables();

		_psInsertTag = _connection.prepareStatement("INSERT INTO " + TAG_TABLE_NAME + " (" + TAG_ID_COLUMN_NAME + ", "
				+ TAG_NAME_COLUMN_NAME + ", " + TAG_DESCRIPTION_COLUMN_NAME + ", " + TAG_PARENT_ID_COLUMN_NAME + ", "
				+ TAG_SELECTABLE_COLUMN_NAME + ") VALUES(?, ?, ?, ?, ?)");
		_psEditTag = _connection.prepareStatement("UPDATE " + TAG_TABLE_NAME + " SET " + TAG_NAME_COLUMN_NAME + "=?, "
				+ TAG_DESCRIPTION_COLUMN_NAME + "=?, " + TAG_PARENT_ID_COLUMN_NAME + "=?, " + TAG_SELECTABLE_COLUMN_NAME
				+ "=? WHERE " + TAG_ID_COLUMN_NAME + "=?");
		_psCountPicturesByTag = _connection.prepareStatement("SELECT COUNT(" + PICTURE_TAG_PICTURE_ID_COLUMN_NAME
				+ ") FROM " + PICTURE_TAG_TABLE_NAME + " WHERE " + PICTURE_TAG_TAG_ID_COLUMN_NAME + "=?");
		_psSelectPicturesByTag = _connection.prepareStatement("SELECT " + PICTURE_TAG_PICTURE_ID_COLUMN_NAME + " FROM "
				+ PICTURE_TAG_TABLE_NAME + " WHERE " + PICTURE_TAG_TAG_ID_COLUMN_NAME + "=?");
		_psCountPictures = _connection
				.prepareStatement("SELECT COUNT(" + PICTURE_ID_COLUMN_NAME + ") FROM " + PICTURE_TABLE_NAME);
		_psInsertPicture = _connection.prepareStatement("INSERT INTO " + PICTURE_TABLE_NAME + " ("
				+ PICTURE_ID_COLUMN_NAME + ", " + PICTURE_GRADE_COLUMN_NAME + ", "
				+ PICTURE_MODIFIED_TIMESTAMP_COLUMN_NAME + ", " + PICTURE_ADDING_TIMESTAMP_COLUMN_NAME + ", "
				+ PICTURE_ORIGINAL_NAME + ", " + PICTURE_WIDTH_COLUMN_NAME + ", " + PICTURE_HEIGTH_COLUMN_NAME + ", "
				+ PICTURE_CREATION_TIMESTAMP_COLUMN_NAME + ", " + PICTURE_ORIENTATION_COLUMN_NAME + ", "
				+ PICTURE_MAKE_COLUMN_NAME + ", " + PICTURE_MODEL_COLUMN_NAME + ", " + PICTURE_EXPOSURE_COLUMN_NAME
				+ ", " + PICTURE_RELATIVE_APERTURE_COLUMN_NAME + ", " + PICTURE_FOCAL_LENGTH_COLUMN_NAME + ", "
				+ PICTURE_FORMAT_COLUMN_NAME + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		_psUpdatePictureMarkAndTimestamp = _connection
				.prepareStatement("UPDATE " + PICTURE_TABLE_NAME + " SET " + PICTURE_GRADE_COLUMN_NAME + "=?, "
						+ PICTURE_MODIFIED_TIMESTAMP_COLUMN_NAME + "=? WHERE " + PICTURE_ID_COLUMN_NAME + "=?");
		_psInsertTagForPicture = _connection.prepareStatement("INSERT INTO " + PICTURE_TAG_TABLE_NAME + " ("
				+ PICTURE_TAG_TAG_ID_COLUMN_NAME + ", " + PICTURE_TAG_PICTURE_ID_COLUMN_NAME + ") VALUES(?, ?)");
		_psRemoveTagsForPicture = _connection.prepareStatement(
				"DELETE FROM " + PICTURE_TAG_TABLE_NAME + " WHERE " + PICTURE_TAG_PICTURE_ID_COLUMN_NAME + "=?");
		_psLoadPicture = _connection.prepareStatement("SELECT " + PICTURE_GRADE_COLUMN_NAME + ", "
				+ PICTURE_ORIGINAL_NAME + ", " + PICTURE_WIDTH_COLUMN_NAME + ", " + PICTURE_HEIGTH_COLUMN_NAME + ", "
				+ PICTURE_MODIFIED_TIMESTAMP_COLUMN_NAME + ", " + PICTURE_CREATION_TIMESTAMP_COLUMN_NAME + ", "
				+ PICTURE_ADDING_TIMESTAMP_COLUMN_NAME + ", " + PICTURE_ORIENTATION_COLUMN_NAME + ", "
				+ PICTURE_MAKE_COLUMN_NAME + ", " + PICTURE_MODEL_COLUMN_NAME + ", " + PICTURE_EXPOSURE_COLUMN_NAME
				+ ", " + PICTURE_RELATIVE_APERTURE_COLUMN_NAME + ", " + PICTURE_FOCAL_LENGTH_COLUMN_NAME + ", "
				+ PICTURE_FORMAT_COLUMN_NAME + " FROM " + PICTURE_TABLE_NAME + " WHERE " + PICTURE_ID_COLUMN_NAME
				+ " =?");
		_psLoadTagsOfPicture = _connection.prepareStatement("SELECT " + PICTURE_TAG_TAG_ID_COLUMN_NAME + " FROM "
				+ PICTURE_TAG_TABLE_NAME + " WHERE " + PICTURE_TAG_PICTURE_ID_COLUMN_NAME + " =?");
		_psLoadTag = _connection.prepareStatement("SELECT " + TAG_ID_COLUMN_NAME + ", " + TAG_NAME_COLUMN_NAME + ", "
				+ TAG_DESCRIPTION_COLUMN_NAME + ", " + TAG_PARENT_ID_COLUMN_NAME + ", " + TAG_SELECTABLE_COLUMN_NAME
				+ " FROM " + TAG_TABLE_NAME + " where " + TAG_ID_COLUMN_NAME + " =?");
		_psListPicture = _connection
				.prepareStatement("SELECT " + PICTURE_ID_COLUMN_NAME + " FROM " + PICTURE_TABLE_NAME);
		_psRemoveTag = _connection
				.prepareStatement("DELETE FROM " + TAG_TABLE_NAME + " WHERE " + TAG_ID_COLUMN_NAME + "=?");
		_psRemoveTagFromAllPictures = _connection.prepareStatement(
				"DELETE FROM " + PICTURE_TAG_TABLE_NAME + " WHERE " + PICTURE_TAG_TAG_ID_COLUMN_NAME + "=?");
		_psRemoveAllTagsForPicture = _connection.prepareStatement(
				"DELETE FROM " + PICTURE_TAG_TABLE_NAME + " WHERE " + PICTURE_TAG_PICTURE_ID_COLUMN_NAME + "=?");
		_psRemovePicture = _connection
				.prepareStatement("DELETE FROM " + PICTURE_TABLE_NAME + " WHERE " + PICTURE_ID_COLUMN_NAME + "=?");
	}

	/**
	 * Properly close the connection to the database.
	 * 
	 * @throws SQLException
	 *             if a error occurs during the closing of the database.
	 */
	public void close() throws SQLException
	{
		if (_psInsertTag != null)
		{
			_psInsertTag.close();
		}
		if (_psEditTag != null)
		{
			_psEditTag.close();
		}
		if (_psCountPicturesByTag != null)
		{
			_psCountPicturesByTag.close();
		}
		if (_psCountPictures != null)
		{
			_psCountPictures.close();
		}
		if (_psSelectPicturesByTag != null)
		{
			_psSelectPicturesByTag.close();
		}
		if (_psSelectPicturesByTag != null)
		{
			_psSelectPicturesByTag.close();
		}
		if (_psInsertPicture != null)
		{
			_psInsertPicture.close();
		}
		if (_psUpdatePictureMarkAndTimestamp != null)
		{
			_psUpdatePictureMarkAndTimestamp.close();
		}
		if (_psInsertTagForPicture != null)
		{
			_psInsertTagForPicture.close();
		}
		if (_psRemoveTagsForPicture != null)
		{
			_psRemoveTagsForPicture.close();
		}
		if (_psLoadPicture != null)
		{
			_psLoadPicture.close();
		}
		if (_psLoadTagsOfPicture != null)
		{
			_psLoadTagsOfPicture.close();
		}
		if (_psLoadTag != null)
		{
			_psLoadTag.close();
		}
		if (_psListPicture != null)
		{
			_psListPicture.close();
		}
		if (_psRemoveAllTagsForPicture != null)
		{
			_psRemoveAllTagsForPicture.close();
		}
		if (_psRemovePicture != null)
		{
			_psRemovePicture.close();
		}
		if (_connection != null)
		{
			_connection.close();
		}
	}

	/**
	 * Save the given {@link ITag} to the database.
	 * 
	 * @param tag
	 *            the {@link ITag} to save.
	 * @throws SQLException
	 *             if an SQL error occurred during the saving of the
	 *             {@link ITag} .
	 */
	public void saveTagToDatabase(final ITag tag) throws SQLException
	{
		synchronized (_psInsertTag)
		{
			_psInsertTag.clearParameters();
			_psInsertTag.setInt(1, tag.getTagId());
			_psInsertTag.setString(2, tag.getName());
			_psInsertTag.setString(3, tag.getDescription());
			if (tag.getParent() == null)
			{
				_psInsertTag.setInt(4, 0);
			}
			else
			{
				_psInsertTag.setInt(4, tag.getParentId());
			}
			_psInsertTag.setBoolean(5, tag.isSelectable());
			_psInsertTag.executeUpdate();
		}
	}

	/**
	 * Modify the given {@link ITag} into the database.
	 * 
	 * @param tag
	 *            the {@link ITag} to modify.
	 * @throws SQLException
	 *             if an SQL error occurred during the saving of the
	 *             {@link ITag} .
	 */
	public void modifyTagIntoDatabase(final ITag tag) throws SQLException
	{
		synchronized (_psEditTag)
		{
			_psEditTag.clearParameters();
			_psEditTag.setString(1, tag.getName());
			_psEditTag.setString(2, tag.getDescription());
			_psEditTag.setInt(3, tag.getParentId());
			_psEditTag.setBoolean(4, tag.isSelectable());
			_psEditTag.setInt(5, tag.getTagId());
			_psEditTag.executeUpdate();
		}
	}

	/**
	 * Create the tables of this database.
	 * 
	 * @throws SQLException
	 *             if an SQL error occurred during the creation of the tables.
	 */
	public void createTables() throws SQLException
	{
		Statement statement = null;
		try
		{
			statement = _connection.createStatement();
			// Tag table
			statement.executeUpdate("create table if not exists " + TAG_TABLE_NAME + " (" + TAG_ID_COLUMN_NAME
					+ " integer, " + TAG_NAME_COLUMN_NAME + " text, " + TAG_DESCRIPTION_COLUMN_NAME + " text, "
					+ TAG_PARENT_ID_COLUMN_NAME + " integer, " + TAG_SELECTABLE_COLUMN_NAME + " boolean)");
			// picture table
			statement.executeUpdate("create table if not exists " + PICTURE_TABLE_NAME + " (" + PICTURE_ID_COLUMN_NAME
					+ " text, " + PICTURE_GRADE_COLUMN_NAME + " integer, " + PICTURE_MODIFIED_TIMESTAMP_COLUMN_NAME
					+ " integer, " + PICTURE_ADDING_TIMESTAMP_COLUMN_NAME + " integer, " + PICTURE_ORIGINAL_NAME
					+ " text, " + PICTURE_WIDTH_COLUMN_NAME + " integer, " + PICTURE_HEIGTH_COLUMN_NAME + " integer, "
					+ PICTURE_CREATION_TIMESTAMP_COLUMN_NAME + " integer, " + PICTURE_ORIENTATION_COLUMN_NAME
					+ " integer, " + PICTURE_MAKE_COLUMN_NAME + " text, " + PICTURE_MODEL_COLUMN_NAME + " text, "
					+ PICTURE_EXPOSURE_COLUMN_NAME + " text, " + PICTURE_RELATIVE_APERTURE_COLUMN_NAME + " text, "
					+ PICTURE_FOCAL_LENGTH_COLUMN_NAME + " text, " + PICTURE_FORMAT_COLUMN_NAME + " text)");
			// picture_tag table
			statement.executeUpdate("create table if not exists " + PICTURE_TAG_TABLE_NAME + " ("
					+ PICTURE_TAG_TAG_ID_COLUMN_NAME + " integer, " + PICTURE_TAG_PICTURE_ID_COLUMN_NAME + " text)");
		}
		finally
		{
			if (statement != null)
			{
				statement.close();
			}
		}
	}

	/**
	 * Load the list of tags.
	 * 
	 * @return a {@link ResultSet} containing the list of tags.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public ResultSet loadTagList() throws SQLException
	{
		final Statement statement = _connection.createStatement();
		return statement.executeQuery("select " + TAG_ID_COLUMN_NAME + ", " + TAG_NAME_COLUMN_NAME + ", "
				+ TAG_DESCRIPTION_COLUMN_NAME + ", " + TAG_SELECTABLE_COLUMN_NAME + " from " + TAG_TABLE_NAME);
	}

	/**
	 * Load the list of tag id and there parent id.
	 * 
	 * @return a {@link ResultSet} containing the list of tag id and there
	 *         parent id.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public ResultSet loadParents() throws SQLException
	{
		final Statement statement = _connection.createStatement();
		return statement.executeQuery(
				"select " + TAG_ID_COLUMN_NAME + ", " + TAG_PARENT_ID_COLUMN_NAME + " from " + TAG_TABLE_NAME);
	}

	/**
	 * Insert a {@link FsPicture} in the database.
	 * 
	 * @param picture
	 *            the {@link FsPicture} to insert.
	 * @return true if the picture was correctly inserted.
	 * @throws SQLException
	 *             if an SQL error occurred during the insertion in the
	 *             database.
	 */
	public boolean insertPicture(final FsPicture picture) throws SQLException
	{
		synchronized (_psInsertPicture)
		{
			_psInsertPicture.clearParameters();
			_psInsertPicture.setString(1, picture.getId());
			_psInsertPicture.setInt(2, picture.getPictureGrade());
			_psInsertPicture.setLong(3, picture.getModifiedTimestamp());
			_psInsertPicture.setLong(4, picture.getAddingTimestamp());
			final PictureInformation info = picture.getPictureInformation();
			if (info != null)
			{
				_psInsertPicture.setString(5, info.getOriginalFileName());
				_psInsertPicture.setInt(6, info.getWidth());
				_psInsertPicture.setInt(7, info.getHeight());
				_psInsertPicture.setLong(8, info.getCreationTimestamp());
				_psInsertPicture.setInt(9, info.getOrientation());
				_psInsertPicture.setString(10, info.getMake());
				_psInsertPicture.setString(11, info.getModel());
				_psInsertPicture.setString(12, info.getExposureTime());
				_psInsertPicture.setString(13, info.getRelativeAperture());
				_psInsertPicture.setString(14, info.getFocalLength());
				_psInsertPicture.setString(15, info.getImageFormatString());
			}
			else
			{
				// TODO throw exception here
			}
			return _psInsertPicture.executeUpdate() > 0;
		}
	}

	/**
	 * Update the {@link ITag} list of a {@link FsPicture}.
	 * 
	 * @param picture
	 *            the {@link FsPicture} to update.
	 * @throws SQLException
	 *             if an SQL error occurred during the insertion in the
	 *             database.
	 */
	public void updateTags(final FsPicture picture) throws SQLException
	{
		synchronized (_psRemoveTagsForPicture)
		{
			_psRemoveTagsForPicture.clearParameters();
			_psRemoveTagsForPicture.setString(1, picture.getId());
			_psRemoveTagsForPicture.execute();
		}
		synchronized (_psInsertTagForPicture)
		{
			for (final ITag tag : picture.getTagSet())
			{
				_psInsertTagForPicture.clearParameters();
				_psInsertTagForPicture.setInt(1, tag.getTagId());
				_psInsertTagForPicture.setString(2, picture.getId());
				_psInsertTagForPicture.executeUpdate();
			}
		}
	}

	/**
	 * Update the meta-informations of the specified picture.
	 * 
	 * @param picture
	 *            the {@link FsPicture} to update.
	 * @throws SQLException
	 *             if an SQL error occurred during the insertion in the
	 *             database.
	 */
	public void updatePicture(final FsPicture picture) throws SQLException
	{
		synchronized (_psUpdatePictureMarkAndTimestamp)
		{
			_psUpdatePictureMarkAndTimestamp.clearParameters();
			_psUpdatePictureMarkAndTimestamp.setInt(1, picture.getPictureGrade());
			_psUpdatePictureMarkAndTimestamp.setLong(2, picture.getModifiedTimestamp());
			_psUpdatePictureMarkAndTimestamp.setString(3, picture.getId());
			_psUpdatePictureMarkAndTimestamp.executeUpdate();
		}
		updateTags(picture);
	}

	/**
	 * Count the number of pictures which have a given {@link ITag}.
	 * 
	 * @param tag
	 *            the {@link ITag} to consider.
	 * @return the number of pictures which have a given {@link ITag}.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public int countPictures(final ITag tag) throws SQLException
	{
		synchronized (_psCountPicturesByTag)
		{
			_psCountPicturesByTag.clearParameters();
			_psCountPicturesByTag.setInt(1, tag.getTagId());
			ResultSet res = null;
			try
			{
				res = _psCountPicturesByTag.executeQuery();
				if (res.next())
				{
					return res.getInt(PICTURE_TAG_PICTURE_ID_COLUMN_NAME);
				}
				return 0;
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

	/**
	 * Count the total number of pictures.
	 * 
	 * @return the total number of pictures.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public int countPictures() throws SQLException
	{
		synchronized (_psCountPictures)
		{
			ResultSet res = null;
			try
			{
				res = _psCountPictures.executeQuery();
				if (res.next())
				{
					return res.getInt(PICTURE_ID_COLUMN_NAME);
				}
				return 0;
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

	/**
	 * Select the list of pictures which have a given {@link ITag}.
	 * 
	 * @param tag
	 *            the {@link ITag} to consider.
	 * @return the list of pictures which have a given {@link ITag}.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public String[] selectPictures(final ITag tag) throws SQLException
	{
		final LinkedList<String> pictureList = new LinkedList<>();
		synchronized (_psSelectPicturesByTag)
		{
			_psSelectPicturesByTag.clearParameters();
			_psSelectPicturesByTag.setInt(1, tag.getTagId());
			ResultSet response = null;
			try
			{
				response = _psSelectPicturesByTag.executeQuery();
				while (response.next())
				{
					pictureList.add(response.getString(PICTURE_TAG_PICTURE_ID_COLUMN_NAME));
				}
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}
		}
		final String[] res = new String[pictureList.size()];
		return pictureList.toArray(res);
	}

	/**
	 * Load the information about the specified picture.
	 * 
	 * @param strPictureId
	 *            the id of the picture.
	 * @return the information about the specified picture.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public ResultSet loadPicture(final String strPictureId) throws SQLException
	{
		synchronized (_psLoadPicture)
		{
			_psLoadPicture.clearParameters();
			_psLoadPicture.setString(1, strPictureId);
			return _psLoadPicture.executeQuery();
		}
	}

	/**
	 * Load all the {@link ITag}s of an {@link IPicture}.
	 * 
	 * @param strPictureId
	 *            the id of the picture.
	 * @return the list of {@link ITag}s of the picture.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public Integer[] loadTagsOfPicture(final String strPictureId) throws SQLException
	{
		final LinkedList<Integer> tagList = new LinkedList<>();
		synchronized (_psLoadTagsOfPicture)
		{
			_psLoadTagsOfPicture.clearParameters();
			_psLoadTagsOfPicture.setString(1, strPictureId);
			ResultSet response = null;
			try
			{
				response = _psLoadTagsOfPicture.executeQuery();
				while (response.next())
				{
					tagList.add(Integer.valueOf(response.getInt(PICTURE_TAG_TAG_ID_COLUMN_NAME)));
				}
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}
		}
		final Integer[] res = new Integer[tagList.size()];
		return tagList.toArray(res);
	}

	/**
	 * Load the information about the specified {@link ITag}.
	 * 
	 * @param iTagId
	 *            the id of the {@link ITag}.
	 * @return the information about the specified {@link ITag}.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public ResultSet loadTag(final int iTagId) throws SQLException
	{
		synchronized (_psLoadTag)
		{
			_psLoadTag.clearParameters();
			_psLoadTag.setInt(1, iTagId);
			return _psLoadTag.executeQuery();
		}
	}

	/**
	 * Load the list of {@link IPicture} of this {@link IPictureBank}.
	 * 
	 * @return the
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public ResultSet loadPictureList() throws SQLException
	{
		synchronized (_psListPicture)
		{
			return _psListPicture.executeQuery();
		}
	}

	/**
	 * Remove the specified {@link ITag} from the database.
	 * 
	 * @param iTagId
	 *            the id of the {@link ITag} to remove.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public void removeTag(final int iTagId) throws SQLException
	{
		synchronized (_psRemoveTagFromAllPictures)
		{
			_psRemoveTagFromAllPictures.clearParameters();
			_psRemoveTagFromAllPictures.setInt(1, iTagId);
			_psRemoveTagFromAllPictures.execute();
		}
		synchronized (_psRemoveTag)
		{
			_psRemoveTag.clearParameters();
			_psRemoveTag.setInt(1, iTagId);
			_psRemoveTag.execute();
		}
	}

	/**
	 * Remove the specified {@link IPicture} from the database.
	 * 
	 * @param strPictureId
	 *            the id of the {@link IPicture} to remove.
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	public void removePicture(final String strPictureId) throws SQLException
	{
		synchronized (_psRemoveAllTagsForPicture)
		{
			_psRemoveAllTagsForPicture.clearParameters();
			_psRemoveAllTagsForPicture.setString(1, strPictureId);
			_psRemoveAllTagsForPicture.execute();
		}
		synchronized (_psRemovePicture)
		{
			_psRemovePicture.clearParameters();
			_psRemovePicture.setString(1, strPictureId);
			_psRemovePicture.execute();
		}
	}
}
