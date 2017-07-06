package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Memory;

/**
 * Implementation of memory.
 * At each memory location object is stored.
 * If location is empty {@code null} is stored.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (30.4.2016.)
 */
public class MemoryImpl implements Memory {
	
	/** Objects stored in memory. */
	private Object[] memory;
	
	/**
	 * Initializes memory size.
	 * 
	 * @param size		memory size.
	 * 
	 * @throws IllegalArgumentException if {@code size} is negative.
	 */
	public MemoryImpl(int size) {
		if(size < 0) {
			throw new IllegalArgumentException("Memory size can not be negative.");
		}
		memory = new Object[size];
	}

	@Override
	public void setLocation(int location, Object value) {
		checkLocation(location);
		memory[location] = value;
	}

	@Override
	public Object getLocation(int location) {
		checkLocation(location);
		return memory[location];
	}

	/**
	 * Throws {@code IndexOutOfBoundsException} if location is invalid.
	 * 
	 * @param location		memory location.
	 */
	private void checkLocation(int location) {
		if(location < 0) {
			throw new IndexOutOfBoundsException("Location can not be negative.");
		}
		if(location > memory.length) {
			throw new IndexOutOfBoundsException("Max location is: " 
						+ memory.length + ", you entered: " + location);
		}
	}
}
