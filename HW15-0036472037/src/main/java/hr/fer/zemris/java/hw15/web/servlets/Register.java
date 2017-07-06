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
 * This Servlet is used to create a new blog user.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@WebServlet("/servlets/register")
public class Register extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = get(req, "nick");
		String name = get(req, "name");
		String last = get(req, "last");
		String mail = get(req, "mail");
		String pass = get(req, "pass");
		String passRepeat = get(req, "passR");
		
		boolean invalidRegistration = false;
		
		if(pass == null || pass.isEmpty()) {
			req.setAttribute("invalidPass", "Please provide password.");
			invalidRegistration = true;
		} else {
			if(!pass.equals(passRepeat)) {
				req.setAttribute("invalidPassR", "Passwords must be the same.");
				invalidRegistration = true;
			}
		}
		if(name == null || name.isEmpty()) {
			req.setAttribute("invalidName", "Please provide first name.");
			invalidRegistration = true;
		}
		if(last == null || last.isEmpty()) {
			req.setAttribute("invalidLast", "Please provide last name.");
			invalidRegistration = true;
		}
		if(nick == null || nick.isEmpty()) {
			req.setAttribute("invalidNick", "Please provide nick.");
			invalidRegistration = true;
		} else {
			BlogUser user = DAOProvider.getDAO().getUser(nick);
			if(user != null) {
				req.setAttribute("invalidNick", "Given nick already exists.");
				invalidRegistration = true;
			}
		}
		if(mail == null || mail.isEmpty()) {
			req.setAttribute("invalidMail", "Please provide e-mail.");
			invalidRegistration = true;
		} else {
			int index = mail.indexOf("@");
			if(index == -1 || index == 0 || index==mail.length()-1) {
				req.setAttribute("invalidMail", "Please provide a valid e-mail.");
				invalidRegistration = true;
			}
		}
		
		if(invalidRegistration) {
			req.setAttribute("nick", nick);
			req.setAttribute("name", name);
			req.setAttribute("last", last);
			req.setAttribute("mail", mail);
			req.setAttribute("pass", pass);
			req.setAttribute("passR", passRepeat);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
		} else {
			BlogUser user = DAOProvider.getDAO().createUser(name, last, mail, nick, Hasher.encrypt(pass));
			req.getSession().setAttribute("user", user);
			resp.sendRedirect("main");
		}
	}
	
	/**
	 * Method used to retrieve parameter from request.
	 * Retrieved value will be empty string if it
	 * does not exist.
	 * Retrieved string will be trimmed.
	 * 
	 * @param req		HTTP request.
	 * @param param		parameter.
	 * @return value of given parameter.
	 */
	private String get(HttpServletRequest req, String param) {
		String value = req.getParameter(param);
		if(value != null) {
			value = value.trim();
		} else {
			value = "";
		}
		return value;
	}
}
