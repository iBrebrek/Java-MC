package hr.fer.zemris.java.hw15.dao;

import hr.fer.zemris.java.hw15.jpa.JPADAOImpl;

/**
 * Singleton class used to store object
 * which implements {@link DAO}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
public class DAOProvider {

	/** Instance of DAO object. */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Retrieves DAO object.
	 * 
	 * @return instance of DAO object.
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}