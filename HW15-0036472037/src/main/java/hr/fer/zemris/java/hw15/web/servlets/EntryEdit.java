package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogEntry;

/**
 * This servlets lets blog user
 * edit its blog entry.
 * User can edit only its
 * own entries.
 * Servlet extends {@link EntryCreate}
 * which will save entry on save, 
 * and check authorization on GET request.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@WebServlet("/servlets/edit")
public class EntryEdit extends EntryCreate {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void prepare(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogEntry entry = null;
		
		try {
			String eid = req.getParameter("eid");
			long id = Long.valueOf(eid);
			entry = DAOProvider.getDAO().getEntry(id);
			
			if(entry == null) {
				req.setAttribute("error", "No such entry.");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				
			} else if(!entry.getCreator().getNick().equals(req.getParameter("nick"))) {
				req.setAttribute("error", "You can edit only your own entries.");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			}
		} catch(NumberFormatException e) {
			req.setAttribute("error", "Invalid page parameters.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}
		
		req.setAttribute("entry", entry); //entry will always be a valid BlogEntry
	}
}
