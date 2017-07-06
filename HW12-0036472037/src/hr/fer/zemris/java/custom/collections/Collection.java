package hr.fer.zemris.java.custom.collections;

/**
 * General collection of objects.
 * 
 * This is only a skeleton for other collections, 
 * therefore, it should never be used as a real collection.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (20.3.2016.)
 */
public class Collection {
	
	/**
	 * Default constructor.
	 * Only difference from Object constructor is the visibility.
	 */
	protected Collection() {
	}
	
	/**
	 * Checks if this collection is empty.
	 * It is equivalent to (size()==0).
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Returns the number of currently stored objects in this collection.
	 * 
	 * This basic implementation always returns 0.
	 * 
	 * @return number of objects in collection
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds the given object into this collection.
	 * 
	 * This basic implementation does nothing.
	 * 
	 * @param value 	value being added
	 */
	public void add(Object value) {
	}
	
	/**
	 * Based on equals method determines if this collection contains given value.
	 * It is OK to ask if collection contains null.
	 * 
	 * This basic implementation always returns false. 
	 * 
	 * @param value 	looks for this value in the collection
	 * 
	 * @return returns true only if the collection contains value
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Checks if collection has given value and if so removes one occurrence of it.
	 * It is not specified which occurrence of it will be removed.
	 * 
	 * This basic implementation always returns false and does not remove.
	 * 
	 * @param value 	value to be checked and removed
	 * 
	 * @return true if value is in this collection
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Creates new array with size of this collection and all collection's values.
	 * This method will never return null.
	 * 
	 * This basic implementation throws UnsupportedOperationException.
	 * 
	 * @return array with all objects from this collection
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method calls processor.process(.) for each element of this collection. 
	 * The order in which elements will be processed is undefined in this class. 
	 * 
	 * This basic implementation does nothing.
	 * 
	 * @param processor 	processor which will process each element of this collection
	 */
	public void forEach(Processor processor) {
	}
	
	/**
	 * Adds into itself all elements from given collection. 
	 * 
	 * This other collection remains unchanged.
	 * 
	 * @param other 	collection containing elements being added to this collection
	 */
	public void addAll(Collection other) {
		
		class Adder extends Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		
		other.forEach(new Adder());
	}
	
	/**
	 * Removes all elements from this collection.
	 * 
	 * This basic implementation does nothing.
	 */
	public void clear() {
	}
}
