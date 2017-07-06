package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker used to draw green circle with r=50
 * in the middle of 200x200 image.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.6.2016.)
 */
public class CircleWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bim.createGraphics();
		g2d.setColor(Color.GREEN);
		g2d.fillOval(50, 50, 100, 100);
		g2d.dispose();
		
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ImageIO.write(bim, "png", bos);
			context.setMimeType("image/png");
			context.write(bos.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Unable to create circle.");
		}
	}

}
