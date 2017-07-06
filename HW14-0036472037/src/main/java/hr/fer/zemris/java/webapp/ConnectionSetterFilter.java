package hr.fer.zemris.java.webapp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;

import hr.fer.zemris.java.webapp.dao.sql.SQLConnectionProvider;

/**
 * Filter used to register to connection pool
 * and deregister before finishing.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
@WebFilter(filterName="filter",urlPatterns={"/*"})
public class ConnectionSetterFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		DataSource ds = (DataSource)request.getServletContext().getAttribute("connectionPool");
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			throw new IOException("Database is not available.", e);
		}
		SQLConnectionProvider.setConnection(con);
		try {
			chain.doFilter(request, response);
		} finally {
			SQLConnectionProvider.setConnection(null);
			try { con.close(); } catch(SQLException ignorable) {}
		}
	}
}