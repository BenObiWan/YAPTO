package yapto.picturebank;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.apache.log4j.BasicConfigurator;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.picturebank.index.DateFilteringType;
import yapto.picturebank.index.GradeFilteringType;
import yapto.picturebank.index.query.PictureQueryBuilder;
import yapto.picturebank.sqlfile.SQLFilePictureBank;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import common.config.InvalidConfigurationException;

public class Test
{
	/**
	 * Logger object.
	 */
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(Test.class);
	
	private final PictureBankList _bankList;

	public Test() throws ExecutionException, InvalidConfigurationException
	{

		final EventBus bus = new AsyncEventBus(Executors.newFixedThreadPool(10));
		_bankList = new PictureBankList(bus);

		// TODO to remove
		_bankList.selectPictureBankById(1);
		final IPictureBrowser<?> browser = _bankList.getAllPictures();
		LOGGER.debug("Picture Count: " + browser.getPictureCount());
	}

	public void filter(final boolean bGrade, final boolean bDate,
			final boolean bTag) throws IOException, ExecutionException
	{
		final BooleanQuery query = new BooleanQuery();
		if (bGrade)
		{
			query.add(PictureQueryBuilder.createGradeFilter(
					GradeFilteringType.GREATER_OR_EQUAL, 3), Occur.MUST);

		}
		if (bDate)
		{
			query.add(PictureQueryBuilder.createDateFilter(
					DateFilteringType.LOWER_OR_EQUAL,
					System.currentTimeMillis(), System.currentTimeMillis()),
					Occur.MUST);
		}
		if (bTag)
		{
			// query.add(PictureQueryBuilder.createTagFilter(_TagQueryPanel
			// .getSelectedTagIds()), Occur.MUST);
		}
		final IPictureBrowser<?> browser = _bankList.filterPictures(query,
				100000);
		LOGGER.debug("Picture Count: " + browser.getPictureCount());
		LOGGER.debug("Query: " + query.toString());
	}

	public void reindex() throws CorruptIndexException, IOException
	{
		LOGGER.debug("Start re indexing all pictures");
		for (final IPictureBank<?> bank : _bankList.getSelectedPictureBank())
		{
			if (bank instanceof SQLFilePictureBank)
			{
				((SQLFilePictureBank) bank).reIndexAllPictures();
			}
		}
		LOGGER.debug("indexing finished");
	}

	public void check() throws CorruptIndexException, IOException
	{
		LOGGER.debug("Start check of all pictures");
		for (final IPictureBank<?> bank : _bankList.getSelectedPictureBank())
		{
			if (bank instanceof SQLFilePictureBank)
			{
				((SQLFilePictureBank) bank).checkAllPictures();
			}
		}
		LOGGER.debug("check finished");
	}

	public void deletePics(String[] strIds) throws ExecutionException,
			SQLException, IOException
	{
		for (final IPictureBank<?> myBank : _bankList.getSelectedPictureBank())
		{
			if (myBank instanceof SQLFilePictureBank)
			{
				SQLFilePictureBank bank = (SQLFilePictureBank) myBank;
				for (String id : strIds)
				{
					bank.deletePicture(id);
				}
			}
		}
	}

	public void close()
	{
		_bankList.unselectAll();
	}

	public static void main(final String[] args)
			throws InvalidConfigurationException, ExecutionException,
			IOException, SQLException
	{
		BasicConfigurator.configure();
		final Test test = new Test();
		test.reindex();
		//test.check();
		// test.filter(false, true, false);
//		test.deletePics(new String[] {
//				"E928C108CA75573AA172018D63581190DFBBA583AE253092579559CE3990DF33",
//				"39D835E90BE818D55E8F8EC38F1D4D65B0D820358470BA464915B52EA76A7C3D",
//				"8F691BD5FEDEF3F2F35B5BF434C4977DA2D420082F76A001C136461B1A22D7E5",
//				"B8A4773D7E764A0639435AC4E37B4EBE28287F67D1DB9D8A9FE55D051E54361C" });
		test.close();
	}
}
