package yapto.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Set;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yapto.datasource.IPicture;
import yapto.datasource.IPictureBrowser;
import yapto.datasource.OperationNotSupportedException;
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
	 * Logger object.
	 */
	protected static transient final Logger LOGGER = LoggerFactory
			.getLogger(ListTagEditorPanel.class);

	/**
	 * {@link JList} used to display all the {@link Tag}s.
	 */
	private final JList<Tag> _tagList;

	/**
	 * Vector of all {@link Tag}s.
	 */
	private final Vector<Tag> _vTags = new Vector<>();

	/**
	 * Boolean used to prevent the modification of the tags associated to an
	 * {@link IPicture} when the picture is changing.
	 */
	private volatile boolean _bDoNotSaveTags = false;

	/**
	 * Creates a new ListTagEditorPanel.
	 * 
	 * @param parent
	 *            parent {@link Frame}.
	 * @param pictureIterator
	 *            the {@link IPictureBrowser} to use.
	 */
	public ListTagEditorPanel(final Frame parent,
			final IPictureBrowser<? extends IPicture> pictureIterator)
	{
		super(parent, pictureIterator);
		_tagList = new JList<>();
		_tagList.setLayoutOrientation(JList.VERTICAL);
		_tagList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		final JScrollPane scrollPane = new JScrollPane(_tagList);
		add(scrollPane, BorderLayout.CENTER);
		updateAvailableTags();
		changePicture();
		_tagList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(final ListSelectionEvent e)
			{
				savePictureTags();
			}
		});
	}

	@Override
	protected void selectAppropriateTags()
	{
		_bDoNotSaveTags = true;
		synchronized (_lock)
		{
			if (_pictureIterator != null)
			{
				_tagList.clearSelection();
				if (_picture != null)
				{
					final Set<Tag> tags = _picture.getTagSet();
					final int[] indices = new int[tags.size()];
					int i = 0;
					for (final Tag t : tags)
					{
						indices[i] = _vTags.indexOf(t);
						i++;
					}
					_tagList.setSelectedIndices(indices);
				}
			}
		}
		_bDoNotSaveTags = false;
	}

	@Override
	protected void updateAvailableTags()
	{
		synchronized (_lock)
		{
			try
			{
				if (_pictureIterator != null)
				{
					_vTags.clear();
					for (final Tag t : _pictureIterator.getTagSet())
					{
						if (t.isSelectable())
						{
							_vTags.add(t);
						}
					}
					_tagList.setListData(_vTags);
				}
			}
			catch (final OperationNotSupportedException e)
			{
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	@Override
	protected void savePictureTags()
	{
		if (!_bDoNotSaveTags)
		{
			synchronized (_lock)
			{
				if (_pictureIterator != null)
				{
					if (_picture != null)
					{
						_picture.setTagList(_tagList.getSelectedValuesList());
					}
				}
			}
		}
	}
}
