package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 *	All styles for calculator.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (15.5.2016.)
 */
public class Style {
	
	/** Background color for every calculator button. */
	public final static Color BUTTONS_BACKGROUND = new Color(114, 159, 207);
	/** Font for every calculator button. */
	public final static Font BUTTONS_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
	/** Border color for every calculator element. */
	public final static Color BORDER_COLOR = new Color(73, 119, 184);
	/** Border for every calculator element. */
	public final static Border BORDER = BorderFactory.createLineBorder(BORDER_COLOR);
	/** Calculator screen background. */
	public final static Color SCREEN_BACKGROUND = new Color(255, 211, 32);
	/** Calculator screen font. */ 
	public final static Font SCREEN_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	
	/** Can't be reached, can't be touched. */
	private Style() {
	}
}
