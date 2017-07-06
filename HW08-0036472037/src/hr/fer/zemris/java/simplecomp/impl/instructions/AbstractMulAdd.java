package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/***
 * All common things for instructions mul and add.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
abstract class AbstractMulAdd implements Instruction {
	
	/** Where result is stored. */
	private int firstRegister;
	/** First operand. */
	private int secondRegister;
	/** Second operand. */
	private int thirdRegister;
	
	/**
	 * Initializes all 3 registers.
	 * 
	 * @param arguments		must be 3 registers, first is where result is stored.
	 */
	public AbstractMulAdd(List<InstructionArgument> arguments) {
		if(arguments.size() != 3) {
			throw new IllegalArgumentException("Expected 3 arguments!");
		}
		for(int i = 0; i < 3; i++) {
			if(!arguments.get(i).isRegister() ||
					RegisterUtil.isIndirect((Integer)arguments.get(i).getValue())) {
				throw new IllegalArgumentException(
						"Type mismatch for argument "+i+"!"
				);
			}
		}
		firstRegister = RegisterUtil.getRegisterIndex(
				(Integer)arguments.get(0).getValue()
		);
		secondRegister = RegisterUtil.getRegisterIndex(
				(Integer)arguments.get(1).getValue()
		);
		thirdRegister = RegisterUtil.getRegisterIndex(
				(Integer)arguments.get(2).getValue()
		);
	}

	@Override
	public boolean execute(Computer computer) {
		Object value1 = computer.getRegisters().getRegisterValue(secondRegister);
		Object value2 = computer.getRegisters().getRegisterValue(thirdRegister);
		computer.getRegisters().setRegisterValue(
					firstRegister,
					operation((Integer)value1, (Integer)value2)
		);
		return false;
	}
	
	/**
	 * What to store in first register.
	 * 
	 * @param value1	value in second register.
	 * @param value2	value in third register.
	 * @return value in first register.
	 */
	protected abstract Integer operation(Integer value1, Integer value2);
}
