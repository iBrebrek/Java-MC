package hr.fer.zemris.java.search.commands;

import java.util.Iterator;

import hr.fer.zemris.java.search.library.Document;

/**
 * Command type print document 
 * which is in n-th place of all results.
 * 
 * Example: "type 3"
 * would print document at 
 * index 3 of all result 
 * (if such exists).
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public class TypeCommand extends ResultCommand {

	/**
	 * Initializes command.
	 * Name is "type".
	 */
	public TypeCommand() {
		super("type");
	}

	@Override
	public boolean execute(String argument) {
		int index;
		try {
			index = Integer.parseInt(argument);
		} catch(NumberFormatException exc) {
			throw new IllegalArgumentException("Invalid argument for command type.");
		}
		
		if(results.size() <= index) {
			System.out.println("No result at index "+index+".");
		} else {
			Iterator<SearchResult> it = results.iterator();
			for(int i = 0; i < index; it.next());
			Document document = it.next().getDocument();
			System.out.println("------------------------");
			System.out.println("Document: "+document.getPath());
			System.out.println("------------------------");
			System.out.println(document.read());
			System.out.println("------------------------");
		}
		
		return false;
	}
}
