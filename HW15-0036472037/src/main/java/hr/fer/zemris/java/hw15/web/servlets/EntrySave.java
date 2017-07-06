package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * This Servlet is used
 * to save blog entry.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.6.2016.)
 */
@WebServlet("/servlets/save")
public class EntrySave extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String title = req.getParameter("title");
		String text = req.getParameter("text");
		boolean save = true;
		
		if(title == null || title.trim().isEmpty()) {
			req.setAttribute("invalidTitle", "Title can not be empty.");
			save = false;
		}
		if(text == null || text.trim().isEmpty()) {
			req.setAttribute("invalidText", "Text can not be empty.");
			save = false;
		}
		
		if(save) {
			Long id = null;
			try {
				String eid = req.getParameter("eid");
				if(eid != null && !eid.isEmpty()) {
					id = Long.valueOf(eid);
				}
			} catch(NumberFormatException e) {
				req.setAttribute("error", "No such entry.");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			}
			
			BlogEntry entry = DAOProvider.getDAO().putEntry(
					(BlogUser)req.getSession().getAttribute("user"),
					id, title, text);
			
			if(entry == null) {
				req.setAttribute("error", "Impossible to modify that entry.");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			} else {
				resp.sendRedirect(req.getContextPath()+"/servlets/author/"+entry.getCreator().getNick()+"/"+entry.getId());
			}
		} else {
			BlogEntry entry = new BlogEntry();
			entry.setTitle(title);
			entry.setText(text);
			req.setAttribute("entry", entry);
			req.getRequestDispatcher("/WEB-INF/pages/edit.jsp").forward(req, resp);
		}
	}
}
