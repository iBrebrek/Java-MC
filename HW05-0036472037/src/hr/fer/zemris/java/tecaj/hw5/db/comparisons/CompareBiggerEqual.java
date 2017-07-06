package hr.fer.zemris.java.tecaj.hw5.db.comparisons;

/**
 * Offers only 1 method. 
 * Checks if first string is equal or bigger than second string.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class CompareBiggerEqual implements IComparisonOperator{
	
	/**
	 * True if value1 is equal or bigger than value2.
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		return !new CompareLower().satisfied(value1, value2);
	}
}