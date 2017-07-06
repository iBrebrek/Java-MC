package hr.fer.zemris.java.webapp.dao;

import hr.fer.zemris.java.webapp.dao.sql.SQLDAO;
import hr.fer.zemris.java.webapp.dao.DAO;

/**
 * Singleton class used to stored object
 * which implements {@link DAO}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
public class DAOProvider {

	/** Instance of DAO object. */
	private static DAO dao = new SQLDAO();
	
	/**
	 * Retrieves DAO object.
	 * 
	 * @return instance of DAO object.
	 */
	public static DAO getDao() {
		return dao;
	}
	
}