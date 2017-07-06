package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 *  If started with no arguments, it lists names of all supported commands.
 *  If started with single argument, it prints name and the description of selected command.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (25.4.2016.)
 */
public class HelpShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = arguments.split("\\s+");
		
		if(args.length > 1) {
			CommandsUtility.writeln(env, "Invalid number of arguments for command help.");
			return ShellStatus.CONTINUE;
		}
		
		if(args[0].isEmpty()) {
			env.commands()
				.forEach(command -> 
					CommandsUtility.writeln(env, command.getCommandName()));
		} else {
			String command = args[0];
			
			for(ShellCommand c : env.commands()) {
				if(command.equals(c.getCommandName())) {
					c.getCommandDescription()
						.forEach(line -> CommandsUtility.writeln(env, line));
					
					return ShellStatus.CONTINUE;
				}
			}
			
			CommandsUtility.writeln(env, command + " is not supported command.");
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> lines = new LinkedList<>();
		
		lines.add("No arguments: lists names of all supported commands.");
		lines.add("Single argument: prints name and the description of selected command.");
		
		return Collections.unmodifiableList(lines);
	}
}
