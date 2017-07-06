package hr.fer.zemris.java.hw15.jpa;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.hw15.dao.DAOException;

/**
 * Storage of all entity managers.
 * It uses lazy fetching, will
 * generate entity manager only
 * if method {@link #getEntityManager()} 
 * is called.
 * Once manager is created it is
 * saved for that thread until 
 * {@link #close()} is called.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
public class JPAEMProvider {

	/** Map of all entity managers. */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Retrieves entity manager for current thread.
	 * On fist call transaction is started.
	 * 
	 * @return entity manager for current thread.
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if(ldata==null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Closes entity manager for current thread
	 * and commits transaction.
	 * 
	 * @throws DAOException if unable to commit transaction
	 * 						or unable to close entity manager.
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if(ldata==null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch(Exception ex) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex!=null) throw dex;
	}
	
	/**
	 * Data saved for current thread.
	 */
	private static class LocalData {
		/** Entity manager for current thread. */
		EntityManager em;
	}
	
}