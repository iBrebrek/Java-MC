package hr.fer.zemris.java.custom.collections;

/**
 * Stack-like collection.
 * It offers regular stack functions such as:
 * push, pop, peek, isEmpty, size, clear.
 * 
 * Null references are not allowed on the stack.
 * Duplicates are allowed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (21.3.2016.)
 */
public class ObjectStack {
	
	/** This is where elements are actually stored. */
	private ArrayIndexedCollection array = new ArrayIndexedCollection();
	
	/**
	 * Checks if this stack is empty.
	 * It is equivalent to (size()==0).
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return array.isEmpty();
	}
	
	/**
	 * Returns the number of currently stored objects on this stack.
	 * 
	 * @return number of objects on this stack
	 */
	public int size() {
		return array.size();
	}
	
	/**
	 * Pushes given value on the stack. 
	 * Trying to push null value will result with IllegalArgumentException.
	 * 
	 * @param value		object being added to stack
	 */
	public void push(Object value) {
		if(value == null) {
			throw new IllegalArgumentException("Null can not be placed on stack");
		}
		array.add(value);
	}
	
	/**
	 * Returns last element placed on stack and removes it from stack.
	 * Throws EmptyStackException if stack is empty.
	 * 
	 * @return last element placed on stack
	 */
	public Object pop() {
		Object element = peek();
		array.remove(size()-1);
		return element;
	}
	
	/**
	 * Returns last element placed on stack (without removing it).
	 * Throws EmptyStackException if stack is empty.
	 * 
	 * @return last element placed on stack
	 */
	public Object peek() {
		if(isEmpty()) {
			throw new EmptyStackException
				("No elements can be returned because the stack is empty.");
		}
		return array.get(size()-1);
	}
	
	/**
	 * Removes all elements from stack.
	 */
	public void clear() {
		array.clear();
	}
}
