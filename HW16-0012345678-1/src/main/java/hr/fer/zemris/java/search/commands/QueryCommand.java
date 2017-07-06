package hr.fer.zemris.java.search.commands;

import hr.fer.zemris.java.search.library.Document;
import hr.fer.zemris.java.search.library.Library;
import hr.fer.zemris.java.search.library.Vocabulary;

/**
 * Query command is used to find document most similar to given query.
 * 
 * Example: "query java, html, develop"
 * would print to screen top 10 documents
 * with given words.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public class QueryCommand extends ResultCommand {
	
	/** Library where all documents/words are. */
	private static Library library;

	/**
	 * Initializes command.
	 * Name is "query".
	 */
	public QueryCommand() {
		super("query");
	}
	
	/**
	 * Sets library from which documents should be searched.
	 * 
	 * @param library		library with all documents.
	 */
	public static void setLibrary(Library library) {
		QueryCommand.library = library;
	}

	@Override
	public boolean execute(String argument) {		
		if(argument == null) {
			throw new IllegalArgumentException("Invalid aruments for command query.");
		}
		Vocabulary vocabulary = new Vocabulary();
		for(String word : argument.split("\\P{L}+")) {
			word = word.toLowerCase();
			if(library.getVocabulary().contains(word)) {
				vocabulary.add(word);
			}
		}
		System.out.println("Query is: " + vocabulary.getWords());
		Document temp = Document.createDocument(
				"no path needed", 
				vocabulary, 
				library.getVocabulary()
		);
		
		results.clear();
		library.getDocuments().forEach(doc -> 
			results.add(new SearchResult(doc, doc.compare(temp)))
		);
		
		return super.execute(null);
	}
}
