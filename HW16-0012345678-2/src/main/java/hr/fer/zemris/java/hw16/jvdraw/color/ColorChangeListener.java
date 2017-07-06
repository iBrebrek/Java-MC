package hr.fer.zemris.java.hw16.jvdraw.color;

import java.awt.Color;

/**
 * Listener for when a color is changed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public interface ColorChangeListener {

	/**
	 * Notifies that given source changed color from old to new.
	 * 
	 * @param source		source with new color.
	 * @param oldColor		color which source had before new color.
	 * @param newColor		color which source currently has.
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
