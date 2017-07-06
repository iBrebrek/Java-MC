package hr.fer.zemris.java.webapp;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Class used to calculate sine and cosine.
 * Angle is in degrees.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (6.6.2016.)
 */
public class Trigonometrics {
	/** Angle in dregrees. */
	private double angle;
	
	/**
	 * Sets angle to 0.
	 */
	public Trigonometrics() {
		angle = 0;
	}
	
	/**
	 * Initializes degree of an angle.
	 * 
	 * @param angle		angle degree.
	 */
	public Trigonometrics(int angle) {
		this.angle = angle;
	}
	
	/**
	 * Retrieves angle as integer number.
	 * 
	 * @return angle in integer.
	 */
	public int getAsInt() {
		return Double.valueOf(angle).intValue();
	}
	
	/**
	 * Retrieves angle degree.
	 * 
	 * @return angle degree.
	 */
	public double getAngle() {
		return angle;
	}
	
	/**
	 * Sets angle degree.
	 * 
	 * @param angle		angle degree.
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	/**
	 * Retrieves sine of current angle.
	 * 
	 * @return sine of current angle.
	 */
	public String getSin() {
		NumberFormat f = new DecimalFormat("#0.00000"); 
		return f.format(Math.sin(Math.toRadians(angle)));
	}
	
	/**
	 * Retrieves cosine of current angle.
	 * 
	 * @return cosine of current angle.
	 */
	public String getCos() {
		NumberFormat f = new DecimalFormat("#0.00000");
		return f.format(Math.cos(Math.toRadians(angle)));
	}
}
