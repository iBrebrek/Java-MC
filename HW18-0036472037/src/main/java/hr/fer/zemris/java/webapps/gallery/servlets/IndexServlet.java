package hr.fer.zemris.java.webapps.gallery.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Starting servlet for this web application.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.7.2016.)
 */
@WebServlet("")
public class IndexServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/gallery.jsp").forward(req, resp);
	}
}
