package hr.fer.zemris.java.tecaj.hw1;

/**
 * Printa prvih n prostih brojeva.
 * Program prima samo jedan argument, n.
 * n mora biti pozitivni prirodni broj.
 * 
 * @author Ivica B.
 * @version 1.0
 */
public class PrimeNumbers {

	/**
	 * Metoda koja započinje program.
	 * @param args argumenti programa
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Wrong number of arguments.");
			return;
		}
		
		int n = Integer.parseInt(args[0]);
		if(n < 1) {
			System.out.println("Argument must be positive natural number.");
			return;
		}
		
		System.out.printf("You requested calculation of %d prime numbers. Here they are:%n1. 2%n", n);
		
		int number = 3;
		int found = 1;
		boolean isPrime;
		int tester;
		
		for(; found < n; number += 2) {
			isPrime = true;
			for(tester = number - 2; tester > 1; tester -= 2) {
				if((number % tester) == 0) {
					isPrime = false;
					break;
				}
			}
			if(isPrime) {
				found++;
				System.out.printf("%d. %d%n", found, number);
			}
		}

	}

}
