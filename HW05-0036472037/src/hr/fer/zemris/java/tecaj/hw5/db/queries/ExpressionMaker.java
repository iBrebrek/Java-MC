package hr.fer.zemris.java.tecaj.hw5.db.queries;

import hr.fer.zemris.java.tecaj.hw5.db.comparisons.CompareBigger;
import hr.fer.zemris.java.tecaj.hw5.db.comparisons.CompareBiggerEqual;
import hr.fer.zemris.java.tecaj.hw5.db.comparisons.CompareDifferent;
import hr.fer.zemris.java.tecaj.hw5.db.comparisons.CompareEqual;
import hr.fer.zemris.java.tecaj.hw5.db.comparisons.CompareLike;
import hr.fer.zemris.java.tecaj.hw5.db.comparisons.CompareLower;
import hr.fer.zemris.java.tecaj.hw5.db.comparisons.CompareLowerEqual;
import hr.fer.zemris.java.tecaj.hw5.db.comparisons.IComparisonOperator;
import hr.fer.zemris.java.tecaj.hw5.db.getters.FirstNameGetter;
import hr.fer.zemris.java.tecaj.hw5.db.getters.IFieldValueGetter;
import hr.fer.zemris.java.tecaj.hw5.db.getters.JmbagGetter;
import hr.fer.zemris.java.tecaj.hw5.db.getters.LastNameGetter;

