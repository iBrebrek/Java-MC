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
	private final int value;

	/**
	 * Initializes read-only integer value.
	 * 
	 * @param value	value of an element.
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Returns element's value.
	 * 
	 * @return value of element.
	 */
	@Override
	public String asText() {
		return String.valueOf(value);
	}

	@Override
	public Object getValue() {
		return Integer.valueOf(value);
	}

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitConstant(this);
	}
}
