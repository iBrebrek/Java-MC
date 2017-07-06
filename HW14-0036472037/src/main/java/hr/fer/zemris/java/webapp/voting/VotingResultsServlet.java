package hr.fer.zemris.java.webapp.voting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.webapp.dao.DAOProvider;
import hr.fer.zemris.java.webapp.model.PollOption;

/**
 * Based on votes creates attributes sorted
 * and winners. "sorted" are options sorted
 * by number of votes, "winners" are options with
 * most votes.
 * Redirects work to votingResults.jsp.
 * 
 * @author Ivica Brebrek
 * @version 1.1  (14.6.2016.)
 */
@WebServlet(name="voting-results", urlPatterns={"/voting-results"})
public class VotingResultsServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		long pollID = Long.parseLong(req.getParameter("pollID"));
		req.setAttribute("poll", DAOProvider.getDao().getPoll(pollID));
		
		List<PollOption> options = DAOProvider.getDao().getPollOptions(pollID);
		Collections.sort(options, (a, b) -> ((int)(b.getVotesCount() - a.getVotesCount())));
		req.setAttribute("sorted", options);
		
		List<PollOption> winners = new ArrayList<>();
		long max = options.get(0).getVotesCount();
		options.forEach(o -> {
			if(o.getVotesCount() == max) {
				winners.add(o);
			}
		});
		req.setAttribute("winners", winners);

		req.getRequestDispatcher("/WEB-INF/pages/votingResults.jsp").forward(req, resp);
	}
}
