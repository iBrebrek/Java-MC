package hr.fer.zemris.java.webapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets attribute "startingAngle" and "tableSize"
 * based on given parameters "start" and "end".
 * Given parameter start is first degree and end is last degree.
 * Result attribute startingAngle is fist degree that will
 * be in table, tableSize is how many angles will be in table.
 * StartingAngle and start are the same,
 * tableSize is equal to abs(start-end).
 * Redirects work to trigonometric.jsp.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (8.6.2016.)
 */
@WebServlet(name="trigonometric", urlPatterns={"/trigonometric"})
public class TrigonometricsServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object objStart = req.getParameter("start");
		Object objEnd = req.getParameter("end");
		Integer start = null;
		Integer end = null;
		try {
			start = Integer.parseInt(objStart.toString());
		} catch(Exception exc) {
			start = 0;
		}
		try {
			end = Integer.parseInt(objEnd.toString());
		} catch(Exception exc) {
			end = 360;
		}
		if(start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		if(end > start + 720) {
			end = start + 720;
		}
		
		req.setAttribute("startingAngle", start);
		req.setAttribute("tableSize", Math.abs(end-start));
		
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
}
