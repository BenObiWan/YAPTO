package yapto.picturebank.index.query;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;

import yapto.picturebank.IPicture;
import yapto.picturebank.index.DateFilteringType;
import yapto.picturebank.index.GradeFilteringType;
import yapto.picturebank.index.lucene.PictureIndexer;
import yapto.picturebank.tag.ITag;

/**
 * Object used to create filters on the {@link IPicture}s.
 * 
 * @author benobiwan
 *
 */
public final class PictureQueryBuilder
{
	/**
	 * Sort by creation timestamp of the picture, increasingly.
	 */
	public static final Sort _dateSort = new Sort(new SortField(
			PictureIndexer.CREATION_TIMESTAMP_INDEX_FIELD, SortField.Type.LONG));

	/**
	 * Sort by creation timestamp of the picture, decreasingly.
	 */
	public static final Sort _invertDateSort = new Sort(new SortField(
			PictureIndexer.CREATION_TIMESTAMP_INDEX_FIELD, SortField.Type.LONG,
			true));

	/**
	 * Private unused constructor.
	 */
	private PictureQueryBuilder()
	{
	}

	/**
	 * Create a filter on the grades of the {@link IPicture}s.
	 * 
	 * @param filteringType
	 *            the type of {@link GradeFilteringType}.
	 * @param iSelectedGrade
	 *            the value to filter on.
	 * @return a query filtering on the grades of the {@link IPicture}s.
	 */
	public static Query createGradeFilter(
			final GradeFilteringType filteringType, final int iSelectedGrade)
	{
		Query query = null;
		switch (filteringType)
		{
		case EQUAL:
			query = NumericRangeQuery.newIntRange(
					PictureIndexer.GRADE_INDEX_FIELD,
					Integer.valueOf(iSelectedGrade),
					Integer.valueOf(iSelectedGrade), true, true);
			break;
		case GREATER_OR_EQUAL:
			query = NumericRangeQuery.newIntRange(
					PictureIndexer.GRADE_INDEX_FIELD,
					Integer.valueOf(iSelectedGrade), Integer.valueOf(5), true,
					true);
			break;
		case LOWER_OR_EQUAL:
			query = NumericRangeQuery.newIntRange(
					PictureIndexer.GRADE_INDEX_FIELD, Integer.valueOf(0),
					Integer.valueOf(iSelectedGrade), true, true);
			break;
		}
		return query;
	}

	/**
	 * Create a filter on the creation timestamps of the {@link IPicture}s.
	 * 
	 * @param filteringType
	 *            the type of {@link DateFilteringType}.
	 * @param lStartingDate
	 *            the starting date for this filter.
	 * @param lEndingDate
	 *            the end date for this filter. Only used when filtering on an
	 *            interval.
	 * @return a query filtering on the grades of the {@link IPicture}s.
	 */
	public static Query createDateFilter(final DateFilteringType filteringType,
			final long lStartingDate, final long lEndingDate)
	{
		Query query = null;
		switch (filteringType)
		{
		case GREATER_OR_EQUAL:
			query = NumericRangeQuery.newLongRange(
					PictureIndexer.CREATION_TIMESTAMP_INDEX_FIELD,
					Long.valueOf(lStartingDate), Long.valueOf(Long.MAX_VALUE),
					true, true);
			break;
		case INTERVAL:
			query = NumericRangeQuery.newLongRange(
					PictureIndexer.CREATION_TIMESTAMP_INDEX_FIELD,
					Long.valueOf(lStartingDate), Long.valueOf(lEndingDate),
					true, true);
			break;
		case LOWER_OR_EQUAL:
			query = NumericRangeQuery.newLongRange(
					PictureIndexer.CREATION_TIMESTAMP_INDEX_FIELD,
					Long.valueOf(0), Long.valueOf(lStartingDate), true, true);
			break;
		}
		return query;
	}

	/**
	 * Create a filter on the creation {@link ITag}s of the {@link IPicture}s.
	 * 
	 * @param selectedTagIds
	 *            selected {@link ITag} ids to filter on.
	 * @return a query filtering on the {@link ITag}s of the {@link IPicture}s.
	 */
	public static Query createTagFilter(final int[] selectedTagIds)
	{
		final BooleanQuery query = new BooleanQuery();
		for (final int iTagId : selectedTagIds)
		{
			query.add(new TermQuery(new Term(PictureIndexer.TAG_INDEX_FIELD,
					Integer.toString(iTagId))), Occur.MUST);
		}
		return query;
	}

}
