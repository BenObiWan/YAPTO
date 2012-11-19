package yapto.picturebank.process;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

/**
 * Task used to resize a picture.
 * 
 * @author benobiwan
 * 
 */
public final class ResizeTask implements Callable<Boolean>
{
	/**
	 * The picture to resize.
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
	 * Creates a new ResizeTask.
	 * 
	 * @param fOriginalPicture
	 *            the picture to resize.
	 * @param fDestinationPicture
	 *            the destination for the resized picture.
	 * @param iWidth
	 *            the width of the resized picture.
	 * @param bkeepMetadata
	 *            whether the resized picture should keep the original picture
	 *            metadata or not.
	 */
	public ResizeTask(final Path fOriginalPicture,
			final Path fDestinationPicture, final int iWidth,
			final boolean bkeepMetadata)
	{
		_fOriginalPicture = fOriginalPicture.toAbsolutePath();
		_fDestinationPicture = fDestinationPicture.toAbsolutePath();
		_operation.addImage();
		_operation.resize(Integer.valueOf(iWidth));
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
