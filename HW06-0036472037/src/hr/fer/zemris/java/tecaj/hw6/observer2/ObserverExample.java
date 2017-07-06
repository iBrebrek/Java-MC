package hr.fer.zemris.java.tecaj.hw6.observer2;

/**
 * Slightly modified example given for first problem.
 */
public class ObserverExample {
	
	/**
	 * Entry point for this program.
	 * 
	 * @param args	not used.
	 */
	public static void main(String[] args) {
		
		IntegerStorage istorage = new IntegerStorage(20);
		
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(3));
		istorage.addObserver( new SquareValue());
	
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}
}