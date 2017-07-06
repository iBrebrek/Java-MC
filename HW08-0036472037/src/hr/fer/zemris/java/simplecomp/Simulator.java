package hr.fer.zemris.java.simplecomp;

import java.io.File;
import java.util.Scanner;

import hr.fer.zemris.java.simplecomp.impl.ComputerImpl;
import hr.fer.zemris.java.simplecomp.impl.ExecutionUnitImpl;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.ExecutionUnit;
import hr.fer.zemris.java.simplecomp.models.InstructionCreator;
import hr.fer.zemris.java.simplecomp.parser.InstructionCreatorImpl;
import hr.fer.zemris.java.simplecomp.parser.ProgramParser;

/**
 * Simulator for 8th homework.
 * Argument can be nothing, then you will be asked for file path,
 * or argument can be file path.
 * If file path is not file, program is terminated.
 * File should be assembly code.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.5.2016.)
 */
public class Simulator {

	/**
	 * Entry point
	 * 
	 * @param args		nothing or file path
	 */
	public static void main(String[] args) {
		if(args.length > 1) {
			System.err.println("Invalid number of arguments.");
		}
		
		String filePath = null;
		
		if(args.length == 0) {
			Scanner sc = new Scanner(System.in);
			do {
				System.out.print("Path to text with code: ");
			} while((filePath = sc.nextLine().trim()).isEmpty());
			sc.close();
		} else {
			//length is 1
			filePath = args[0];
		}
		
		if(!(new File(filePath).isFile())) {
			System.err.println("File doesn't exist.");
			System.exit(-1);
		}
		
		Computer comp = new ComputerImpl(256, 16);
		InstructionCreator creator = new InstructionCreatorImpl(
				"hr.fer.zemris.java.simplecomp.impl.instructions"
		);

		try {
			ProgramParser.parse(
					filePath,
					comp,
					creator
			);
			ExecutionUnit exec = new ExecutionUnitImpl();
		
			exec.go(comp);
			
		} catch(Exception exc) {
			System.err.println("Exception while parsing/executing " + filePath + ":");
			exc.printStackTrace();
			System.exit(-1);
		}
	}
}
