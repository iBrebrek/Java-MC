package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Jumps to given location.
 * Jump is done by changing program counter.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrJump extends AbstractJump {

	/**
	 * Initializes jump location.
	 * 
	 * @param arguments		must be single memory location.
	 */
	public InstrJump(List<InstructionArgument> arguments) {
		super(arguments);
	}
}
