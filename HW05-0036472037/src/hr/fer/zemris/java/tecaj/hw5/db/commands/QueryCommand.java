package hr.fer.zemris.java.tecaj.hw5.db.commands;

import hr.fer.zemris.java.tecaj.hw5.db.StudentDatabase;
import hr.fer.zemris.java.tecaj.hw5.db.queries.QueryFilter;

/**
 * Command used for single query command.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class QueryCommand extends DatabaseCommand {
	
	/** Command name. */
	public static final String NAME = "query";

	/**
	 * Initializes command's argument and database.
	 * 
	 * @param database		database on which command is used.
	 */
	public QueryCommand(StudentDatabase database) {
		super(NAME, database);
	}

	/**
	 * Filters table with given query.
	 */
	@Override
	public CommandResult execute(String argument) {
		drawTable(database.filter(new QueryFilter(argument)));
		
		return CommandResult.CONTINUE;
	}
}
