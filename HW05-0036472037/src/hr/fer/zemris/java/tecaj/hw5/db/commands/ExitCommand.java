package hr.fer.zemris.java.tecaj.hw5.db.commands;


/**
 * Command used to finish usage of database.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class ExitCommand extends DatabaseCommand {

	/** Command name. */
	public static final String NAME = "exit";
	
	/**
	 * Initializes exit command.
	 */
	public ExitCommand() {
		super(NAME, null);
	}

	/**
	 * Exits program.
	 */
	@Override
	public CommandResult execute(String argument) {
		System.out.println("Goodbye!");
		return CommandResult.FINISH;
	}
}
