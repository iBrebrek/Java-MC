package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Jumps to given location ONLY IF register flag is {@code true}.
 * Jump is done by changing program counter.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrJumpIfTrue extends AbstractJump {

	/**
	 * Initializes jump location.
	 * 
	 * @param arguments		must be single memory location.
	 */
	public InstrJumpIfTrue(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	public boolean execute(Computer computer) {
		if(computer.getRegisters().getFlag()) {
			return super.execute(computer);
		} else {
			return false;
		}
	}
}
