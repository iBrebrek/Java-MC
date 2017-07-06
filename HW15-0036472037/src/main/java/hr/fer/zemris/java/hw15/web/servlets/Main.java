package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.utils.Hasher;

/**
 * Home page for this web application.
 * This Servlet is used to view list
 * of all users and to login.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@WebServlet("/servlets/main")
public class Main extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("users", DAOProvider.getDAO().getAllUsers());
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("user");
		String pass = req.getParameter("password");
		BlogUser user = DAOProvider.getDAO().getUser(nick);
		if(user == null) {
			req.setAttribute("invalidUser", "No user with such nick.");
			req.setAttribute("nick", nick);
		} else {
			if(user.getPasswordHash().equals(Hasher.encrypt(pass))) {
				req.getSession().setAttribute("user", user);
				resp.sendRedirect("main");
				return;
			} else {
				req.setAttribute("invalidPassword", "Incorrect password.");
				req.setAttribute("nick", nick);
			}
		}
		doGet(req, resp);
	}
}
