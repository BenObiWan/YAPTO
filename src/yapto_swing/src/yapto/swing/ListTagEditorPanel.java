package yapto.swing;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureList;
import yapto.datasource.tag.Tag;

/**
 * Panel displaying the list of {@link Tag} that can be associated with an
 * {@link IPicture} using a {@link JList}.
 * 
 * @author benobiwan
 * 
 */
public final class ListTagEditorPanel extends AbstractTagEditorPanel
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 1577570873903931403L;

	/**
	 * {@link JList} used to display all the {@link Tag}s.
	 */
	private final JList<Tag> _tagList;

	/**
	 * Creates a new ListTagEditorPanel
	 * 
	 * @param pictureList
	 *            the {@link IPictureList} from which {@link Tag}s can be
	 *            selected.
	 * @param picture
	 *            the picture from which to edit the {@link Tag}s.
	 */
	public ListTagEditorPanel(final IPictureList pictureList,
			final IPicture picture)
	{
		_tagList = new JList<Tag>();
		_tagList.setLayoutOrientation(JList.VERTICAL);
		_tagList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		final JScrollPane scrollPane = new JScrollPane(_tagList);
		add(scrollPane, BorderLayout.CENTER);
		changePictureList(pictureList);
		changePicture(picture);
	}

	@Override
	public void selectAppropriateTags()
	{
		synchronized (_lock)
		{
			if (_pictureList != null)
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
	public void updateAvailableTags()
	{
		synchronized (_lock)
		{
			if (_pictureList != null)
			{
				final Vector<Tag> vTags = new Vector<Tag>();
				populateChildren(_pictureList.getRootTag(), vTags);
				_tagList.setListData(vTags);
			}
		}
	}

	private void populateChildren(final Tag parentTag, final Vector<Tag> vTags)
	{
		if (parentTag.isSelectable())
		{
			vTags.add(parentTag);
		}
		for (final Tag t : parentTag.getChildren())
		{
			populateChildren(t, vTags);
		}
	}
}
