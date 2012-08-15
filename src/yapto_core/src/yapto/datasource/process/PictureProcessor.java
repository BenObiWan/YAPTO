package yapto.datasource.process;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import yapto.datasource.PictureInformation;

/**
 * Object used to execute external command on pictures.
 * 
 * @author benobiwan
 * 
 */
public final class PictureProcessor
{
	/**
	 * ExecutorService used to execute identify commands.
	 */
	private final ExecutorService _identifyExecutor;

	/**
	 * ExecutorService used to execute every command except identify command.
	 */
	private final ExecutorService _generalPurposeExecutor;

	/**
	 * Creates a new PictureProcessor.
	 * 
	 * @param iMaxConcurrentIdentifyTask
	 *            the maximum number of {@link IdentifyTask} to run at the same
	 *            time.
	 * @param iMaxConcurrentOtherTask
	 *            the maximum number of task other than {@link IdentifyTask} to
	 *            run at the same time.
	 */
	public PictureProcessor(final int iMaxConcurrentIdentifyTask,
			final int iMaxConcurrentOtherTask)
	{
		_identifyExecutor = Executors
				.newFixedThreadPool(iMaxConcurrentIdentifyTask);
		_generalPurposeExecutor = Executors
				.newFixedThreadPool(iMaxConcurrentOtherTask);
	}

	/**
	 * Run an identify command on the specified picture and return a
	 * {@link PictureInformation} object containing its metadatas.
	 * 
	 * @param fPicture
	 *            the picture to identify.
	 * @return the metadatas of the picture.
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception.
	 */
	public PictureInformation identifyPicture(final File fPicture)
			throws InterruptedException, ExecutionException
	{
		final Future<PictureInformation> fut = asyncIdentifyPicture(fPicture);
		return fut.get();
	}

	/**
	 * submit an identify command on the specified picture and return without
	 * waiting for the task to complete. Uses an {@link IdentifyTask}.
	 * 
	 * @param fPicture
	 *            the picture to identify.
	 * @return the {@link Future} object used to follow the submitted command
	 *         and gets its result when it is completed.
	 */
	public Future<PictureInformation> asyncIdentifyPicture(final File fPicture)
	{
		return _identifyExecutor.submit(new IdentifyTask(fPicture));
	}

	/**
	 * Resize the given picture.
	 * 
	 * @param iWidth
	 *            the target width of the new picture.
	 * @param fOriginalPicture
	 *            the picture to resize.
	 * @param fDestinationPicture
	 *            the path to the new resized picture.
	 * @param bkeepMetadata
	 *            whether or not to keep the metadata of the original picture.
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws ExecutionException
	 *             if the computation threw an exception.
	 */
	public void resizePicture(final int iWidth, final File fOriginalPicture,
			final File fDestinationPicture, final boolean bkeepMetadata)
			throws InterruptedException, ExecutionException
	{
		final Future<Boolean> fut = asyncResizePicture(iWidth,
				fOriginalPicture, fDestinationPicture, bkeepMetadata);
		fut.get();
	}

	/**
	 * submit a task to resize the given picture and return without waiting for
	 * the task to complete. uses a ResizeTask.
	 * 
	 * @param iWidth
	 *            the target width of the new picture.
	 * @param fOriginalPicture
	 *            the picture to resize.
	 * @param fDestinationPicture
	 *            the path to the new resized picture.
	 * @param bkeepMetadata
	 *            whether or not to keep the metadata of the original picture.
	 * @return the {@link Future} object used to follow the submitted command.
	 */
	public Future<Boolean> asyncResizePicture(final int iWidth,
			final File fOriginalPicture, final File fDestinationPicture,
			final boolean bkeepMetadata)
	{
		return _generalPurposeExecutor.submit(new ResizeTask(fOriginalPicture,
				fDestinationPicture, iWidth, bkeepMetadata));
	}
}
