package yapto.datasource.sqlfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import yapto.datasource.IDataSource;
import yapto.datasource.IPicture;
import yapto.datasource.IPictureFilter;
import yapto.datasource.IPictureList;
import yapto.datasource.OperationNotSupportedException;
import yapto.datasource.sqlfile.config.ISQLFileDataSourceConfiguration;
import yapto.datasource.tag.Tag;

/**
 * {@link IDataSource} using an SQLite file to stock the meta-informations, and
 * a standard filesystem for the pictures.
 * 
 * @author benobiwan
 * 
 */
public class SQLFileDataSource implements IDataSource
{
	/**
	 * Name for the 'tag' table.
	 */
	private static final String TAG_TABLE_NAME = "tag";

	/**
	 * Name for the 'id' column of the 'tag' table.
	 */
	private static final String TAG_ID_COLUMN_NAME = "id";

	/**
	 * Name for the 'name' column of the 'tag' table.
	 */
	private static final String TAG_NAME_COLUMN_NAME = "name";

	/**
	 * Name for the 'description' column of the 'tag' table.
	 */
	private static final String TAG_DESCRIPTION_COLUMN_NAME = "description";

	/**
	 * Name for the 'parentId' column of the 'tag' table.
	 */
	private static final String TAG_PARENT_ID_COLUMN_NAME = "parentId";

	/**
	 * Name for the 'selectable' column of the 'tag' table.
	 */
	private static final String TAG_SELECTABLE_COLUMN_NAME = "selectable";

	/**
	 * Connection to the database.
	 */
	private final Connection _connection;

	/**
	 * Statement to insert a {@link Tag}.
	 */
	private final PreparedStatement _psInsertTag;

	/**
	 * Set containing all the {@link Tag}s.
	 */
	private final Set<Tag> _tagSet = new TreeSet<Tag>();

	/**
	 * Map containing all the {@link Tag}s.
	 */
	private final Map<Integer, Tag> _tagMap = new TreeMap<Integer, Tag>();

	/**
	 * Configuration for this {@link SQLFileDataSource}.
	 */
	private final ISQLFileDataSourceConfiguration _conf;

	/**
	 * Creates a new SQLFileDataSource.
	 * 
	 * @param conf
	 *            configuration for this {@link SQLFileDataSource}.
	 * @throws SQLException
	 *             if an SQL error occurred during the connection to the
	 *             database.
	 * @throws ClassNotFoundException
	 *             if the database driver class can't be found.
	 */
	public SQLFileDataSource(final ISQLFileDataSourceConfiguration conf)
			throws SQLException, ClassNotFoundException
	{
		_conf = conf;
		Class.forName("org.sqlite.JDBC");

		_connection = DriverManager.getConnection("jdbc:sqlite:"
				+ _conf.getDatabaseFileName());
		_psInsertTag = _connection.prepareStatement("insert into "
				+ TAG_TABLE_NAME + " (" + TAG_ID_COLUMN_NAME + ", "
				+ TAG_NAME_COLUMN_NAME + ", " + TAG_DESCRIPTION_COLUMN_NAME
				+ ", " + TAG_PARENT_ID_COLUMN_NAME + ", "
				+ TAG_SELECTABLE_COLUMN_NAME + ") values(?, ?, ?, ?, ?)");
	}

	@Override
	public int getPictureCount() throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IPictureList filterList(final IPictureFilter filter)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPictureList mergeList(final IPictureList otherList)
			throws OperationNotSupportedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPictureList> getParent() throws OperationNotSupportedException
	{
		// no parent for a datasource.
		return null;
	}

	@Override
	public Set<Tag> getTagSet() throws OperationNotSupportedException
	{
		return Collections.unmodifiableSet(_tagSet);
	}

	@Override
	public Tag getRootTag()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPicture> getPictureList()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPicture(final File picturePath)
			throws OperationNotSupportedException, FileNotFoundException,
			IOException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void addTag(final Tag newTag)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getId()
	{
		return _conf.getDataSourceId();
	}

	/**
	 * Create the {@link Tag} database.
	 * 
	 * @throws SQLException
	 *             if an SQL error occurred during the creation of the table.
	 */
	private void createTagTable() throws SQLException
	{
		final Statement statement = _connection.createStatement();
		statement.executeUpdate("create table " + TAG_TABLE_NAME + " ("
				+ TAG_ID_COLUMN_NAME + " integer, " + TAG_NAME_COLUMN_NAME
				+ " text, " + TAG_DESCRIPTION_COLUMN_NAME + " text, "
				+ TAG_PARENT_ID_COLUMN_NAME + " integer, "
				+ TAG_SELECTABLE_COLUMN_NAME + " boolean)");
	}

	/**
	 * Save the given {@link Tag} to the database.
	 * 
	 * @param tag
	 *            the {@link Tag} to save.
	 * @throws SQLException
	 *             if an SQL error occurred during the saving of the {@link Tag}
	 *             .
	 */
	private void saveTagToDatabase(final Tag tag) throws SQLException
	{
		_psInsertTag.setInt(1, tag.getTagId());
		_psInsertTag.setString(2, tag.getName());
		_psInsertTag.setString(3, tag.getDescription());
		if (tag.getParent() == null)
		{
			_psInsertTag.setInt(4, -1);
		}
		else
		{
			_psInsertTag.setInt(4, tag.getParent().getTagId());
		}
		_psInsertTag.setBoolean(5, tag.isSelectable());
		_psInsertTag.executeUpdate();
	}

	/**
	 * Load {@link Tag}s from the database.
	 * 
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	private void loadTags() throws SQLException
	{
		final Statement statementLoad = _connection.createStatement();
		final ResultSet resLoad = statementLoad.executeQuery("select "
				+ TAG_ID_COLUMN_NAME + ", " + TAG_NAME_COLUMN_NAME + ", "
				+ TAG_DESCRIPTION_COLUMN_NAME + ", "
				+ TAG_SELECTABLE_COLUMN_NAME + " from " + TAG_TABLE_NAME);
		Tag tag = null;
		Tag parentTag = null;
		while (resLoad.next())
		{
			final int iTagId = resLoad.getInt(TAG_ID_COLUMN_NAME);
			final String strName = resLoad.getString(TAG_NAME_COLUMN_NAME);
			final String strDescription = resLoad
					.getString(TAG_DESCRIPTION_COLUMN_NAME);
			final boolean bSelectable = resLoad
					.getBoolean(TAG_SELECTABLE_COLUMN_NAME);
			tag = new Tag(getId(), iTagId, strName, strDescription, bSelectable);
			_tagSet.add(tag);
			_tagMap.put(Integer.valueOf(iTagId), tag);
		}
		final Statement statementParent = _connection.createStatement();
		final ResultSet resParent = statementParent.executeQuery("select "
				+ TAG_ID_COLUMN_NAME + ", " + TAG_PARENT_ID_COLUMN_NAME
				+ " from " + TAG_TABLE_NAME);
		while (resParent.next())
		{
			final Integer iId = Integer.valueOf(resLoad
					.getInt(TAG_ID_COLUMN_NAME));
			final Integer iParentId = Integer.valueOf(resLoad
					.getInt(TAG_PARENT_ID_COLUMN_NAME));
			tag = _tagMap.get(iId);
			parentTag = _tagMap.get(iParentId);
			if (tag != null && parentTag != null)
			{
				tag.setParent(parentTag);
			}
		}
	}
}
