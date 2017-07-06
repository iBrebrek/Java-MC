package hr.fer.zemris.java.search;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import hr.fer.zemris.java.search.commands.Command;
import hr.fer.zemris.java.search.commands.ExitCommand;
import hr.fer.zemris.java.search.commands.QueryCommand;
import hr.fer.zemris.java.search.commands.ResultCommand;
import hr.fer.zemris.java.search.commands.TypeCommand;
import hr.fer.zemris.java.search.library.Library;

/**
 * Command line search engine.
 * Using command query program will find top 10 document
 * containing given words.
 * Using command result program will
 * print last shown results.
 * Using command type program will
 * write document on the screen.
 * Command exit exits the program.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public class SearchEngine {
	
	/** File where list of stopwords is. */
	private static final String PATH_STOPWORDS = "hrvatske_stoprijeci.txt";
	/** Directory containing documents. */
	private static final String PATH_BOOKS = "books";
	
	/** Map of all commands supported by this program. */
	private static Map<String, Command> commands = new HashMap<>();
	static {
		ExitCommand exit = new ExitCommand();
		commands.put(exit.getName(), exit);
		QueryCommand query = new QueryCommand();
		commands.put(query.getName(), query);
		ResultCommand result = new ResultCommand();
		commands.put(result.getName(), result);
		TypeCommand type = new TypeCommand();
		commands.put(type.getName(), type);
	}
	
	/**
	 * Entry point.
	 * 
	 * @param args		not used.
	 */
	public static void main(String[] args) {
		Library library = null;
		try {
			library = Library.createLibrary(getBooks(), getStopwords());
			QueryCommand.setLibrary(library);
		} catch (IOException e) {
			System.err.println("Unable to create library.");
		}
		
		System.out.println("Number of words in vocabulary: "+library.getVocabulary().size());
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			String input = sc.nextLine().trim();
			int index = input.indexOf(" ");
			Command command = null;
			String argument = null;
			if(index == -1) {
				command = commands.get(input);
			} else {
				argument = input.substring(index+1);
				command = commands.get(input.substring(0, index));
			}
			if(command == null) {
				System.err.println("Unknown command.");
				continue;
			} 
			if(command.execute(argument)) {
				break;
			}
		}
		sc.close();
	}
	
	/**
	 * Retrieves collection of all stopwords.
	 * 
	 * @return collection of all stopwords.
	 * @throws IOException if unable to open file with list of stopwords.
	 */
	private static Collection<String> getStopwords() throws IOException {
		Set<String> stopwords = new HashSet<>();
		//remove dots from shortcuts, such as npr.
		for(String s : Files.readAllLines(Paths.get(PATH_STOPWORDS), StandardCharsets.UTF_8)) {
			if(s.endsWith(".")) {
				stopwords.add(s.substring(0, s.length()-1));
			} else {
				stopwords.add(s);
			}
		}
		return stopwords;
	}

	/**
	 * Retrieves collection of paths to all collections.
	 * 
	 * @return collection of paths to all documents.
	 * @throws IOException if unable to find directory where documents should be.
	 */
	private static Collection<String> getBooks() throws IOException {
		File directory = new File(PATH_BOOKS);
		if(!directory.isDirectory()) {
			throw new IOException();
		}
		Set<String> books = new HashSet<>();
		for(File file : directory.listFiles()) {
			books.add(file.getAbsolutePath());
		}
		return books;
	}
}
