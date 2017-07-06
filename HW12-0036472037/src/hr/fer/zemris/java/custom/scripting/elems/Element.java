package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class used for the representation of expressions.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public abstract class Element {
	
	/**
	 * Returns an empty string.
	 * 
	 * @return empty string.
	 */
	public String asText() {
		return new String();
	}

	/**
	 * Retrieve value stored inside this object.
	 * 
	 * @return stored value.
	 */
	public abstract Object getValue();
	
	/**
	 * Accepts given visitor.
	 * In other words, it says visitor
	 * which method to use on this object.
	 * 
	 * @param visitor		object being accepted.
	 */
	public abstract void accept(IElementVisitor visitor);
}
