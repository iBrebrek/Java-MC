package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Class that stores data for a single student.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class StudentRecord {

	/** Student's id. */
	private String jmbag;
	/** Student's last name. */
	private String lastName;
	/** Student's first name. */
	private String firstName;
	/** Student's final grade. */
	private int finalGrade;
	
	/**
	 * Initializes student's data.
	 * 
	 * @param jmbag			student's id.
	 * @param lastName		student's last name.
	 * @param firstName		student's first name.
	 * @param finalGrade	student's final grade.
	 * 
	 * @throws IllegalArgumentException if any string is null or if final grade is not real grade.
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		if(jmbag == null || lastName == null || firstName == null) {
			throw new IllegalArgumentException("Student record can not have nulls.");
		}
		if(finalGrade < 1 || finalGrade > 5) {
			throw new IllegalArgumentException(finalGrade + " is not valid grade.");
		}
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}
	
	/**
	 * Getter for student's jmbag/id.
	 * 
	 * @return jmbag/id.
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter for student's last name.
	 * 
	 * @return last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter for student's first name.
	 * 
	 * @return first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter for student's final grade.
	 * 
	 * @return final grade.
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	/**
	 * Auto-generated hash code.
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	/**
	 * Auto-generated equals.
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}
}
