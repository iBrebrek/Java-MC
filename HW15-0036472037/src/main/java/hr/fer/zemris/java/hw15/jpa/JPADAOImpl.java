package hr.fer.zemris.java.hw15.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.hw15.dao.DAO;
import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * This is an implementation of {@link DAO} using JPA.
 * <br>
 * This implementation uses {@link JPAEMProvider}
 * to retrieve entity manager which can talk to database.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getEntry(Long id) throws DAOException {
		if(id == null) return null;
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}
	
	@Override
	public BlogEntry putEntry(BlogUser creator, Long id, String title, String text) {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		BlogEntry entry = getEntry(id);
		if(entry == null) {
			if(id != null) {
				return null;
			}
			entry = new BlogEntry();
		}
		entry.setTitle(title);
		entry.setText(text);
		entry.setCreator(creator);
		entry.setLastModifiedAt(new Date());
		if(entry.getCreatedAt() == null) {
			entry.setCreatedAt(entry.getLastModifiedAt());
		}
		
		em.persist(entry);
		
		return entry;
	}
	
	@Override
	public BlogUser getUser(String nick) {
		@SuppressWarnings("unchecked")
		List<BlogUser> user = JPAEMProvider.getEntityManager()
				.createQuery("select u from BlogUser as u where u.nick=:n")
				.setParameter("n", nick)
				.getResultList();
		if(user.isEmpty()) {
			return null;
		} else {
			return user.get(0);
		}
	}

	@Override
	public BlogUser createUser(String firstName, String lastName, String email, String nick, String passwordHash) {
		EntityManager em = JPAEMProvider.getEntityManager();
				
		BlogUser user = new BlogUser();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setNick(nick);
		user.setPasswordHash(passwordHash);
		
		em.persist(user);
		
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlogUser> getAllUsers() {
		return JPAEMProvider.getEntityManager()
				.createQuery("select u from BlogUser u")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlogEntry> getAllEntries(Long userID) {
		BlogUser user = JPAEMProvider.getEntityManager().find(BlogUser.class, userID);
		return JPAEMProvider.getEntityManager()
				.createQuery("select e from BlogEntry as e where e.creator=:u")
				.setParameter("u", user)
				.getResultList();
	}

	@Override
	public BlogComment addComment(Long eid, String message, String email) {
		EntityManager em = JPAEMProvider.getEntityManager();
		
		BlogComment blogComment = new BlogComment();
		blogComment.setUsersEMail(email);
		blogComment.setPostedOn(new Date());
		blogComment.setMessage(message);
		blogComment.setBlogEntry(getEntry(eid));
		
		em.persist(blogComment);

		return blogComment;
	}
}