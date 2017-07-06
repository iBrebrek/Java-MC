package hr.fer.zemris.java.gui.calc;

/**
 * A stack that can push, pop and peek.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (15.5.2016.)
 */
public interface IStack {
	
	/**
	 * Push value onto stack.
	 * 
	 * @param value		value being pushed.
	 */
	void push(Object value);
	
	/**
	 * Take and remove value from top of the stack.
	 * 
	 * @return remove value from top.
	 * 
	 * @throws IndexOutOfBoundsException if stack is empty.
	 */
	Object pop();
	
	/**
	 * Get value from top of the stack.
	 * 
	 * @return value on top of the stack.
	 * 
	 * @throws IndexOutOfBoundsException if stack is empty.
	 */
	Object peek();
	
	/**
	 * Check if stack is empty.
	 * 
	 * @return {@code true} if stack is empty.
	 */
	boolean isEmpty();
	
	/**
	 * Check how many elements are on the stack.
	 * 
	 * @return number of elements.
	 */
	int size();
}
