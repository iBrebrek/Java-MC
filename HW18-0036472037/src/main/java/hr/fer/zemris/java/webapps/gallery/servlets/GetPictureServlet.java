package hr.fer.zemris.java.webapps.gallery.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.webapps.gallery.AlbumDB;
import hr.fer.zemris.java.webapps.gallery.Picture;

/**
 * Servlet is used to retrieve original image
 * with given picture name.
 * Page needs one parameter, name.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.7.2016.)
 */
@WebServlet("/servlets/picture")
public class GetPictureServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8"); 
		String name = req.getParameter("name");
		Picture original = AlbumDB.getAlbum().getPicture(name);
		Picture sent = new Picture(
				"servlets/show?path=/WEB-INF/slike/" + original.getSource(), 
				original.getName(), 
				original.getTags().toArray(new String[0])
		);
		
		Gson gson = new Gson();
		
		String jsonText = gson.toJson(sent);
		resp.getWriter().write(jsonText);
		resp.getWriter().flush();
	}
}
