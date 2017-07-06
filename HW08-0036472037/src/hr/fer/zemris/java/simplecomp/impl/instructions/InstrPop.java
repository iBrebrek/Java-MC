package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * From top of the stack takes value 
 * and puts it in given register.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrPop extends AbstractStackInstr {
	
	/**
	 * Initializes register.
	 * 	
	 * @param arguments		must be a single register.
	 */
	public InstrPop(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	public boolean execute(Computer computer) {
		Integer stackHead = AbstractStackInstr.getStackHead(computer);
		
		computer.getRegisters().setRegisterValue(
				registerIndex, 
				computer.getMemory().getLocation(stackHead + 1)
		);
	
		computer.getRegisters().setRegisterValue(
				Registers.STACK_REGISTER_INDEX, 
				stackHead + 1
		);
		return false;
	}
}
