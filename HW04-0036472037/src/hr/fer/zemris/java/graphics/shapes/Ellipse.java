package hr.fer.zemris.java.graphics.shapes;

/**
 * Representation of geometric shape ellipse.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (4.4.2016.)
 */
public class Ellipse extends NonStretchableEllipse {

	/**
	 * Initializes center point and horizontal and vertical radius of this ellipse.
	 * 
	 * @param centerX			x coordinate of the center.
	 * @param centerY			y coordinate of the center.
	 * @param horizontalRadius	horizonal radius.
	 * @param verticalRadius	vertical radius.
	 */
	public Ellipse(int centerX, int centerY, int horizontalRadius, int verticalRadius) {
		super(centerX, centerY, horizontalRadius, verticalRadius);
	}
	
	/**
	 * Getter for horizontal radius of this ellipse.
	 * 
	 * @return horizontal radius.
	 */
	public int getHorizontalRadius() {
		return horizontalRadius;
	}

	/**
	 * Setter for horizontal radius of this ellipse.
	 * 
	 * @param horizontalRadius		horizontal radius.
	 */
	public void setHorizontalRadius(int horizontalRadius) {
		this.horizontalRadius = horizontalRadius;
	}

	/**
	 * Getter for vertical radius of this ellipse.
	 * 
	 * @return vertical radius.
	 */
	public int getVerticalRadius() {
		return verticalRadius;
	}

	/**
	 * Setter for vertical radius of this ellipse.
	 * 
	 * @param verticalRadius		vertical radius.
	 */
	public void setVerticalRadius(int verticalRadius) {
		this.verticalRadius = verticalRadius;
	}
}
