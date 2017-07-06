package hr.fer.zemris.java.search.commands;

/**
 * Abstract class defining all commands.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public abstract class Command {

	/** Command name. */
	private String name;
	
	/**
	 * Initializes command name.
	 * 
	 * @param name 		command name.
	 */
	public Command(String name) {
		if(name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid command name.");
		}
		this.name = name;
	}
	
	/**
	 * Retrieves command name.
	 * 
	 * @return command name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Executes command.
	 * Returns <code>true</code> if program 
	 * should end after command is executed.
	 * 
	 * @param argument		command argument(s).
	 * @return <code>false</code> if program 
	 * 			should continue after the execution.
	 */
	public abstract boolean execute(String argument);
}
