package hr.fer.zemris.java.tecaj.hw5.db.getters;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * Offers only 1 method. 
 * Returns last name of student record.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class LastNameGetter implements IFieldValueGetter {
	
	/**
	 * Returns last name of a given student.
	 */
	@Override
	public String get(StudentRecord record) {
		return record.getLastName();
	}
}
