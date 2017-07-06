package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception used in smart script parser.
 * Any exception that happens in said parser will be re-thrown with this exception.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
@SuppressWarnings("serial")
public class SmartScriptParserException extends RuntimeException {
	
	/**
	 * Initializes message of new exception.
	 * 
	 * @param message 	description of exception.
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}
}
