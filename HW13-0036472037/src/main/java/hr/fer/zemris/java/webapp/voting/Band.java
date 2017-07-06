package hr.fer.zemris.java.webapp.voting;

/**
 * Class containing data about a single band.
 * Each band has ID, name, song link and number of votes.
 * Bands are equals if they have same ID.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (6.6.2016.)
 */
public class Band {
	/** Band ID. */
	private int id;
	/**Band name. */
	private String name;
	/** Web link to band's song. */
	private String songLink;
	/** Number of votes for this band. */
	private int votes = 0;
	
	/** Empty constructor. */
	public Band() {
	}
	
	/**
	 * Initializes everything but votes.
	 * 
	 * @param id		band ID.
	 * @param name		band name.
	 * @param songLink	link to band song.
	 */
	public Band(int id, String name, String songLink) {
		this.id = id;
		this.name = name;
		this.songLink = songLink;
	}
	
	/**
	 * Retrieves number of votes.
	 * 
	 * @return number of votes.
	 */
	public int getVotes() {
		return votes;
	}

	/**
	 * Sets number of votes.
	 * 
	 * @param votes		number of votes.
	 */
	public void setVotes(int votes) {
		this.votes = votes;
	}

	/**
	 * Retrieves band ID.
	 * 
	 * @return band ID.
	 */
	public int getId() {
		return id;
	}
	
	/** 
	 * Sets band ID.
	 * 
	 * @param id	band ID.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Retrieves band name.
	 * 
	 * @return band name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets band name.
	 * 
	 * @param name		band name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves web link to one band song.
	 * 
	 * @return link to band song.
	 */
	public String getSongLink() {
		return songLink;
	}
	
	/**
	 * Sets link to band song.
	 * 
	 * @param songLink		link to band song.
	 */
	public void setSongLink(String songLink) {
		this.songLink = songLink;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Band other = (Band) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
