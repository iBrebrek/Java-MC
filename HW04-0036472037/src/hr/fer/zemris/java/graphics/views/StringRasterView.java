package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Implementation of RasterView which returns a single String that
 * contains textual representation of image (pixel rows are delimited by '\n').
 * 
 * @author Ivica Brebrek
 * @version 1.0  (4.4.2016.)
 */
public class StringRasterView implements RasterView {
	
	/** Default value for turned off pixel. */
	private static final char DEFAULT_BLACK = '.';
	/** Default value for turned off pixel. */
	private static final char DEFAULT_WHITE = '*';
	
	/** Representation of pixel that is turned off. */
	private char black;
	/** Representation of pixel that is turned off. */
	private char white;

	/**
	 * Initializes representations of on and off pixel.
	 * 
	 * @param black		char for turned off pixel.
	 * @param white		char for turned on pixel.
	 */
	public StringRasterView(char black, char white) {
		this.black = black;
		this.white = white;
	}

	/**
	 * Turned off/on pixels will be set to default value.
	 */
	public StringRasterView() {
		this(DEFAULT_BLACK, DEFAULT_WHITE);
	}

	/**
	 * Returns a single String which contains textual 
	 * representation of image.
	 * Pixel rows are delimited by '\n'.
	 */
	@Override
	public Object produce(BWRaster raster) {
		int maxx = raster.getWidth();
		int maxy = raster.getHeight();
		StringBuilder sb = new StringBuilder();
		
		for(int y = 0; y < maxy; y++) {
			for(int x = 0; x < maxx; x++) {
				
				if(raster.isTurnedOn(x, y)) {
					sb.append(white);
				} else {
					sb.append(black);
				}
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}

}
