package hr.fer.zemris.java.search.library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class is representation of single
 * document and its TF-IDF value.
 * Class offers comparison with other document
 * to see how similar they are.
 * 
 * Class also offers a single static method
 * {@link #createDocument(String, Vocabulary, Vocabulary)}
 * which creates document with given path
 * and also calculates TF-IDF vector.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (29.6.2016.)
 */
public class Document {
	
	/**
	 * Creates new document.
	 * Document is located at given path.
	 * Given vocabularies are used to calculate TF-IDF vector.
	 * 
	 * @param path				where document is on disk.
	 * @param ofDocument		vocabulary of this document.
	 * @param ofAll				vocabulary of ALL document.
	 * @return newly created document.
	 */
	public static Document createDocument(String path, Vocabulary ofDocument, Vocabulary ofAll) {
		double numberOfDocs = ofAll.size();
		double[] vector = new double[ofAll.size()];
		for(String word : ofDocument.getWords()) {
			int index = ofAll.indexOf(word);
			if(index == -1) continue;
			vector[index] = ofDocument.appearancesOf(word)
				* Math.log(numberOfDocs / ofAll.appearancesOf(word));
		}
		return new Document(path, vector);
	}
	
	/** From where to read document. */
	private String path;
	/** TF-IDF vector. */
	private double[] vector;
	
	/**
	 * Intializes document with given path and TF-IDF vector.
	 * 
	 * @param path		path where document is on disk.
	 * @param vector	TF-IDF vector.
	 */
	public Document(String path, double[] vector) {
		if(path == null || vector == null) {
			throw new IllegalArgumentException("Document must have path and vector");
		}
		this.path = path;
		this.vector = vector;
	}

	/**
	 * Retrieves document's path.
	 * 
	 * @return document's path.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Retrieves text of document.
	 * 
	 * @return document's text.
	 */
	public String read() {
		try {
			StringBuilder text = new StringBuilder();
			for(String line : Files.readAllLines(Paths.get(path))) {
				text.append(line);
				text.append("\n");
			}
			return text.toString();
		} catch (IOException e) {
			throw new RuntimeException("Unable to open document.");
		}
	}
	
	/**
	 * Compares this and given document.
	 * In other words, it calculates how
	 * similar they are.
	 * If returned number is 0 they are 
	 * not similar at all.
	 * If returned value is 1 they are same.
	 * 
	 * @param other		other document.
	 * @return number between 0 and 1. 
	 */
	public double compare(Document other) {
		double scalar = 0.0;
		for(int i = 0; i < vector.length; i++) {
			scalar += vector[i] * other.vector[i];
		}
		
		double norm1 = calculateNorm(this);
		double norm2 = calculateNorm(other);
		
		return scalar / (norm1 * norm2);
	}
	
	/**
	 * Calculates norm of vector for given document.
	 * 
	 * @param document		document with vector.
	 * @return vector's norm.
	 */
	private double calculateNorm(Document document) {
		double sum = 0.0;
		for(double d : document.vector) {
			sum += d*d;
		}
		return Math.sqrt(sum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}
