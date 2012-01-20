package yapto.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureList;
import yapto.datasource.tag.Tag;

/**
 * Panel displaying the list of {@link Tag} that can be associated with an
 * {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public abstract class AbstractTagEditorPanel extends JPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = -5913543215027452909L;

	/**
	 * The {@link IPictureList} used to load the list of available {@link Tag}
	 * s.
	 */
	protected IPictureList _pictureList;

	/**
	 * The {@link IPicture} with which to associate the tags.
	 */
	protected IPicture _picture;

	/**
	 * Lock protecting access to the {@link IPictureList} and the
	 * {@link IPicture}.
	 */
	protected final Object _lock = new Object();

	/**
	 * Creates a new AbstractTagEditorPanel.
	 */
	public AbstractTagEditorPanel()
	{
		super(new BorderLayout());
	}

	/**
	 * Select the {@link Tag}s appropriate to the selected {@link IPicture}.
	 */
	public abstract void selectAppropriateTags();

	/**
	 * Update the list of available {@link Tag}s according to those available in
	 * the {@link IPictureList}.
	 */
	public abstract void updateAvailableTags();

	/**
	 * Change the {@link IPicture} which {@link Tag}s can be changed on this
	 * {@link TreeTagEditorPanel}.
	 * 
	 * @param picture
	 *            the new {@link IPicture}.
	 */
	public abstract void changePicture(final IPicture picture);

	/**
	 * Update the list of available {@link Tag}s according the {@link Tag}s of
	 * the specified {@link IPictureList}.
	 * 
	 * @param pictureList
	 *            the new {@link IPictureList}.
	 */
	public abstract void changePictureList(final IPictureList pictureList);
}