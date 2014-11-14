package yapto.picturebank.sqlfile;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.picturebank.index.lucene.TagIndexer;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;
import yapto.picturebank.tag.EditableTag;
import yapto.picturebank.tag.ITag;
import yapto.picturebank.tag.IWritableTagRepository;
import yapto.picturebank.tag.RecentlyUsedTagSet;
import yapto.picturebank.tag.TagAddException;
import yapto.picturebank.tag.TagAddExceptionType;
import yapto.picturebank.tag.TagRepositoryChangedEvent;
import yapto.picturebank.tag.UneditableTag;

import com.google.common.eventbus.EventBus;

/**
 * Implementation of the IWritableTagRepository for use with
 * {@link SQLFilePictureBank}.
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
	 * Configuration for the {@link SQLFilePictureBank}.
	 */
	private final ISQLFilePictureBankConfiguration _conf;

	/**
	 * Object holding the connection to the database and the prepared
	 * statements.
	 */
	private final SQLFileListConnection _fileListConnection;

	/**
	 * Set containing all the {@link ITag}s.
	 */
	private final Set<ITag> _tagSet = new TreeSet<>();

	/**
	 * Map containing all the {@link ITag}s ordered by {@link ITag} id.
	 */
	private final Map<Integer, ITag> _tagIdMap = new HashMap<>();

	/**
	 * Map containing all the {@link ITag}s ordered by {@link ITag} name.
	 */
	private final Map<String, ITag> _tagNameMap = new HashMap<>();

	/**
	 * Id given to the next tag added to this {@link SQLFilePictureBank}.
	 */
	private int _iNextTagId = 1;

	/**
	 * Log protecting the access to the next tag id.
	 */
	private final Object _lockNextTag = new Object();

	/**
	 * {@link EventBus} used to signal registered objects of changes in this
	 * {@link SQLFileTagRepository}.
	 */
	private final EventBus _bus;

	private final RecentlyUsedTagSet _recentlyUsedTagSet;

	/**
	 * Object used to index {@link ITag}s.
	 */
	private final TagIndexer _tagIndexer;

	/**
	 * Creates a new SQLFileTagRepository.
	 * 
	 * @param conf
	 *            configuration of the parent {@link SQLFilePictureBank}.
	 * @param fileListConnection
	 *            object holding the connection to the database and the prepared
	 *            statements.
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in this {@link SQLFileTagRepository}.
	 * @throws SQLException
	 *             if an SQL error occurred during the loading of the tags from
	 *             the database.
	 * @throws IOException
	 */
	public SQLFileTagRepository(final ISQLFilePictureBankConfiguration conf,
			final SQLFileListConnection fileListConnection, final EventBus bus)
			throws SQLException, IOException
	{
		_conf = conf;
		_fileListConnection = fileListConnection;
		_bus = bus;
		_recentlyUsedTagSet = new RecentlyUsedTagSet(conf.getTagHistorySize());
		_tagIndexer = new TagIndexer(_conf);
		final ITag rootTag = new UneditableTag(conf.getPictureBankId(), this,
				0, -1, "/", "Root tag.", false);
		_tagSet.add(rootTag);
		_tagIdMap.put(Integer.valueOf(rootTag.getTagId()), rootTag);
		_tagNameMap.put(rootTag.getName(), rootTag);
		loadTags();
	}

	@Override
	public Set<ITag> getTagSet()
	{
		return Collections.unmodifiableSet(_tagSet);
	}

	@Override
	public ITag getRootTag()
	{
		return _tagIdMap.get(Integer.valueOf(0));
	}

	@Override
	public void addTag(final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException
	{
		addTag(null, strName, strDescription, bSelectable);
	}

	@Override
	public void addTag(final int iParentId, final String strName,
			final String strDescription, final boolean bSelectable)
			throws TagAddException
	{
		addTag(getTag(iParentId), strName, strDescription, bSelectable);
	}

	@Override
	public void addTag(final ITag parent, final String strName,
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
		ITag newTag;
		if (parent == null)
		{
			newTag = new EditableTag(_conf.getPictureBankId(), this,
					_iNextTagId, strName, strDescription, bSelectable);
		}
		else
		{
			newTag = new EditableTag(_conf.getPictureBankId(), this,
					_iNextTagId, parent.getTagId(), strName, strDescription,
					bSelectable);
		}
		try
		{
			_fileListConnection.saveTagToDatabase(newTag);
			_tagSet.add(newTag);
			_tagIdMap.put(Integer.valueOf(_iNextTagId), newTag);
			_tagNameMap.put(strName, newTag);
			final ITag parentTag = _tagIdMap.get(Integer.valueOf(newTag
					.getParentId()));
			if (parentTag != null)
			{
				parentTag.addChild(newTag);
			}
			else
			{
				_tagIdMap.get(Integer.valueOf(0)).addChild(newTag);
			}
			_iNextTagId++;
			_tagIndexer.indexTag(newTag);
			_bus.post(new TagRepositoryChangedEvent());
		}
		catch (final SQLException e)
		{
			throw new TagAddException(TagAddExceptionType.SQL_INSERT_ERROR, e);
		}
		catch (IOException e)
		{
			throw new TagAddException(TagAddExceptionType.TAG_INDEXING_ERROR, e);
		}
	}

	@Override
	public boolean hasTagNamed(final String strName)
	{
		return _tagNameMap.containsKey(strName);
	}

	/**
	 * Load {@link ITag}s from the database.
	 * 
	 * @throws SQLException
	 *             if an SQL error occurred during the interrogation of the
	 *             database.
	 */
	private void loadTags() throws SQLException
	{
		ResultSet resLoad = null;
		ResultSet resParent = null;
		ITag tag = null;
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
					tag = new EditableTag(_conf.getPictureBankId(), this,
							iTagId, strName, strDescription, bSelectable);
					_tagSet.add(tag);
					_tagIdMap.put(Integer.valueOf(iTagId), tag);
					_tagNameMap.put(tag.getName(), tag);
					if (iTagId >= _iNextTagId)
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
				final int iParentId = resParent
						.getInt(SQLFileListConnection.TAG_PARENT_ID_COLUMN_NAME);
				tag = _tagIdMap.get(iId);
				tag.setParent(iParentId);
				final ITag parentTag = _tagIdMap
						.get(Integer.valueOf(iParentId));
				if (parentTag != null)
				{
					parentTag.addChild(tag);
				}
				else
				{
					_tagIdMap.get(Integer.valueOf(0)).addChild(tag);
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
	public void editTag(final int iTagId, final int iParentId,
			final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException
	{
		editTag(iTagId, getTag(iParentId), strName, strDescription, bSelectable);
	}

	@Override
	public void editTag(final int iTagId, final ITag parent,
			final String strName, final String strDescription,
			final boolean bSelectable) throws TagAddException
	{
		final ITag currentTag = _tagIdMap.get(Integer.valueOf(iTagId));
		final boolean bSameName = (currentTag.getName() != null)
				&& currentTag.getName().equals(strName);
		final boolean bSameDescription = (currentTag.getDescription() != null)
				&& currentTag.getDescription().equals(strDescription);
		final boolean bSameSelectableStatus = (currentTag.isSelectable() == bSelectable);
		final boolean bSameParent = (currentTag.getParentId() == parent
				.getTagId());
		// check if another tag has the same name
		if (!bSameName && _tagNameMap.containsKey(strName))
		{
			throw new TagAddException(TagAddExceptionType.DUPLICATE_TAG_NAME);
		}
		if (currentTag instanceof EditableTag)
		{
			final EditableTag editTag = (EditableTag) currentTag;
			if (!bSameName)
			{
				_tagNameMap.remove(editTag.getName());
				editTag.setName(strName);
				_tagNameMap.put(strName, editTag);
			}
			if (!bSameDescription)
			{
				editTag.setDescription(strDescription);
			}
			if (!bSameSelectableStatus)
			{
				editTag.setSelectable(bSelectable);
			}
			if (!bSameParent)
			{
				editTag.getParent().removeChild(editTag);
				editTag.setParent(parent.getParentId());
			}
			if (!bSameName || !bSameDescription || !bSameSelectableStatus
					|| !bSameParent)
			{
				try
				{
					_fileListConnection.modifyTagIntoDatabase(editTag);
					_tagIndexer.indexTag(editTag);
					_bus.post(new TagRepositoryChangedEvent());
				}
				catch (final SQLException e)
				{
					throw new TagAddException(
							TagAddExceptionType.SQL_INSERT_ERROR, e);
				}
				catch (IOException e)
				{
					throw new TagAddException(
							TagAddExceptionType.TAG_INDEXING_ERROR, e);
				}
			}
		}
	}

	@Override
	public ITag getTag(final int iTagId)
	{

		return _tagIdMap.get(Integer.valueOf(iTagId));
	}

	@Override
	public ITag getTag(final Integer iTagId)
	{
		return _tagIdMap.get(iTagId);
	}

	@Override
	public ITag getTag(final String strTagName)
	{
		return _tagNameMap.get(strTagName);
	}

	@Override
	public void removeTag(final int iTagId) throws TagAddException
	{
		if (iTagId <= 0)
		{
			throw new TagAddException(TagAddExceptionType.ILLEGAL_TAG_ID);
		}
		final ITag tagToRemove = _tagIdMap.get(Integer.valueOf(iTagId));
		if (tagToRemove != null)
		{
			// parent of children changed to root tag
			for (final ITag tag : tagToRemove.getChildren())
			{
				tag.setParent(0);
			}
			// remove tag from repository
			_tagSet.remove(tagToRemove);
			ITag remTag = _tagIdMap.remove(Integer.valueOf(iTagId));
			_tagNameMap.remove(tagToRemove.getName());
			// remove tag from database
			try
			{
				_fileListConnection.removeTag(iTagId);
				_tagIndexer.unindexTag(remTag);
			}
			catch (final SQLException e)
			{
				throw new TagAddException(
						TagAddExceptionType.SQL_REMOVAL_ERROR, e);
			}
			catch (IOException e)
			{
				throw new TagAddException(
						TagAddExceptionType.TAG_INDEXING_ERROR, e);
			}
		}
	}

	@Override
	public SortedSet<ITag> getRecentlyUsed()
	{
		return _recentlyUsedTagSet.getAlphabeticTagSet();
	}

	@Override
	public void addLastUsed(final ITag tag)
	{
		_recentlyUsedTagSet.addLastUsed(tag);
	}

	@Override
	public void close() throws IOException
	{
		_tagIndexer.close();
	}
}
