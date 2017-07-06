package hr.fer.zemris.java.webapp.voting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.webapp.dao.DAOProvider;
import hr.fer.zemris.java.webapp.model.PollOption;

/**
 * If needed creates file voting-results.txt
 * and adds vote to selected band.
 * 
 * @author Ivica Brebrek
 * @version 1.1  (14.6.2016.)
 */
@WebServlet(name="voting-vote", urlPatterns={"/voting-vote"})
public class VotingVoteServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		
		PollOption selectedOption = DAOProvider.getDao().getPollOption(id);
		DAOProvider.getDao().submitVote(selectedOption);

		resp.sendRedirect(req.getContextPath() + "/voting-results?pollID="+req.getParameter("pollID"));
	}
}
