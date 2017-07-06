package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * From system input reads integer 
 * and writes it on given memory location.
 * 
 * If reading was successful (number was integer),
 * sets register flag to {@code true}, or
 * to {@code false} if not integer.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class InstrIinput implements Instruction {
	
	/** Where to write. */
	private int location;
	
	/**
	 * Initializes memory location.
	 * 
	 * @param arguments		must be single memory location.
	 */
	public InstrIinput(List<InstructionArgument> arguments) {
		if(arguments.size() != 1) {
			throw new IllegalArgumentException("Expected 1 argument!");
		}
		if(!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException(
					"Argument must be a location."
			);
		}
		
		location = (Integer)arguments.get(0).getValue();
	}

	@Override
	public boolean execute(Computer computer) {
		Scanner scanner = new Scanner(System.in);
		
		do {
			String line = scanner.nextLine().trim();
			try {
				computer.getMemory().setLocation(
						location, 
						Integer.parseInt(line)
				);
				computer.getRegisters().setFlag(true);
			} catch (NumberFormatException notInteger) {
				computer.getRegisters().setFlag(false);
				scanner.close();
				return false;
			}
		} while(!computer.getRegisters().getFlag());
		
		scanner.close();
		return false;
	}
}
