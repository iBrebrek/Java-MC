package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Abstract representation of all geometric shapes that can be drawn on the BWRaster.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.4.2016.)
 */
public abstract class GeometricShape {
	
	/**
	 * Checks if given point belongs to specified geometric shape. 
	 * 
	 * Returns false only if the location is outside of the geometric shape.
	 * It returns true for all other cases.
	 * 
	 * @param x		x coordinate of the pixel.
	 * @param y		y coordinate of the pixel.
	 * @return false if pixel is outside of the geometric shape.
	 */
	public abstract boolean containsPoint(int x, int y);
	
	/**
	 * Draw filled image of the geometric shape on the given raster.
	 * 
	 * @param r		raster where shape will be drawn.
	 */
	public void draw(BWRaster r) {
		int maxx = r.getWidth();
		int maxy = r.getHeight();
		
		for(int x = 0; x < maxx; x++) {
			for(int y = 0; y < maxy; y++) {
				if(containsPoint(x, y)) {
					r.turnOn(x, y);
				}
			}
		}
	}
}
