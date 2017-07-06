package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;
import hr.fer.zemris.java.tecaj.hw5.db.queries.IFilter;

/**
 * Simulation of database. Stores all student records.
 * Allows fast retrieval of students by using jmbag.
 * Allows filtering.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class StudentDatabase {
	
	/** All students indexed by jmbag. */
	private SimpleHashtable<String, StudentRecord> studentIndex;
	/** List of all students. */
	private List<StudentRecord> allStudents;
	
	/**
	 * Initializes database rows.
	 * Given rows are stored as student records.
	 * 
	 * @param rows		each row is a single string.
	 * 
	 * @throws IllegalArgumentException if student is recorded multiple times,
	 * 									if row can not be converted to student record.
	 */
	public StudentDatabase(List<String> rows) {
		allStudents = new ArrayList<>();
		studentIndex = new SimpleHashtable<>();
		
		for(String row : rows) {
			StudentRecord student = recordFromRow(row);
			if(allStudents.contains(student)) {
				throw new IllegalArgumentException("There should be only one record for each student. "
												+  student.getJmbag() + " is recorded more than once.");
			}
			allStudents.add(student);
			studentIndex.put(student.getJmbag(), student);
		}
	}

	/**
	 * Given row converts to new student record.
	 * 
	 * @param row	given row.
	 * @return new student record.
	 */
	private StudentRecord recordFromRow(String row) {
		String[] words = row.split("\t");
		
		if(words.length != 4) {
			throw new IllegalArgumentException("Each row must have 4 values.");
		}
		
		String jmbag = words[0].trim();
		String lastName = words[1].trim();
		String firstName = words[2].trim();
		int finalGrade;
		try {
			finalGrade = Integer.parseInt(words[3].trim());
		} catch(Exception exc) {
			throw new IllegalArgumentException(words[3] + " is not an integer.");
		}
		
		return new StudentRecord(jmbag, lastName, firstName, finalGrade);
	}

	/**
	 * In O(1) returns student with given jmbag.
	 * 
	 * @param jmbag		given jmbag
	 * @return student with given jmbag.
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return studentIndex.get(jmbag);
	}
	
	/**
	 * Filters all student records.
	 * Returns all students that meet the filtering conditions.
	 * 
	 * @param filter	filter used on each record.
	 * @return list of filtered students.
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> filterResult = new ArrayList<>();
		
		for(StudentRecord student : allStudents) {
			if(filter.accepts(student)) {
				filterResult.add(student);
			}
		}
		
		return filterResult;
	}
}
