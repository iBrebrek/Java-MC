package hr.fer.zemris.java.search.commands;

import java.util.Set;
import java.util.TreeSet;

/**
 * Command result prints last top 10 results,
 * or less if there is no similarities.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public class ResultCommand extends Command {
	
	/** Last shown results. */
	protected static final Set<SearchResult> results = new TreeSet<>();

	/** Max number of results to be displayed. */
	private static final int TOP_X = 10;
	/** If similarity is lower then this, it will be consider as 0. */
	private static final double MIN_SIM = 0.00001;

	/**
	 * Initializes command.
	 * Name is "results".
	 */
	public ResultCommand() {
		super("results");
	}
	
	/**
	 * Enables other commands to extend this command.
	 * 
	 * @param name		command name.
	 */
	protected ResultCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(String argument) {
		if(results.isEmpty()) {
			System.out.println("No results to display. First execute query.");
		} else {
			System.out.println("Top results:");
			int i = 0; 
			for(SearchResult result : results) {
				if(result.getSimilarity() < MIN_SIM || i == TOP_X) {
					break;
				}
				System.out.printf("[%d](%.4f)%s\n", 
					i++, 
					result.getSimilarity(), 
					result.getDocument().getPath()
				);
			}
		}
		return false;
	}
}
