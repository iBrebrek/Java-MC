package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ObjectMultistackTests {
	
	private ObjectMultistack multistack;
	
	@Before
	public void createMultistack() {
		multistack = new ObjectMultistack();
		multistack.push("A", new ValueWrapper("A1"));
		multistack.push("A", new ValueWrapper("A2"));
		multistack.push("B", new ValueWrapper("B1"));
	}
	
	@Test
	public void testPush() {
		multistack.push("A", new ValueWrapper("A3"));
		String expected = "A3";
		String actual = multistack.peek("A").getValue().toString();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPeekExisting() {
		String expected = "A2";
		String actual1 = multistack.peek("A").getValue().toString();
		String actual2 = multistack.peek("A").getValue().toString();
		
		assertEquals(expected, actual1);
		assertEquals(expected, actual2);
	}
	
	@Test(expected = EmptyStackException.class)
	public void testPeekNonExisting() {
		//should throw
		multistack.peek("Does not exist");
	}
	
	@Test
	public void testPopExisting() {
		String expected1 = "A2";
		String expected2 = "A1";
		String actual1 = multistack.pop("A").getValue().toString();
		String actual2 = multistack.pop("A").getValue().toString();
		
		assertEquals(expected1, actual1);
		assertEquals(expected2, actual2);
	}
	
	@Test(expected = EmptyStackException.class)
	public void testPopNonExisting() {
		//should throw
		multistack.pop("Does not exist");
	}
	
	@Test(expected = EmptyStackException.class)
	public void testPop2() {
		String expected = "B1";
		String actual = multistack.pop("B").getValue().toString();
		
		assertEquals(expected, actual);
		
		//should throw
		multistack.pop("B");
	}
	
	@Test
	public void testIsEmpty() {
		boolean shouldBeTrue = multistack.isEmpty("Does not exist");
		boolean shouldBeFalse = multistack.isEmpty("B");
		
		assertTrue(shouldBeTrue);
		assertFalse(shouldBeFalse);
	}
	
	@Test
	public void testIsEmpty2() {
		multistack.pop("B");
		
		boolean shouldBeTrue = multistack.isEmpty("B");
		
		assertTrue(shouldBeTrue);
	}
}
