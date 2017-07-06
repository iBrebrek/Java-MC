package hr.fer.zemris.java.tecaj.hw6.observer2;

/**
 * Instances of {@code IntegerStorageChange} encapsulate (as read-only properties) 
 * following information: 
 * a reference to {@code IntegerStorage}, 
 * the value of stored integer before the change has occurred,
 * and the new value of currently stored integer.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.4.2016.)
 */
public class IntegerStorageChange {
	/** A reference to the storage. */
	private final IntegerStorage storage;
	/** Value before change. */
	private final int oldValue;
	/** Value after change. */
	private final int newValue;
	
	/**
	 * Initializes read-only properties of this object.
	 * 
	 * @param storage		a reference to the storage.
	 * @param oldValue		value before change.
	 * @param newValue		value after change.
	 */
	public IntegerStorageChange(IntegerStorage storage, int oldValue, int newValue) {
		super();
		this.storage = storage;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Getter for storage reference.
	 * 
	 * @return storage reference.
	 */
	public IntegerStorage getStorage() {
		return storage;
	}

	/**
	 * Getter for value before change.
	 * 
	 * @return value before change.
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * Getter for value after change.
	 * 
	 * @return value after change.
	 */
	public int getNewValue() {
		return newValue;
	}
}
