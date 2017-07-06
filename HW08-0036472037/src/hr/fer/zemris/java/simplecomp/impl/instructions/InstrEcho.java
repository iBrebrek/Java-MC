package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Writes value from register to system output.
 * Supports indirect addressing.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrEcho implements Instruction {
	
	/** Register. */
	private int registerIndex;
	/** Offset if register is indirect addressing, {@code null} if not indirect. */
	private Integer offset;
	
	/**
	 * Initializes register.
	 * 	
	 * @param arguments		must be single register.
	 */
	public InstrEcho(List<InstructionArgument> arguments) {
		if(arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if(!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException(
					"Argument must be a register."
			);
		}
		
		Integer descriptor = (Integer)arguments.get(0).getValue();
		
		registerIndex = RegisterUtil.getRegisterIndex(descriptor);
		offset = RegisterUtil.isIndirect(descriptor) ?
					RegisterUtil.getRegisterOffset(descriptor) : null;
	}

	@Override
	public boolean execute(Computer computer) {
		Object regValue = computer.getRegisters().getRegisterValue(registerIndex);
		if(offset == null) {
			System.out.print(String.valueOf(regValue));
		} else {
			Object memValue = computer.getMemory().getLocation((Integer)regValue + offset);
			System.out.print(String.valueOf(memValue));
		}
		return false;
	}
}
