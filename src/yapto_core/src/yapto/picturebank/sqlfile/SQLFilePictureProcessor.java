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
						.getPictureDirectory(),
				subDir,
				picture.getIdWithExt() + '.'
						+ ImageFormatType.PNG.getExtension());
		asyncCreatePictureThumbnail(128, picturePath, thumbnailPath);
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
			StringBuilder sb = new StringBuilder(picture.getId());
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
