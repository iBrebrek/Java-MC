package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Elements represents read-only function name.
 * Valid function name starts with a letter 
 * and after than can follow zero or more letters, digits or underscores. 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class ElementFunction extends Element {
	
	/** Read-only function name. */
	private final String name;
	
	/**
	 * Initializes read-only function name.
	 * 
	 * Valid function name starts with  a letter 
	 * and after than can follow zero or more letters, digits or underscores. 
	 * If function name is invalid IllegalArgumentException is thrown.
	 * 
	 * @param name	name of an element.
	 */
	public ElementFunction(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Function name can not be null.");
		}
		if(!name.matches("[a-zA-Z][\\w_]*")) {
			throw new IllegalArgumentException(name + " is not valid function name.");
		}
		this.name = name;
	}
	
	/**
	 * Returns string returned is "@function_name",
	 * where function_name is this object's value.
	 * 
	 * @return value of element.
	 */
	@Override
	public String asText() {
		return "@" + name;
	}

}
