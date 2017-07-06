package hr.fer.zemris.java.hw16.jvdraw.color;

import java.awt.Color;

/**
 * Interface which offers a single method, getter for current color.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public interface IColorProvider {

	/**
	 * Retrieves current color.
	 * 
	 * @return current color.
	 */
	public Color getCurrentColor();
}
