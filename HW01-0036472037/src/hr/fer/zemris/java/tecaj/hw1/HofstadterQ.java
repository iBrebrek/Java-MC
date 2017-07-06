package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program koji računa i-ti broj Hofstadterovog Q reda.
 * Broj i se unosi kao argument programa.
 * Argument mora biti jedan cijeli pozivitni broj.
 * 
 * @author Ivica B.
 * @version 1.0
 */
public class HofstadterQ {

	/**
	 * Metoda koja pokreće program.
	 * @param args i-ti broj Hofstadterovog Q reda
	 */
	public static void main(String[] args) {
		
		if(args.length != 1) {
			System.out.println("Only 1 and exactly 1 argument is required.");
			return;
		}
		long input = Long.parseLong(args[0]);
		if(input < 1l) {
			System.out.println("Argument must be positive.");
			return;
		}
		long result = hofQ(input);
		System.out.printf("You requested calculation of %d number of Hofstadter's Q-sequence. "
						+ "The requested number is %d.%n", input, result);

	}

	/**
	 * Računa i-ti broj Hofstadterovog Q reda.
	 * Prima i vraća long vrijednost.
	 * @param i redni broj elementa koji se tra�i
	 * @return broj na i-tom mjestu
	 */
	private static long hofQ(long i) {
		if(i < 3l) {
			return 1l;
		}
		return hofQ(i - hofQ(i-1l)) + hofQ(i - hofQ(i-2l));
	}

}
