package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Command used to see or change prompt, more-lines and multi-lines symbol.
 * Prompt symbol indicates that user input is expected. 
 * More-lines symbol is used at the end of line if command is not whole. 
 * Multi-line symbol is used at the beginning of a line before which more-lines was used.
 * 
 * @author Ivica Brebrek
 * @version 1.0 (25.4.2016.)
 */
public class SymbolShellCommand implements ShellCommand {

	/** Key word for prompt symbol. */
	private static final String PROMPT = "PROMPT";
	/** Key word for more-line symbol. */
	private static final String MORELINE = "MORELINES";
	/** Key word for multi-line symbol. */
	private static final String MULTILINE = "MULTILINE";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = arguments.trim().split("\\s+");

		if (args == null || args.length > 2) {
			CommandsUtility.writeln(env, "Invalid arguments for command symbol.");
			return ShellStatus.CONTINUE;
		}
		if (args.length == 2 && args[1].length() != 1) {
			CommandsUtility.writeln(env, "Symbol is supposed to be a single character.");
			return ShellStatus.CONTINUE;
		}

		Character symbol = null;

		switch (args[0]) {
			case PROMPT:
				symbol = env.getPromptSymbol();
				if (args.length == 2) {
					env.setPromptSymbol(args[1].charAt(0));
				}
				break;

			case MORELINE:
				symbol = env.getMorelinesSymbol();
				if (args.length == 2) {
					env.setMorelinesSymbol(args[1].charAt(0));
				}
				break;

			case MULTILINE:
				symbol = env.getMultilineSymbol();
				if (args.length == 2) {
					env.setMultilineSymbol(args[1].charAt(0));
				}
				break;

			default:
				CommandsUtility.writeln(env, args[0] + " is invalid symbol name.");
				return ShellStatus.CONTINUE;
		}
		
		String message = null;
		
		if(args.length == 1) {
			message = String.format("Symbol for %s is '%s'", args[0], symbol);
		} else {
			message = String.format("Symbol for %s changed from '%s' to '%s'", args[0], symbol, args[1]);
		}
		
		CommandsUtility.writeln(env, message);

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "symbol";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> lines = new LinkedList<>();
		
		lines.add("Command used to see or change prompt, more-lines and multi-lines symbol.");
		lines.add("First argument must be PROMPT, MULTILINE or MORELINE.");
		lines.add("Second argument will change symbol for first argument.");
		lines.add("If there is not second argument symbol for first argument will be written,");
		lines.add("PROMPT indicates that user input is expected.");
		lines.add("MORELINE is used at the end of line if command is not whole.");
		lines.add("MULTILINE is used at the beginning of a line before which MORELINE was used.");
		
		return Collections.unmodifiableList(lines);
	}
}
