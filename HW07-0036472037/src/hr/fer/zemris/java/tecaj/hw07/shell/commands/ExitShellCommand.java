package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Terminates shell.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (26.4.2016.)
 */
public class ExitShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return ShellStatus.TERMINATE;
	}

	@Override
	public String getCommandName() {
		return "exit";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> line = new LinkedList<>();
		line.add("Terminates shell.");
		return line;
	}
}
