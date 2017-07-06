package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogEntry;

/**
 * This Servlet is used to view a single
 * blog entry, all it's comments and 
 * write a new comment.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@WebServlet("/servlets/view")
public class EntryView extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		req.setAttribute("nick", nick);

		BlogEntry entry = null;

		try {
			String eid = req.getParameter("eid");
			long id = Long.valueOf(eid);
			entry = DAOProvider.getDAO().getEntry(id);

			if (entry == null) {
				req.setAttribute("error", "No such entry.");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			} else if(!entry.getCreator().getNick().equals(nick)) {
				req.setAttribute("error", "<b>"+nick+"</b> is not the author of this entry.");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			}
		} catch (NumberFormatException e) {
			req.setAttribute("error", "Invalid page parameters.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}

		req.setAttribute("blog", entry); // entry will always be valid BlogEntry
		req.getRequestDispatcher("/WEB-INF/pages/entry.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String message = req.getParameter("message");
		if(message == null || message.trim().isEmpty()) {
			req.setAttribute("invalidMessage", "Comment can not be empty.");
			doGet(req, resp);
		} else {
			String eid = req.getParameter("eid");
			String email = req.getParameter("mail");
			String nick = req.getParameter("nick");
			if(email == null || email.trim().isEmpty()) {
				email = "unknown";
			}
			DAOProvider.getDAO().addComment(Long.valueOf(eid), message, email);
			
			resp.sendRedirect(req.getContextPath()+"/servlets/author/"+nick+"/"+eid); 
		}
	}
}
