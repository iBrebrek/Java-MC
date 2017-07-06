package hr.fer.zemris.java.tecaj.hw5.db.commands;

/**
 * Possible results of every database command.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public enum CommandResult {
	/** Continue working after command. */
	CONTINUE,
	/** Stop working after command, no command can be called after this. */
	FINISH;
}
