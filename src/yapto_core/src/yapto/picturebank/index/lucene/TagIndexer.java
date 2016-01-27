package yapto.picturebank.index.lucene;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import yapto.picturebank.index.query.PictureQueryBuilder;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;
import yapto.picturebank.tag.ITag;

/**
 * Object used to interact with the lucene index for tags.
 * 
 * @author benobiwan
 * 
 */
public final class TagIndexer
{
	/**
	 * Name of the field used to index the name of the tag.
	 */
	public static final String ID_INDEX_FIELD = "id";

	/**
	 * Name of the field used to index the name of the tag.
	 */
	public static final String NAME_INDEX_FIELD = "name";

	/**
	 * Name of the field used to index the description of the tag.
	 */
	public static final String DESCRIPTION_INDEX_FIELD = "dscription";

	/**
	 * The configuration.
	 */
	private final ISQLFilePictureBankConfiguration _conf;

	/**
	 * {@link IndexWriter} used to write the indexes.
	 */
	private final IndexWriter _indexWriter;

	/**
	 * Object used to protect the access to the reader.
	 */
	private final Object _readerLock = new Object();

	/**
	 * {@link IndexReader} used to write the indexes.
	 */
	private IndexReader _indexReader;

	/**
	 * {@link IndexSearcher} used to search the indexes.
	 */
	private IndexSearcher _indexSearcher;

	/**
	 * Boolean telling whether the {@link IndexReader} needs to be updated or
	 * not.
	 */
	private boolean _bReaderNeedsUpdate;

	/**
	 * Creates a new {@link TagIndexer}.
	 * 
	 * @param conf
	 *            the configuration.
	 * @throws IOException
	 */
	public TagIndexer(final ISQLFilePictureBankConfiguration conf)
			throws IOException
	{
		_conf = conf;

		final Directory dir = FSDirectory.open(FileSystems.getDefault().getPath(_conf.getIndexDirectory(), "tag"));

		// index writer configuration
		final IndexWriterConfig iwConf = new IndexWriterConfig(null);
		iwConf.setOpenMode(OpenMode.CREATE_OR_APPEND);

		_indexWriter = new IndexWriter(dir, iwConf);

		synchronized (_readerLock)
		{
			_indexReader = DirectoryReader.open(_indexWriter, true);
			_indexSearcher = new IndexSearcher(_indexReader);
			_bReaderNeedsUpdate = false;
		}
	}

	/**
	 * Index the specified (@link IPicture}.
	 * 
	 * @param tag
	 *            the specified (@link ITag}.
	 * @throws IOException
	 *             if there is an I/O error while writing the index.
	 */
	public void indexTag(final ITag tag) throws IOException
	{
		final Term currDoc = new Term(ID_INDEX_FIELD, tag.getTagIdAsString());
		final Document doc = createDocument(tag);

		_indexWriter.updateDocument(currDoc, doc);
		synchronized (_readerLock)
		{
			_bReaderNeedsUpdate = true;
		}
	}

	/**
	 * Create the lucene {@link Document} for the specified (@link ITag}.
	 * 
	 * @param tag
	 *            the specified (@link ITag}.
	 * @return the newly created lucene {@link Document}.
	 */
	private Document createDocument(final ITag tag)
	{
		final Document doc = new Document();
		// id
		doc.add(new StringField(ID_INDEX_FIELD, tag.getTagIdAsString(),
				Field.Store.YES));
		// name
		doc.add(new StringField(NAME_INDEX_FIELD, tag.getName(),
				Field.Store.YES));
		// description
		doc.add(new StringField(DESCRIPTION_INDEX_FIELD, tag.getDescription(),
				Field.Store.NO));
		return doc;
	}

	/**
	 * Remove an {@link ITag} from the index
	 * 
	 * @param tag
	 *            the {@link ITag} to remove.
	 * @throws IOException
	 *             if an error occurs while deleting the {@link ITag} from the
	 *             index.
	 */
	public void unindexTag(final ITag tag) throws IOException
	{
		final Term currDoc = new Term(NAME_INDEX_FIELD, tag.getName());
		_indexWriter.deleteDocuments(currDoc);
	}

	/**
	 * Closes the {@link IndexWriter}.
	 * 
	 * @throws CorruptIndexException
	 *             if the index is corrupted.
	 * @throws IOException
	 *             if there is an I/O error while writing the index.
	 */
	public void close() throws CorruptIndexException, IOException
	{
		_indexWriter.close();
		synchronized (_readerLock)
		{
			_indexReader.close();
		}
	}

	/**
	 * Search tag id corresponding to the specified query.
	 * 
	 * @param query
	 *            the query.
	 * @param iLimit
	 *            maximum number of result.
	 * @return a {@link List} of the matching tag id.
	 * @throws IOException
	 *             if an error occurs during the search.
	 */
	public List<String> searchTag(final Query query, final int iLimit)
			throws IOException
	{
		synchronized (_readerLock)
		{
			if (_bReaderNeedsUpdate)
			{
				_indexReader.close();
				_indexReader = DirectoryReader.open(_indexWriter, true);
				_indexSearcher = new IndexSearcher(_indexReader);
				_bReaderNeedsUpdate = false;
			}
			final ScoreDoc[] searchResult = _indexSearcher.search(query,
					iLimit, PictureQueryBuilder._dateSort).scoreDocs;
			final ArrayList<String> result = new ArrayList<>();
			result.ensureCapacity(searchResult.length);
			for (final ScoreDoc scoreDoc : searchResult)
			{
				final String strTagId = _indexReader.document(scoreDoc.doc)
						.get(ID_INDEX_FIELD);
				result.add(strTagId);
			}
			return result;
		}
	}
}
