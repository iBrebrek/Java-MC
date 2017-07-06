package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Checks if values in 2 registers are same.
 * Sets register flag to {@code true} if given registers share same value,
 * or sets to {@code false} if not same.
 * Does not support indirect addressing.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrTestEquals implements Instruction {
	
	/** First register. */ 
	private int firstIndex;
	/** Second register. */
	private int secondIndex;
	
	/**
	 * Initializes both registers.
	 * 
	 * @param arguments			must be 2 registers.
	 */
	public InstrTestEquals(List<InstructionArgument> arguments) {
		if(arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}
		if(!arguments.get(0).isRegister() || 
				RegisterUtil.isIndirect((Integer)arguments.get(0).getValue())) {
			throw new IllegalArgumentException(
					"First argument must be direct register."
			);
		}
		if(!arguments.get(1).isRegister() || 
				RegisterUtil.isIndirect((Integer)arguments.get(1).getValue())) {
			throw new IllegalArgumentException(
					"Second argument must be direct register."
			);
		}
		
		firstIndex = RegisterUtil.getRegisterIndex(
				(Integer) arguments.get(0).getValue()
		);
		secondIndex = RegisterUtil.getRegisterIndex(
				(Integer) arguments.get(1).getValue()
		);
	}

	@Override
	public boolean execute(Computer computer) {
		Object first = computer.getRegisters().getRegisterValue(firstIndex);
		Object second = computer.getRegisters().getRegisterValue(secondIndex);
		
		computer.getRegisters().setFlag(
				first.equals(second)
		);
		
		return false;
	}

}
