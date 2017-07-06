package hr.fer.zemris.java.hw15.dao;

/**
 * Runtime exception used when
 * there is a problem with database.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
public class DAOException extends RuntimeException {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	 /**
     * Constructs a new DAO exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this DAO exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 
	 * Constructs a new DAO exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
	public DAOException(String message) {
		super(message);
	}
}