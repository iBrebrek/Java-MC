package hr.fer.zemris.java.graphics.shapes;


/**
 * Representation of geometric shape circle.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (4.4.2016.)
 */
public class Circle extends NonStretchableEllipse {

	/**
	 * Initializes center point and radius of this circle.
	 * 
	 * @param centerX		x coordinate of the center.
	 * @param centerY		y coordinate of the center.
	 * @param radius		radius.
	 */
	public Circle(int centerX, int centerY, int radius) {
		super(centerX, centerY, radius, radius);
	}
	
	/**
	 * Getter for radius of this circle.
	 * 
	 * @return radius of the circle.
	 */
	public int getRadius() {
		return horizontalRadius; //same as verticalRadius
	}
	
	/**
	 * Setter for radius of this circle.
	 * 
	 * @param radius	radius o the circle.
	 */
	public void setRadius(int radius) {
		horizontalRadius = radius;
		verticalRadius = radius;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		int distanceX = Math.abs(centerX - x);
		int distanceY = Math.abs(centerY - y);
		return distanceX + distanceY < horizontalRadius;  //same as verticalRadius
	}
	
	//method draw in NonStretchableEllipse is actually good enough, so no need to override it
}
