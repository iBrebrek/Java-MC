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
	private final double value;
	
	/**
	 * Initializes read-only double value.
	 * 
	 * @param value	value of an element.
	 */
	public ElementConstantDouble(double value) {
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
		return Double.valueOf(value);
	}

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitConstant(this);
	}
}
