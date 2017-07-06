package hr.fer.zemris.java.search.library;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Library contains list of all document
 * and set of all important words in those documents.
 * 
 * In other to create new library use static method
 * {@link #createLibrary(Collection, Collection)}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public class Library {
	
	/**
	 * Creates new library with all document
	 * and shared vocabulary for those documents.
	 * 
	 * @param books			collection of paths where documents are located on disk.
	 * @param stopwords		words which should not be added in the vocabulary.
	 * @return newly created library.
	 */
	public static Library createLibrary(Collection<String> books, Collection<String> stopwords) {
		Library lib = new Library();
		Map<String, Vocabulary> documents = new HashMap<>();
		
		//fill dictionary for every document, and library dictionary
		for(String book : books) {
			Vocabulary dictionary = new Vocabulary();
			documents.put(book, dictionary);
			
			String[] words = new Document(book, new double[1]).read().split("\\P{L}+");
			for(String word : words) {
				word = word.toLowerCase();
				if(stopwords.contains(word)) continue;
				dictionary.add(word);
			}
			
			for(String word : dictionary.getWords()) {
				lib.vocabulary.add(word);
			}
		}
		
		lib.documents = createDocuments(documents, lib.vocabulary);
		return lib;
	}
	
	/**
	 * Creates ALL documents based on vocabulary for each document
	 * and vocabulary of all documents.
	 * 
	 * @param documents		map of all document's paths and document's vocabulary.
	 * @param all			union of all vocabularies.
	 * @return all documents for new library.
	 */
	private static Set<Document> createDocuments(Map<String, Vocabulary> documents, Vocabulary all) {
		Set<Document> docs = new HashSet<>();
		for(Map.Entry<String, Vocabulary> doc : documents.entrySet()) {
			docs.add(Document.createDocument(doc.getKey(), doc.getValue(), all));
		}
		return docs;
	}

	/**
	 * So everyone has to use {@link #createLibrary(Collection, Collection)}.
	 */
	private Library() {
	}

	/** Set of all documents in library. */
	private Set<Document> documents = new HashSet<>();
	/** Shared vocabulary for all documents in library. */
	private Vocabulary vocabulary = new Vocabulary();
	
	/**
	 * Retrieves vocabulary which
	 * is shared for all documents.
	 * 
	 * @return shared vocabulary.
	 */
	public Vocabulary getVocabulary() {
		return vocabulary;
	}

	/**
	 * Retrieves ALL documents from library.
	 * Retrieves set is unmodifiable.
	 * 
	 * @return all documents in library.
	 */
	public Set<Document> getDocuments() {
		return Collections.unmodifiableSet(documents);
	}
}
