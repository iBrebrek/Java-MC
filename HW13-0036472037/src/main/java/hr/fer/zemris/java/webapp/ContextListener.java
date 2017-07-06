package hr.fer.zemris.java.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * When application is started this listeners
 * add "mislisecStart" attribute which contains
 * millisecond when this application was started.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (6.6.2016.)
 */
@WebListener
public class ContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("milisecStart", System.currentTimeMillis());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute("milisecStart");
	}

}
