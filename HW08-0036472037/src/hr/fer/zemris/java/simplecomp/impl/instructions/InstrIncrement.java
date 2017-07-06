package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Increases value in register by 1.
 * Does not support indirect addressing.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrIncrement extends AbstractIncDec {

	/**
	 * Initializes used register.
	 * 
	 * @param arguments		must be single register.
	 */
	public InstrIncrement(List<InstructionArgument> arguments) {
		super(arguments);
	}

	@Override
	protected Integer operation(Integer value) {
		return value + 1;
	}
}
