package hr.fer.zemris.java.gui.charts;

/**
 * Class used to store read-only x and y values.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.5.2016.)
 */
public class XYValue {
	/** X value. */
	private final int x;
	/** Y value. */
	private final int y;
	
	/**
	 * Initializes read-only x and y values.
	 * 
	 * @param x		X value.
	 * @param y		Y value.
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/** 
	 * Get X value.
	 * 
	 * @return	X value.
	 */
	public int getX() {
		return x;
	}

	/** 
	 * Get Y value.
	 * 
	 * @return	Y value.
	 */
	public int getY() {
		return y;
	}
}
