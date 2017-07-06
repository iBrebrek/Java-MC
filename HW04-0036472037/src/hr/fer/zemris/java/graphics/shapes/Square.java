package hr.fer.zemris.java.graphics.shapes;

/**
 * Representation of geometric shape square. 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.4.2016.)
 */
public class Square extends RectangularQuadrangle {

	/**
	 * Initializes size and top left corner of this rectangle.
	 * Size is length of all sides.
	 * 
	 * @param minx		x coordinate of top left corner.
	 * @param miny		y coordinate of top left corner.
	 * @param size		length of sides.
	 */
	public Square(int minx, int miny, int size) {
		super(minx, miny, size, size);
	}

	/**
	 * Getter for width of this square.
	 * 
	 * @return width of the square.
	 */
	public int getSize() {
		return width; //does not matter if we return width or height
	}

	/**
	 * Setter for size of this square.
	 * 
	 * @param size		size of the square.
	 */
	public void setSize(int size) {
		width = size;
		height = size;
	}
}
