package hr.fer.zemris.java.webapp.voting;

import hr.fer.zemris.java.webapp.dao.DAOProvider;
import hr.fer.zemris.java.webapp.model.Poll;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Index page.
 * Shows a list of all polls.
 * Clicking on a poll will open that poll.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
@WebServlet(name="index", urlPatterns={"/index", "/index.html"})
public class Index extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<Poll> polls = DAOProvider.getDao().getAllPolls();
		req.setAttribute("polls", polls);
		
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
	
}