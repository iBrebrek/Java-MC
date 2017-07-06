package hr.fer.zemris.java.graphics.raster;

/**
 * This is an abstraction for all raster devices of fixed width and height 
 * for which each pixel can be painted with only two colors: 
 * black (when pixel is turned off) and white (when pixel is turned on). 
 *
 * The coordinate system for raster has (0,0) at the top-left corner of raster.
 * Positive x-axis is to the right and positive y-axis is toward the bottom.
 * 
 * The working of turnOn method is closely controlled with flipping mode of raster. 
 * If flipping mode of raster is disabled, then the call of the turnOn method 
 * turns on the pixel at specified location (if location is valid). 
 * However, if flipping mode is enabled, then the call of the turnOn method 
 * flips the pixel at the specified location (if it was turned on, 
 * it will be turned off, and if it was turned off, it will be turned on).
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.4.2016.)
 */
public interface BWRaster {
	
	/**
	 * Returns width of the raster.
	 * 
	 * @return rasters width.
	 */
	int getWidth();
	
	/**
	 * Return height of the raster.
	 * 
	 * @return rasters height.
	 */
	int getHeight();
	
	/**
	 * Turns off all pixels in raster.
	 */
	void clear();
	
	/**
	 * Turns on pixel at specified location.
	 * BUT, if flip mode is enabled, then this method will
	 * turn on pixels that are off, and turn off pixels that are on. 
	 * 
	 * Throws IllegalArgumentException if (x,y) 
	 * is invalid with respect to raster dimensions.
	 * 
	 * @param x		x coordinate.
	 * @param y		y coordinate.
	 */
	void turnOn(int x, int y);
	
	/**
	 * Turns off pixel at specified location.
	 * 
	 * Throws IllegalArgumentException if (x,y) 
	 * is invalid with respect to raster dimensions.
	 * 
	 * @param x		x coordinate.
	 * @param y		y coordinate.
	 */
	void turnOff(int x, int y);
	
	/**
	 * Turns the flip mode on.
	 * Flip mode controls turnOn method, read class/turnOn javadoc for more info.
	 */
	void enableFlipMode();
	
	/**
	 * Turns the flip mode off.
	 * Flip mode controls turnOn method, read class/turnOn javadoc for more info.
	 */
	void disableFlipMode();
	
	/**
	 * Checks if the pixel at the given location is turned on and if it is, 
	 * returns true, otherwise returns false.
	 * 
	 * Throws IllegalArgumentException if (x,y) 
	 * is invalid with respect to raster dimensions.
	 * 
	 * @param x		x coordinate of the pixel.
	 * @param y		y coordinate of the pixel.
	 * @return true if pixel in on.
	 */
	boolean isTurnedOn(int x, int y);
}
