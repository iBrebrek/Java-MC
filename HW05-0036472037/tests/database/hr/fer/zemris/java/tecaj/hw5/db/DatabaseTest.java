package hr.fer.zemris.java.tecaj.hw5.db;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw5.db.queries.QueryFilter;

@SuppressWarnings("javadoc")
public class DatabaseTest {
	
	/*
	 * SECOND TEST!
	 * 
	 * Before starting this test first make sure that CreationOfDatabaseTest is passed.
	 */
	
	StudentDatabase db;
	
	//this is done before every @test
	@Before //in CreationOfDatabaseTest we made sure that this works so now we can do it before every test case
	public void createDatabase() {
		List<String> rows = new ArrayList<>();
		rows.add("1\tSurename\tName\t3");
		rows.add("2\tSmith\tJohn\t5");
		rows.add("3\tDoe\tJane\t1");
		rows.add("4\tWilson\tRuss\t2");
		rows.add("5\tHarlan\tOra\t3");
		rows.add("6\tPryor\tRose\t4");
		rows.add("7\tDunf\tJane\t5");
		rows.add("8\tMaster\tJane\t4");
		rows.add("9\tSmith\tJanice\t3");
		rows.add("10\tFoster\tJane\t2");
		
		db = new StudentDatabase(rows);
	}
	
	@Test
	public void testForJMBAG() {
		//10th last name should be Foster
		assertEquals("Foster", db.forJMBAG("10").getLastName());
		
		//Should be null because that key does not exists in map
		assertNull(db.forJMBAG("150"));
	}
	
	@Test
	public void testFilter() {

		String expectedIds = "78"; 
		String result = "";
		
		for(StudentRecord student : db.filter(s-> s.getFirstName().equals("Jane") && s.getFinalGrade() > 2)) {
			result += student.getJmbag();
		}
		
		assertEquals(expectedIds, result);
	}
	
	@Test
	public void testIndex() {
		String expectedLastName = "Master";
		assertEquals(expectedLastName, db.forJMBAG("8").getLastName());
		
		assertNull(db.forJMBAG("Non-existing"));
	}
	
	@Test
	public void testQuery1() {
		String query = " firstName = \"Jane\" AnD lastName LIKE \"*er\" ";
		
		String expectedIds = "810";
		String result = "";
		
		for(StudentRecord student : db.filter(new QueryFilter(query))) {
			result += student.getJmbag();
		}

		assertEquals(expectedIds, result);
	}
	
	@Test
	public void testQuery2() {
		String query = " jmbag>=\"5\" ";
		
		String expectedIds = "56789"; //10 isn't because those are strings, not numbers.
		String result = "";
		
		for(StudentRecord student : db.filter(new QueryFilter(query))) {
			result += student.getJmbag();
		}

		assertEquals(expectedIds, result);
	}
	
	@Test
	public void testQuery3() {
		String query = "lastName LIKE \"Mat*\" ";
		
		//Should be empty because there is no one with last name Mat...
		assertTrue(db.filter(new QueryFilter(query)).isEmpty());		
	}
	
	@Test
	public void testQuery4() {
		String query = "firstNameLIKE\"J*e\"  \t  and lastNameLIKE\"D*\"   ";
		
		String expectedIds = "37";
		String result = "";
		
		for(StudentRecord student : db.filter(new QueryFilter(query))) {
			result += student.getJmbag();
		}
		
		assertEquals(expectedIds, result);
	}
	
	@Test
	public void testQueryWildcards() {
		String query = "lastName LIKE \"W*\" and  lastName LIKE \"*son\" anD lastNameLIKE\"Wi*n\"";
		
		String expectedLastName = "Wilson";
		
		String result = "";
		for(StudentRecord student : db.filter(new QueryFilter(query))) {
			result += student.getLastName();
		}
		
		assertEquals(expectedLastName, result);
	}
	
	@Test
	public void testQueryAllComparatorsButEquals() {
		String query = "jmbag<=\"7\" and jmbag>=\"3\" and jmbag!=\"6\" "
					+ "and jmbag<\"9\" and jmbag>\"1\" and jmbag LIKE \"*\" ";
		
		String expectedIds = "3457";
		String result = "";
		
		for(StudentRecord student : db.filter(new QueryFilter(query))) {
			result += student.getJmbag();
		}
		
		assertEquals(expectedIds, result);
	}
}
