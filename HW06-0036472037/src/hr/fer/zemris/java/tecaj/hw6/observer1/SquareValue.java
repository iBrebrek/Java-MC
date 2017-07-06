package hr.fer.zemris.java.tecaj.hw6.observer1;

/**
 * An action in Observer pattern.
 * Instances of {@code SquareValue} class write a square of the 
 * integer stored in the {@code IntegerStorage} to the standard output
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.4.2016.)
 */
public class SquareValue implements IntegerStorageObserver {

	/**
	 * Writes a square of the  integer stored in the 
	 * {@code istorage} to the standard output.
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		int value = istorage.getValue();
		int square = value * value;
		System.out.printf("Provided new value: %d, square is %d\r", value, square);	
	}

}
