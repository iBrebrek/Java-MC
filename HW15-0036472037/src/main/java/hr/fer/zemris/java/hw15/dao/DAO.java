package hr.fer.zemris.java.hw15.dao;

import java.util.List;

import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Interface used to use database.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
public interface DAO {

	/**
	 * Retrieves entry with given <code>id</code>.
	 * If there is no entry with that id
	 * <code>null</code> is returned.
	 * 
	 * @param id		blog entry id.
	 * @return blog entry or <code>null</code> if doesn't exist.
	 */
	BlogEntry getEntry(Long id);
	
	/**
	 * Updates or creates new blog entry, or returns null.
	 * If given id is null then this method will create
	 * new entry, if id is not null then this method
	 * updates entry with given id and if there is no 
	 * entry with given id this method returns null.
	 * 
	 * @param creator	creator of entry.
	 * @param id		id of entry which will be updated,
	 * 					or null if new entry should be created.
	 * @param title		entry title.
	 * @param text		entry text.
	 * @return created or updated entry, or null if unable to update.
	 */
	BlogEntry putEntry(BlogUser creator, Long id, String title, String text);
	
	/**
	 * Retrieves all entries made by user with given id.
	 * 
	 * @param userID	author id.
	 * @return all entries of given author id.
	 */
	List<BlogEntry> getAllEntries(Long userID);
	
	/**
	 * Creates new blog comment.
	 * 
	 * @param eid		blog id.
	 * @param message	comment message.
	 * @param email		user's email.
	 * @return created comment.
	 */
	BlogComment addComment(Long eid, String message, String email);

	/**
	 * Retrieves user with given nick.
	 * 
	 * @param nick	user's nick.
	 * @return blog user.
	 */
	BlogUser getUser(String nick);
	
	/**
	 * Retrieves list of all blog users.
	 * 
	 * @return list of all blog users.
	 */
	List<BlogUser> getAllUsers();
	
	/**
	 * Creates new blog user.
	 * 
	 * @param firstName			user's first name.
	 * @param lastName			user's last name.
	 * @param email				user's email.
	 * @param nick				user's nick.
	 * @param passwordHash		hashed user's password.
	 * @return created user.
	 */
	BlogUser createUser(String firstName, String lastName, String email, String nick, String passwordHash);
}