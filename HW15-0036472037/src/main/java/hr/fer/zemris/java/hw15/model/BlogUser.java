package hr.fer.zemris.java.hw15.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Domain class BlogUser models a single blog user.
 * User is linked to all its 
 * entries by {@link #entries}.
 * User has nick and password
 * which are used to login.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@Entity
@Table(name="blog_users")
public class BlogUser {
	
	/** User id. */
	@Id
	@GeneratedValue
	private long id;
	
	/** User's first name. */
	@Column(nullable=false, length=25)
	private String firstName;
	
	/** User's last name. */
	@Column(nullable=false, length=25)
	private String lastName;
	
	/** User's nick. */
	@Column(unique=true, nullable=false, length=25)
	private String nick;
	
	/** User's email. */
	@Column(nullable=false, length=50)
	private String email;
	
	/** Hashed user's password. */
	@Column(nullable=false, length=40)
	private String passwordHash;
	
	/** All blog entries made by this user. */
	@OneToMany(mappedBy="creator", fetch=FetchType.LAZY)
	@OrderBy("createdAt")
	private List<BlogEntry> entries = new ArrayList<>();

	/**
	 * Retrieves user's id. 
	 * 
	 * @return user's id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets user's id.
	 * 
	 * @param id	user's id.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Retrieves user's first name. 
	 * 
	 * @return user's first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets user's first name.
	 * 
	 * @param firstName		user's first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Retrieves user's last name.
	 * 
	 * @return user's last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets user's last name.
	 * 
	 * @param lastName		user's last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Retrieves user's nick.
	 * 
	 * @return user's nick.
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets user's nick.
	 * 
	 * @param nick		user's nick.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Retrieves user's email.
	 * 
	 * @return user's email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets user's email.
	 * 
	 * @param email		user's email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Retrieves hashed user's password.
	 * 
	 * @return hashed user's password.
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Sets user's hahsed password.
	 * 
	 * @param passwordHash		hashed password.
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * Retrieves all user's blog entries.
	 * 
	 * @return all user's blog entries.
	 */
	public List<BlogEntry> getEntries() {
		return entries;
	}

	/**
	 * Sets user's blog entries.
	 * 
	 * @param entries	blog entries.
	 */
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		BlogUser other = (BlogUser) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
