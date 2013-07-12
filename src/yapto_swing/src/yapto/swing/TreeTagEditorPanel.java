package yapto.swing;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.BorderLayout;
import java.awt.Frame;

import yapto.picturebank.IPicture;
import yapto.picturebank.IPictureBank;
import yapto.picturebank.PictureBankList;
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
	private final AssociatingTreeTagPanel _panelTreeTag;

	/**
	 * Creates a new TagEditorPanel.
	 * 
	 * @param parent
	 *            parent {@link Frame}.
	 * @param bankList
	 *            the {@link PictureBankList} used to load the
	 *            {@link IPictureBank} used as source for the {@link IPicture}.
	 */
	public TreeTagEditorPanel(final Frame parent, final PictureBankList bankList)
	{
		super(parent, bankList);
		_panelTreeTag = new AssociatingTreeTagPanel(parent, bankList);
		add(_panelTreeTag, BorderLayout.CENTER);
		changePictureBrowser();
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
			if (_pictureBrowser != null)
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
