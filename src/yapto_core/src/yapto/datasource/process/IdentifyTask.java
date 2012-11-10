package yapto.datasource.process;

import java.io.File;
import java.util.concurrent.Callable;

import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.OutputConsumer;

import yapto.datasource.PictureInformation;

/**
 * Task used to get all meta information of a picture.
 * 
 * @author benobiwan
 * 
 */
public final class IdentifyTask implements Callable<PictureInformation>
{
	/**
	 * The picture to identify.
	 */
	private final File _fPicture;

	/**
	 * The command to execute on the picture.
	 */
	private final IdentifyCmd _command = new IdentifyCmd();

	/**
	 * The operation to perform during the execution of the command.
	 */
	private final IMOperation _operation = new IMOperation();

	/**
	 * The {@link OutputConsumer} used to process the output of the command.
	 */
	private final LinkedListOutputConsumer _consumer = new LinkedListOutputConsumer();

	/**
	 * Creates a new IdentifyTask.
	 * 
	 * @param fPicture
	 *            the picture to identify.
	 */
	public IdentifyTask(final File fPicture)
	{
		_fPicture = fPicture;
		_command.setOutputConsumer(_consumer);
		_operation.verbose();
		_operation.addImage();
	}

	@Override
	public PictureInformation call() throws Exception
	{
		_command.run(_operation, _fPicture.getAbsolutePath());
		return CommandOutputParser.readIdentifyOutput(_fPicture.getName(),
				_consumer.getOutput());
	}
}
