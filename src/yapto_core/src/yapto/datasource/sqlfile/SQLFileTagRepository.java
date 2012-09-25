package yapto.datasource.sqlfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.datasource.sqlfile.config.ISQLFileDataSourceConfiguration;
import yapto.datasource.tag.IWritableTagRepository;
import yapto.datasource.tag.Tag;
import yapto.datasource.tag.TagAddException;
import yapto.datasource.tag.TagAddExceptionType;

/**
 * Implementation of the IWritableTagRepository for use with
 * {@link SQLFileDataSource}.
 * 
 * @author benobiwan
 * 
 */
public final class SQLFileTagRepository implements IWritableTagRepository
{
	/**
	 * Logger object.
	 */
	protected static transient final Logger LOGGER = LoggerFactory
			.getLogger(SQLFileTagRepository.class);

	/**
	 * Configuration for this {@link SQLFileDataSource}.
	 */
	private final ISQLFileDataSourceConfiguration _conf;

	/**
	 * Object holding the connection to the database and the prepared
	 * statements.
	 */
	private final SQLFileListConnection _fileListConnection;

	/**
	 * Set containing all the {@link Tag}s.
	 */
	private final Set<Tag> _tagSet = new HashSet<>();

	/**
	 * Map containing all the {@link Tag}s ordered by {@link Tag} id.
	 */
	private final Map<Integer, Tag> _tagIdMap = new HashMap<>();

	/**
	 * Map containing all the {@link Tag}s ordered by {@link Tag} name.
	 */
	private final Map<String, Tag> _tagNameMap = new HashMap<>();

	/**
	 * Id given to the next tag added to this datasource.
	 */
	private int _iNextTagId = 0;

	/**
	 * Log protecting the access to the next tag id.
	 */
	private final Object _lockNextTag = new Object();

	/**
	 * Creates a new SQLFileTagRepository.
	 * 
	 * @param conf
	 *            configuration of the parent {@link SQLFileDataSource}.
	 * @param fileListConnection
	 *            object holding the connection to the database and the prepared
	 *            statements.
	 * @throws SQLException
	 *             if an SQL error occurred during the loading of the tags from
	 *             the database.
	 */
	public SQLFileTagRepository(final ISQLFileDataSourceConfiguration conf,
			final SQLFileListConnection fileListConnection) throws SQLException
	{
		_conf = conf;
		_fileListConnection = fileListConnection;
		loadTags();
	}

	@Override
	public Set<Tag> getTagSet()
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
	public void addTag(final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException
	{
		addTag(null, strName, strDescription, bSelectable);
	}

	@Override
	public void addTag(final Tag parent, final String strName,
			final String strDescription, final boolean bSelectable)
			throws TagAddException
	{
		if (_tagNameMap.containsKey(strName))
		{
			throw new TagAddException(strName,
					TagAddExceptionType.DUPLICATE_TAG_NAME);
		}
		if (_iNextTagId == Integer.MAX_VALUE)
		{
			throw new TagAddException(TagAddExceptionType.NO_MORE_IDS);
		}
		// TODO add a Pattern for better control of the tag name
		if (strName.equals(""))
		{
			throw new TagAddException(strName,
					TagAddExceptionType.MALFORMED_TAG_NAME);

		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Adding tag named: " + strName);
		}
		Tag newTag;
		if (parent == null)
		{
			newTag = new Tag(_conf.getDataSourceId(), _iNextTagId, strName,
					strDescription, bSelectable);
		}
		else
		{
			newTag = new Tag(_conf.getDataSourceId(), _iNextTagId, parent,
					strName, strDescription, bSelectable);
		}
		try
		{
			_fileListConnection.saveTagToDatabase(newTag);
			_tagSet.add(newTag);
			_tagIdMap.put(Integer.valueOf(_iNextTagId), newTag);
			_tagNameMap.put(strName, newTag);
			_iNextTagId++;
		}
		catch (final SQLException e)
		{
			throw new TagAddException(TagAddExceptionType.SQL_INSERT_ERROR, e);
		}
	}

	@Override
	public boolean hasTagNamed(final String strName)
	{
		return _tagNameMap.containsKey(strName);
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
		ResultSet resLoad = null;
		ResultSet resParent = null;
		Tag tag = null;
		Tag parentTag = null;
		try
		{
			synchronized (_lockNextTag)
			{
				resLoad = _fileListConnection.loadTagList();
				while (resLoad.next())
				{
					final int iTagId = resLoad
							.getInt(SQLFileListConnection.TAG_ID_COLUMN_NAME);
					final String strName = resLoad
							.getString(SQLFileListConnection.TAG_NAME_COLUMN_NAME);
					final String strDescription = resLoad
							.getString(SQLFileListConnection.TAG_DESCRIPTION_COLUMN_NAME);
					final boolean bSelectable = resLoad
							.getBoolean(SQLFileListConnection.TAG_SELECTABLE_COLUMN_NAME);
					tag = new Tag(_conf.getDataSourceId(), iTagId, strName,
							strDescription, bSelectable);
					_tagSet.add(tag);
					_tagIdMap.put(Integer.valueOf(iTagId), tag);
					_tagNameMap.put(tag.getName(), tag);
					if (iTagId > _iNextTagId)
					{
						_iNextTagId = iTagId + 1;
					}
				}
			}
		}
		finally
		{
			if (resLoad != null)
			{
				resLoad.close();
			}
		}
		try
		{
			resParent = _fileListConnection.loadParents();
			while (resParent.next())
			{
				final Integer iId = Integer.valueOf(resParent
						.getInt(SQLFileListConnection.TAG_ID_COLUMN_NAME));
				final Integer iParentId = Integer
						.valueOf(resParent
								.getInt(SQLFileListConnection.TAG_PARENT_ID_COLUMN_NAME));
				tag = _tagIdMap.get(iId);
				parentTag = _tagIdMap.get(iParentId);
				if (tag != null && parentTag != null)
				{
					tag.setParent(parentTag);
				}
			}
		}
		finally
		{
			if (resParent != null)
			{
				resParent.close();
			}
		}
	}

	@Override
	public void editTag(final int iTagId, final Tag parent,
			final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Tag get(final int iTagId)
	{

		return _tagIdMap.get(Integer.valueOf(iTagId));
	}

	@Override
	public Tag get(final Integer iTagId)
	{
		return _tagIdMap.get(iTagId);
	}

	@Override
	public Tag get(final String strTagName)
	{
		return _tagNameMap.get(strTagName);
	}
}
