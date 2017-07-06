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

/**
 * Create png image containing pie chart 
 * based on all bands and their votes.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (8.6.2016.)
 */
@WebServlet(name="voting-graph", urlPatterns={"/voting-graph"})
public class VotingGraphServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		
		List<Band> bands = VotingUtils.getBands(req, resp, "sortedBands");
		
		DefaultPieDataset result = new DefaultPieDataset();
		for(Band band : bands) {
			result.setValue(band.getName(), band.getVotes());
		}
		JFreeChart chart = ChartFactory.createPieChart3D(
				"Votes", result, true, true, false);
		
		ChartUtilities.writeChartAsPNG(resp.getOutputStream(), chart, 500, 500);
	}
}
