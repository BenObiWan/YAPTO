package yapto.picturebank.index.query;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;

import yapto.picturebank.index.DateFilteringType;
import yapto.picturebank.index.GradeFilteringType;
import yapto.picturebank.index.lucene.PictureIndexer;

public final class PictureQueryBuilder
{
	public static final Sort _dateSort = new Sort(new SortField(
			PictureIndexer.CREATION_TIMESTAMP_INDEX_FIELD, SortField.Type.LONG));

	public static final Sort _invertDateSort = new Sort(new SortField(
			PictureIndexer.CREATION_TIMESTAMP_INDEX_FIELD, SortField.Type.LONG,
			true));

	public PictureQueryBuilder()
	{
	}

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
