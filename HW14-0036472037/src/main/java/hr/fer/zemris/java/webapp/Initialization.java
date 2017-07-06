package hr.fer.zemris.java.webapp;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * When application is started this listener will
 * connect to database described in WEB-INF/dbsetting.properties 
 * and create connection pool for that database.
 * On application start this listener will also
 * create and fill tables Polls and PollOptions 
 * if they do not exist in database.
 * <br>
 * When application is over this listener will 
 * destroy created connection pool. 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
@WebListener
public class Initialization implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		String connectionURL = getURL(sce.getServletContext());
		
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Unable to create connection pool.", e1);
		}
		cpds.setJdbcUrl(connectionURL);
		
		try {
			PollsContract.prepareTables(cpds, sce.getServletContext());
		} catch (SQLException e) {
			throw new RuntimeException("Unable to create database tables.", e);
		}

		sce.getServletContext().setAttribute("connectionPool", cpds);
	}

	/**
	 * From properties file generates connection url.
	 * It will stop application if unable to create url.
	 * 
	 * @param context 	Servlet context used to locate properties file.	
	 * @return connection url.
	 * 
	 * @throws RuntimeException if unable to read properties file
	 */
	private String getURL(ServletContext context) {
		StringBuilder sb = new StringBuilder();
		
		try (InputStream stream = context.getResourceAsStream("/WEB-INF/dbsettings.properties")){
			Properties settings = new Properties();
			
			settings.load(stream);
			
			sb.append("jdbc:derby://")
				.append(readOrFail(settings, "host"))
				.append(":")
				.append(readOrFail(settings, "port"))
				.append("/")
				.append(readOrFail(settings, "name"))
				.append(";user=")
				.append(readOrFail(settings, "user"))
				.append(";password=")
				.append(readOrFail(settings, "password"));
			
		} catch(IOException fail) {
			throw new RuntimeException("File dbsettings.properties is invalid or missing.", fail);
		}
		return sb.toString();
	}
	
	/**
	 * From given properties reads given key.
	 * If there is not such key throws IOException.
	 * 
	 * @param properties	properties with values.
	 * @param key			key in properties.
	 * @return value of given key.
	 * 
	 * @throws IOException if no key in properties.
	 */
	private String readOrFail(Properties properties, String key) throws IOException {
		String value = properties.getProperty(key);
		if(value == null) {
			throw new IOException();
		}
		return value;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("connectionPool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}