package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Simple class used to output the textual representation of image to standard output.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (4.3.2016.)
 */
public class SimpleRasterView implements RasterView {
	
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
	public SimpleRasterView(char white, char black) {
		this.black = black;
		this.white = white;
	}

	/**
	 * Turned off/on pixels will be set to default value.
	 */
	public SimpleRasterView() {
		this(DEFAULT_WHITE, DEFAULT_BLACK);
	}

	/**
	 * Produces raster on system output and return null.
	 */
	@Override
	public Object produce(BWRaster raster) {
		int maxx = raster.getWidth();
		int maxy = raster.getHeight();
		
		for(int y = 0; y < maxy; y++) {
			for(int x = 0; x < maxx; x++) {
				
				if(raster.isTurnedOn(x, y)) {
					System.out.print(white);
				} else {
					System.out.print(black);
				}
			}
			System.out.print("\n");
		}
		return null;
	}
}
