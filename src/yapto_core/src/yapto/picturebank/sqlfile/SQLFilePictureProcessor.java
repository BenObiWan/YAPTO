package yapto.picturebank.sqlfile;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import yapto.picturebank.process.PictureProcessor;
import yapto.picturebank.sqlfile.config.ISQLFilePictureBankConfiguration;

public class SQLFilePictureProcessor extends PictureProcessor
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
						.getPictureDirectory(), subDir,
				picture.getId() + ".png");
		asyncCreatePictureThumbnail(128, picturePath, thumbnailPath);
	}
}
