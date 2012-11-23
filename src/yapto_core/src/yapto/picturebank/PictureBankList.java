package yapto.picturebank;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.eventbus.EventBus;

/**
 * Keeps track of all {@link IPictureBank}s known to the application, and also
 * of which ones are selected.
 * 
 * @author benobiwan
 * 
 */
public final class PictureBankList
{
	/**
	 * Map of all {@link IPictureBank}.
	 */
	private final Map<Integer, IPictureBank<?>> _pictureBankMap = new HashMap<>();

	/**
	 * Set of all {@link IPictureBank}.
	 */
	private final SortedSet<IPictureBank<?>> _allPictureBankSet = new TreeSet<>();

	/**
	 * Set of selected {@link IPictureBank}.
	 */
	private final SortedSet<IPictureBank<?>> _selectedPictureBankSet = new TreeSet<>();

	/**
	 * {@link EventBus} used to signal registered objects of changes in this
	 * {@link PictureBankList}.
	 */
	protected final EventBus _bus;

	/**
	 * Creates a new {@link PictureBankList}.
	 * 
	 * @param bus
	 *            the {@link EventBus} used to signal registered objects of
	 *            changes in this {@link PictureBankList}.
	 */
	public PictureBankList(final EventBus bus)
	{
		_bus = bus;
	}

	/**
	 * Add a new {@link IPictureBank}.
	 * 
	 * @param pictureBank
	 *            the new {@link IPictureBank}.
	 */
	public void addPictureBank(final IPictureBank<?> pictureBank)
	{
		if (pictureBank != null)
		{
			_pictureBankMap.put(Integer.valueOf(pictureBank.getId()),
					pictureBank);
			_allPictureBankSet.add(pictureBank);
			_bus.post(new PictureBankListChangedEvent());
		}
	}

	/**
	 * Remove a {@link IPictureBank}.
	 * 
	 * @param iId
	 *            id of the {@link IPictureBank} to remove.
	 */
	public void removePictureBank(final int iId)
	{
		final IPictureBank<?> pictureBank = _pictureBankMap.remove(Integer
				.valueOf(iId));
		if (pictureBank != null)
		{
			_allPictureBankSet.remove(pictureBank);
			_selectedPictureBankSet.remove(pictureBank);
			_bus.post(new PictureBankListChangedEvent());
		}
	}

	/**
	 * Select {@link IPictureBank}s.
	 * 
	 * @param iIds
	 *            ids of the {@link IPictureBank} to select.
	 */
	public void selectPictureBank(final int... iIds)
	{
		_selectedPictureBankSet.clear();
		for (final int iId : iIds)
		{
			_selectedPictureBankSet.add(_pictureBankMap.get(Integer
					.valueOf(iId)));
		}
	}

	/**
	 * Get the selected {@link IPictureBank}s.
	 * 
	 * @return the selected {@link IPictureBank}s.
	 */
	public Set<IPictureBank<?>> getSelectedPictureBank()
	{
		return Collections.unmodifiableSet(_selectedPictureBankSet);
	}

	/**
	 * Get all {@link IPictureBank}s.
	 * 
	 * @return all {@link IPictureBank}s.
	 */
	public Set<IPictureBank<?>> getAllPictureBank()
	{
		return Collections.unmodifiableSet(_allPictureBankSet);
	}

	/**
	 * Register an object to the listen for change in this
	 * {@link PictureBankList}.
	 * 
	 * @param object
	 *            the object to register.
	 */
	public void register(Object object)
	{
		_bus.register(object);
	}
}
