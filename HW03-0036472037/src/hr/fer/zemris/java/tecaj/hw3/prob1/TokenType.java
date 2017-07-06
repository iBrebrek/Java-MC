package hr.fer.zemris.java.tecaj.hw3.prob1;

/**
 * Enumeration that defines type of a token.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (26.3.2016.)
 */
public enum TokenType {
	/** End of file. */
	EOF, 
	/** Sequence of letters, every letter return true from Character.isLetter */
	WORD, 
	/** Instances of class Long. */
	NUMBER, 
	/** Instances of class Character */
	SYMBOL;
}
