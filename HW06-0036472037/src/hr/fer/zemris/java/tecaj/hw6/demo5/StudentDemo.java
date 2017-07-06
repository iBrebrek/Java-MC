package hr.fer.zemris.java.tecaj.hw6.demo5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Fifth problem in sixth homework.
 * This program will read students from "/.studenti.txt" and demonstrate usage of stream API.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (17.4.2016.)
 */
public class StudentDemo {

	/**
	 * Entry point for this program.
	 * 
	 * @param args	not used.
	 */
	public static void main(String[] args) {
		
		List<String> list = null;
		
		try {
			list = Files.readAllLines(
				Paths.get("./studenti.txt"),
				StandardCharsets.UTF_8
			);
		} catch (IOException fileError) {
			System.err.println("Unable to load studenti.txt");
			System.exit(-1);
		}
		
		List<StudentRecord> records = convert(list);
		
		
		// 1. Number of students with MI+ZI+LAB > 25 points.
		long number = records
			.stream()
			.filter(s -> s.getPointsMI() + s.getPointsZI() + s.getPointsLab() > 25)
			.count();
		
		// 2. Number of students that have an A (grade 5).
		long number5 = records
			.stream()
			.filter(s -> s.getGrade() == 5)
			.count();
		
		// 3. List of students that have an A (grade 5).
		List<StudentRecord> studentsA = records
			.stream()
			.filter(s -> s.getGrade() == 5)
			.collect(Collectors.toList());
		
		// 4. List of students that have an A (grade 5). But sorted by points, most points first, lowest points last.
		Comparator<StudentRecord> pointsComparator = (s1, s2) -> {
			Double pointsFirst = s1.getPointsMI() + s1.getPointsZI() + s1.getPointsLab();
			Double pointsSecond = s2.getPointsMI() + s2.getPointsZI() + s2.getPointsLab();
			double comparison = pointsFirst - pointsSecond;
			if(comparison > 0) return -1;
			if(comparison < 0) return 1;
			return 0;
		};
		
		List<StudentRecord> studentsASorted = records
			.stream()
			.filter(s -> s.getGrade() == 5)
			.sorted(pointsComparator)
			.collect(Collectors.toList());
		
		// 5. List of student jmbag that did not pass, sorted by jmbag (from lower to bigger)
		List<String> jmbagFailed = records
			.stream()
			.filter(s -> s.getGrade() == 1)
			.map(StudentRecord::getJmbag)
			.sorted()
			.collect(Collectors.toList());
		
		// 6. Map- keys are grades, values are list of students with that grade
		Map<Integer, List<StudentRecord>> mapByGrades = records
			.stream()
			.collect(Collectors.groupingBy(StudentRecord::getGrade));
		
		// 7. Map- keys are grades, values are number of students with that grade
		Map<Integer, Integer> mapByGrades2 = records
			.stream()
			.collect(Collectors.toMap(
					StudentRecord::getGrade, 
					s -> 1, 
					(a, b) -> a + b));
		
		// 8. Map- keys are true/false, values are list of students that passed(key true) and list of students that failed(key false)
		Map<Boolean, List<StudentRecord>> passedFailed = records
			.stream()
			.collect(Collectors.partitioningBy(s -> s.getGrade() > 1));
		
		
		System.out.println("*****************************************************************************************************");
		System.out.println("1.");
		System.out.println("Number of students with more than 25 points:");
		System.out.println(number);
		System.out.println("*****************************************************************************************************");
		System.out.println("2.");
		System.out.println("Number of students with a grade 5:");
		System.out.println(number5);
		System.out.println("*****************************************************************************************************");
		System.out.println("3.");
		System.out.println("List of students with a grade 5:");
		System.out.println(studentsA);
		System.out.println("List has " + studentsA.size() + " students.");
		System.out.println("*****************************************************************************************************");
		System.out.println("4.");
		System.out.println("Sorted list of students with a grade 5:");
		System.out.println(studentsASorted);
		System.out.println("List has " + studentsASorted.size() + " students.");
		System.out.println("*****************************************************************************************************");
		System.out.println("5.");
		System.out.println("List of student's jmbag that failed:");
		System.out.println(jmbagFailed);
		System.out.println("List has " + jmbagFailed.size() + " jmbags.");
		System.out.println("*****************************************************************************************************");
		System.out.println("6.");
		System.out.println("Students mapped by grade, values are list of students:");
		System.out.println(mapByGrades);
		System.out.println("Grade 5 have " + mapByGrades.get(5).size() + " students.");
		System.out.println("Grade 4 have " + mapByGrades.get(4).size() + " students.");
		System.out.println("Grade 3 have " + mapByGrades.get(3).size() + " students.");
		System.out.println("Grade 2 have " + mapByGrades.get(2).size() + " students.");
		System.out.println("Grade 1 have " + mapByGrades.get(1).size() + " students.");
		System.out.println("*****************************************************************************************************");
		System.out.println("7.");
		System.out.println("Students mapped by grade, values are number of students:");
		System.out.println(mapByGrades2);
		System.out.println("*****************************************************************************************************");
		System.out.println("8.");
		System.out.println("Students mapped by fail/pass:");
		System.out.println(passedFailed);
		System.out.println("passed:" + passedFailed.get(true).size());
		System.out.println("failed:" + passedFailed.get(false).size());
		System.out.println("*****************************************************************************************************");
		
		
	}

	/**
	 * Converts given list of {@code String}s to list of {@code StudentRecord}s.
	 * If there is an element which can not be converted it will be just skipped.
	 * 
	 * @param list		list of {@code String}s.
	 * @return list of {@code StudentRecord}s.
	 */
	private static List<StudentRecord> convert(List<String> list) {
		final List<StudentRecord> students = new ArrayList<>();
		
		list.forEach(line -> {
			if(line != null && !line.trim().isEmpty()) {
				String[] elements = line.split("\t");
				if(elements.length != 7) {
					System.err.println("There was a line with incorrect number of elements. That line is skipped.");
					return;
				}
				
				String jmbag = elements[0].trim();
				String surname = elements[1].trim();
				String name = elements[2].trim();
				Double pointsMI = toDouble(elements[3].trim());
				Double pointsZI = toDouble(elements[4].trim());
				Double pointsLab = toDouble(elements[5].trim());
				Integer grade = toInteger(elements[6].trim());
				
				if(pointsMI == null || pointsZI == null || pointsLab == null || grade == null) {
					System.err.println("There was a line with incorrect information. That line is skipped.");
					return;
				}
				if(grade < 1 || grade > 5) {
					System.err.println("There was a line grade: " + grade +". That line is skipped.");
					return;
				}
				
				students.add(new StudentRecord(jmbag, name, surname, pointsMI, pointsZI, pointsLab, grade));
			}
		});
		
		return students;
	}

	/**
	 * Converts {@code number} to {@code Double}.
	 * 
	 * @param number	{@code String} being converted to {@code Double}.
	 * @return {@code Double} or {@code null} if given {@code String} is not a number.
	 */
	private static Double toDouble(String number) {
		try {
			return Double.parseDouble(number);
		} catch(Exception exc) {
			return null;
		}
	}
	
	/**
	 * Converts {@code number} to {@code Integer}.
	 * 
	 * @param number	{@code String} being converted to {@code Integer}.
	 * @return {@code Integer} or {@code null} if given {@code String} is not a number.
	 */
	private static Integer toInteger(String number) {
		try {
			return Integer.parseInt(number);
		} catch(Exception exc) {
			return null;
		}
	}
}

	
