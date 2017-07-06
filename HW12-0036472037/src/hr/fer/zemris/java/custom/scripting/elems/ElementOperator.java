package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Elements represents read-only operator.
 * Valid operators are + (plus), - (minus), * (multiplication), / (division), ^ (power).
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class ElementOperator extends Element {

	/** Read-only operator. */
	private final String symbol;
	
	/**
	 * Initializes read-only operator.
	 * 
	 * Valid operators are + (plus), - (minus), * (multiplication), / (division), ^ (power).
	 * If operators is invalid IllegalArgumentException is thrown.
	 * 
	 * @param symbol	symbol of an element.
	 */
	public ElementOperator(String symbol) {
		if(symbol == null) {
			throw new IllegalArgumentException("Symbol can not be null.");
		}
		if(!symbol.matches("[+-[*]/^]")) {
			throw new IllegalArgumentException(symbol + " is not valid operator.");
		}
		this.symbol = symbol;
	}

	/**
	 * Returns element's operator.
	 * 
	 * @return operator.
	 */
	@Override
	public String asText() {
		return symbol;
	}

	@Override
	public Object getValue() {
		return symbol;
	}

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitOperator(this);
	}

}
