package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Data structure of computer.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.5.2016.)
 */
public class ComputerImpl implements Computer {
	
	/** Computer's registers. */
	private Registers registers;
	/** Computer's memory. */
	private Memory memory;
	
	/**
	 * Initializes computer's registers and memory.
	 * 
	 * @param memSize	memory size.
	 * @param regsLen	registers length.
	 */
	public ComputerImpl(int memSize, int regsLen) {
		memory = new MemoryImpl(memSize);
		registers = new RegistersImpl(regsLen);
	}

	@Override
	public Registers getRegisters() {
		return registers;
	}

	@Override
	public Memory getMemory() {
		return memory;
	}
}
