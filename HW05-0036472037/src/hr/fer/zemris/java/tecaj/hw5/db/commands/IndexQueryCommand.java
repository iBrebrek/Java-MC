package hr.fer.zemris.java.tecaj.hw5.db.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.db.StudentDatabase;
import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;
import hr.fer.zemris.java.tecaj.hw5.db.comparisons.CompareEqual;
import hr.fer.zemris.java.tecaj.hw5.db.getters.JmbagGetter;
import hr.fer.zemris.java.tecaj.hw5.db.queries.ConditionalExpression;
import hr.fer.zemris.java.tecaj.hw5.db.queries.ExpressionMaker;
import hr.fer.zemris.java.tecaj.hw5.db.queries.InvalidQueryException;

/**
 * Command used to retrieve a single student using jmbag.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class IndexQueryCommand extends DatabaseCommand {
	
	/** Command name. */
	public static final String NAME = "indexquery";

	/**
	 * Initializes command's argument and database.
	 * 
	 * @param database		database on which command is used.
	 */
	public IndexQueryCommand(StudentDatabase database) {
		super(NAME, database);
	}

	/**
	 * From jmbag gets student in O(1).
	 */
	@Override
	public CommandResult execute(String argument) {
		ExpressionMaker maker = new ExpressionMaker(argument);
		maker.generateNext();
		ConditionalExpression expression = maker.getExpression();
		
		if(maker.generateNext()) {
			throw new InvalidQueryException("Index query can have only one conditional expression.");
		}
		if(expression == null) {
			throw new InvalidQueryException("Invalid usage of index query.");
		}
		if(!(expression.getFieldGetter() instanceof JmbagGetter)) {
			throw new InvalidQueryException("In index query attribute must be jmbag.");
		}
		if(!(expression.getComparisonOperator() instanceof CompareEqual)) {
			throw new InvalidQueryException("In index query operator must be equals.");
		}
		
		List<StudentRecord> student = new ArrayList<>(); //need to use list because of drawTable method
		student.add(database.forJMBAG(expression.getStringLiteral()));
		
		System.out.println("Using index for record retrieval.");
		drawTable(student);
		
		return CommandResult.CONTINUE;
	}
}
