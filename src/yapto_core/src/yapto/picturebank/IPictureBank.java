package yapto.picturebank;

import java.nio.file.Path;
import java.util.List;

import yapto.picturebank.tag.ITag;
import yapto.picturebank.tag.IWritableTagRepository;

/**
 * An interface describing a source for {@link IPicture}.
 * 
 * @author benobiwan
 * @param <PICTURE>
 *            type of {@link IPicture} of this {@link IPictureBank}.
 * 
 */
public interface IPictureBank<PICTURE extends IPicture> extends
		IPictureList<PICTURE>, IWritableTagRepository,
		Comparable<IPictureBank<?>>, IPictureBrowserCreator
{
	/**
	 * Add an {@link IPicture} to the {@link IPictureBank}.
	 * 
	 * @param pictureFile
	 *            the path to the {@link IPicture} to add.
	 * @param tagList
	 *            list of {@link ITag} to add to the picture.
	 * @throws PictureAddException
	 *             if an error occurs during the addition of the
	 *             {@link IPicture}.
	 */
	void syncAddPicture(Path pictureFile, List<ITag> tagList)
			throws PictureAddException;

	/**
	 * Add all {@link IPicture}s from a directory to the {@link IPictureBank}.
	 * 
	 * @param pictureDirectory
	 *            the path to the directory.
	 * @param tagList
	 *            list of {@link ITag} to add to the pictures.
	 * @return object containing information about every added pictures.
	 * @throws PictureAddException
	 *             if an error occurs during the addition of the
	 *             {@link IPicture}.
	 */
	PictureAddResult asyncAddPicture(Path pictureDirectory, List<ITag> tagList)
			throws PictureAddException;

	/**
	 * Create the thumbnail for the specified picture.
	 * 
	 * @param picture
	 *            the picture.
	 */
	void createThumbnail(PICTURE picture);

	/**
	 * Get the id of this {@link IPictureBank}.
	 * 
	 * @return the id of this {@link IPictureBank}.
	 */
	int getId();

	/**
	 * Close this {@link IPictureBank}.
	 */
	@Override
	void close();
}
