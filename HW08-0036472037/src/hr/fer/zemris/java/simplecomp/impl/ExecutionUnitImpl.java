package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.ExecutionUnit;
import hr.fer.zemris.java.simplecomp.models.Instruction;

/**
 * Starting for program counter = 0
 * keeps executing instructions.
 * Stops if exception happens or if 
 * instruction returns true with method execute.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class ExecutionUnitImpl implements ExecutionUnit {

	@Override
	public boolean go(Computer computer) {
		computer.getRegisters().setProgramCounter(0);
		
		while(true) {
			Instruction instruction = 
				(Instruction)computer.getMemory().getLocation(
						computer.getRegisters().getProgramCounter()
			);
			computer.getRegisters().incrementProgramCounter();
			if(instruction.execute(computer)) {
				return true;
			}
		}
	}
}