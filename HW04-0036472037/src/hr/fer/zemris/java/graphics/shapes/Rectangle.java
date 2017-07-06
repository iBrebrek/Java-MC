package hr.fer.zemris.java.graphics.shapes;

/**
 * Representation of geometric shape rectangle. 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.4.2016.)
 */
public class Rectangle extends RectangularQuadrangle {

	/**
	 * Initializes width, height and top left corner of this rectangle.
	 * 
	 * @param width		length from minx to the right.
	 * @param height	length from miny to the bottom.
	 * @param minx		x coordinate of top left corner.
	 * @param miny		y coordinate of top left corner.
	 */
	public Rectangle(int minx, int miny, int width, int height) {
		super(minx, miny, width, height);
	}
	
	/**
	 * Getter for width of this rectangle.
	 * 
	 * @return width of the rectangle.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Setter for width of this rectangle.
	 * 
	 * @param width		width of the rectangle.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Getter for height of this rectangle.
	 * 
	 * @return height of the rectangle.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Setter for height of this rectangle.
	 * 
	 * @param height 	width of the rectangle.
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}
