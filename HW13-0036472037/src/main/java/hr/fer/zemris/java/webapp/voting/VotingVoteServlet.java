package hr.fer.zemris.java.webapp.voting;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * If needed creates file voting-results.txt
 * and adds vote to selected band.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (8.6.2016.)
 */
@WebServlet(name="voting-vote", urlPatterns={"/voting-vote"})
public class VotingVoteServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int selectedId = 0;
		try {
			selectedId = Integer.parseInt(req.getParameter("id"));
		} catch(Exception exc) {
			return; //do nothing if id is not number
		}
		
		String fileName = req.getServletContext().getRealPath("/WEB-INF/voting-results.txt");
		
		// need to get all bands in case voting-results.txt doesn't have every band,
		// wouldn't need bands if voting-results.txt is always valid
		List<Band> bands = VotingUtils.getBands(req, resp, "bands");
		VotingUtils.readVotes(fileName, bands); // getBands will readVotes only if attribute is not set,
												// we read votes in case someone else updated file voting-result
		String resultsText = "";
		for(Band band : bands) {
			if(band.getId() == selectedId) band.setVotes(band.getVotes()+1);
			resultsText += band.getId()+"\t"+band.getVotes()+"\n";
		}
		
		PrintWriter out = new PrintWriter(fileName);
		out.write(resultsText); //will overwrite file
		out.close();

		resp.sendRedirect(req.getContextPath() + "/voting-results");
	}
}
