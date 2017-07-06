package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is invoked when tables from database are missing.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.6.2016.)
 */
@WebServlet("/error/database")
public class ErrorDatabase extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("error", "Database is not valid, there are tables missing.<br>Restart application in tomcat.");
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}
}
