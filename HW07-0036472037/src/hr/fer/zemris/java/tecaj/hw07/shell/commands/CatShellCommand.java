package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Opens given file and writes its content.
 * First argument is mandatory, second is optional.
 * First argument is a path to some file.
 * Second argument is a charset used to interpret chars from bytes.
 * If second argument was not given default charset is used.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (26.4.2016.)
 */
public class CatShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = CommandsUtility.splitArgument(arguments);
		
		if(args.length != 1 && args.length != 2) {
			CommandsUtility.writeln(env, "Invalid number of arguments for command cat.");
			return ShellStatus.CONTINUE;
		}
		
		BufferedReader br = null;
		try {
			 br = new BufferedReader(
					new InputStreamReader(
							new BufferedInputStream(
									new FileInputStream(args[0])),
							args.length == 1 ? Charset.defaultCharset() : Charset.forName(args[1])));
			
		} catch (FileNotFoundException e1) {
			CommandsUtility.writeln(env, "Incorrect file name.");
			return ShellStatus.CONTINUE;
		} catch (IllegalArgumentException e2) {
			CommandsUtility.writeln(env, "Incorrect charset name.");
			return ShellStatus.CONTINUE;
		}
		
		char[] buffer = new char[1024];
		int x;
		
		try {
			while((x = br.read(buffer)) > 0) {
				env.write(new String(buffer, 0, x));
			}
			
			env.writeln(""); //just to start in a new line
			
			if(br != null) {
				br.close();
			}
		} catch (IOException exc) {
			throw new RuntimeException("Unable to write in environment/read from file.");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "cat";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> lines = new LinkedList<>();
		
		lines.add("Opens given file and writes its content.");
		lines.add("First argument is mandatory, second is optional.");
		lines.add("First argument: path to some file.");
		lines.add("Second argument: charset used to interpret chars from bytes.");
		
		return Collections.unmodifiableList(lines);
	}

}
