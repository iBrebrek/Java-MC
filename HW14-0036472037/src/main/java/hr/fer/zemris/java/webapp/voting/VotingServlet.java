package hr.fer.zemris.java.webapp.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.webapp.dao.DAOProvider;
import hr.fer.zemris.java.webapp.model.Poll;
import hr.fer.zemris.java.webapp.model.PollOption;

/**
 * Creates attribute "bands" and
 * redirects work to votingIndex.jsp
 * 
 * @author Ivica Brebrek
 * @version 1.1  (14.6.2016.)
 */
@WebServlet(name="voting", urlPatterns={"/voting"})
public class VotingServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		long pollID = Long.parseLong(req.getParameter("pollID"));
		Poll poll = DAOProvider.getDao().getPoll(pollID);
		List<PollOption> options = DAOProvider.getDao().getPollOptions(pollID);
		req.setAttribute("options", options);
		req.setAttribute("poll", poll);
		
		req.getRequestDispatcher("/WEB-INF/pages/votingIndex.jsp").forward(req, resp);
	}
}
