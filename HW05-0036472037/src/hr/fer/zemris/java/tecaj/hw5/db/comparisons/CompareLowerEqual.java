package hr.fer.zemris.java.tecaj.hw5.db.comparisons;

/**
 * Offers only 1 method. 
 * Checks if first string is equal or lower than second string.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class CompareLowerEqual implements IComparisonOperator {

	/**
	 * True if value1 is equal or lower than value2.
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		return !new CompareBigger().satisfied(value1, value2);
	}
}
