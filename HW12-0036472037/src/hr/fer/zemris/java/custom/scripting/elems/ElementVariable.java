package hr.fer.zemris.java.custom.scripting.elems;


/**
 * Elements represents a variable.
 * Valid variable name starts by letter and after follows 
 * zero or more letters, digits or underscores.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class ElementVariable extends Element {
	
	/** Read-only variable name. */
	private final String name;

	/**
	 * Initializes read-only variable name.
	 * 
	 * Valid variable name starts by letter and after 
	 * follows zero or more letters, digits or underscores.
	 * If variable name is invalid IllegalArgumentException is thrown.
	 * 
	 * @param name	name of variable.
	 */
	public ElementVariable(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Variable can not be null.");
		} 
		if(!name.matches("[a-zA-Z][\\w_]*")) {
			throw new IllegalArgumentException(name + " is not valid variable name.");
		}
		
		this.name = name;
	}

	/**
	 * Returns element's name.
	 * 
	 * @return name of element.
	 */
	@Override
	public String asText() {
		return name;
	}

	@Override
	public Object getValue() {
		return name;
	}

	@Override
	public void accept(IElementVisitor visitor) {
		visitor.visitVariable(this);
	}
}
