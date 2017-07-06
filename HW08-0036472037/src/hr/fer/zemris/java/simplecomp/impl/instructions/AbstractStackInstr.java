package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Abstract representation of stack instruction that have 1 argument.
 * Class offers static utility command to obtain stack head.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
abstract class AbstractStackInstr implements Instruction {
	
	/** Register used for this command. */
	protected int registerIndex;
	
	/**
	 * Initializes register.
	 * 	
	 * @param arguments		must be a single register.
	 */
	public AbstractStackInstr(List<InstructionArgument> arguments) {
		if(arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if(!arguments.get(0).isRegister() || 
				RegisterUtil.isIndirect((Integer)arguments.get(0).getValue())) {
			throw new IllegalArgumentException(
					"Argument must be direct register."
			);
		}
		
		registerIndex = RegisterUtil.getRegisterIndex(
				(Integer)arguments.get(0).getValue()
		);
	}
	
	/**
	 * Returns memory location of stack head.
	 * 
	 * @param computer		computer where memory is.
	 * @return stack head address.
	 */
	static Integer getStackHead(Computer computer) {  //package - private method
		try {
			return (Integer)computer.getRegisters()
					.getRegisterValue(Registers.STACK_REGISTER_INDEX);
		} catch(IndexOutOfBoundsException | ClassCastException exc) {
			//if there is no that many registers, or if not number in that register
			throw new RuntimeException("Stack is not supported for this program.");
		}
				
	}
}
