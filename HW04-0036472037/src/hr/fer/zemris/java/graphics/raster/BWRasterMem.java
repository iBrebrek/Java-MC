package hr.fer.zemris.java.graphics.raster;

/**
 * Implementation of BWRaster which keeps all of its data in computer memory.
 * 
 * On creation of new objects all pixels will be turned off and flip mode will be disabled. 
 * For more information about flip more read documentation of implemented interface.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (4.4.2016.)
 */
public class BWRasterMem implements BWRaster {
	
	/** Contains flag for each pixel. True if it is on, false if off. */
	boolean[][] pixels; //default is false, therefore, all pixels will be turned off
	
	/** If this is true method turnOn toggles pixel on/off. */
	private boolean flip;
	
	/**
	 * Initializes new BWRasterMem with given height and width.
	 * All pixels are turned off and flip mode is disabled.
	 * 
	 * @param width		raster width.
	 * @param height	raster height.
	 */
	public BWRasterMem(int width, int height) {
		if(width < 1) throw new IllegalArgumentException("Width must be bigger than 1.");
		if(height < 1) throw new IllegalArgumentException("Height must be bigger than 1.");
		
		pixels = new boolean[width][height];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWidth() {
		return pixels.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHeight() {
		return pixels[0].length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		int width = getWidth();
		int height = getHeight();
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				pixels[x][y] = false;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnOn(int x, int y) {
		if(x >= getWidth() || y >= getHeight() || x < 0 || y < 0) {
			 throw new IllegalArgumentException("Given pixel is not within the raster.");
		}
		
		if(flip) {
			pixels[x][y] = !pixels[x][y];
		} else {
			pixels[x][y] = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnOff(int x, int y) {
		if(x >= getWidth() || y >= getHeight() || x < 0 || y < 0) {
			 throw new IllegalArgumentException("Given pixel is not within the raster.");
		}
		
		pixels[x][y] = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enableFlipMode() {
		flip = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disableFlipMode() {
		flip = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTurnedOn(int x, int y) {
		if(x >= getWidth() || y >= getHeight() || x < 0 || y < 0) {
			 throw new IllegalArgumentException("Given pixel is not within the raster.");
		}
		
		return pixels[x][y];
	}
}
