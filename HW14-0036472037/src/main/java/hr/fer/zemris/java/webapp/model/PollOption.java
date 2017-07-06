package hr.fer.zemris.java.webapp.model;

/**
 * Data about single poll option.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
public class PollOption {
	/** Poll option id in database. */
	private final long id;
	/** Poll option title. */
	private final String title;
	/** Poll option link. */
	private final String link;
	/** Poll option number of votes. */
	private final long votesCount;
	
	/**
	 * Initializes new poll option.
	 * 
	 * @param id			id in database.
	 * @param title			option name.
	 * @param link			option link.
	 * @param votesCount	number of votes this option has.
	 */
	public PollOption(long id, String title, String link, long votesCount) {
		this.id = id;
		this.title = title;
		this.link = link;
		this.votesCount = votesCount;
	}

	/**
	 * Retrieves id in database.
	 * 
	 * @return id in database.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Retrieves poll option's name.
	 * 
	 * @return poll option's name.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Retrieves poll option's link.
	 * 
	 * @return poll option's link.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Retrieves poll option's number of votes.
	 * 
	 * @return number of votes.
	 */
	public long getVotesCount() {
		return votesCount;
	}
}
