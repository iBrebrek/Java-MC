package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Elements represents read-only decimal number.
 * Can be positive or negative.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class ElementConstantDouble extends Element {
	
	/** Read-only double value. */
	private final String value;
	
	/**
	 * Initializes read-only double value.
	 * 
	 * Throws IllegalArgumentException if given value is not decimal number.
	 * 
	 * @param value	value of an element.
	 */
	public ElementConstantDouble(String value) {
		if(value == null) {
			throw new IllegalArgumentException("Value can not be null.");
		}
		if(!value.matches("[-+]?\\d*\\.?\\d+")) {
			throw new IllegalArgumentException(value + " is not a decimal number.");
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
