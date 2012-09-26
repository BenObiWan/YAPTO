package yapto.datasource.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import yapto.datasource.IPicture;
import yapto.datasource.PictureInformation;
import yapto.datasource.sqlfile.config.ISQLFileDataSourceConfiguration;
import yapto.datasource.tag.ITag;

/**
 * Object used to interact with the lucene index.
 * 
 * @author benobiwan
 * 
 */
public final class PictureIndexer
{
	/**
	 * Name of the field used to index the name of the picture.
	 */
	public static final String ID_INDEX_FIELD = "picture_id";

	/**
	 * Name of the field used to index the grade of the picture.
	 */
	public static final String GRADE_INDEX_FIELD = "picture_grade";

	/**
	 * Name of the field used to index the orientation of the picture.
	 */
	public static final String ORIENTATION_INDEX_FIELD = "picture_orientation";

	/**
	 * Name of the field used to index the exif 'make' of the picture.
	 */
	public static final String MAKE_INDEX_FIELD = "picture_make";

	/**
	 * Name of the field used to index the exif 'model' of the picture.
	 */
	public static final String MODEL_INDEX_FIELD = "picture_model";

	/**
	 * Name of the field used to index the height of the picture.
	 */
	public static final String HEIGHT_INDEX_FIELD = "picture_height";

	/**
	 * Name of the field used to index the width of the picture.
	 */
	public static final String WIDTH_INDEX_FIELD = "picture_width";

	/**
	 * Name of the field used to index the exif 'exposure' of the picture.
	 */
	public static final String PICTURE_EXPOSURE_INDEX_FIELD = "picture_exposure";

	/**
	 * Name of the field used to index the exif 'relative aperture' of the
	 * picture.
	 */
	public static final String MODEL_RELATIVE_APERTURE_FIELD = "picture_relative_aperture";

	/**
	 * Name of the field used to index the exif 'focal length' of the picture.
	 */
	public static final String FOCAL_LENGTH_INDEX_FIELD = "picture_focal_length";

	/**
	 * The configuration.
	 */
	private final ISQLFileDataSourceConfiguration _conf;

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
	 * Creates a new {@link PictureIndexer}.
	 * 
	 * @param conf
	 *            the configuration.
	 * @throws IOException
	 */
	public PictureIndexer(final ISQLFileDataSourceConfiguration conf)
			throws IOException
	{
		_conf = conf;

		final Directory dir = FSDirectory.open(new File(_conf
				.getIndexDirectory()));

		// index writer configuration
		final IndexWriterConfig iwConf = new IndexWriterConfig(
				Version.LUCENE_40, null);
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
	 * @param picture
	 *            the specified (@link IPicture}.
	 * @throws CorruptIndexException
	 *             if the index is corrupted.
	 * @throws IOException
	 *             if there is an I/O error while writing the index.
	 */
	public void indexPicture(final IPicture picture)
			throws CorruptIndexException, IOException
	{
		final Term currDoc = new Term(ID_INDEX_FIELD, picture.getId());
		final Document doc = createDocument(picture);

		_indexWriter.updateDocument(currDoc, doc);
		synchronized (_readerLock)
		{
			_bReaderNeedsUpdate = true;
		}
	}

	/**
	 * Create the lucene {@link Document} for the specified (@link IPicture}.
	 * 
	 * @param picture
	 *            the specified (@link IPicture}.
	 * @return the newly created lucene {@link Document}.
	 */
	private Document createDocument(final IPicture picture)
	{
		final Document doc = new Document();
		// id
		doc.add(new StringField(ID_INDEX_FIELD, picture.getId(),
				Field.Store.YES));
		// grade
		doc.add(new IntField(GRADE_INDEX_FIELD, picture.getPictureGrade(),
				Field.Store.YES));
		// tags
		for (final ITag t : picture.getTagSet())
		{
			doc.add(new StringField(t.getTagIdAsString(), t.getTagIdAsString(),
					Field.Store.NO));
		}
		// informations
		final PictureInformation info = picture.getPictureInformation();
		doc.add(new IntField(ORIENTATION_INDEX_FIELD, info.getOrientation(),
				Field.Store.NO));
		doc.add(new IntField(HEIGHT_INDEX_FIELD, info.getHeight(),
				Field.Store.NO));
		doc.add(new IntField(WIDTH_INDEX_FIELD, info.getWidth(), Field.Store.NO));
		if (info.getMake() != null)
		{
			doc.add(new StringField(MAKE_INDEX_FIELD, info.getMake(),
					Field.Store.NO));
		}
		if (info.getModel() != null)
		{
			doc.add(new StringField(MODEL_INDEX_FIELD, info.getModel(),
					Field.Store.NO));
		}
		if (info.getExposureTime() != null)
		{
			doc.add(new StringField(PICTURE_EXPOSURE_INDEX_FIELD, info
					.getExposureTime(), Field.Store.NO));
		}
		if (info.getRelativeAperture() != null)
		{
			doc.add(new StringField(MODEL_RELATIVE_APERTURE_FIELD, info
					.getRelativeAperture(), Field.Store.NO));
		}
		if (info.getFocalLength() != null)
		{
			doc.add(new StringField(FOCAL_LENGTH_INDEX_FIELD, info
					.getFocalLength(), Field.Store.NO));
		}

		// doc.add(new IntField("",
		// info.getCreationTimestamp(), Field.Store.NO));
		return doc;
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
	 * Search picture id corresponding to the specified query.
	 * 
	 * @param query
	 *            the query.
	 * @param iLimit
	 *            maximum number of result.
	 * @return a {@link List} of the matching picture id.
	 * @throws IOException
	 *             if an error occurs during the search.
	 */
	public List<String> searchPicture(final Query query, final int iLimit)
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
			final ScoreDoc[] searchResult = _indexSearcher
					.search(query, iLimit).scoreDocs;
			final ArrayList<String> result = new ArrayList<>();
			result.ensureCapacity(searchResult.length);
			for (final ScoreDoc scoreDoc : searchResult)
			{
				result.add(_indexReader.document(scoreDoc.doc)
						.get("picture_id"));
			}
			return result;
		}
	}
}
