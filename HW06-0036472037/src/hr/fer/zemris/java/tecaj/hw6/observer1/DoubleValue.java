package hr.fer.zemris.java.tecaj.hw6.observer1;

/**
 * An action in Observer pattern.
 * 
 * Instances of {@code DoubleValue} class write to the standard output double value 
 * of the current value which is stored in subject, but only first n times since 
 * its registration with subject (n is given in constructor).
 * 
 * After writing the double value for the n-th time, 
 * the observer automatically de-registers itself from the subject.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.4.2016.)
 */
public class DoubleValue implements IntegerStorageObserver {
	/** When this hits 0 this object stops observing. */
	private int countDown;
	
	/**
	 * Initializes how many times value can be changed before this object de-registers itself.
	 * 
	 * @param countDown		"turns" before de-registration.
	 * 
	 * @throws IllegalArgumentException	if {@code countDown} is less than 1.
	 */
	public DoubleValue(int countDown) {
		if(countDown < 1) {
			throw new IllegalArgumentException("Countdown for DoubleValue must be a positive number.");
		}
		this.countDown = countDown;
	}

	/**
	 * Writes double value of stored value to standard output.
	 * If value was changed said amount of times this object de-registers itself from given storage.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.println("Double value: " + istorage.getValue()*2);
		countDown--;
		if(countDown < 1) {
			istorage.removeObserver(this);
		}
	}

}
