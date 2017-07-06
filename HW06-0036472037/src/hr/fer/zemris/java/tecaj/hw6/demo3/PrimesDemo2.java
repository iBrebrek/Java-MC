package hr.fer.zemris.java.tecaj.hw6.demo3;

/**
 * Second given example for third problem (PrimesCollection).
 */
public class PrimesDemo2 {

	/**
	 * Entry point for this program.
	 * 
	 * @param args	not used.
	 */
	public static void main(String[] args) {

		PrimesCollection primesCollection = new PrimesCollection(2);
		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: " + prime + ", " + prime2);
			}
		}
	}

}
