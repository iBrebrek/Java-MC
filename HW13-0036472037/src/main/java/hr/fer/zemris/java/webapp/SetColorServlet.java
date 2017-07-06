package hr.fer.zemris.java.webapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets attribute "pickedBgCol" based on
 * given parameter "color".
 * That color will be background of every page, this session.
 * Redirects to colors.jsp.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (8.6.2016.)
 */
@WebServlet(name="setcolor", urlPatterns={"/setcolor"})
public class SetColorServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color = null;
		try {
			color = (String)req.getParameter("color");
		} catch(Exception exc) {
		}
		if(color == null) {
			color = "white";
		}
		
		req.getSession().setAttribute("pickedBgCol", color);
		
		req.getRequestDispatcher("colors.jsp").forward(req, resp);
	}
}
