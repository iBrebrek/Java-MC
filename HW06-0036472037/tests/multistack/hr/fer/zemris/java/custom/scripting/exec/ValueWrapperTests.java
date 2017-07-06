package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ValueWrapperTests {
	
	@Test
	public void testStoreAnything() {
		//point of this is to see if exception will be thrown, it shouldn't be thrown
		ValueWrapper wrapper = new ValueWrapper(new Boolean(true));
		boolean isTrue = (Boolean) wrapper.getValue();
		
		assertTrue(isTrue);
	}

	@Test
	public void testSetValue() {
		ValueWrapper wrapper = new ValueWrapper("Test");
		wrapper.setValue("Changed");
		
		String expected = "Changed";
		String actual = (String)wrapper.getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCurrentNullToInteger0() {
		ValueWrapper wrapper = new ValueWrapper(null);
		Integer zero = Integer.valueOf(0);
		
		int expected = 0; // 0 means that they are the same
		int actual = wrapper.numCompare(zero);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testArgumentNullToInteger0() {
		ValueWrapper wrapper = new ValueWrapper(Integer.valueOf(0));
		Object object = null;
		
		int expected = 0; // 0 means that they are the same
		int actual = wrapper.numCompare(object);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testIncrementInteger() {
		ValueWrapper wrapper = new ValueWrapper(3);
		Integer five = 5;
		wrapper.increment(five);
		
		Integer expected = 8;
		Integer actual = (Integer)wrapper.getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStringToInteger() {
		ValueWrapper wrapper = new ValueWrapper("3");
		String five = "5";
		wrapper.increment(five);
		
		Integer expected = 8;
		Integer actual = (Integer)wrapper.getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testIncrementDouble() {
		ValueWrapper wrapper = new ValueWrapper(3.0);
		Double five = 5.0;
		wrapper.increment(five);
		
		Double expected = 8.0;
		Double actual = (Double)wrapper.getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testStringToDouble() {
		ValueWrapper wrapper = new ValueWrapper("3.0");
		String five = "5.0";
		wrapper.increment(five);
		
		Double expected = 8.0;
		Double actual = (Double)wrapper.getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = RuntimeException.class)
	public void testInvalidCurrent() {
		ValueWrapper wrapper = new ValueWrapper("A");
		String five = "5.0";
		//should throw
		wrapper.increment(five);
	}
	
	@Test(expected = RuntimeException.class)
	public void testInvalidArgument() {
		ValueWrapper wrapper = new ValueWrapper("3");
		String string = "A";
		//should throw
		wrapper.increment(string);
	}
	
	@Test(expected = RuntimeException.class)
	public void testInvalidCurrent2() {
		ValueWrapper wrapper = new ValueWrapper(new Long(3));
		String five = "5.0";
		//should throw
		wrapper.increment(five);
	}
	
	@Test(expected = RuntimeException.class)
	public void testInvalidArgument2() {
		ValueWrapper wrapper = new ValueWrapper("3");
		Long five = 5L;
		//should throw
		wrapper.increment(five);
	}
	
	@Test
	public void testDecimalWithEToDouble() {
		ValueWrapper wrapper = new ValueWrapper("1E-2");
		wrapper.increment("0"); //to force him to convert from string to double
		
		Double expected = 0.01;
		Double actual = (Double)wrapper.getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDecrement() {
		ValueWrapper wrapper = new ValueWrapper("6.3");
		wrapper.decrement("1.3");
		
		Double expected = 5.0;
		Double actual = (Double)wrapper.getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMultiply() {
		ValueWrapper wrapper = new ValueWrapper("5");
		wrapper.multiply("3");
		
		Integer expected = 15;
		Integer actual = (Integer)wrapper.getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDivide() {
		ValueWrapper wrapper = new ValueWrapper("15.5");
		wrapper.divide("3.1");
		
		Double expected = 5.0;
		Double actual = (Double)wrapper.getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCompareIntegerAndDoubleBigger() {
		ValueWrapper wrapper = new ValueWrapper("1.01");
		Integer one = 1;
		
		//test if wrapped value is bigger than one
		boolean shouldBeTrue = wrapper.numCompare(one) > 0;
		
		assertTrue(shouldBeTrue);
	}
	
	@Test
	public void testCompareIntegerAndDoubleEqual() {
		ValueWrapper wrapper = new ValueWrapper("1.0");
		Integer one = 1;
		
		//test if wrapped value is equal to one
		boolean shouldBeTrue = wrapper.numCompare(one) == 0;
		
		assertTrue(shouldBeTrue);
	}
	
	@Test
	public void testCompareIntegerAndDoubleSmaller() {
		ValueWrapper wrapper = new ValueWrapper(".99");
		Integer one = 1;
		
		//test if wrapped value is smaller than one
		boolean shouldBeTrue = wrapper.numCompare(one) < 0;
		
		assertTrue(shouldBeTrue);
	}
	
	@Test
	public void testCompareOnlyIntegers() {
		ValueWrapper wrapper = new ValueWrapper(5);
		Integer six = 6;
		
		//test if wrapped value is smaller than six
		boolean shouldBeTrue = wrapper.numCompare(six) < 0;
				
		assertTrue(shouldBeTrue);
	}
	
	@Test
	public void testCompareOnlyDoubles() {
		ValueWrapper wrapper = new ValueWrapper(1.3);
		Double onePointTwo = 1.2;
		
		//test if wrapped value is bigger than 1.2
		boolean shouldBeTrue = wrapper.numCompare(onePointTwo) > 0;
				
		assertTrue(shouldBeTrue);
	}
	
	@Test
	public void testNullsShouldBeTheSame() {
		ValueWrapper wrapper = new ValueWrapper(null);
		Object nullReference = null;
		
		//test if nulls are the same
		boolean shouldBeTrue = wrapper.numCompare(nullReference) == 0;
		
		assertTrue(shouldBeTrue);
	}
}
