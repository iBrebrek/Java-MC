package hr.fer.zemris.java.webapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartUtilities;

/**
 * Creates 500x300 png image containing
 * pie chart "OS usage" and writes to
 * response output stream.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (6.6.2016.)
 */
@WebServlet(name="reportImage", urlPatterns={"/reportImage"})
public class ChartServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		
		DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Linux", 29);
        result.setValue("Mac", 20);
        result.setValue("Windows", 51);
		JFreeChart chart = ChartFactory.createPieChart3D(
				"OS usage", result, true, true, false);
		
		ChartUtilities.writeChartAsPNG(resp.getOutputStream(), chart, 500, 300);
	}

}
