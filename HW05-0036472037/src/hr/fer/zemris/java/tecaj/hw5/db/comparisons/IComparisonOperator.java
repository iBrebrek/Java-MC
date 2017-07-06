package hr.fer.zemris.java.tecaj.hw5.db.comparisons;

/**
 * Interface that checks if string relations are correct.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public interface IComparisonOperator {
	
	/**
	 * Checks if value1 and value2 are in specified relation.
	 * 
	 * @param value1	first string.
	 * @param value2	second string.
	 * @return true if specified relation is correct.
	 */
	boolean satisfied(String value1, String value2);
}
