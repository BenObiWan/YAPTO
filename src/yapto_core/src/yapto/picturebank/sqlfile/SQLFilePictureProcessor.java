package yapto.picturebank.sqlfile;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
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
		final Path picturePath = createPicturePath(picture);
		final Path thumbnailPath = createThumbnailPath(picture);
		asyncCreatePictureThumbnail(128, picturePath, thumbnailPath);
	}
	
	public void deleteThumbnail(final FsPicture picture) throws IOException
	{
		Files.delete(createThumbnailPath(picture));
	}

	public void createDisplayPicture(final FsPicture picture)
	{
		final Path picturePath = createPicturePath(picture);
		final Path secondaryPath = createDisplayPath(picture);
		asyncResizePicture(picture.getPictureInformation().getWidth(),
				picturePath, secondaryPath, false);
	}
	
	public void deleteDisplayPicture(final FsPicture picture) throws IOException
	{
		Files.delete(createDisplayPath(picture));
	}	

	public void createSizedPicture(final int iWidth, final FsPicture picture)
	{
		final Path picturePath = createPicturePath(picture);
		final Path secondaryPath = createSizedPath(iWidth, picture);
		asyncResizePicture(iWidth, picturePath, secondaryPath, false);
	}

	public boolean hasThumbnail(final FsPicture picture)
	{
		return Files.exists(createThumbnailPath(picture));
	}

	public boolean hasDisplayPicture(final FsPicture picture)
	{
		return Files.exists(createDisplayPath(picture));
	}
	
	public void deleteMainPicture(final FsPicture picture) throws IOException
	{
		Files.delete(createPicturePath(picture));
	}	
	
	private Path createPicturePath(final FsPicture picture)
	{
		return FileSystems
				.getDefault()
				.getPath(
						_conf.getMainPictureLoaderConfiguration()
								.getPictureDirectory(),
						picture.getId().substring(0, 2), picture.getIdWithExt());
	}

	private Path createThumbnailPath(final FsPicture picture)
	{
		return FileSystems.getDefault().getPath(
				_conf.getThumbnailPictureLoaderConfiguration()
						.getPictureDirectory(),
				picture.getId().substring(0, 2),
				picture.getId() + '.' + ImageFormatType.PNG.getExtension());
	}

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
