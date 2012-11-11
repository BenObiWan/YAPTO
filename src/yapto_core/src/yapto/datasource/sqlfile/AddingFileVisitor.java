package yapto.datasource.sqlfile;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import yapto.datasource.IDataSource;
import yapto.datasource.PictureAddException;
import yapto.datasource.PictureAddResult;

/**
 * {@link SimpleFileVisitor} which add picture files to the provided
 * {@link IDataSource}.
 * 
 * @author benobiwan
 * 
 */
public final class AddingFileVisitor extends SimpleFileVisitor<Path>
{
	/**
	 * The {@link IDataSource} used for adding pictures.
	 */
	private final IDataSource<?> _dataSource;

	/**
	 * Result of the picture adding.
	 */
	private final PictureAddResult _result;

	/**
	 * Creates a new AddingFileVisitor.
	 * 
	 * @param dataSource
	 *            the {@link IDataSource} used for adding pictures.
	 */
	public AddingFileVisitor(final IDataSource<?> dataSource)
	{
		_dataSource = dataSource;
		_result = new PictureAddResult();
	}

	@Override
	public FileVisitResult visitFile(final Path file,
			final BasicFileAttributes attrs) throws IOException
	{
		FileVisitResult res;
		try
		{
			_dataSource.addPicture(file);
			_result.addFileSuccess(file);
			res = FileVisitResult.CONTINUE;
		}
		catch (final PictureAddException e)
		{
			switch (e.getExceptionType())
			{
			// errors which don't stop directory exploration
			case NOT_A_FILE:
			case FILE_ALREADY_EXISTS:
			case FILE_NOT_FOUND:
			case CAN_T_READ:
			case IDENTIFY_EXECUTION_ERROR:
				_result.addFileError(file, e);
				res = FileVisitResult.CONTINUE;
				break;
			// errors which stop directory exploration
			case COPY_ERROR:
			case IO_ERROR:
			case INDEX_ERROR:
			case CORRUPT_INDEX_ERROR:
			case NO_SUCH_ALGORITHM:
			case SQL_INSERT_ERROR:
			default:
				_result.addUnrecoverableError(file, e);
				res = FileVisitResult.TERMINATE;
				break;
			}
		}
		return res;
	}

	/**
	 * get the result.
	 * 
	 * @return the result.
	 */
	public PictureAddResult getResult()
	{
		return _result;
	}
}
