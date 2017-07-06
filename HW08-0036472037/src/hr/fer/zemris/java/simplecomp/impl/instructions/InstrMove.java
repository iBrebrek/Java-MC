package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Moves second argument into first argument.
 * First argument can be anything but number or string.
 * Second argument can be anything but string.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrMove implements Instruction {
	
	/** Register index, destination. */
	private int first;
	/** Offset if second is indirect, {@code null} if not indirect. */
	private Integer firstOffset;
	/** Source, register index or number. */
	private int second;
	/** Offset if second is indirect, {@code null} if not indirect. */
	private Integer secondOffset;
	/** {@code true} if second is number. */
	private boolean isNumber;
	
	/**
	 * Initializes first and second argument.
	 * See class documentation for more information.
	 * 
	 * @param arguments		must be 2 arguments.
	 */
	public InstrMove(List<InstructionArgument> arguments) {
		if(arguments.size() != 2) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}
		if(arguments.get(0).isNumber() || arguments.get(0).isString()) {
			throw new IllegalArgumentException(
					"First argument must be register or indirect address."
			);
		}
		if(arguments.get(1).isString()) {
			throw new IllegalArgumentException(
					"Second argument must be register, indirect address or number."
			);
		}
		
		Integer firstDescriptor = (Integer)arguments.get(0).getValue(); 
		first = RegisterUtil.getRegisterIndex(firstDescriptor);
		firstOffset = RegisterUtil.isIndirect(firstDescriptor) ?
				RegisterUtil.getRegisterOffset(firstDescriptor) : null;
				
		Integer secondDescriptor = (Integer)arguments.get(1).getValue(); 
		isNumber = arguments.get(1).isNumber();
		if(isNumber) {
			second = secondDescriptor; //means that this is not descriptor
			secondOffset = null;
		} else {
			second = RegisterUtil.getRegisterIndex(secondDescriptor);
			secondOffset = RegisterUtil.isIndirect(secondDescriptor) ?
					RegisterUtil.getRegisterOffset(secondDescriptor) : null;	
		}
	}

	@Override
	public boolean execute(Computer computer) {
		Object movedValue = null;
		if(isNumber) {
			movedValue = second;
		} else {
			Object regValue = computer.getRegisters().getRegisterValue(second);
			if(secondOffset == null) {
				movedValue = regValue;
			} else {
				movedValue = computer.getMemory().getLocation(
						(Integer)regValue + secondOffset
				);
			}
		}
		
		if(firstOffset == null) {
			computer.getRegisters().setRegisterValue(first, movedValue);
		} else {
			computer.getMemory().setLocation(
					(Integer)computer.getRegisters().getRegisterValue(first) + firstOffset, 
					movedValue
			);
		}
		
		return false;
	}
}
