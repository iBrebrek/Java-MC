package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Stores multiplication of second and third register into first register.
 * Supports only registers. Does not support indirect addressing.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrMul extends AbstractMulAdd {

	/**
	 * Initializes all 3 register, see class documentation for more information.
	 * 
	 * @param arguments		must be 3 registers.
	 */
	public InstrMul(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	protected Integer operation(Integer value1, Integer value2) {
		return value1 * value2;
	}
}