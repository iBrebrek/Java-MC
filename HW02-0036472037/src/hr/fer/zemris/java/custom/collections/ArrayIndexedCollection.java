package hr.fer.zemris.java.custom.collections;


/**
 * Resizable array-backed collection of objects.
 * 
 * Duplicate elements are allowed.
 * Storage of null references is not allowed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (20.3.2016.)
 */
public class ArrayIndexedCollection extends Collection {
	
	/** If capacity is not specified, this will be starting capacity. */
	private static final int DEFAULT_CAPACITY = 16;
	
	/** When collection is full, capacity is multiplied by this constant */
	private static final int MULTIPLIER = 2;
	
	/** Number of elements in this collection. */
	private int size;
	
	/** Current capacity of allocated array of object references. */
	private int capacity;
	
	/** An array of object references which length is determined by capacity variable. */
	private Object[] elements;
	
	/**
	 * Constructor that accepts both other collection from which elements 
	 * will be "taken" and starting capacity of this collection.
	 * 
	 * @param other		this (array indexed)collection will have 
	 * 					all elements from given collection
	 * @param initialCapacity	starting capacity of this collection
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity can not be less than 1");
		}
		capacity = initialCapacity;
		elements = new Object[capacity];

		if(other != null) {
			addAll(other);
		}
	}
	
	/**
	 * Constructor that accepts collection from which elements will be "taken".
	 * 
	 * Newly created collection will have capacity that is same or bigger
	 * than a capacity of given collection.
	 * 
	 * @param other		this (array indexed)collection will have 
	 * 					all elements from given collection	
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructor that lets you define starting capacity of this collection.
	 * Collection will be empty.
	 * 
	 * @param initialCapacity 	this collection's starting capacity
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		this(null, initialCapacity);
	}

	/**
	 * Default constructor sets capacity to default value.
	 * Collection will be empty.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Returns the number of currently stored objects in this collection.
	 * 
	 * @return number of objects in this collection
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Adds the given object into this collection.
	 * Value is added at the end of collection.
	 * 
	 * Complexity is O(1), unless the collection is full 
	 * then increasing capacity will slow the process.
	 * 
	 * Adding null will result with IllegalArgumentException.
	 * 
	 * @param value 	object being added
	 */
	@Override
	public void add(Object value) {
		insert(value, size);
	}
	
	/**
	 * Checks if this collection contains given value.
	 * 
	 * It is OK to ask if collection contains null, 
	 * but it will always result with false since
	 * ArrayIndexedCollection does not store nulls.
	 * 
	 * @param value 	looks for this value in the collection
	 * 
	 * @return returns true only if the collection contains value
	 */
	@Override
	public boolean contains(Object value) {
		return (indexOf(value) != -1);
	}
	
	/**
	 * Checks if collection has given value and if so removes one occurrence of it.
	 * That one occurrence will be the one at the lowest position.
	 * 
	 * @param value 	value to be checked and removed
	 * 
	 * @return true if value was in this collection, false otherwise
	 */
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if(index == -1) {
			return false;
		}
		
