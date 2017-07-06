package hr.fer.zemris.java.webapps.gallery.servlets;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.webapps.gallery.Album;
import hr.fer.zemris.java.webapps.gallery.AlbumDB;
import hr.fer.zemris.java.webapps.gallery.Picture;

/**
 * Servlet used to retrieve JSON with pictures with given tag.
 * Page needs one parameter, tag.
 * Retrieved pictures are thumbnails, not originals.
 * Each thumbnail image is 150x150.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.7.2016.)
 */
@WebServlet("/servlets/thumbnails")
public class GetThumbnailsServlet extends HttpServlet {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Width for resized image (thumbnails). */
	private static final int IMG_WIDTH = 150;
	/** Height for resized image (thumbnails). */
	private static final int IMG_HEIGHT = 150;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8"); 
		String thumbsDir = getServletContext().getRealPath("/WEB-INF/thumbnails"); 
		String picsDir = getServletContext().getRealPath("/WEB-INF/slike"); 
		
		String tag = req.getParameter("tag");
		Album album = AlbumDB.getAlbum();
		List<Picture> originals = album.getPictures(tag);
		List<Picture> sent = new ArrayList<>();
		
		for(Picture p : originals) {
			String thumbFile = thumbsDir + File.separator + p.getSource();
			String picFile = picsDir + File.separator + p.getSource();
			if(!new File(thumbFile).exists()) {
				createThumbnail(thumbFile, picFile);
			}
			sent.add(new Picture(
					"servlets/show?path=/WEB-INF/thumbnails/"+p.getSource(), 
					p.getName()
			));
		}
		
		Gson gson = new Gson();
		
		String jsonText = gson.toJson(sent);
		resp.getWriter().write(jsonText);
		resp.getWriter().flush();
	}
	
	/**
	 * Creates thumbnail picture.
	 * 
	 * @param thumbFile		location where thumbnail picture should be created.
	 * @param picFile		location where original picture is.
	 * @throws IOException	if unable to use given files.
	 */
	private void createThumbnail(String thumbFile, String picFile) throws IOException {
		BufferedImage originalImage = ImageIO.read(new File(picFile));
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		BufferedImage resizeImageJpg = resizeImage(originalImage, type);
		File file = new File(thumbFile);
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		ImageIO.write(resizeImageJpg, "jpg", file); 
	}
	
	/**
	 * Resizes given image.
	 * Given image is not changed.
	 * 
	 * @param originalImage		original image.
	 * @param type				type of new image.
	 * @return new resized image.
	 */
	private BufferedImage resizeImage(BufferedImage originalImage, int type) {
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		return resizedImage;
	}
}
