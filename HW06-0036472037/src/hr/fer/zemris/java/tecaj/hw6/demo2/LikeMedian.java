package hr.fer.zemris.java.tecaj.hw6.demo2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * In internal collection stores elements which have defined natural ordering.
 * On calling method get, median is calculated and returned. 
 * 
 * @param <T> 	type of object used as median
 * 
 * @author Ivica Brebrek
 * @version 1.0  (18.4.2016.)
 */
public class LikeMedian<T extends Comparable<? super T>> implements Iterable<T>{
	/** This list stores element in order they were added. */
	private List<T> givenOrder = new LinkedList<>();
	/** This list is used to sort all added elements. Has same elements as {@code givenOrder}.*/
	private List<T> sortedOrder = new ArrayList<>();
	
	/**
	 * Adds element to internal collection. 
	 * 
	 * @param element	element being added.
	 */
	public void add(T element) {
		givenOrder.add(element);
		sortedOrder.add(element);
	}
	
	/**
	 * Returns median of all added elements, 
	 * or empty optional if no elements were added.
	 * 
	 * If number of added elements is odd, median element is returned. 
	 * If number of added elements is even, smaller of the two middle elements is returned.
	 * 
	 * @return median or empty optional.
	 */
	public Optional<T> get() {
		if(sortedOrder.isEmpty()) {
			return Optional.empty();
		}
		
		Collections.sort(sortedOrder);
		int size = sortedOrder.size();
		
		if(size % 2 == 0) { //even
			int secondIndex = size / 2;
			int firstIndex = secondIndex - 1;
			T first = sortedOrder.get(firstIndex);
			T second = sortedOrder.get(secondIndex);
			
			if(first.compareTo(second) < 0) {
				return Optional.of(first);
			} else {
				return Optional.of(second);
			}
			
		} else { //odd
			int index = size / 2;
			T object = sortedOrder.get(index);
			return Optional.of(object);
		}
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return givenOrder.iterator();
	}
}
