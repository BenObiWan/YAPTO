package yapto.picturebank;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import yapto.picturebank.config.IPictureBankConfiguration;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

/**
 * Keeps track of all {@link IPictureBankConfiguration}s known to the
 * application, and also of which {@link IPictureBank} are selected and loaded.
 * 
 * @author benobiwan
 * 
 */
public final class PictureBankList
{
	/**
	 * Set of all {@link IPictureBankConfiguration}.
	 */
	private final SortedSet<IPictureBankConfiguration> _allPictureBankConfSet = new TreeSet<>();

	/**
	 * Map with selected {@link IPictureBankConfiguration} and the corresponding
	 * {@link IPictureBank} object as value.
	 */
	private final Map<IPictureBankConfiguration, IPictureBank<?>> _selectedPictureBankMap = new HashMap<>();

	/**
	 * {@link EventBus} used to signal registered objects of changes in this
	 * {@link PictureBankList}.
	 */
	private final EventBus _bus;

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
	 * @param pictureBankConfiguration
	 *            {@link IPictureBankConfiguration} of the new
	 *            {@link IPictureBank}.
	 */
	public void addPictureBank(
			final IPictureBankConfiguration pictureBankConfiguration)
	{
		if (pictureBankConfiguration != null)
		{
			_allPictureBankConfSet.add(pictureBankConfiguration);
			_bus.post(new PictureBankListChangedEvent());
		}
	}

	/**
	 * Remove a {@link IPictureBank}.
	 * 
	 * @param pictureBankConfiguration
	 *            {@link IPictureBankConfiguration} of the {@link IPictureBank}
	 *            to remove.
	 */
	public void removePictureBank(
			final IPictureBankConfiguration pictureBankConfiguration)
	{
		_allPictureBankConfSet.remove(pictureBankConfiguration);
		// TODO remove from _selectedPictureBankMap
		_bus.post(new PictureBankListChangedEvent());
	}

	/**
	 * Select {@link IPictureBank}s.
	 * 
	 * @param confList
	 *            {@link IPictureBankConfiguration} of the {@link IPictureBank}
	 *            to select.
	 */
	public void selectPictureBank(final IPictureBankConfiguration... confList)
	{
		final Set<IPictureBankConfiguration> confToLoad = Sets
				.newHashSet(confList);
		// Closing no longer used {@link IPictureBank}.
		for (final IPictureBankConfiguration bankToClose : Sets.difference(
				_selectedPictureBankMap.keySet(), confToLoad))
		{
			// TODO
		}
		// Opening new {@link IPictureBank}.
		for (final IPictureBankConfiguration bankToClose : Sets.difference(
				confToLoad, _selectedPictureBankMap.keySet()))
		{
			// TODO
		}
	}

	/**
	 * Get the selected {@link IPictureBank}s.
	 * 
	 * @return the selected {@link IPictureBank}s.
	 */
	public Set<IPictureBank<?>> getSelectedPictureBank()
	{
		return Collections.unmodifiableSet(Sets
				.newHashSet(_selectedPictureBankMap.values()));
	}

	/**
	 * Get all {@link IPictureBank}s.
	 * 
	 * @return all {@link IPictureBank}s.
	 */
	public Set<IPictureBankConfiguration> getAllPictureBankConfiguration()
	{
		return Collections.unmodifiableSet(_allPictureBankConfSet);
	}

	/**
	 * Register an object to the listen for change in this
	 * {@link PictureBankList}.
	 * 
	 * @param object
	 *            the object to register.
	 */
	public void register(final Object object)
	{
		_bus.register(object);
	}
}
