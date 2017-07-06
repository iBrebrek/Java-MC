package hr.fer.zemris.java.tecaj.hw5.db.getters;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * Interface used to return a single field value from student record.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public interface IFieldValueGetter {
	
	/**
	 * Returns one field value from given student record.
	 * 
	 * @param record	student record.
	 * @return field value.
	 */
	public String get(StudentRecord record);
}
