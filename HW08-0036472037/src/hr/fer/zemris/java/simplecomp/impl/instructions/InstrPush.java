package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Value from given register pushes on top of the stack.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrPush extends AbstractStackInstr {
	
	/**
	 * Initializes register.
	 * 	
	 * @param arguments		must be a single register.
	 */
	public InstrPush(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	public boolean execute(Computer computer) {
		Integer stackHead = AbstractStackInstr.getStackHead(computer);
		
		computer.getMemory().setLocation(
				stackHead, 
				computer.getRegisters().getRegisterValue(registerIndex)
		);
		
		computer.getRegisters().setRegisterValue(
				Registers.STACK_REGISTER_INDEX, 
				stackHead - 1 //because we pushed PC
		);
		return false;
	}
}
