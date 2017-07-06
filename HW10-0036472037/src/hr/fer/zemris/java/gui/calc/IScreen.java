package hr.fer.zemris.java.gui.calc;

/**
 * Something that has screen.
 * That something can: show text, read text, delete text, be asked if text is deleted.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (15.5.2016.)
 */
public interface IScreen {
	
	/**
	 * Writes given text on its screen.
	 * 
	 * @param text		text to be written on screen.
	 */
	void show(String text);
	
	/**
	 * Read what is on screen.
	 * 
	 * @return text written on screen.
	 */
	String read();
	
	/**
	 * Ask if screen is empty.
	 * Empty screen doesn't have to be "", 
	 * screen should define what's empty for it.
	 * 
	 * @return {@code true} if screen is considered empty.
	 */
	boolean isEmpty();
	
	/**
	 * Clear screen.
	 * After this is called, isEmpty will return {@code true}.
	 * It is unknown what read will return after this is called.
	 */
	void clear();
}
