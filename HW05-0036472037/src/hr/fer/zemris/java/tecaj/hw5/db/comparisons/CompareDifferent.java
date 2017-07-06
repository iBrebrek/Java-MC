package hr.fer.zemris.java.tecaj.hw5.db.comparisons;

/**
 * Offers only 1 method. 
 * Checks if first string and second string are different.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class CompareDifferent implements IComparisonOperator{

	/**
	 * True if value1 and value2 are different.
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		return !value1.equals(value2);
	}	
}
