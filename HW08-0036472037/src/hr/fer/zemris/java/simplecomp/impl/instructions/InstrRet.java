package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Returns from function.
 * From stack head takes address and sets it as program counter.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.4.2016.)
 */
public class InstrRet implements Instruction {
	
	/**
	 * Takes no arguments.
	 * 
	 * @param arguments		must be empty.
	 */
	public InstrRet(List<InstructionArgument> arguments) {
		if(!arguments.isEmpty()) {
			throw new IllegalArgumentException("Expected 0 arguments!");
		}
	}

	@Override
	public boolean execute(Computer computer) {
		Integer stackHead = AbstractStackInstr.getStackHead(computer);
		
		Integer addressPC = (Integer)computer.getMemory().getLocation(stackHead+1);
		computer.getRegisters().setProgramCounter(addressPC);
	
		computer.getRegisters().setRegisterValue(
				Registers.STACK_REGISTER_INDEX, 
				stackHead + 1 //because we poped PC
		);
		return false;
	}
}
