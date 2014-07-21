package yapto.picturebank.sqlfile;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import yapto.picturebank.ImageFormatType;
import yapto.picturebank.process.IdentifyTask;
import yapto.picturebank.process.PictureProcessor;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;

/**
 * Object used to execute external command on pictures.
 * 
 * @author benobiwan
 * 
 */
public final class SQLFilePictureProcessor extends PictureProcessor
{
	/**
	 * Configuration for this {@link SQLFilePictureBank}.
	 */
	private final ISQLFilePictureBankConfiguration _conf;

	/**
	 * Creates a new SQLFilePictureProcessor.
	 * 
	 * @param conf
	 *            configuration to use.
	 * @param iMaxConcurrentIdentifyTask
	 *            the maximum number of {@link IdentifyTask} to run at the same
	 *            time.
	 * @param iMaxConcurrentOtherTask
	 *            the maximum number of task other than {@link IdentifyTask} to
	 *            run at the same time.
	 */
	public SQLFilePictureProcessor(final ISQLFilePictureBankConfiguration conf,
			final int iMaxConcurrentIdentifyTask,
			final int iMaxConcurrentOtherTask)
	{
		super(iMaxConcurrentIdentifyTask, iMaxConcurrentOtherTask);
		_conf = conf;
	}

	/**
	 * Create thumbnail for the specified picture asynchronously.
	 * 
	 * @param picture
	 *            picture which thumbnail we want to create.
	 */
	public void createThumbnail(final FsPicture picture)
	{
		final Path picturePath = createPicturePath(picture);
		final Path thumbnailPath = createThumbnailPath(picture);
		asyncCreatePictureThumbnail(128, picturePath, thumbnailPath);
	}

	/**
	 * Delete the thumbnail for the specified picture.
	 * 
	 * @param picture
	 *            picture which thumbnail we want to delete.
	 * @throws IOException
	 *             if an error occurs while deleting the file.
	 */
	public void deleteThumbnail(final FsPicture picture) throws IOException
	{
		Files.delete(createThumbnailPath(picture));
	}

	/**
	 * Create a display picture for the specified picture asynchronously.
	 * 
	 * @param picture
	 *            picture for which we want to create a display picture.
	 */
	public void createDisplayPicture(final FsPicture picture)
	{
		final Path picturePath = createPicturePath(picture);
		final Path secondaryPath = createDisplayPath(picture);
		asyncResizePicture(picture.getPictureInformation().getWidth(),
				picturePath, secondaryPath, false);
	}

	/**
	 * Delete the display picture for the specified picture.
	 * 
	 * @param picture
	 *            picture for which we want to delete the display picture.
	 * @throws IOException
	 *             if an error occurs while deleting the file.
	 */
	public void deleteDisplayPicture(final FsPicture picture)
			throws IOException
	{
		Files.delete(createDisplayPath(picture));
	}

	/**
	 * Create a resized picture asynchronously.
	 * 
	 * @param iWidth
	 *            the target width of the new picture.
	 * @param picture
	 *            picture for which we want to create a resized picture.
	 */
	public void createSizedPicture(final int iWidth, final FsPicture picture)
	{
		final Path picturePath = createPicturePath(picture);
		final Path secondaryPath = createSizedPath(iWidth, picture);
		asyncResizePicture(iWidth, picturePath, secondaryPath, false);
	}

	/**
	 * Check whether this {@link FsPicture} has a thumbnail.
	 * 
	 * @param picture
	 *            the picture to check.
	 * @return true if this picture has a thumbnail.
	 */
	public boolean hasThumbnail(final FsPicture picture)
	{
		return Files.exists(createThumbnailPath(picture));
	}

	/**
	 * Check whether this {@link FsPicture} has a display picture.
	 * 
	 * @param picture
	 *            the picture to check.
	 * @return true if this picture has a display picture.
	 */
	public boolean hasDisplayPicture(final FsPicture picture)
	{
		return Files.exists(createDisplayPath(picture));
	}

	/**
	 * Delete the main picture of the specified {@link FsPicture}.
	 * 
	 * @param picture
	 *            the picture to delete.
	 * @throws IOException
	 *             if an error occurs during the deletion of the file.
	 */
	public void deleteMainPicture(final FsPicture picture) throws IOException
	{
		Files.delete(createPicturePath(picture));
	}

	/**
	 * Create a {@link Path} to the main picture file of this {@link FsPicture}.
	 * 
	 * @param picture
	 *            the specified {@link FsPicture}.
	 * @return {@link Path} to the main picture file.
	 */
	private Path createPicturePath(final FsPicture picture)
	{
		return FileSystems
				.getDefault()
				.getPath(
						_conf.getMainPictureLoaderConfiguration()
								.getPictureDirectory(),
						picture.getId().substring(0, 2), picture.getIdWithExt());
	}

	/**
	 * Create a {@link Path} to the thumbnail file of this {@link FsPicture}.
	 * 
	 * @param picture
	 *            the specified {@link FsPicture}.
	 * @return {@link Path} to the thumbnail file.
	 */
	private Path createThumbnailPath(final FsPicture picture)
	{
		return FileSystems.getDefault().getPath(
				_conf.getThumbnailPictureLoaderConfiguration()
						.getPictureDirectory(),
				picture.getId().substring(0, 2),
				picture.getId() + '.' + ImageFormatType.PNG.getExtension());
	}

	/**
	 * Create a {@link Path} to the display picture file of this
	 * {@link FsPicture}.
	 * 
	 * @param picture
	 *            the specified {@link FsPicture}.
	 * @return {@link Path} to the display picture file.
	 */
	private Path createDisplayPath(final FsPicture picture)
	{
		if (picture.getImageType().doesKeepFormat())
		{
			return FileSystems.getDefault().getPath(
					_conf.getSecondaryPictureLoaderConfiguration()
							.getPictureDirectory(),
					picture.getId().substring(0, 2), picture.getIdWithExt());
		}
		return FileSystems.getDefault().getPath(
				_conf.getSecondaryPictureLoaderConfiguration()
						.getPictureDirectory(),
				picture.getId().substring(0, 2),
				picture.getId() + '.' + ImageFormatType.JPG.getExtension());
	}

	/**
	 * Create a {@link Path} to a resized picture file of this {@link FsPicture}
	 * .
	 * 
	 * @param iWidth
	 *            width of the resized picture.
	 * @param picture
	 *            the specified {@link FsPicture}.
	 * @return {@link Path} to the resized picture file.
	 */
	private Path createSizedPath(final int iWidth, final FsPicture picture)
	{
		final ImageFormatType type = picture.getImageType();
		final StringBuilder sb = new StringBuilder(picture.getId());
		sb.append('_');
		sb.append(iWidth);
		sb.append('.');
		if (type.doesKeepFormat())
		{
			sb.append(type.getExtension());
		}
		else
		{
			sb.append(ImageFormatType.JPG.getExtension());
		}
		return FileSystems.getDefault().getPath(
				_conf.getSecondaryPictureLoaderConfiguration()
						.getPictureDirectory(),
				picture.getId().substring(0, 2), sb.toString());
	}
}
