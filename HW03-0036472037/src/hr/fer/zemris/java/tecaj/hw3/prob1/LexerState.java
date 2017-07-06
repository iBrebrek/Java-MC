package hr.fer.zemris.java.tecaj.hw3.prob1;

/**
 * Two possible Lexer states.
 * Basic will will generate tokens normally into words, number and symbols,
 * while extended will generate only words.
 * Both treat EOF the same.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (26.3.2016.)
 */
public enum LexerState {
	/** Basic state before # */
	BASIC,
	/** New state after # */
	EXTENDED;
}
