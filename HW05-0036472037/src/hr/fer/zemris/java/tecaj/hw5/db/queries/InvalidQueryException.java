package hr.fer.zemris.java.tecaj.hw5.db.queries;

/**
 * Exception used when query is incorrect.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class InvalidQueryException extends RuntimeException {
	/** Used in serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes message for this exception.
	 * 
	 * @param message	description of exception.
	 */
	public InvalidQueryException(String message) {
		super(message);
	}

}
