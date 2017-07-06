package hr.fer.zemris.java.tecaj.hw6.observer1;

/**
 * The Observe interface in Observer pattern.
 * Offers a single method that will do something once observed value is changed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.4.2016.)
 */
public interface IntegerStorageObserver {
	
	/**
	 * Does something once once value stored is {@code IntegerStorage} is changed.
	 * 
	 * @param istorage		storage being observed where changed value is.
	 */
	public void valueChanged(IntegerStorage istorage);
}