package hr.fer.zemris.java.webapp.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Creates attribute "bands" and
 * redirects work to votingIndex.jsp
 * 
 * @author Ivica Brebrek
 * @version 1.0  (8.6.2016.)
 */
@WebServlet(name="voting", urlPatterns={"/voting"})
public class VotingServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Band> bands = VotingUtils.getBands(req, resp, "bands");
		req.getSession().setAttribute("bands", bands);
		
		req.getRequestDispatcher("/WEB-INF/pages/votingIndex.jsp").forward(req, resp);
	}
}
