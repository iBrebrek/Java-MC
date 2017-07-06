package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * This Servlet is used to see 
 * list of all entries from a user.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@WebServlet("/servlets/user")
public class User extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		BlogUser user = DAOProvider.getDAO().getUser(nick);
		req.setAttribute("nick", nick);
		req.setAttribute("entries", DAOProvider.getDAO().getAllEntries(user.getId()));
		req.getRequestDispatcher("/WEB-INF/pages/author.jsp").forward(req, resp);
	}
}
