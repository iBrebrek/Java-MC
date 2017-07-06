package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Elements represents read-only integer number.
 * Can be positive or negative.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class ElementConstantInteger extends Element {
	
	/** Read-only integer value. */
	private final String value;

	/**
	 * Initializes read-only integer value.
	 * 
	 * Throws IllegalArgumentException if given value is not an integer.
	 * 
	 * @param value	value of an element.
	 */
	public ElementConstantInteger(String value) {
		if(value == null) {
			throw new IllegalArgumentException("Value can not be null.");
		}
		if(!value.matches("[-+]?\\d+")) {
			throw new IllegalArgumentException(value + " is not an integer.");
		}
		this.value = value;
	}
	
	/**
	 * Returns element's value.
	 * 
	 * @return value of element.
	 */
	@Override
	public String asText() {
		return value;
	}
}
