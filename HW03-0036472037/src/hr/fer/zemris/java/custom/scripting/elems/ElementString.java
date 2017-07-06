package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Elements represents read-only string.
 * 
 * String enables escaping with \, only \ and " can be escaped.
 * 
 * Constructor expects string with already finished escaping and without quotation marks,
 * but toString will write \ in front of escaped chars and will add quotation marks at start and end. 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class ElementString extends Element {
	
	/** Read-only string value. */
	private final String value;
	
	/**
	 * Initializes read-only string value.
	 * 
	 * @param value	value of an element.
	 */
	public ElementString(String value) {
		if(value == null) {
			throw new IllegalArgumentException("Value can not be null.");
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
		// replace all \ and " with \\ and \" so parsing returned string gives same output.
		String result = value.replace("\\", "\\\\").replace("\"", "\\\"");
		return "\"" + result + "\"";
	}
}
