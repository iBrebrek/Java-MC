package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Implementation of registers.
 * Contains registers, program counter and flag.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (30.4.2016.)
 */
public class RegistersImpl implements Registers {
	
	/** Program counter. */
	private int counter;
	/** A single flag. */
	private boolean flag;
	/** Registers. */
	private Object[] register;
	
	/**
	 * Initializes register length.
	 * 
	 * @param regsLen		register length.
	 * 
	 * @throws IllegalArgumentException if {@code regsLen} is negative.
	 */
	public RegistersImpl(int regsLen) {
		if(regsLen < 0) {
			throw new IllegalArgumentException("Length can not be negative.");
		}
		register = new Object[regsLen];
	}

	@Override
	public Object getRegisterValue(int index) {
		checkIndex(index);
		return register[index];
	}

	@Override
	public void setRegisterValue(int index, Object value) {
		checkIndex(index);
		register[index] = value;
	}
	
	/**
	 * Throws {@code IndexOutOfBoundsException} if index is invalid.
	 * 
	 * @param index		memory location.
	 */
	private void checkIndex(int index) {
		if(index < 0) {
			throw new IndexOutOfBoundsException("Indexs can not be negative.");
		}
		if(index > register.length) {
			throw new IndexOutOfBoundsException("Max index is: " 
						+ register.length + ", you entered: " + index);
		}
	}

	@Override
	public int getProgramCounter() {
		return counter;
	}

	@Override
	public void setProgramCounter(int value) {
		counter = value;
	}

	@Override
	public void incrementProgramCounter() {
		counter++;
	}

	@Override
	public boolean getFlag() {
		return flag;
	}

	@Override
	public void setFlag(boolean value) {
		flag = value;
	}
}
