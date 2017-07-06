package hr.fer.zemris.java.tecaj.hw5.db.comparisons;

import java.text.Collator;
import java.util.Locale;

/**
 * Offers only 1 method. 
 * Checks if first string is bigger than second string.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class CompareBigger implements IComparisonOperator{

	/**
	 * True if value1 is bigger than value2.
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		return Collator.getInstance(new Locale("hr", "HR")).compare(value1, value2) > 0;
	}
}
