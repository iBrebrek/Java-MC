package hr.fer.zemris.java.tecaj.hw5.db.queries;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * Used store multiple conditional expression given in query.
 * Offers a method that will filter student record using all given conditional expressions.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class QueryFilter implements IFilter {
	
	/** All {@code ConditionalExpression}s in query. */
	private List<ConditionalExpression> expressions;

	/**
	 * Reads all {@code ConditionalExpression}s from given query line.
	 * 
	 * @param query		line with one or more queries.
	 */
	public QueryFilter(String query) {
		ExpressionMaker expressionMaker = new ExpressionMaker(query);
		expressions = new ArrayList<>();
		
		while(expressionMaker.generateNext()) {
			expressions.add(expressionMaker.getExpression());
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for(ConditionalExpression expression : expressions) {
			if(!expression.getComparisonOperator().satisfied(
												expression.getFieldGetter().get(record), 
												expression.getStringLiteral())) {
				return false;
			}
		}
		return true;
	}
}
