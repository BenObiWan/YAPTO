package yapto.picturebank.sqlfile;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import yapto.picturebank.ImageFormatType;
import yapto.picturebank.process.PictureProcessor;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;

public final class SQLFilePictureProcessor extends PictureProcessor
{
	/**
	 * Configuration for this {@link SQLFilePictureBank}.
	 */
	private final ISQLFilePictureBankConfiguration _conf;

	public SQLFilePictureProcessor(final ISQLFilePictureBankConfiguration conf,
			final int iMaxConcurrentIdentifyTask,
			final int iMaxConcurrentOtherTask)
	{
		super(iMaxConcurrentIdentifyTask, iMaxConcurrentOtherTask);
		_conf = conf;
	}

	/**
	 * Create thumbnail for the specified picture.
	 * 
	 * @param picture
	 *            picture which thumbnail we want to create.
	 */
	public void createThumbnail(final FsPicture picture)
	{
		final String subDir = picture.getId().substring(0, 2);
		final Path picturePath = FileSystems.getDefault()
				.getPath(
						_conf.getMainPictureLoaderConfiguration()
								.getPictureDirectory(), subDir,
						picture.getIdWithExt());
		final Path thumbnailPath = FileSystems.getDefault().getPath(
				_conf.getThumbnailPictureLoaderConfiguration()
						.getPictureDirectory(), subDir,
				picture.getId() + '.' + ImageFormatType.PNG.getExtension());
		asyncCreatePictureThumbnail(128, picturePath, thumbnailPath);
	}

	public void createDisplayPicture(final FsPicture picture)
	{
		final String subDir = picture.getId().substring(0, 2);
		final Path picturePath = FileSystems.getDefault()
				.getPath(
						_conf.getMainPictureLoaderConfiguration()
								.getPictureDirectory(), subDir,
						picture.getIdWithExt());
		final Path secondaryPath;
		final ImageFormatType type = picture.getImageType();
		if (type.doesKeepFormat())
		{
			secondaryPath = FileSystems.getDefault().getPath(
					_conf.getSecondaryPictureLoaderConfiguration()
							.getPictureDirectory(), subDir,
					picture.getIdWithExt());
		}
		else
		{
			secondaryPath = FileSystems.getDefault().getPath(
					_conf.getSecondaryPictureLoaderConfiguration()
							.getPictureDirectory(), subDir,
					picture.getId() + '.' + ImageFormatType.JPG.getExtension());
		}
		asyncResizePicture(picture.getPictureInformation().getWidth(),
				picturePath, secondaryPath, false);
	}

	public void createSizedPicture(final int iWidth, final FsPicture picture)
	{
		final String subDir = picture.getId().substring(0, 2);
		final Path picturePath = FileSystems.getDefault()
				.getPath(
						_conf.getMainPictureLoaderConfiguration()
								.getPictureDirectory(), subDir,
						picture.getIdWithExt());
		final Path secondaryPath;
		final ImageFormatType type = picture.getImageType();
		if (type.doesKeepFormat())
		{
			secondaryPath = FileSystems.getDefault().getPath(
					_conf.getSecondaryPictureLoaderConfiguration()
							.getPictureDirectory(), subDir,
					picture.getIdWithExt());
		}
		else
		{
			final StringBuilder sb = new StringBuilder(picture.getId());
			sb.append('_');
			sb.append(iWidth);
			sb.append('.');
			sb.append(ImageFormatType.JPG.getExtension());
			secondaryPath = FileSystems.getDefault().getPath(
					_conf.getSecondaryPictureLoaderConfiguration()
							.getPictureDirectory(), subDir, sb.toString());
		}
		asyncResizePicture(iWidth, picturePath, secondaryPath, false);
	}
}
