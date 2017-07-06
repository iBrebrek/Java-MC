package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;


import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Decreases value in register by 1.
 * Does not support indirect addressing.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrDecrement extends AbstractIncDec {

	/**
	 * Initializes used register.
	 * 
	 * @param arguments		must be single register.
	 */
	public InstrDecrement(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	protected Integer operation(Integer value) {
		return value - 1;
	}
}
