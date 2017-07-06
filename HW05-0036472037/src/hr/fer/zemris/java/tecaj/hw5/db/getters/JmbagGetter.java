package hr.fer.zemris.java.tecaj.hw5.db.getters;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * Offers only 1 method. 
 * Returns JMBAG of student record.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class JmbagGetter implements IFieldValueGetter {

	/**
	 * Returns jmbag of a given student.
	 */
	@Override
	public String get(StudentRecord record) {
		return record.getJmbag();
	}
}