/**
 * From line generates {@code ConditionalExpression}s.
 * This class is combination of lexer and parser.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class ExpressionMaker {
	
	//valid field names
	/** Database field name: first name. */
	private static final String FIRST_NAME = "firstName";
	/** Database field name: last name. */
	private static final String LAST_NAME = "lastName";
	/** Database field name: jmbag. */
	private static final String JMBAG = "jmbag";
	
	//valid comparison operators
	/** Comparison operator: < */
	private static final String LOWER = "<";
	/** Comparison operator: <= */
	private static final String LOWER_OR_EQUAL = "<=";
	/** Comparison operator: > */
	private static final String BIGGER = ">";
	/** Comparison operator: >= */
	private static final String BIGGER_OR_EQUAL = ">=";
	/** Comparison operator: = */
	private static final String EQUAL = "=";
	/** Comparison operator: != */
	private static final String DIFFERENT = "!=";
	/** Comparison operator: LIKE */
	private static final String LIKE = "LIKE";
	
	//valid logical operator
	/** Query separator: AND */
	private static final String AND = "AND";
	
	/** Whole query line. */
	private String queryLine;
	/** Current index in {@code queryLine}. */
	private int currentIndex = 0;
	/** {@code true} only when whole query is read. */
	private boolean isOver = false;
	
	/** Last generated {@code ConditionalExpression}. */
	private ConditionalExpression lastExpression;

	/**
	 * Initializes expression maker.
	 * 
	 * @param queryLine		query line with 1 or more conditional expressions.
	 */
	public ExpressionMaker(String queryLine) {
		this.queryLine = queryLine.trim();
	}
	
	/**
	 * Returns last generated {@code ConditionalExpression}.
	 * If no expression were generated {@code null} is returned.
	 * 
	 * @return {@code ConditionalExpression} or {@code null}.
	 */
	public ConditionalExpression getExpression() {
		return lastExpression;
	}
	
	/**
	 * Generates next {@code ConditionalExpression} from query line given in constructor.
	 * If generation was successful {@code true} is returned.
	 * 
	 * @return  {@code true} if generation successful,  {@code false} otherwise.
	 * 
	 * @throws InvalidQueryException	if given query line in constructor was invalid in any way. 
	 */
	public boolean generateNext() {
		if(isOver) {
			return false;
		}
		
		skipSpace();
		IFieldValueGetter fieldGetter = getFieldGetter();
		skipSpace();
		IComparisonOperator comparator = getComparator();
		skipSpace();
		String stringLiteral = getStringLiteral();
		skipSpace();
		readAndOrCheckEnd();
		
		lastExpression = new ConditionalExpression(fieldGetter, stringLiteral, comparator);
		return true;
	}
	
	/**
	 * If whole {@code queryLine} is read sets {@code isOver} to {@code false}.
	 * If next word is "and" skips it.
	 * If none of above throws exception.
	 */
	private void readAndOrCheckEnd() {
		if(currentIndex + AND.length() < queryLine.length()) {
			String shouldBeAnd = queryLine.substring(currentIndex, currentIndex + AND.length());
			if(AND.equalsIgnoreCase(shouldBeAnd)) {
				currentIndex += AND.length();
			}
		} else {
			if(currentIndex == queryLine.length()) {
				isOver = true;
			} else {
				throw new InvalidQueryException("Invalid query.");
			}
		}
	}

	/**
	 * Reads next string. String must be written in quotation marks.
	 * If there is no string throws exception.
	 * 
	 * @return next string.
	 */
	private String getStringLiteral() {
		if(queryLine.charAt(currentIndex) != '"') {
			/*
			 *  Let's say you write jmbag=>".." instead of jmbag>="..",
			 *  then = would be read as operator equal and > would be considered start of string.
			 *  That's why this if-block is here.
			 */
			throw new InvalidQueryException("Invalid query.");
		}
		
		int firstQuotationMark = queryLine.indexOf('"', currentIndex);
		int secondQuotationMark = queryLine.indexOf('"', currentIndex+1);
		
		if(firstQuotationMark == -1 || secondQuotationMark == -1) {
			throw new InvalidQueryException("String must be written in quotation marks.");
		}
		
		String stringLiteral = queryLine.substring(firstQuotationMark+1, secondQuotationMark);
		currentIndex += stringLiteral.length() + 2; // +2 because of first and last quotation mark
		
		return stringLiteral;
	}

	/**
	 * Tries to read next comparison operator.
	 * If next word is not valid comparison operator throws exception.
	 * 
	 * @return comparison operator that represents next word.
	 */
	private IComparisonOperator getComparator() {
		if(isNext(BIGGER_OR_EQUAL)) {
			return new CompareBiggerEqual();
		}
		if(isNext(BIGGER)) {
			return new CompareBigger();
		} 
		if(isNext(LOWER_OR_EQUAL)) {
			return new CompareLowerEqual();
		}
		if(isNext(LOWER)) {
			return new CompareLower();
		}
		if(isNext(EQUAL)) {
			return new CompareEqual();
		}
		if(isNext(DIFFERENT)) {
			return new CompareDifferent();
		}
		if(isNext(LIKE)) {
			return new CompareLike();
		}
		throw new InvalidQueryException("Invalid comparison operator.");
	}

	/**
	 * Tries to read next field name.
	 * If next word is not valid field name throws exception.
	 * 
	 * @return value getter that represents next word.
	 */
	private IFieldValueGetter getFieldGetter() {
		if(isNext(FIRST_NAME)) {
			return new FirstNameGetter();
		}
		if(isNext(LAST_NAME)) {
			return new LastNameGetter();
		}
		if(isNext(JMBAG)) {
			return new JmbagGetter();
		}
		throw new InvalidQueryException("Invalid field name.");
	}
	
	/**
	 * If given word is next returns true and skips that word.
	 * 
	 * @param word		checks if this word is next in {@code queryLine}.
	 * @return true if given word is next.
	 */
	private boolean isNext(String word) {
		if(queryLine.startsWith(word, currentIndex)) {
			currentIndex += word.length();
			return true;
		}
		return false;
	}
	
	/**
	 * Skips all spaces in {@codequeryLine} starting from {@code currentIndex} till next non space.
	 */
	private void skipSpace() {
		while(notOver() && Character.isWhitespace(queryLine.charAt(currentIndex))) {
			currentIndex++;
		}
	}
	
	/**
	 * Checks if end of line is reached.
	 * And if so, sets {@code isOver} to {@code true}.
	 * 
	 * @return true if end of line is NOT reached.
	 */
	private boolean notOver() {
		if(currentIndex == queryLine.length()) {
			isOver = true;
		}
		return !isOver;
	}
}
