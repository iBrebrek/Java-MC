package hr.fer.zemris.java.tecaj.hw6.observer2;

import java.util.ArrayList;
import java.util.List;


/**
 * The Subject in Observer pattern.
 * This class allows adding/removing observers that will be notified once stored integer is changed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.4.2016.)
 */
public class IntegerStorage {
	/** Value stored by this class. */
	private int value;
	/** Observers that will be notified if {@code value} changes. */
	private List<IntegerStorageObserver> observers;
	/** {@code true} if observers are being iterated. */
	private boolean iterating = false;

	/**
	 * Initializes integer value of a new object.
	 * 
	 * @param initialValue		starting value for stored integer value.
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Adds observer that will be notified if stored value is changed.
	 * Does noting if {@code observer} is already added.
	 * 
	 * @param observer		observer that will be notified once value is changed.
	 * 
	 * @throws IllegalArgumentException if {@code observer} is {@code null}.
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if(observer == null) {
			throw new IllegalArgumentException("Observer can not be null.");
		}
		if(observers == null) {
			observers = new ArrayList<>();
		}
		if(!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	/**
	 * Given observer will be no more notified if value is changed.
	 * If {@code observer} is {@code null} or was not even observing then nothing is done.
	 * 
	 * @param observer		observer that won't be notified anymore.
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if(observers == null) return;
		if(iterating) {
			Bin.add(observer);
		} else {
			observers.remove(observer);
		}
	}

	/**
	 * After calling this all observers that were added are now removed.
	 */
	public void clearObservers() {
		observers = null;
	}

	/**
	 * Returns stored integer value.
	 * 
	 * @return stored value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Changes stored integer value.
	 * If given value is same as stored value nothing is done.
	 * When value is changed all observers will be notified about change.
	 * 
	 * @param value		new stored value.
	 */
	public void setValue(int value) {
		// Only if new value is different than the current value:
		if (this.value != value) {
			// Update current value
			IntegerStorageChange storageChange = new IntegerStorageChange(this, this.value, value);
			this.value = value;
			// Notify all registered observers
			if (observers != null) {
				iterating = true;
				for (IntegerStorageObserver observer : observers) {
					observer.valueChanged(storageChange);
				}
				iterating = false;
				Bin.delete(observers);
			}
		}
	}
	
	/**
	 * Used like a trash can. Elements will be added to this class and when delete is called
	 * all added items to trash can will be removed from given list and trash can will be cleared.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (16.4.2016.)
	 */
	private static class Bin {
		/** Elements that will be delete. */
		private static List<IntegerStorageObserver> bin = new ArrayList<>();
		
		/**
		 * Adds element to delete-queue (trash can).
		 * 
		 * @param observer		element added to trash can.
		 */
		private static void add(IntegerStorageObserver observer) {
			bin.add(observer);
		}
		
		/**
		 * In given list deletes all elements from bin(trash can).
		 * After deleting empties trash can.
		 * 
		 * @param original		list from which elements will be deleted
		 */
		private static void delete(List<IntegerStorageObserver> original) {
			original.removeAll(bin);
			bin.clear();
		}
	}
}