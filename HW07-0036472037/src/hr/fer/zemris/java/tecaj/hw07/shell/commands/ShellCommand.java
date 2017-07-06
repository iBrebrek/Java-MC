package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Abstraction of every shell command.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (25.4.2016.)
 */
public interface ShellCommand {

	/**
	 * Executes command.
	 * The second argument is a single string which represents everything that user entered AFTER the command name.
	 * 
	 * @param env			environment in which command is executed.
	 * @param arguments		command's arguments.
	 * @return shells status after command is executed.
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns the name of the command.
	 * 
	 * @return command's name.
	 */
	String getCommandName();
	
	/**
	 * Returns a command description (usage instructions).
	 * Since the description can span more than one line, a read-only List is used.
	 * 
	 * @return command's description.
	 */
	List<String> getCommandDescription();
}
