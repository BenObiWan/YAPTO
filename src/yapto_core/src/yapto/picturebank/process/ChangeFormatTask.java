package yapto.picturebank.process;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

/**
 * Task used to change the picture format.
 * 
 * @author benobiwan
 * 
 */
public final class ChangeFormatTask implements Callable<Boolean>
{
	/**
	 * The picture to change format.
	 */
	private final Path _fOriginalPicture;

	/**
	 * The destination picture.
	 */
	private final Path _fDestinationPicture;

	/**
	 * The command to execute on the picture.
	 */
	private final ConvertCmd _command = new ConvertCmd();

	/**
	 * The operation to perform during the execution of the command.
	 */
	private final IMOperation _operation = new IMOperation();

	/**
	 * Creates a new ChangeFormatTask.
	 * 
	 * @param fOriginalPicture
	 *            the picture to change format.
	 * @param fDestinationDirectory
	 *            the destination directory to write the new picture.
	 * @param format
	 *            the destination format of the picture.
	 * @param bkeepMetadata
	 *            whether the new picture should keep the original picture
	 *            metadata or not.
	 */
	public ChangeFormatTask(final Path fOriginalPicture,
			final Path fDestinationDirectory, final ImageFormatType format,
			final boolean bkeepMetadata)
	{
		_fOriginalPicture = fOriginalPicture.toAbsolutePath();
		String fileName = _fOriginalPicture.getFileName().toString();
		final int indexPoint = fileName.lastIndexOf('.');
		if (indexPoint != -1)
		{
			fileName = fileName.substring(0, indexPoint).concat(
					format.getExtension());
		}
		_fDestinationPicture = fDestinationDirectory.resolve(fileName);
		_operation.addImage();
		if (!bkeepMetadata)
		{
			_operation.strip();
		}
		_operation.addImage();
	}

	@Override
	public Boolean call() throws Exception
	{
		_command.run(_operation, _fOriginalPicture.toString(),
				_fDestinationPicture.toString());
		return Boolean.TRUE;
	}
}
