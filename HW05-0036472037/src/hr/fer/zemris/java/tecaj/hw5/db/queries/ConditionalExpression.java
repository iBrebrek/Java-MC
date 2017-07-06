package hr.fer.zemris.java.tecaj.hw5.db.queries;

import hr.fer.zemris.java.tecaj.hw5.db.comparisons.IComparisonOperator;
import hr.fer.zemris.java.tecaj.hw5.db.getters.IFieldValueGetter;

/**
 * Class used for a single expression in database queries.
 * Class stores name of a column in database, string, 
 * and comparator which will compare that string and field.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class ConditionalExpression {
	
	/** Getter for a single student record field. */
	private final IFieldValueGetter fieldGetter;
	/** String given in query. */
	private final String stringLiteral;
	/** Comparator for student record field and string literal. */
	private final IComparisonOperator comparisonOperator;
	
	/**
	 * Initializes field used for comparison, comparator, and string being compared to field.
	 * 
	 * @param fieldGetter			student record field getter.
	 * @param stringLiteral			string being compared.
	 * @param comparisonOperator	comparator.
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
									IComparisonOperator comparisonOperator) {
		
		if(fieldGetter == null || stringLiteral == null || comparisonOperator == null) {
			throw new IllegalArgumentException("Conditional expression does not support nulls.");
		}
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Getter for student record field getter.
	 * 
	 * @return student record field getter.
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Getter for string being compared.
	 * 
	 * @return literal string.
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Getter for comparator of student's field and literal string.
	 * 
	 * @return comparator.
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
}
