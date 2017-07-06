package hr.fer.zemris.java.tecaj.hw6.observer1;

/**
 * An action in Observer pattern.
 * Instances of {@code ChangeCounter} count, and write to the standard output, 
 * the number of times value stored integer has been changed since the registration.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.4.2016.)
 */
public class ChangeCounter implements IntegerStorageObserver {
	/** Number of value changes since registration. */
	private int counter = 0;

	/**
	 * Counts how many times value has changed.
	 * To the standard output writes said counted number.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		counter++;
		System.out.println("Number of value changes since tracking: " + counter);
	}

}
