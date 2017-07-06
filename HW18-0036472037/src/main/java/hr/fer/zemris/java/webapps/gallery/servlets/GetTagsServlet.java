package hr.fer.zemris.java.webapps.gallery.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.webapps.gallery.Album;
import hr.fer.zemris.java.webapps.gallery.AlbumDB;

/**
 * Servlet used to retrieve JSON with all tags.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.7.2016.)
 */
@WebServlet("/servlets/tags")
public class GetTagsServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8"); 
		
		Album album = AlbumDB.createAlbum(
			getServletContext().getRealPath("/WEB-INF/opisnik.txt")
		);
		
		Gson gson = new Gson();
		
		String jsonText = gson.toJson(album.getTags());
		resp.getWriter().write(jsonText);
		resp.getWriter().flush();
	}
}
