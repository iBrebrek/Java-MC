package hr.fer.zemris.java.custom.collections;

/**
 * Exception used when trying to take element from an empty stack.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (21.3.2016.)
 */
public class EmptyStackException extends RuntimeException {
	/** */
	private static final long serialVersionUID = -2097566153666133661L;

	/**
	 * Defines message for for this exception.
	 * 
	 * @param message	description of exception
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
