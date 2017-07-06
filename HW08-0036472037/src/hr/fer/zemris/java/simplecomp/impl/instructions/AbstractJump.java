package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Abstract representation of jump commands.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
abstract class AbstractJump implements Instruction {

	/** Where to jump. */
	private int location;
	
	/**
	 * Initializes jump location.
	 * 
	 * @param arguments		must be single memory location.
	 */
	public AbstractJump(List<InstructionArgument> arguments) {
		if(arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if(!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException(
					"Argument must be a number(location)."
			);
		}
		
		location = RegisterUtil.getRegisterIndex( 
				(Integer)arguments.get(0).getValue()
		);
	}

	@Override
	public boolean execute(Computer computer) {
		computer.getRegisters().setProgramCounter(location);
		return false;
	}
}
