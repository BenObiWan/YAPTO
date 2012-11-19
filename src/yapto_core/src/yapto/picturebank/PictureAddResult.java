package yapto.picturebank;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Result of the adding of multiple pictures to an {@link IPictureBank}.
 * 
 * @author benobiwan
 * 
 */
public final class PictureAddResult
{
	/**
	 * Lock protecting access to other attributes.
	 */
	private final Object _lock = new Object();

	/**
	 * Number of file successfully added.
	 */
	private int _iSuccessCount = 0;

	/**
	 * Number of recoverable error encountered.
	 */
	private int _iErrorCount = 0;

	/**
	 * List of successfully added picture.
	 */
	private final List<Path> _successPath;

	/**
	 * {@link Map} off the pictures that are duplicates of already present
	 * pictures.
	 */
	private final Map<String, Path> _duplicateMap = new HashMap<>();

	/**
	 * Creates a new PictureAddResult.
	 */
	public PictureAddResult()
	{
		_successPath = new LinkedList<>();
	}

	/**
	 * Add a file in error.
	 * 
	 * @param file
	 *            the {@link Path} to the file in error.
	 * @param e
	 *            the encountered error.
	 */
	public void addFileError(final Path file, final PictureAddException e)
	{
		switch (e.getExceptionType())
		{
		case FILE_ALREADY_EXISTS:
			_duplicateMap.put(e.getPictureId(), file);
			break;
		case NOT_A_FILE:
		case FILE_NOT_FOUND:
		case CAN_T_READ:
		case IDENTIFY_EXECUTION_ERROR:
		default:
			// TODO add error to a list
			synchronized (_lock)
			{
				_iErrorCount++;
			}
			break;
		}
	}

	/**
	 * Register a file successfully added.
	 * 
	 * @param file
	 *            the {@link Path} to the successfully added file.
	 */
	public void addFileSuccess(final Path file)
	{
		synchronized (_lock)
		{
			_iSuccessCount++;
			_successPath.add(file);
		}
	}

	/**
	 * Register an unrecoverable error.
	 * 
	 * @param file
	 *            the {@link Path} to the file in error.
	 * @param e
	 *            the encountered error.
	 */
	public void addUnrecoverableError(final Path file,
			final PictureAddException e)
	{
		switch (e.getExceptionType())
		{
		case COPY_ERROR:
		case IO_ERROR:
		case INDEX_ERROR:
		case CORRUPT_INDEX_ERROR:
		case NO_SUCH_ALGORITHM:
		case SQL_INSERT_ERROR:
		default:
			// TODO log the error somehow
			break;
		}
	}

	/**
	 * Get the number of encountered errors.
	 * 
	 * @return the number of encountered errors.
	 */
	public int getErrorCount()
	{
		synchronized (_lock)
		{
			return _iErrorCount;
		}
	}

	/**
	 * Get the number of files successfully added.
	 * 
	 * @return the number of files successfully added.
	 */
	public int getSuccessCount()
	{
		synchronized (_lock)
		{
			return _iSuccessCount;
		}
	}
}
