package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;

/**
 * Utilities used by shell commands.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (26.4.2016.)
 */
public class CommandsUtility {
	
	/**
	 * Writes in environment and throws {@link IOException} as {@code RuntimeException}.
	 * 
	 * @param env		environment where message is written.
	 * @param msg		message being written.
	 */
	public static void writeln(Environment env, String msg) {
		//using this method so I don't need to try-catch every time I want to write...
		try {
			env.writeln(msg);
		} catch (IOException stopWorking) {
			throw new RuntimeException("Unable to write in environment.");
		}
	}

	/**
	 * Splits argument into one or two arguments.
	 * OR returns {@code null} if argument is invalid(example: only 1 ").
	 * Argument is split by spaces, but not if argument is inside "".
	 * 
	 * Examples: 
	 * argument: A B	will be split into: [A, B]	2 args
	 * argument: "A B" 	will NOT be split: 	[A B] 	1 arg
	 * argument "A"B	will be split into: [A, B] 	2 args
	 * argument "A""B" 	will be split into: [A, B] 	2 args
	 * 
	 * @param argument		whole argument in a single line.
	 * @return arguments or {@code null} if invalid.
	 */
	public static String[] splitArgument(String argument) {
		String line = argument.trim();
		
		if(line.isEmpty()) {
			return new String[0];
		}
		
		int index = line.indexOf('"');
		
		if(index != -1 && index == line.lastIndexOf('"')) {
			return null; //because invalid argument
		}
		
		index = index == 0 ? 
				line.substring(index+1).indexOf('"')+1 :
				line.indexOf(' ');
				
		if(index == -1 || index == line.length()-1) {
			return new String[]{line.replace("\"", "").trim()};
		}
		
		String first = line.substring(0, index).replace("\"", "").trim();
		String second = line.substring(index + 1).replace("\"", "").trim();

		return new String[]{first, second};
	}
}
