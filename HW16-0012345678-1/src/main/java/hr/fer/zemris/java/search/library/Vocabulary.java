package hr.fer.zemris.java.search.library;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Vocabulary contains all important words used to search documents.
 * Objects of this class offer 3 important thing:
 * -collection of all words in vocabulary
 * -how many times a single word appeared
 * -index of a word
 * 		-that index represents given word
 * 		-that index is used in document's vector
 * 		-example: vector[5] represents word at index 5.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public class Vocabulary {
	
	/** Map of all words and their stats. */
	private Map<String, WordStats> words = new HashMap<>();
	
	/**
	 * Retrieves unmodifiable
	 * set of all words.
	 * 
	 * @return set of all words.
	 */
	public Set<String> getWords() {
		return Collections.unmodifiableSet(words.keySet());
	}
	
	/**
	 * Retrieves how many words are in this vocabulary.
	 * 
	 * @return total number of words.
	 */
	public int size() {
		return words.size();
	}
	
	/**
	 * Checks if given word is in vocabulary.
	 * 
	 * @param word		word being checked.
	 * @return <code>true</code> if word is in vocabulary.
	 */
	public boolean contains(String word) {
		return words.containsKey(word);
	}
	
	/**
	 * Adds word to vocabulary.
	 * 
	 * @param word 		word being added to vocabulary.
	 */
	public void add(String word) {
		WordStats stats = words.get(word);
		if(stats == null) {
			stats = new WordStats(size(), 1);
		} else {
			stats.counter++;
		}
		words.put(word, stats);
	}
	
	/**
	 * Retrieves index of given word.
	 * 
	 * @param word 		word at retrieved index.
	 * @return index of given word.
	 */
	public int indexOf(String word) {
		WordStats stats = words.get(word);
		if(stats == null) {
			return -1;
		} else {
			return stats.index;
		}
	}
	
	/**
	 * Retrieves how many times did
	 * word appear in vocabulary.
	 * 
	 * @param word		word being checked.
	 * @return how many given word appeared.
	 */
	public int appearancesOf(String word) {
		WordStats stats = words.get(word);
		if(stats == null) {
			return 0;
		} else {
			return stats.counter;
		}
	}
	
	/**
	 * This class contains stats of 
	 * a single word in vocabulary.
	 * 
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (29.6.2016.)
	 */
	private static class WordStats {
		/** Index of a word. */
		private int index;
		/** How many times word appeared. */
		private int counter;
		
		/**
		 * Initializes new stats with
		 * given index and counter.
		 * 
		 * @param index		index of word.
		 * @param counter	counter of given word.
		 */
		private WordStats(int index, int counter) {
			this.index = index;
			this.counter = counter;
		}
	}
}
