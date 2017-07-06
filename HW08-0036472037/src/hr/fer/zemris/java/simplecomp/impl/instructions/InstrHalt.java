package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Stops processor.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrHalt implements Instruction {
	
	/**
	 * Takes no arguments
	 * 
	 * @param arguments		must be empty.
	 */
	public InstrHalt(List<InstructionArgument> arguments) {
		if(!arguments.isEmpty()) {
			throw new IllegalArgumentException("Expected 0 arguments!");
		}
	}

	@Override
	public boolean execute(Computer computer) {
		return true;
	}

}
