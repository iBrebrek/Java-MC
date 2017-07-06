package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Stores current program counter on stack
 * and sets program counter to given address.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrCall implements Instruction {
	
	/** Address where function is. */
	private int address;
	
	/**
	 * Initializes function location.
	 * 
	 * @param arguments		must be single memory location.
	 */
	public InstrCall(List<InstructionArgument> arguments) {
		if(arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if(!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException(
					"Argument must be a location."
			);
		}
		
		address = (Integer)arguments.get(0).getValue();
	}

	@Override
	public boolean execute(Computer computer) {
		Integer stackHead = AbstractStackInstr.getStackHead(computer);
		
		computer.getMemory().setLocation(
				stackHead, 
				computer.getRegisters().getProgramCounter()
		);
		computer.getRegisters().setProgramCounter(address);
		
		computer.getRegisters().setRegisterValue(
				Registers.STACK_REGISTER_INDEX, 
				stackHead - 1 //because we pushed PC
		);
		return false;
	}

}
