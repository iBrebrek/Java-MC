package hr.fer.zemris.java.tecaj.hw6.demo3;

/**
 * First given example for third problem (PrimesCollection).
 */
public class PrimesDemo1 {
	
	/**
	 * Entry point for this program.
	 * 
	 * @param args	not used.
	 */
	public static void main(String[] args) {
		
		PrimesCollection primesCollection = new PrimesCollection(5); // 5: how many of them
		for(Integer prime : primesCollection) {
			System.out.println("Got prime: "+prime);
		}
	}

}
