package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Into register loads something for memory location.
 * Does not support indirect addressing.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrLoad implements Instruction {
	/** Register - where to load. */
	private int registerIndex;
	/** Address - from where to load. */
	private int memoryLocation;
	
	/**
	 * Initializes register and memory.
	 * 
	 * @param arguments		first must be register, second address.
	 */
	public InstrLoad(List<InstructionArgument> arguments) {
		if(arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}
		if(!arguments.get(0).isRegister() || 
				RegisterUtil.isIndirect((Integer)arguments.get(0).getValue())) {
			throw new IllegalArgumentException(
					"First argument must be direct register."
			);
		}
		if(!arguments.get(1).isNumber()) {
			throw new IllegalArgumentException(
					"Second argument must a be memory location."
			);
		}
		
		registerIndex = RegisterUtil.getRegisterIndex(
				(Integer) arguments.get(0).getValue()
		);
		memoryLocation = (Integer) arguments.get(1).getValue();
	}

	@Override
	public boolean execute(Computer computer) {
		Object value = computer.getMemory().getLocation(memoryLocation);
		computer.getRegisters().setRegisterValue(registerIndex, value);
		
		return false;
	}

}