		remove(index);
		return true;
	}
	
	/**
	 * Creates new array with length equal to size (not capacity) 
	 * of this collection and all collection's values.
	 * 
	 * This method will never return null.
	 * 
	 * @return array with all objects from this collection
	 */
	@Override
	public Object[] toArray() {
		return copyElements(size);
	}
	
	/**
	 * Method calls processor.process(...) for each element of this collection. 
	 * The order in which elements will be processed is ascending(0->size). 
	 * 
	 * @param processor 	processor which will process each element of this collection
	 */
	@Override
	public void forEach(Processor processor) {
		for(int index = 0; index < size; index++) {
			processor.process(elements[index]);
		}
	}
	
	/**
	 * Returns object stored at position index.
	 * 
	 * Valid index is between 0 and size-1.
	 * Invalid index will result with IndexOutOfBoundsException.
	 * 
	 * Complexity is O(1).
	 * 
	 * @param index 	position where object should be
	 * @return object found at position index
	 */
	public Object get(int index) {
		if((index < 0) || (index > size-1)) {
			throw new IndexOutOfBoundsException("Index must be between 0 and " + (size-1));
		}
		return elements[index];
	}
	
	/**
	 * Removes all elements from the collection.
	 * Capacity stays the same.
	 */
	@Override
	public void clear() {
		/*
		 * This might be better than for:
		 * elements = new Object[capacity]
		 * it would take more memory but might be faster
		 */
		for(int index = 0; index < size; index++) {
			elements[index] = null;
		}
		size = 0;
	}
	
	/**
	 * Inserts object to given position.
	 * Valid position is between 0 and size.
	 * Invalid position will result with IndexOutOfBoundsException,
	 * while trying to add null will result with IllegalArgumentException.
	 * 
	 * @param value		object being inserted
	 * @param position	position in the array where the given value is being added
	 */
	public void insert(Object value, int position) {
		if((position < 0) || (position > size)) {
			throw new IndexOutOfBoundsException("Index must be between 0 and " + size);
		}
		if(value == null) {
			throw new IllegalArgumentException
				("ArrayIdexedCollection does not allow null references");
		}
		
		ensureCapacity();
		shiftByOneToRight(position);
		elements[position] = value;
		size++;
	}
	
	/**
	 * Searches the collection for given value and returns its position.
	 * 
	 * If more of the same values are in the collection 
	 * the lowest position will be returned.
	 * 
	 * If value is not found -1 is return.
	 * 
	 * Complexity is O(n).
	 * 
	 * @param value		object being searched
	 * @return position of given value or -1 if collection does not contain value
	 */
	public int indexOf(Object value) {
		for(int index = 0; index < size; index++) {
			if(elements[index].equals(value)) {
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * Removes element at specified index from collection.
	 * 
	 * Element that was previously at location index+1 
	 * after this operation is on location index, etc.
	 * 
	 * Valid index is between 0 and size-1.
	 * Invalid index will result with IndexOutOfBoundsException.
	 * 
	 * @param index object at this index is being removed
	 */
	public void remove(int index) {
		if((index < 0) || (index > size-1)) {
			throw new IndexOutOfBoundsException("Legal indexes are 0 to " + (size-1));
		}
		/*
		 * If instead of end we used size there would be a problem if the array was full.
		 * Last one would be elements[capacity], which is obviously impossible.
		 */
		for(int end = size-1; index < end; index++) {
			elements[index] = elements[index+1];
		}
		elements[size] = null;
		
		size--;
	}
	
	/**
	 * Makes sure that capacity is big enough.
	 * This is being called before adding any object.
	 */
	private void ensureCapacity() {
		if(size >= capacity) {
			increaseCapacity();
		}
	}

	/**
	 * Increases capacity by multiplying it with MULTIPLIER.
	 * Called ONLY by ensureCapacity().
	 */
	private void increaseCapacity() {
		capacity = capacity * MULTIPLIER;		
		elements = copyElements(capacity);
	}
	
	/**
	 * Creates new array with same elements in this collection but different capacity.
	 * All elements will stay at the same position.
	 * 
	 * @param capacityOfReturnedArray 	number of fields in array (both filled and empty)
	 * 
	 * @return new array with described properties
	 */
	private Object[] copyElements(int capacityOfReturnedArray) {
		Object[] array = new Object[capacityOfReturnedArray];
		
		for(int index = 0; index < size; index++) {
			array[index] = elements[index];
		}
		
		return array;
	}	
	
	/**
	 * Starting from given position to last position 
	 * shifts every element by one to RIGHT.
	 * 
	 * @param startingPosition starts shifting from that position
	 */
	private void shiftByOneToRight(int startingPosition) {
		ensureCapacity(); //because if array is full shifting would fail with null reference exc.
		for(int index = size-1; index >= startingPosition; index--) {
			elements[index+1] = elements[index];
		}
	}
}
