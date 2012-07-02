package yapto.datasource.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import yapto.datasource.IPicture;
import yapto.datasource.sqlfile.config.ISQLFileDataSourceConfiguration;
import yapto.datasource.tag.Tag;

public final class PictureIndexer
{
	private static final String ID_FIELD = "picture_id";

	private static final String GRADE_FIELD = "picture_grade";

	/**
	 * An empty byte array for uses in tags {@link Field}.
	 */
	private static final byte[] EMPTY_BYTE_ARRAY = new byte[] {};

	/**
	 * The configuration.
	 */
	private final ISQLFileDataSourceConfiguration _conf;

	private final IndexWriter _indexWriter;

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
				Version.LUCENE_36, null);
		iwConf.setOpenMode(OpenMode.CREATE_OR_APPEND);

		_indexWriter = new IndexWriter(dir, iwConf);
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
		final Term currDoc = new Term(ID_FIELD, picture.getId());
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
		doc.add(new Field(ID_FIELD, picture.getId().getBytes()));
		// grade
		final NumericField grade = new NumericField(GRADE_FIELD);
		grade.setIntValue(picture.getPictureGrade());
		doc.add(grade);
		// tags
		for (final Tag t : picture.getTagSet())
		{
			doc.add(new Field(t.getName(), EMPTY_BYTE_ARRAY));
		}

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
	}
}
