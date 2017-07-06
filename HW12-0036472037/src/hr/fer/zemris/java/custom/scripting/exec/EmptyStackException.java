package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Exception used when trying to take element from an empty stack.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (17.4.2016.)
 */
public class EmptyStackException extends RuntimeException {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Defines message for for this exception.
	 * 
	 * @param message	description of exception.
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
