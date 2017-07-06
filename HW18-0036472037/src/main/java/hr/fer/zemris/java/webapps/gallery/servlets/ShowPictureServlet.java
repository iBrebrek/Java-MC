package hr.fer.zemris.java.webapps.gallery.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used for image sources.
 * Retrieved images are jpg.
 * Page needs one parameter, path.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.7.2016.)
 */
@WebServlet("/servlets/show")
public class ShowPictureServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/jpg"); 
		String path = getServletContext().getRealPath(req.getParameter("path"));
		
		BufferedImage image = ImageIO.read(new File(path));
		
		ImageIO.write(image, "jpg", resp.getOutputStream());
	}
}
