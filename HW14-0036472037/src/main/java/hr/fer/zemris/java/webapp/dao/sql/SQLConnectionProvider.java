package hr.fer.zemris.java.webapp.dao.sql;

import java.sql.Connection;

/**
 * Storage of all database connections.
 * Connections are stored in {@link ThreadLocal}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
public class SQLConnectionProvider {

	/** Map of all connections. */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();
	
	/**
	 * Sets connection for current thread (or deletes connection if argument is <code>null</code>).
	 * 
	 * @param con		connection to database.
	 */
	public static void setConnection(Connection con) {
		if(con==null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}
	
	/**
	 * Retrieves connection for current thread.
	 * 
	 * @return connection to database.
	 */
	public static Connection getConnection() {
		return connections.get();
	}
	
}