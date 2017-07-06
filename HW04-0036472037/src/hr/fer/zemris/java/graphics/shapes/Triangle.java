package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Representation of a geometric shape triangle.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (4.4.2016.)
 */
public class Triangle extends GeometricShape {
	
	/** Deviation for barycentric coordinates */ 
	private static final double ACCEPTABLE_DEVIATION = 1.0E-12; //I hate decimal rounding, never accurate
	
	/** First point, x coordinate. */
	private int t1x;
	/** First point, y coordinate. */
	private int t1y;
	/** Second point, x coordinate. */
	private int t2x;
	/** Second point, y coordinate. */
	private int t2y;
	/** Third point, x coordinate. */
	private int t3x;
	/** Third point, y coordinate. */
	private int t3y;	
	
	/**
	 * Initializes all points of triangle.
	 * 
	 * 
	 * @param t1x	first point, x coordinate.
	 * @param t1y	first point, y coordinate.
	 * @param t2x	second point, x coordinate
	 * @param t2y	second point, y coordinate.
	 * @param t3x	third point, x coordinate.
	 * @param t3y	third point, y coordinate.
	 */
	public Triangle(int t1x, int t1y, int t2x, int t2y, int t3x, int t3y) {
		super();
		this.t1x = t1x;
		this.t1y = t1y;
		this.t2x = t2x;
		this.t2y = t2y;
		this.t3x = t3x;
		this.t3y = t3y;
	}
	
	/**
	 * Getter for x coordinate of the first point.
	 * 
	 * @return x coordinate of the first point
	 */
	public int getT1x() {
		return t1x;
	}

	/**
	 * Setter for x coordinate of the first point.
	 * 
	 * @param t1x	x coordinate of the first point.
	 */
	public void setT1x(int t1x) {
		this.t1x = t1x;
	}

	/**
	 * Getter for y coordinate of the first point.
	 * 
	 * @return y coordinate of the first point
	 */
	public int getT1y() {
		return t1y;
	}

	/**
	 * Setter for y coordinate of the first point.
	 * 
	 * @param t1y	y coordinate of the first point.
	 */
	public void setT1y(int t1y) {
		this.t1y = t1y;
	}

	/**
	 * Getter for x coordinate of the second point.
	 * 
	 * @return x coordinate of the second point
	 */
	public int getT2x() {
		return t2x;
	}

	/**
	 * Setter for x coordinate of the second point.
	 * 
	 * @param t2x	x coordinate of the second point.
	 */
	public void setT2x(int t2x) {
		this.t2x = t2x;
	}

	/**
	 * Getter for y coordinate of the second point.
	 * 
	 * @return y coordinate of the second point
	 */
	public int getT2y() {
		return t2y;
	}

	/**
	 * Setter for y coordinate of the second point.
	 * 
	 * @param t2y	y coordinate of the second point.
	 */
	public void setT2y(int t2y) {
		this.t2y = t2y;
	}

	/**
	 * Getter for x coordinate of the third point.
	 * 
	 * @return x coordinate of the third point
	 */
	public int getT3x() {
		return t3x;
	}

	/**
	 * Setter for x coordinate of the third point.
	 * 
	 * @param t3x	x coordinate of the third point.
	 */
	public void setT3x(int t3x) {
		this.t3x = t3x;
	}

	/**
	 * Getter for y coordinate of the third point.
	 * 
	 * @return y coordinate of the third point
	 */
	public int getT3y() {
		return t3y;
	}

	/**
	 * Setter for y coordinate of the third point.
	 * 
	 * @param t3y	y coordinate of the third point.
	 */
	public void setT3y(int t3y) {
		this.t3y = t3y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		// barycentric coordinates are used for this
		
		//this is calculated every time, but whatever
		double areaOfOriginal = getArea(t1x, t1y, t2x, t2y, t3x, t3y); 
		
		double alpha = getArea(x, y, t2x, t2y, t3x, t3y) / areaOfOriginal;
		double beta = getArea(t1x, t1y, x, y, t3x, t3y) / areaOfOriginal;
		double gamma = getArea(t1x, t1y, t2x, t2y, x, y) / areaOfOriginal;

		return (alpha+beta+gamma - 1) < ACCEPTABLE_DEVIATION; 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(BWRaster r) {
		int maxX = min(max(max(t1x, t2x), t3x), r.getWidth());
		int maxY = min(max(max(t1y, t2y), t3y), r.getHeight());
		int minX = max(min(min(t1x, t2x), t3x), 0);
		int minY = max(min(min(t1y, t2y), t3y), 0);
		
		for(int x = minX; x < maxX; x++) {
			for(int y = minY; y < maxY; y++) {
				if(containsPoint(x, y)) {
					r.turnOn(x, y);
				}
			}
		}
	}	
	
	/**
	 * Calculates area of triangle.
	 * 
	 * @param ax	point1 x
	 * @param ay	point1 y
	 * @param bx	point2 x
	 * @param by	point2 y
	 * @param cx	point3 x
	 * @param cy	point3 y
	 * @return area of triangle
	 */
	private double getArea(double ax, double ay, double bx, double by, double cx, double cy) {
		return Math.abs((ax*(by-cy)+bx*(cy-ay)+cx*(ay-by))/2.0);
	}
}
