package hr.fer.zemris.java.tecaj.hw5.db;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class CreationOfDatabaseTest {
	
	/*
	 * FIRST TEST!
	 * 
	 * This test should be passed before starting DatabaseTest.
	 */
	
	@Test
	public void testCreatingDatabase() {
		List<String> rows = new ArrayList<>();
		rows.add("123\tSurename\tName\t3");
		rows.add("456\tSecondSurename\tSecondName\t5");
		
		StudentDatabase database = new StudentDatabase(rows);
		
		int insertedRows = database.filter(s->true).size();
		
		assertEquals(rows.size(), insertedRows);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTwoSame() {
		List<String> rows = new ArrayList<>();
		rows.add("123\tSurename\tName\t3");
		rows.add("123\tA\tB\t2");
		//jmbag is same, should throw exception
		new StudentDatabase(rows);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTooLowFinalGrade() {
		List<String> rows = new ArrayList<>();
		rows.add("123\tSurename\tName\t0");
		
		new StudentDatabase(rows);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTooHighFinalGrade() {
		List<String> rows = new ArrayList<>();
		rows.add("123\tSurename\tName\t6");
		
		new StudentDatabase(rows);
	}
}
