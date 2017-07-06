package hr.fer.zemris.java.hw15.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Domain class BlogEntry models a single blog entry.
 * Entry is linked to all its 
 * comments by {@link #comments}.
 * Entry is linked to its
 * author by {@link #creator}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@Entity
@Table(name="blog_entries")
@Cacheable(true)
public class BlogEntry {

	/** Entry id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** All comments for this entry. */
	@OneToMany(mappedBy="blogEntry", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
	private List<BlogComment> comments = new ArrayList<>();
	
	/** When was entry made. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date createdAt;
	
	/** Last time when entry was updated. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	private Date lastModifiedAt;
	
	/** Entry title. */
	@Column(nullable=false, length=60)
	private String title;
	
	/** Entry text. */
	@Column(nullable=false, length=4*1024)
	private String text;
	
	/** Author of entry. */
	@ManyToOne
	@JoinColumn(nullable=false)
	private BlogUser creator;
	
	/**
	 * Retrieves entry id.
	 * 
	 * @return entry id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets entry id.
	 * 
	 * @param id 	entry id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Retrieves list of comments for this entry.
	 * 
	 * @return list of comments for this entry.
	 */
	public List<BlogComment> getComments() {
		return comments;
	}
	/**
	 * Sets list of comments for this entry.
	 * 
	 * @param comments 		list of comments for this entry.
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Retrieves date when entry was made.
	 * 
	 * @return date when entry was made.
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets date when entry was made.
	 * 
	 * @param createdAt		date when entry was made.
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Retrieves date when entry was updated.
	 * 
	 * @return date when entry was updated.
	 */
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Sets date when entry was updated.
	 * 
	 * @param lastModifiedAt	date when entry was updated.
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Retrieves entry title.
	 * 
	 * @return entry title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets entry title.
	 * 
	 * @param title		entry title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Retrieves entry text.
	 * 
	 * @return entry text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets entry text.
	 * 
	 * @param text		entry text.
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Retrieves author.
	 * 
	 * @return entry author.
	 */
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Sets author.
	 * 
	 * @param creator		author.
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}