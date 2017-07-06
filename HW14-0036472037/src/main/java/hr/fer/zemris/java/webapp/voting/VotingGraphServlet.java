package hr.fer.zemris.java.webapp.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.webapp.dao.DAOProvider;
import hr.fer.zemris.java.webapp.model.PollOption;

/**
 * Creates png image containing pie chart 
 * based on all options and their votes.
 * 
 * @author Ivica Brebrek
 * @version 1.1  (14.6.2016.)
 */
@WebServlet(name="voting-graph", urlPatterns={"/voting-graph"})
public class VotingGraphServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		
		long pollID = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> options = DAOProvider.getDao().getPollOptions(pollID);
		
		DefaultPieDataset result = new DefaultPieDataset();
		for(PollOption option : options) {
			result.setValue(option.getTitle(), option.getVotesCount());
		}
		
		String pollName = DAOProvider.getDao().getPoll(pollID).getTitle();
		JFreeChart chart = ChartFactory.createPieChart3D(
				"VOTES - "+pollName, result, true, true, false);
		
		ChartUtilities.writeChartAsPNG(resp.getOutputStream(), chart, 500, 500);
	}
}
