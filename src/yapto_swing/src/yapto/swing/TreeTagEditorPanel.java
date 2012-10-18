package yapto.swing;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JTree;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.tag.ITag;

/**
 * Panel displaying the list of {@link ITag} that can be associated with an
 * {@link IPicture} using a {@link JTree}.
 * 
 * @author benobiwan
 * 
 */
public final class TreeTagEditorPanel extends AbstractTagEditorPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 5669141686850523799L;

	private final AbstractTreeTagPanel _panelTreeTag;

	/**
	 * Creates a new TagEditorPanel.
	 * 
	 * @param parent
	 *            parent {@link Frame}.
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public TreeTagEditorPanel(final Frame parent,
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		super(parent, pictureIterator);
		_panelTreeTag = new SelectingTreeTagPanel(pictureIterator);
		add(_panelTreeTag, BorderLayout.CENTER);
		changePicture();
	}

	@Override
	protected void updateAvailableTags()
	{
		_panelTreeTag.updateAvailableTags();
	}

	@Override
	protected void selectAppropriateTags()
	{
		synchronized (_lock)
		{
			if (_pictureIterator != null)
			{
				// TODO unselect all tags
				if (_picture != null)
				{
					// TODO select appropriate tags
				}
			}
		}
	}

	@Override
	protected void savePictureTags()
	{
		// TODO Auto-generated method stub

	}
}
