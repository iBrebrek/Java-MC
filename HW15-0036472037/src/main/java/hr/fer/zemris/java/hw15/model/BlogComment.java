package hr.fer.zemris.java.hw15.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Domain class BlogComment models a single blog comment.
 * Comment can not have null values.
 * Comment is linked to blog entry 
 * by {@link #blogEntry}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {

	/** Comment id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Entry to which this comment belong. */
	@ManyToOne
	@JoinColumn(nullable=false)
	private BlogEntry blogEntry;
	
	/** EMail of user who created comment. */
	@Column(length=100, nullable=false)
	private String usersEMail;
	
	/** Comment message. */
	@Column(length=4096, nullable=false)
	private String message;
	
	/** When was comment posted. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date postedOn;
	
	/**
	 * Retrieves comment id.
	 * 
	 * @return comment id.
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets comment id.
	 * 
	 * @param id		comment id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Retrieves blog entry which was commented.
	 * 
	 * @return commented blog entry.
	 */
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Sets commented blog entry.
	 * 
	 * @param blogEntry		commented blog entry.
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Retrieves creator's email.
	 * 
	 * @return creator's email.
	 */
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Sets creator's email.
	 * 
	 * @param usersEMail		creator's email.
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Retrieves message.
	 * 
	 * @return comment message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets comment message.
	 * 
	 * @param message		comment message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Retrieves date when 
	 * comment was posted.
	 * 
	 * @return date when comment was posted.
	 */
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets date when comment was posted.
	 * 
	 * @param postedOn		date when comment was posted.
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}