package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * This servlets lets blog
 * user create new blog entry.
 * Servlet extends {@link EntrySave}
 * which will save entry on save.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@WebServlet("/servlets/new")
public class EntryCreate extends EntrySave {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		BlogUser user = DAOProvider.getDAO().getUser(nick);
		BlogUser currentUser = (BlogUser) req.getSession().getAttribute("user");
		
		if(user == null) {
			req.setAttribute("error", "Invalid page parameters.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			
		} else if(currentUser == null || !nick.equals(currentUser.getNick())) {
			req.setAttribute("error", "You do not have the authorization to see this page.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			
		} else {
			prepare(req, resp);
			req.getRequestDispatcher("/WEB-INF/pages/edit.jsp").forward(req, resp);
		}
	}
	
	/**
	 * Everything that needs to be set before calling edit.jsp.
	 * 
	 * @param req		http request.
	 * @param resp 		http response.
	 * 
	 * @throws ServletException if the request could not be handled.
	 * @throws IOException  if an input or output error is detected 
	 * 						when the servlet handles the request
	 */
	protected void prepare(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
}
