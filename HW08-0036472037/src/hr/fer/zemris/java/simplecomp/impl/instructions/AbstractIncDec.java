package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * All common things for instruction increment and decrement.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
abstract class AbstractIncDec implements Instruction {

	/** Register where number is stored. */
	private int register;
	
	/**
	 * Initializes register.
	 * 
	 * @param arguments		must be 1 argument that is a register.
	 */
	public AbstractIncDec(List<InstructionArgument> arguments) {
		if(arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		InstructionArgument argument = arguments.get(0);
		
		if(!argument.isRegister() || 
					RegisterUtil.isIndirect((Integer)argument.getValue())) {
			throw new IllegalArgumentException(
					"Argument must be direct register."
			);
		}
		
		register = RegisterUtil.getRegisterIndex(
				(Integer)argument.getValue()
		);
	}

	@Override
	public boolean execute(Computer computer) {
		Object value = computer.getRegisters().getRegisterValue(register);
		computer.getRegisters().setRegisterValue(
					register,
					operation((Integer)value)
		);
		return false;
	}
	
	/**
	 * Determines how will value in register change.
	 * 
	 * @param value		current value in register.
	 * @return new value in register
	 */
	protected abstract Integer operation(Integer value);
}
