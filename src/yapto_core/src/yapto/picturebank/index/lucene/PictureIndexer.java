package yapto.picturebank.index.lucene;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import yapto.picturebank.IPicture;
import yapto.picturebank.PictureInformation;
import yapto.picturebank.PictureListWithIndex;
import yapto.picturebank.index.query.PictureQueryBuilder;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;
import yapto.picturebank.tag.ITag;

/**
 * Object used to interact with the lucene index for pictures.
 * 
 * @author benobiwan
 * 
 */
public final class PictureIndexer
{
	/**
	 * Name of the field used to index the name of the picture.
	 */
	public static final String ID_INDEX_FIELD = "id";

	/**
	 * Name of the field used to index the grade of the picture.
	 */
	public static final String GRADE_INDEX_FIELD = "grade";

	/**
	 * Name of the field used to index the orientation of the picture.
	 */
	public static final String ORIENTATION_INDEX_FIELD = "orientation";

	/**
	 * Name of the field used to index the exif 'make' of the picture.
	 */
	public static final String MAKE_INDEX_FIELD = "make";

	/**
	 * Name of the field used to index the exif 'model' of the picture.
	 */
	public static final String MODEL_INDEX_FIELD = "model";

	/**
	 * Name of the field used to index the height of the picture.
	 */
	public static final String HEIGHT_INDEX_FIELD = "height";

	/**
	 * Name of the field used to index the width of the picture.
	 */
	public static final String WIDTH_INDEX_FIELD = "width";

	/**
	 * Name of the field used to index the exif 'exposure' of the picture.
	 */
	public static final String PICTURE_EXPOSURE_INDEX_FIELD = "exposure";

	/**
	 * Name of the field used to index the exif 'relative aperture' of the
	 * picture.
	 */
	public static final String MODEL_RELATIVE_APERTURE_FIELD = "relative_aperture";

	/**
	 * Name of the field used to index the exif 'focal length' of the picture.
	 */
	public static final String FOCAL_LENGTH_INDEX_FIELD = "focal_length";

	/**
	 * Name of the field used to index the timestamp of the last modification of
	 * the picture.
	 */
	public static final String MODIFIED_TIMESTAMP_INDEX_FIELD = "modified_timestamp";

	/**
	 * Name of the field used to index the timestamp of the creation of the
	 * picture.
	 */
	public static final String CREATION_TIMESTAMP_INDEX_FIELD = "creation_timestamp";

	/**
	 * Name of the field used to index the tags of the picture.
	 */
	public static final String TAG_INDEX_FIELD = "tag";

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
	 * {@link DirectoryReader} used to read the indexes.
	 */
	private DirectoryReader _indexReader;

	/**
	 * {@link IndexSearcher} used to search the indexes.
	 */
	private IndexSearcher _indexSearcher;

