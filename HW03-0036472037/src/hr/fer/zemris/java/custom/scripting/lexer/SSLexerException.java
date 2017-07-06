package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception used in smart script lexer.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
@SuppressWarnings("serial")
public class SSLexerException extends RuntimeException {
	
	/**
	 * Initializes message for this exception.
	 * 
	 * @param message	description of this exception.
	 */
	public SSLexerException(String message) {
		super(message);
	}

}
