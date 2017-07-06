package hr.fer.zemris.java.hw15.jpa;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Filter called before every "/servlets/..." page.
 * This filter sets encoding to UTF-8 and closes
 * entity manager after Servlet is finished.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@WebFilter("/servlets/*")
public class JPAFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			request.setCharacterEncoding("UTF-8"); //so everything will be utf-8
			chain.doFilter(request, response);
		} finally {
			JPAEMProvider.close();
		}
	}

	@Override
	public void destroy() {
	}
}