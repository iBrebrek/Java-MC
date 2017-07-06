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
 * This Servlet knows all valid
 * paths containing "servlets/author/...".
 * This Servlet dispatches work
 * to other Servlets based
 * on given path.
 * <br>
 * Valid paths are:
 * /servlets/author/NICK
 * /servlets/author/NICK/entryID
 * /servlets/author/NICK/new
 * /servlets/author/NICK/entryID/edit
 * <br>
 * .../NICK is dispatched to {@link User}.
 * .../NICK/entryID is dispatched to {@link EntryView}.
 * .../NICK/new is dispatched to {@link EntryCreate}.
 * .../NICK/entryID/edit is dispatched to {@link EntryEdit}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@WebServlet("/servlets/author/*")
public class Dispatcher extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String input = req.getPathInfo();
		if(input == null || !input.startsWith("/")) {
			req.setAttribute("error", "No such page.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		input = input.substring(1); // to remove starting /
		String[] elements = input.split("/");
		
		BlogUser user = DAOProvider.getDAO().getUser(elements[0]);
		if(user == null) {
			req.setAttribute("error", "There is no user <b>"+elements[0]+"</b>.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		if(elements.length == 1) {
			getServletContext().getRequestDispatcher("/servlets/user?nick="+elements[0]).forward(req, resp); 
			return;
			
		} else if(elements.length == 2) {
			if(elements[1].equalsIgnoreCase("new")) {
				getServletContext().getRequestDispatcher("/servlets/new?nick="+elements[0]).forward(req, resp); 
				return;
			} else {
				getServletContext().getRequestDispatcher("/servlets/view?nick="+elements[0]+"&eid="+elements[1]).forward(req, resp); 
				return;
			}
			
		} else if(elements.length == 3 && elements[2].equalsIgnoreCase("edit")) {
			getServletContext().getRequestDispatcher("/servlets/edit?nick="+elements[0]+"&eid="+elements[1]).forward(req, resp);
			return;
		}
		
		//if can't dispatch to other Servlet show error
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);	
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}