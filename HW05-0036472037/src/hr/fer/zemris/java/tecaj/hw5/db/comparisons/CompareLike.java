package hr.fer.zemris.java.tecaj.hw5.db.comparisons;

/**
 * Offers only 1 method. 
 * Checks if first string is like a second string.
 * 
 * Wildcard * is used to see if one string is like another string.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class CompareLike implements IComparisonOperator {
	
	/**
	 * True if value1 is like a value2.
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		if(value2.indexOf("*") != value2.lastIndexOf("*")) {
			throw new IllegalArgumentException("Only one wildcard * is allowed.");
		}
		return value1.matches(value2.replace("*", ".*"));
	}
}