	/**
	 * Creates a new {@link PictureIndexer}.
	 * 
	 * @param conf
	 *            the configuration.
	 * @throws IOException
	 */
	public PictureIndexer(final ISQLFilePictureBankConfiguration conf) throws IOException
	{
		_conf = conf;

		final Directory dir = FSDirectory.open(FileSystems.getDefault().getPath(_conf.getIndexDirectory(), "picture"));

		// index writer configuration
		final IndexWriterConfig iwConf = new IndexWriterConfig(null);
		iwConf.setOpenMode(OpenMode.CREATE_OR_APPEND);

		_indexWriter = new IndexWriter(dir, iwConf);

		synchronized (_readerLock)
		{
			_indexReader = DirectoryReader.open(_indexWriter, true);
			_indexSearcher = new IndexSearcher(_indexReader);
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
	public void indexPicture(final IPicture picture) throws CorruptIndexException, IOException
	{
		final Term currDoc = new Term(ID_INDEX_FIELD, picture.getId());
		final Document doc = createDocument(picture);

		_indexWriter.updateDocument(currDoc, doc);
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
		doc.add(new StringField(ID_INDEX_FIELD, picture.getId(), Field.Store.YES));
		// grade
		doc.add(new IntField(GRADE_INDEX_FIELD, picture.getPictureGrade(), Field.Store.YES));
		// modified timestamp
		doc.add(new LongField(MODIFIED_TIMESTAMP_INDEX_FIELD, picture.getModifiedTimestamp(), Field.Store.NO));

		// tags
		for (final ITag t : picture.getTagSet())
		{
			doc.add(new StringField(TAG_INDEX_FIELD, t.getTagIdAsString(), Field.Store.NO));
		}
		// informations
		final PictureInformation info = picture.getPictureInformation();
		doc.add(new IntField(ORIENTATION_INDEX_FIELD, info.getOrientation(), Field.Store.NO));
		doc.add(new IntField(HEIGHT_INDEX_FIELD, info.getHeight(), Field.Store.NO));
		doc.add(new IntField(WIDTH_INDEX_FIELD, info.getWidth(), Field.Store.NO));
		doc.add(new LongField(CREATION_TIMESTAMP_INDEX_FIELD, info.getCreationTimestamp(), Field.Store.NO));
		if (info.getMake() != null)
		{
			doc.add(new StringField(MAKE_INDEX_FIELD, info.getMake(), Field.Store.NO));
		}
		if (info.getModel() != null)
		{
			doc.add(new StringField(MODEL_INDEX_FIELD, info.getModel(), Field.Store.NO));
		}
		if (info.getExposureTime() != null)
		{
			doc.add(new StringField(PICTURE_EXPOSURE_INDEX_FIELD, info.getExposureTime(), Field.Store.NO));
		}
		if (info.getRelativeAperture() != null)
		{
			doc.add(new StringField(MODEL_RELATIVE_APERTURE_FIELD, info.getRelativeAperture(), Field.Store.NO));
		}
		if (info.getFocalLength() != null)
		{
			doc.add(new StringField(FOCAL_LENGTH_INDEX_FIELD, info.getFocalLength(), Field.Store.NO));
		}

		// doc.add(new IntField("",
		// info.getCreationTimestamp(), Field.Store.NO));
		return doc;
	}

	/**
	 * Remove an {@link IPicture} from the index
	 * 
	 * @param picture
	 *            the {@link IPicture} to remove.
	 * @throws IOException
	 *             if an error occurs while deleting the {@link IPicture} from
	 *             the index.
	 */
	public void unindexPicture(final IPicture picture) throws IOException
	{
		final Term currDoc = new Term(ID_INDEX_FIELD, picture.getId());
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
	 * Search picture id corresponding to the specified query.
	 * 
	 * @param query
	 *            the query.
	 * @param iLimit
	 *            maximum number of result.
	 * @param strInitId
	 *            index of the currently displayed picture.
	 * @return a {@link List} of the matching picture id.
	 * @throws IOException
	 *             if an error occurs during the search.
	 */
	public PictureListWithIndex searchPicture(final Query query, final int iLimit, final String strInitId)
			throws IOException
	{
		synchronized (_readerLock)
		{
			int iInitIndex = -1;
			final DirectoryReader newReader = DirectoryReader.openIfChanged(_indexReader, _indexWriter, true);
			if (newReader != null)
			{
				_indexReader.close();
				_indexReader = newReader;
				_indexSearcher = new IndexSearcher(_indexReader);
			}
			final ScoreDoc[] searchResult = _indexSearcher.search(query, iLimit,
					PictureQueryBuilder._dateSort).scoreDocs;
			final ArrayList<String> result = new ArrayList<>();
			result.ensureCapacity(searchResult.length);
			for (final ScoreDoc scoreDoc : searchResult)
			{
				final String strPictureId = _indexReader.document(scoreDoc.doc).get(ID_INDEX_FIELD);
				if (strInitId != null && strInitId.equals(strPictureId))
				{
					iInitIndex = result.size();
				}
				result.add(strPictureId);
			}
			return new PictureListWithIndex(result, iInitIndex);
		}
	}
}
