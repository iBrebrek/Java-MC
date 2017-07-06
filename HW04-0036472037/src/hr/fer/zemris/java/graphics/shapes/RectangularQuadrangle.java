package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Abstract representation of all geometric shapes 
 * that are both rectangular and quadrangles.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.4.2016.)
 */
abstract class RectangularQuadrangle extends GeometricShape {
	/*
	 * -do note that variables are protected and that class is abstract and package private
	 * -for constructor it does not matter if public or protected since class is abstract
	 * 
	 * Is there a better way to connect square and rectangle?
	 * Could you please comment this while reading this homework? 
	 * Would like to hear some better opinions.
	 */
	
	/** Length from minx to the right. */
	protected int width;
	/** Length from miny to the bottom. */
	protected int height;
	/** Shape's x coordinate of top left corner. */
	protected int minx;
	/** Shape's y coordinate of top left corner. */
	protected int miny;
	
	/**
	 * Initializes width, height and top left corner of this shape.
	 * 
	 * @param minx		x coordinate of top left corner.
	 * @param miny		y coordinate of top left corner.
	 * @param width		length from minx to the right.
	 * @param height	length from miny to the bottom.
	 */
	protected RectangularQuadrangle(int minx, int miny, int width, int height) {
		if(width <= 0) {
			throw new IllegalArgumentException("Width must be a postive integer.");
		}
		if(height <= 0) {
			throw new IllegalArgumentException("Height must be a postive integer.");
		}
		
		this.width = width;
		this.height = height;
		this.minx = minx;
		this.miny = miny;
	}

	/**
	 * Getter for x coordinate of top left corner of this shape.
	 * 
	 * @return x coordinate of top left corner.
	 */
	public int getMinx() {
		return minx;
	}

	/**
	 * Setter for x coordinate of top left corner of this shape.
	 * 
	 * @param minx		x coordinate of top left corner.
	 */
	public void setMinx(int minx) {
		this.minx = minx;
	}

	/**
	 * Getter for y coordinate of top left corner of this shape.
	 * 
	 * @return y coordinate of top left corner.
	 */
	public int getMiny() {
		return miny;
	}

	/**
	 * Setter for y coordinate of top left corner of this shape.
	 * 
	 * @param miny		y coordinate of top left corner.
	 */
	public void setMiny(int miny) {
		this.miny = miny;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		if(x < minx) return false;
		if(y < miny) return false;
		if(x >= minx + width) return false;
		if(y >= miny + height) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(BWRaster r) {
		/*
		 * (startX,startY) is top left and (endX, endY) is bottom right pixel
		 * of the rectangle that will be drawn
		 */
		int startX = Math.max(minx, 0); //because raster starts at (0,0)
		int startY = Math.max(miny, 0);
		int endX = Math.min(minx + width, r.getWidth());
		int endY = Math.min(miny + height, r.getHeight());
		
		for(int x = startX; x < endX; x++) {
			for(int y = startY; y < endY; y++) {
				r.turnOn(x, y);
			}
		}
	}
}
