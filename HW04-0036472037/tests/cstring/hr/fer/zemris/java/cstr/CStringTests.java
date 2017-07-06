package hr.fer.zemris.java.cstr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/*
 * In some test cases we use substring because it is hard to test offset.
 */
@SuppressWarnings("javadoc")
public class CStringTests {
	
	@Test
	public void testImmutable() {
		char[] array = new char[] {'t', 'e', 's', 't' };
		CString string = new CString(array);
		
		array[0] = '0';
		assertEquals("String should stay the same.", "test", string.toString());
		
		array = new char[] {'a'};
		assertEquals("String should stay the same.", "test", string.toString());
		
		CString string2 = new CString(string);
		string = CString.fromString("123");
		assertEquals("String should stay the same.", "test", string2.toString());
		
		string2.toCharArray()[0] = 'a';
		assertEquals("String should stay the same.", "test", string2.toString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullDataInConstructor() {
		new CString(null, 0, 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullStringInConstructor() {
		CString str = null;
		new CString(str);
	}
	
	@Test
	public void testConstructors() {
		char[] array = "Test123223".toCharArray();
		CString str1 = new CString(array);
		CString str2 = new CString(array, 1, 8);
		CString str3 = new CString(str1);
		CString str4 = new CString(str2);
		//str1 and str3 are the same, str2 and str4 are the same
		
		assertEquals("Must be the same.", str1.toString(), str3.toString());
		assertEquals("Must be the same.", str2.toString(), str4.toString());
		
		assertEquals("Must be the same.", str1.length(), str3.length());
		assertEquals("Must be the same.", str2.length(), str4.length());
		
		assertEquals("Must be the same.", str1.indexOf('e'), str3.indexOf('e'));
		assertEquals("Must be the same.", str1.indexOf('i'), str3.indexOf('i'));
		assertEquals("Must be the same.", str2.indexOf('e'), str4.indexOf('e'));
		assertEquals("Must be the same.", str2.indexOf('i'), str4.indexOf('i'));
		
		assertEquals("Must be the same.", str2.charAt(2), str4.charAt(2));
		assertEquals("Must be the same.", str2.charAt(2), str4.charAt(2));
		
		assertEquals("Must be the same.", str1.replaceAll('2', '1').toString(), 
										str3.replaceAll('2', '1').toString());
		assertEquals("Must be the same.", str2.replaceAll('2', '1').toString(), 
										str4.replaceAll('2', '1').toString());
		
		assertEquals("Must be the same.", str1.left(3).toString(), 
										str3.left(3).toString());
		assertEquals("Must be the same.", str2.left(3).toString(), 
										str4.left(3).toString());
		
		assertEquals("Must be the same.", new String(str1.toCharArray()), 
										new String(str3.toCharArray()));
		assertEquals("Must be the same.", new String(str2.toCharArray()), 
										new String(str4.toCharArray()));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testTooBigLength1() {
		char[] data = "123456789".toCharArray();
		new CString(data, 0, 10); //10 > data.length		
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testTooBigLength2() {
		char[] data = "123456789".toCharArray();
		new CString(data, 2, 8); //10 > data.length		
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testNegativeOffset() {
		new CString(new char[] {'a'}, -1, 1);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testNegativeLength() {
		new CString(new char[] {'a'}, 0, -1);
	}
	
	@Test
	public void testFromAndToString() {
		String string = "1a@{89.-l  \n 6asgn";
		CString cString = CString.fromString(string);
		String string2 = cString.toString();
		//for this we must be sure that equals() in String works 100% of the time
		assertEquals("Should be true.", true, string.equals(string2));
		assertEquals("Should be the same.", "", CString.fromString("").toString());
	}
	
	@Test
	public void testLength() {
		String string = "0123456789";
		char[] array = string.toCharArray(); //length is 10
		CString str1 = new CString(array);
		CString str2 = new CString(array, 2, 3);
		CString str3 = new CString(str1);
		CString str4 = CString.fromString(string);
		
		assertEquals("Should be 10.", array.length, str1.length());
		assertEquals("Should be 3.", 3, str2.length());
		assertEquals("Should be 10.", array.length, str3.length());
		assertEquals("Should be 10.", array.length, str4.length());
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCharAtNegativeIndex() {
		new CString(new char[] {'a', 'b'}).charAt(-1);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCharAtTooBigIndex() {
		new CString(new char[] {'a', 'b'}).charAt(2);
	}
	
	@Test
	public void testCharAt() {
		CString str = new CString(new char[] {'a', 'b'});
		
		assertEquals("Should be 'a'.", 'a', str.charAt(0));
		assertEquals("Should be 'b'.", 'b', str.charAt(1));
	}
	
	@Test
	public void testToCharArray() {
		String string1 = "1a@{89.-l  \n 6asgn";
		CString str = CString.fromString(string1);
		String string2 = new String(str.toCharArray());
		//for this we must be sure that equals() and constructor in String work 100% of the time
		assertEquals("Should be the same.", true, string1.equals(string2));
	}
	
	@Test
	public void testIndexOf() {
		CString str = CString.fromString("01234567890X");
		
		assertEquals("Should be -1.", -1, str.indexOf('a'));
		assertEquals("Should be 0.", 0, str.indexOf('0')); //because it must find first one
		assertEquals("Should be 11.", 11, str.indexOf('X'));
		assertEquals("Should be -1.", -1, str.indexOf('x'));
		assertEquals("Should be 6.", 6, str.indexOf('6'));
		
		CString str2 = new CString(str.toCharArray(), 1, str.length()-1);
		assertEquals("Should be 9.", 9, str2.indexOf('0')); //now we shouldn't see starting 0
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testStartsWithNull() {
		new CString("123".toCharArray()).startsWith(null);
	}
	
	@Test
	public void testStartsWith() {
		CString str1 = new CString("StartEnd".toCharArray());
		CString str2 = new CString("Start".toCharArray());
		CString str3 = new CString("End".toCharArray());
		
		assertEquals("Should be true.", true, str1.startsWith(str2));
		assertEquals("Should be false.", false, str1.startsWith(str3));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEndsWithNull() {
		new CString("123".toCharArray()).endsWith(null);
	}
	
	@Test
	public void testEndsWith() {
		CString str1 = new CString("StartEnd".toCharArray());
		CString str2 = new CString("Start".toCharArray());
		CString str3 = new CString("End".toCharArray());
		
		assertEquals("Should be true.", true, str1.endsWith(str3));
		assertEquals("Should be false.", false, str1.endsWith(str2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testContainsNull() {
		new CString("123".toCharArray()).contains(null);
	}
	
	@Test
	public void testContainsTrue() {
		CString str = new CString("123 456".toCharArray());
		
		CString str2 = new CString("123 456".toCharArray());
		CString str3 = new CString(" ".toCharArray());
		CString str4 = new CString("1".toCharArray());
		CString str5 = new CString("12".toCharArray());
		CString str6 = new CString("6".toCharArray());
		CString str7 = new CString("456".toCharArray());
		CString str8 = new CString("23 45".toCharArray());
		CString str9 = new CString("23 456".toCharArray());
		
		assertEquals("Should be true.", true, str.contains(str2));
		assertEquals("Should be true.", true, str.contains(str3));
		assertEquals("Should be true.", true, str.contains(str4));
		assertEquals("Should be true.", true, str.contains(str5));
		assertEquals("Should be true.", true, str.contains(str6));
		assertEquals("Should be true.", true, str.contains(str7));
		assertEquals("Should be true.", true, str.contains(str8));
		assertEquals("Should be true.", true, str.contains(str9));
	}
	
	@Test
	public void testContainsFalse() {
		CString str = new CString("123 456".toCharArray());
		
		CString str2 = new CString("23 456 ".toCharArray());
		CString str3 = new CString("1234".toCharArray());
		CString str4 = new CString("abc".toCharArray());
		CString str5 = new CString("c".toCharArray());
		CString str6 = new CString("  ".toCharArray()); //2 spaces

		assertEquals("Should be false.", false, str.contains(str2));
		assertEquals("Should be false.", false, str.contains(str3));
		assertEquals("Should be false.", false, str.contains(str4));
		assertEquals("Should be false.", false, str.contains(str5));
		assertEquals("Should be false.", false, str.contains(str6));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubstringNegativeStart() {
		new CString("123".toCharArray()).substring(-1, 2);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubstringTooBigStart() {
		new CString("123".toCharArray()).substring(3, 4);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubstringTooBigEnd() {
		new CString("123".toCharArray()).substring(1, 4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSubstringStartBiggerThanEnd() {
		new CString("123".toCharArray()).substring(2, 1);
	}
	
	@Test
	public void testSubstring() {
		CString str = new CString("Test for substring.".toCharArray());
		//note that starting index is inclusive and ending exclusive
		assertEquals("Should be the same.", "Test", str.substring(0, 4).toString());
		assertEquals("Should be the same.", " for ", str.substring(4, 9).toString());
		assertEquals("Should be the same.", ".", str.substring(str.length()-1, str.length()).toString());
		assertEquals("Should be the same.", "", str.substring(5, 5).toString());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testLeftNegative() {
		new CString("123".toCharArray()).left(-1);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testLeftTooBig() {
		new CString("123".toCharArray()).left(4);
	}
	
	@Test
	public void testLeft() {
		CString str = new CString("123456".toCharArray()).substring(1, 4); //"234"
		
		assertEquals("Should be the same.", "", str.left(0).toString());
		assertEquals("Should be the same.", "2", str.left(1).toString());
		assertEquals("Should be the same.", "23", str.left(2).toString());
		assertEquals("Should be the same.", "234", str.left(3).toString());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRightNegative() {
		new CString("123".toCharArray()).right(-1);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testRightTooBig() {
		new CString("123".toCharArray()).right(4);
	}
	
	@Test
	public void testRight() {
		CString str = new CString("123456".toCharArray()).substring(1, 4); //"234"
		
		assertEquals("Should be the same.", "", str.right(0).toString());
		assertEquals("Should be the same.", "4", str.right(1).toString());
		assertEquals("Should be the same.", "34", str.right(2).toString());
		assertEquals("Should be the same.", "234", str.right(3).toString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddNull() {
		new CString("123".toCharArray()).add(null);
	}

	@Test
	public void testAdd() {
		CString str1 = new CString("{{123}}".toCharArray()).substring(2, 5); //"123"
		CString str2 = CString.fromString("abcd");
		CString str3 = CString.fromString("");
		
		assertEquals("Should be the same.", "123abcd", str1.add(str2).toString());
		assertEquals("Should be the same.", "abcd123", str2.add(str1).toString());
		assertEquals("Should be the same.", "123", str1.add(str3).toString());
		assertEquals("Should be the same.", "", str3.add(str3).add(str3).toString());
		assertEquals("Should be the same.", "123123",str1.add(str1).toString());
		assertEquals("Should be the same.", "abcd123abcd", str2.add(str1).add(str2).toString());
	}
	
	@Test
	public void testReplaceAllChar() {
		CString str = new CString("ababacb".toCharArray()).substring(1, 6); //"babac"
		
		assertEquals("Should be the same.", "1a1ac", str.replaceAll('b', '1').toString());
		assertEquals("Should be the same.", "b1b1c", str.replaceAll('a', '1').toString());
		assertEquals("Should be the same.", "baba1", str.replaceAll('c', '1').toString());
		assertEquals("Should be the same.", "babac", str.replaceAll('d', '1').toString());
		assertEquals("Should be the same.", "babac", str.replaceAll('a', 'a').toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReplaceAllNull1() {
		CString str1 = new CString("abc".toCharArray());
		CString str2 = new CString(str1);
		
		str1.replaceAll(str2, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReplaceAllNull2() {
		CString str1 = new CString("abc".toCharArray());
		CString str2 = new CString(str1);
		
		str1.replaceAll(null, str2);
	}
	
	@Test
	public void testReplaceAll1() {
		CString str1 = new CString("ababab".toCharArray());
		CString str2 = new CString("ab".toCharArray());
		CString str3 = new CString("abab".toCharArray());
		CString str4 = new CString("".toCharArray());
		
		assertEquals("Should be the same.", "abababababab", str1.replaceAll(str2, str3).toString());
		assertEquals("Should be the same.", "abababababab", str3.replaceAll(str2, str1).toString());
		assertEquals("Should be the same.", "", str1.replaceAll(str2, str4).toString());
	}
	
	@Test
	public void testReplaceAll2() {
		CString str1 = new CString("{{aaabbbccc}}".toCharArray()).substring(1, 12);//"{..}"
		CString str2 = new CString("ab".toCharArray());
		CString str3 = new CString("bbb".toCharArray());
		CString str4 = new CString("1".toCharArray());
		CString str5 = new CString("".toCharArray());
		CString str6 = new CString("{".toCharArray());
		CString str7 = new CString("}".toCharArray());
		
		assertEquals("Should be the same.", "{aaa1ccc}", str1.replaceAll(str3, str2)
														.replaceAll(str2, str4).toString());
		assertEquals("Should be the same.", "1aaabbbccc1", str1.replaceAll(str6, str4)
															.replaceAll(str7, str4).toString());
		assertEquals("Should be the same.", "1b1b1b1", str3.replaceAll(str5, str4).toString());
	}
}
