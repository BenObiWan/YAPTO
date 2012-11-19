package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.BorderLayout;
import java.awt.Frame;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBrowser;
import yapto.picturebank.tag.ITag;

/**
 * Panel displaying the list of {@link ITag} that can be associated with an
 * {@link IPicture} using a {@link CheckboxTree}.
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

	/**
	 * Panel used to display the {@link CheckboxTree}.
	 */
	private final SelectingTreeTagPanel _panelTreeTag;

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
		pictureIterator.register(_panelTreeTag);
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
				_panelTreeTag.unsetSelectedTags();
				if (_picture != null)
				{
					_panelTreeTag.setSelectedTags();
				}
			}
		}
	}

	@Override
	protected void savePictureTags()
	{
		// nothing to do, tags saved each time one of them is selected.
	}
}
