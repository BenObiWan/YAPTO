package yapto.picturebank.sqlfile;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureAddException;
import yapto.picturebank.PictureAddResult;
import yapto.picturebank.tag.ITag;

/**
 * {@link SimpleFileVisitor} which add picture files to the provided
 * {@link IPictureBank}.
 * 
 * @author benobiwan
 * 
 */
public final class AddingFileVisitor extends SimpleFileVisitor<Path>
{
	/**
	 * The {@link IPictureBank} used for adding pictures.
	 */
	private final IPictureBank<?> _pictureBank;

	/**
	 * Result of the picture adding.
	 */
	private final PictureAddResult _result;

	/**
	 * List of {@link ITag} to add to the pictures.
	 */
	private final List<ITag> _tagList;

	/**
	 * Creates a new AddingFileVisitor.
	 * 
	 * @param pictureBank
	 *            the {@link IPictureBank} used for adding pictures.
	 * @param tagList
	 *            list of {@link ITag} to add to the pictures.
	 */
	public AddingFileVisitor(final IPictureBank<?> pictureBank,
			final List<ITag> tagList)
	{
		_pictureBank = pictureBank;
		_tagList = tagList;
		_result = new PictureAddResult();
	}

	@Override
	public FileVisitResult visitFile(final Path file,
			final BasicFileAttributes attrs) throws IOException
	{
		FileVisitResult res;
		try
		{
			_pictureBank.addPicture(file, _tagList);
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
