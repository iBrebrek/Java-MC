package hr.fer.zemris.java.search.commands;

/**
 * Exits program.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public class ExitCommand extends Command {

	/**
	 * Initializes command.
	 * Name is "exit".
	 */
	public ExitCommand() {
		super("exit");
	}

	@Override
	public boolean execute(String argument) {
		return true;
	}
}
