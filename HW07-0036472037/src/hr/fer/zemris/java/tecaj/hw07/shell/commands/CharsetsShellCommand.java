package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Command charsets takes no arguments and lists names of supported charsets for your Java platform.
 * A single charset name is written per line.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (25.4.2016.)
 */
public class CharsetsShellCommand implements ShellCommand{

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		Charset.availableCharsets()
				.keySet()
				.forEach(cs -> CommandsUtility.writeln(env, cs));
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "charsets";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> line = new LinkedList<>();
		
		line.add("Command takes no arguments and lists names of supported charsets for your Java platform.");
		
		return Collections.unmodifiableList(line);
	}
}
