package hr.fer.zemris.java.custom.collections;

/**
 * Linked list-backed collection of objects.
 * 
 * Duplicate elements are allowed 
 * (each of those element will be held in different list node).
 * 
 * Storage of null references is not allowed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (20.3.2016.)
 */
public class LinkedListIndexedCollection extends Collection {
	
	/** Number of elements in this collection. */
	private int size;
	
	/** Reference to the first node of this linked list. */
	private ListNode first;
	
	/** Reference to the last node of this linked list. */
	private ListNode last;
	
	/**
	 * Default constructor, creates empty linked list.
	 */
	public LinkedListIndexedCollection() {
		/*
		 * This is unnecessary because they will be null by default anyway;
		 * but it was instructed to do it.
		 */
		setToDefault();
	}
	
	/**
	 * Creates linked list with all elements from given collection.
	 * 
	 * @param other	collection which elements are copied into this newly constructed collection
	 */
	public LinkedListIndexedCollection(Collection other) {
		if(other != null) {
			addAll(other);
		}
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
	 * Adds the given object into this collection at the end of collection
	 * (newly added element becomes the element at the biggest index).
	 * 
	 * Throws IllegalArgumentException if value is null.
	 * 
	 * @param value 	value being added
	 */
	@Override
	public void add(Object value) {
		insert(value, size);
	}
	
	/**
	 * Based on equals method determines if this collection contains given value.
	 * It is OK to ask if collection contains null, but it will result with false.
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
	 * If there are more objects equal to value 
	 * removes first found object (the one with lowest index).
	 * 
	 * @param value 	value to be checked and removed
	 * 
	 * @return true if value was in this collection
	 */
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		
		if(index == -1) {
			return false;
		} else {
			remove(index);
			return true;
		}
	}
	
	/**
	 * Creates new array with size of this collection and all collection's values.
	 * This method will never return null.
	 * 
	 * @return array with all objects from this collection
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		
		ListNode node = first;
		
		for(int index = 0; index < size; index++, node = node.next) {
			array[index] = node.value;
		}
		
		return array;
	}
	
	/**
	 * Method calls processor.process(...) for each element of this collection. 
	 * The order in which elements will be processed is ascending(0->size). 
	 * 
	 * @param processor 	processor which will process each element of this collection
	 */
	@Override
	public void forEach(Processor processor) {
		ListNode node = first;
		
		while(node != null) {
			processor.process(node.value);
			node = node.next;
		}
	}
	
	/**
	 * Removes all elements from this collection.
	 */
	@Override
	public void clear() {
		setToDefault();
	}
	
	/**
	 * Returns the object that is stored in linked list at position index. 
	 * 
	 * Valid indexes are 0 to size-1. 
	 * If index is invalid IndexOutOfBoundsException is thrown.
	 * 
	 * Max complexity is O(n/2).
	 * 
	 * @param index		find object at this index(first is at 0)
	 * 
	 * @return object at given index
	 */
	public Object get(int index) {
		if((index < 0) || (index > size-1)) {
			throw new IndexOutOfBoundsException("Valid indexes are 0 to " + (size-1));
		}
		
		return getNodeAt(index).value;
	}
	
	/**
	 * Inserts the given value at the given position in linked-list.
	 * Elements starting from this position are shifted one position.
	 * 
	 * Valid positions are 0 to size. 
	 * If position is invalid IndexOutOfBoundsException is thrown.
	 * 
	 * Value must not be null.
	 * If null is given IllegalArgumentException is thrown.
	 * 
	 * Max complexity is O(n/2).
	 * 
	 * @param value		object being inserted
	 * 
	 * @param position	position where given value is added
	 */
	public void insert(Object value, int position) {
		if(value == null) {
			throw new IllegalArgumentException("Value can not be null");
		}
		if((position < 0) || (position > size)) {
			throw new IndexOutOfBoundsException("Valid indexes are 0 to " + size);
		}

		if(position == 0) { //adding to the start
			ListNode newNode = new ListNode(value);
			
			if(first != null) {
				newNode.next = first;
				first.previous = newNode;
			} else { //because if first is null then last is null too
				last = newNode;
			}
			
			first = newNode;
			
		} else if(position == size) { //adding to the end
			ListNode newNode = new ListNode(value);
			
			last.next = newNode;
			newNode.previous = last;
			last = newNode;
			
		} else { //adding to the middle
			ListNode existingNode = getNodeAt(position);
			ListNode newNode = new ListNode(existingNode.previous, existingNode, value);
			
			existingNode.previous.next = newNode;
			existingNode.previous = newNode;
		}
		size++;
	}
	
	/**
	 * Searches the collection and returns the index of the first occurrence 
	 * of the given value or -1 if the value is not found.
	 * 
	 * Max complexity is O(n), while average is O(n/2).
	 * 
	 * @param value		searches this object
	 * 
	 * @return index of given value, or -1 if not found
	 */
	public int indexOf(Object value) {
		ListNode currentNode = first;
		int index = 0;
		
		while(currentNode != null) {
			if(currentNode.value.equals(value)) {
				return index;
			}
			currentNode = currentNode.next;
			index++;
		}
		
		return -1;
	}
	
	/**
	 * Removes element at specified index from collection.
	 * Element that was previously at location index+1 
	 * after this operation is on location index, etc. 
	 * 
	 * Valid indexes are 0 to size-1.
	 * If index is invalid IndexOutOfBoundsException is thrown. 
	 * 
	 * @param index		removes object at this position
	 */
	public void remove(int index) {
		if((index < 0) || (index > size-1)) {
			throw new IndexOutOfBoundsException("Valid indexes are 0 to " + (size-1));
		}
		
		if(index == 0) {
			if(first.next != null) {
				first.next.previous = null;
			} 
			first = first.next;
		} else if(index == size-1) {
			if(last.previous != null) {
				last.previous.next = null;
			}
			last = last.previous;
		} else {
			ListNode node = getNodeAt(index);
			node.previous.next = node.next;
			node.next.previous = node.previous;
		}
		size--;
	}

	/**
	 * Returns node at given index.
	 * 
	 * Index MUST be checked BEFORE calling this method.
	 * 
	 * @param index		index where node is
	 * 
	 * @return ListNode at given index
	 */
	private ListNode getNodeAt(int index) {
		if(index > size/2) {
			ListNode current = last;
			int position = size - 1;
			
			while(position != index) {
				current = current.previous;
				position--;
			}
			return current;
			
		} else {
			ListNode current = first;
			int position = 0;
			
			while(position != index) {
				current = current.next;
				position++;
			}
			return current;
		}
	}
	
	/**
	 * Sets size to 0, first and last to null.
	 */
	private void setToDefault() {
		size = 0;
		first = null;
		last = null;
	}
	
	/**
	 * Nested class that defines one node in linked list.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (20.3.2016.)
	 */
	private static class ListNode {
		
		/** First node before this node. */
		private ListNode previous;
		
		/** First node after this node. */
		private ListNode next;
		
		/** Value stored in this node. */
		private Object value;

		/**
		 * Sets all variables.
		 * 
		 * @param previous	node before
		 * @param next		node after
		 * @param value		value in this node
		 */
		private ListNode(ListNode previous, ListNode next, Object value) {
			this.previous = previous;
			this.next = next;
			this.value = value;
		}

		/**
		 * All but value are null.
		 * 
		 * @param value		value stored in this node
		 */
		private ListNode(Object value) {
			this(null, null, value);
		}
		
		/**
		 * Default constructor sets all to null.
		 */
		private ListNode() {
			this(null, null, null);
		}
	}
}
