package hr.fer.zemris.java.search.commands;


import hr.fer.zemris.java.search.library.Document;

/**
 * A single result used in {@link ResultCommand}.
 * This class implement {@link Comparable} which
 * means that results can be compared/ranked.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public class SearchResult implements Comparable<SearchResult> {

	/** Document which is result. */
	private final Document document;
	/** Similarity with document and query. */
	private final double similarity;
	
	/**
	 * Initializes query result.
	 * Max similarity is 1.
	 * Min similarity is 0.
	 * 
	 * @param document		document wrapped in this result.
	 * @param similarity	how similar are document and query.
	 */
	public SearchResult(Document document, double similarity) {
		this.document = document;
		if(similarity < 0.0) {
			similarity = 0;
		} else if(similarity > 1.0) {
			similarity = 1;
		}
		this.similarity = similarity;
	}

	/**
	 * Retrieves document wrapped in this result.
	 * 
	 * @return document.
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Retrieves similarity of document and query.
	 * 
	 * @return similarity of document and query.
	 */
	public double getSimilarity() {
		return similarity;
	}

	@Override
	public int compareTo(SearchResult o) {
		if(similarity > o.similarity) {
			return -1;
		} else {
			return 1;
		}
	}
}
