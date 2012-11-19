package yapto.picturebank.process;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

/**
 * Task used to create the thumbnail of a picture.
 * 
 * @author benobiwan
 * 
 */
public final class ThumbnailTask implements Callable<Boolean>
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
	 * Creates a new ThumbnailTask.
	 * 
	 * @param fOriginalPicture
	 *            the picture to resize.
	 * @param fDestinationPicture
	 *            the destination for the resized picture.
	 * @param iWidth
	 *            the width of the resized picture.
	 */
	public ThumbnailTask(final Path fOriginalPicture,
			final Path fDestinationPicture, final int iWidth)
	{
		_fOriginalPicture = fOriginalPicture.toAbsolutePath();
		_fDestinationPicture = fDestinationPicture.toAbsolutePath();
		final Integer iSize = Integer.valueOf(iWidth);
		_operation.addImage();
		_operation.resize(iSize, iSize);
		_operation.background("transparent");
		_operation.gravity("center");
		_operation.extent(iSize, iSize);
		_operation.strip();
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
