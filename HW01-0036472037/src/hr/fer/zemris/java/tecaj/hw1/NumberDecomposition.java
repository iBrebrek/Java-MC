package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program koji printa sve proste faktore zadanog broja.
 * Program prima točno jedan argument.
 * Taj argument mora biti prirodni broj veći od 1.
 * 
 * @author Ivica B.
 * @version 1.0
 */
public class NumberDecomposition {

	/**
	 * Metoda koja započinje program.
	 * @param args argumenti programa
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Wrong number of arguments.");
			return;
		}
		
		long number = Long.parseLong(args[0]);
		if(number <= 1l) {
			System.out.println("Argument must be natural number greater than 1.");
			return;
		}
		
		System.out.printf("You requested decomposition of number %d onto prime factors. Here they are:%n", number);
		
		int counter = 0;
		long divider = 2l;
		while(number != 1l) {
			if(isPrime(divider)) {
				while((number % divider) == 0l) {
					counter++;
					System.out.printf("%d. %d%n", counter, divider);
					number /= divider;
				}
			}
			divider++;
		}
	}

	/**
	 * Metoda koja provjerava je li broj prost.
	 * @param number provjerava se je li ovaj broj prost
	 * @return true ako je prost, false ako nije
	 */
	private static boolean isPrime(long number) {
		if(number == 2l) {
			return true;
		}
		if(number % 2l == 0l) {
			return false;
		}
		for(long half = number/2l, tester = 3l; tester < half; tester += 2l) {
			if(number % tester == 0l) {
				return false;
			}
		}
		return true;
	}

}
