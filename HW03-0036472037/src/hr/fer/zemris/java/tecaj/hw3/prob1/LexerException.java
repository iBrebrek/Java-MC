package hr.fer.zemris.java.tecaj.hw3.prob1;

/**
 * Exception used when there is a problem with creating tokens or with lexical analysis.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (26.3.2016.)
 */
@SuppressWarnings("serial")
public class LexerException extends RuntimeException{
	
	/**
	 * Constructor for lexical analysis exceptions.
	 * 
	 * @param message 	describes this exception.
	 */
	public LexerException(String message) {
		super(message);
	}

}
