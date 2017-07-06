package hr.fer.zemris.java.hw15.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Stores entity manager factory.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
public class JPAEMFProvider {

	/** Entity manager factory. */
	public static EntityManagerFactory emf;
	
	/**
	 * Retrieves entity manager factory.
	 * 
	 * @return entity manager factory.
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Sets entity manager factory.
	 * 
	 * @param emf		entitiy manager factory.
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}