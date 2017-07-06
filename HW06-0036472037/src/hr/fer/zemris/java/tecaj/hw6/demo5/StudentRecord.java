package hr.fer.zemris.java.tecaj.hw6.demo5;

/**
 * Information about a single student.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (17.4.2016.)
 */
public class StudentRecord {
	/** Student's JMBAG. */
	private final String jmbag;
	/** Student's first name. */
	private final String name;
	/** Student's last name. */
	private final String surname;
	/** Student's points on MI. */
	private final double pointsMI;
	/** Student's points on ZI. */
	private final double pointsZI;
	/** Student's points from laboratory. */
	private final double pointsLab;
	/** Student's final grade, A is 5, B is 4, etc... */
	private final int grade;
	
	/**
	 * Initializes all informations about this student.
	 * 
	 * @param jmbag			JMBAG.
	 * @param name			first name.
	 * @param surname		last name.
	 * @param pointsMI		result on MI.
	 * @param pointsZI		result on ZI.
	 * @param pointsLab		points from laboratory.
	 * @param grade			final grade.
	 */
	public StudentRecord(String jmbag, String name, String surname, double pointsMI, 
							double pointsZI, double pointsLab, int grade) {
		this.jmbag = jmbag;
		this.name = name;
		this.surname = surname;
		this.pointsMI = pointsMI;
		this.pointsZI = pointsZI;
		this.pointsLab = pointsLab;
		this.grade = grade;
	}

	/**
	 * Getter for student's JMBAG.
	 * 
	 * @return JMBAG.
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter for student's first name.
	 * 
	 * @return first name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for student's last name.
	 * 
	 * @return lasat name.
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Getter for student's points on MI.
	 * 
	 * @return points on MI.
	 */
	public double getPointsMI() {
		return pointsMI;
	}

	/**
	 * Getter for student's points on ZI.
	 * 
	 * @return points on ZI.
	 */
	public double getPointsZI() {
		return pointsZI;
	}

	/**
	 * Getter for student's points from laboratory.
	 * 
	 * @return points from laboratory.
	 */
	public double getPointsLab() {
		return pointsLab;
	}

	/**
	 * Getter for student's final grade.
	 * 
	 * @return final grade.
	 */
	public int getGrade() {
		return grade;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * 
	 * string will be: "jmbag [total points] (final grade)"
	 * total points is sum of points from MI, ZI and laboratory.
	 */
	@Override
	public String toString() {
		return jmbag + " [" + (pointsLab+pointsMI+pointsZI) + "]" + " (" + grade + ")";
	}
}
