package hr.fer.zemris.java.webapp.model;

/**
 * Data about single poll.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
public class Poll {
	/** Poll id in database. */
	private final long id;
	/** Poll title. */
	private final String title;
	/** Poll message. */
	private final String message;
	
	/**
	 * Initializes a single poll.
	 * 
	 * @param id		poll id.
	 * @param title		poll title.
	 * @param message	poll message.
	 */
	public Poll(long id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * Retrieves poll id.
	 * 
	 * @return poll id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Retrieves poll title.
	 * 
	 * @return poll title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Retrieves poll message.
	 * 
	 * @return poll message.
	 */
	public String getMessage() {
		return message;
	}
}
