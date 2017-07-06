package hr.fer.zemris.java.tecaj.hw6.demo3;

import java.util.Iterator;

/**
 * Collection of first n prime number. N is defined in constructor.
 * Nothing can be changed in this collection.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.4.2016.)
 */
public class PrimesCollection implements Iterable<Integer> {
	/** First how many prime numbers are in this collection. */
	private int size;

	/**
	 * Initializes how many primes numbers will be in this collection.
	 * First prime number is 2.
	 * 
	 * @param size	first n prime numbers.
	 */
	public PrimesCollection(int size) {
		super();
		this.size = size;
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new PrimeIterator();
	}
	
	/**
	 * Used for iterating through prime numbers.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (16.4.2016.)
	 */
	private class PrimeIterator implements Iterator<Integer> {
		/** Last iterated prime number. Default value is 1 and that is not first prime number. */
		private int current = 1; //starts from 2, but this will be incremented
		/** Number of iterated prime numbers. */
		private int counter = 0;

		/**
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return counter < size;
		}

		/**
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Integer next() {
			counter++;
			
			do {
				current++;
			}while(notPrime(current));
			
			return current;
		}
		
		/*
		 * isPrime would look nicer but made method notPrime instead,
		 * so there is no need to write !isPrime is do-while
		 */
		/**
		 * Checks if given number is NOT prime number.
		 * 
		 * @param number	number being checked.
		 * @return {@code true} if {@code number} is NOT prime, {@code false} if PRIME.
		 */
		private boolean notPrime(int number) {
			for(int half = number/2, tester = 2; tester <= half; tester ++) {
				if(number % tester == 0) {
					return true;
				}
			}
			return false;
		}
	}
}
