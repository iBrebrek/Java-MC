package hr.fer.zemris.java.tecaj.hw5.db.queries;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * Interface used to filter student records.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public interface IFilter {

	/**
	 * Checks if student record meet the filtering condition.
	 * 
	 * @param record	student record being checked.
	 * @return true if student meets the condition, false otherwise.
	 */
	boolean accepts(StudentRecord record);
}
