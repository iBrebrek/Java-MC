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

/**
 * Based on votes creates attributes sortedBands
 * and winners. Sorted bands are bands sorted
 * by number of votes, winners are band(s) with
 * most votes.
 * Redirects work to votingResults.jsp.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (8.6.2016.)
 */
@WebServlet(name="voting-results", urlPatterns={"/voting-results"})
public class VotingResultsServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/voting-results.txt");
		
		List<Band> sortedBands = VotingUtils.getBands(req, resp, "bands");
		VotingUtils.readVotes(fileName, sortedBands);
		
		Collections.sort(sortedBands, (a, b) -> (b.getVotes() - a.getVotes()));
		req.getSession().setAttribute("sortedBands", sortedBands);
		
		List<Band> winners = new ArrayList<>();
		int max = sortedBands.get(0).getVotes();
		sortedBands.forEach(b -> {
			if(b.getVotes() == max) {
				winners.add(b);
			}
		});
		req.setAttribute("winners", winners);
		
		req.getRequestDispatcher("/WEB-INF/pages/votingResults.jsp").forward(req, resp);
	}
}
