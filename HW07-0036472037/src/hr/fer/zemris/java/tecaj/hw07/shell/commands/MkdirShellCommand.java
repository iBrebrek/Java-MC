package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Takes a single argument: directory name,
 * and creates the appropriate directory structure.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.4.2016.)
 */
public class MkdirShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		File dir = new File(CommandsUtility.splitArgument(argument)[0]);
		
		if(dir.exists()) {
			if(dir.isDirectory()) {
				CommandsUtility.writeln(env, "Directory already exists.");
			} else {
				CommandsUtility.writeln(env, "There is a file with that name.");
			}
		} else {
			dir.mkdirs();
			CommandsUtility.writeln(env, "Directory created.");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "mkdir";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> lines = new LinkedList<>();
		
		lines.add("Creates the appropriate directory structure.");
		lines.add("Needs one argument- directory name.");
		
		return Collections.unmodifiableList(lines);
	}
}
