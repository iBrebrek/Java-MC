package hr.fer.zemris.java.cstr;

/**
 * Once created, string can not be changed.
 * <p>
 * The general idea of the implementation is for multiple instances of strings to share 
 * a single character array and to remember which part of the array belongs to each instance.
 * </p>
 * Because of that, substring and similar methods are executed in O(1) complexity.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (2.4.2016.)
 */
public class CString {
	
	/** All characters in string. */
	private final char[] data;
	
	/** Index where string starts. */
	private int offset;
	
	/** This is how long string is (how many indexes after offset). */
	private int length;
	
	/**
	 * NOTE THAT THIS IS A PRIVATE CONSTRUCTOR.
	 * It is used only for implementation.
	 * <p>
	 * Offset and length can not be negative 
	 * and offset+length can not be higher than data length.
	 * All other combinations are valid.
	 * </p>
	 * If shareData is true then new CString will just take reference of data,
	 * if that flag is false then new CString will copy all chars from data.
	 * 
	 * NOTE: true for shareData is used in a lot of methods that are creating new CString.
	 * 
	 * @param data		array where characters are.
	 * @param offset	in data, where string start(indexes count from 0).
	 * @param length	how long is string(starting from offset).
	 * @param shareData	to have same reference as given data or to copy data.
	 */
	private CString(char[] data, int offset, int length, boolean shareData) {
		if(data == null) {
			throw new IllegalArgumentException("Data can not be null.");
		}
		if(offset < 0) {
			throw new IndexOutOfBoundsException("Offset can not be negative.");
		}
		if(length < 0) {
			throw new IndexOutOfBoundsException("Length can not be negative.");
		}
		if(length+offset > data.length) {
			throw new IndexOutOfBoundsException
							("Given offset and length would exceed data size");
		}
		
		this.length = length;
		
		if(shareData) {
			this.offset = offset;
			this.data = data;
		} else {
			this.offset = 0;
			this.data = new char[length];
			copyFromTo(data, this.data, offset, 0, length);
		}
	}

	/**
	 * Initializes new CString.
	 * <p>
	 * Offset and length can not be negative 
	 * and offset+length can not be higher than data length.
	 * All other combinations are valid.
	 * </p>
	 * @param data		new string will have this internal character array.
	 * @param offset	in data, where string start(count that index, indexes count from 0).
	 * @param length	how long is string(starting from offset).
	 */
	public CString(char[] data, int offset, int length) {
		this(data, offset, length, false);
	}
	
	/**
	 * Initializes new CString with all characters from data.
	 * Length of data and new string will be the same.
	 * 
	 * @param data	all string's characters.
	 */
	public CString(char[] data) {
		this(data, 0, data.length);
	}
	
	/**
	 * If original's internal character array is larger than needed, new instance 
	 * will allocate its own character array of minimal required size and copy data.
	 * Otherwise, it will reuse original's character array.
	 * 
	 * @param original		new string will represent same characters as original. 
	 */
	public CString(CString original) {
		if(original == null) {
			throw new IllegalArgumentException("Original string can not be null");
		}

		length = original.length;
		//offset is zero
		
		if(original.length != original.data.length) {
			data = original.toCharArray();
		} else {
			data = original.data;
		}
	}
	
	/**
	 * From given String creates new CString.
	 * New CString will have same value as given string.
	 * 
	 * @param s		string being "transformed" into CString.
	 * @return new CString with same values as parameter s.
	 */
	public static CString fromString(String s) {
		if(s == null) {
			throw new IllegalArgumentException("Null string is not valid argument.");
		}
		
		return new CString(s.toCharArray(), 0, s.length(), true);
		//could use CString(s.toCharArray()) too, but that would re-make same char array
	}
	
	/**
	 * Size of this string.
	 * How many chars this string represents.
	 * 
	 * @return size of this string.
	 */
	public int length() {
		return length;
	}
	
	/**
	 * Returns character which this string stores at given index.
	 * Invalid index(negative or too big) will result with IndexOutOfBoundsException.
	 * 
	 * @param index		index where character is.
	 * @return character at given index.
	 */
	public char charAt(int index) {
		if(index < 0) {
			throw new IndexOutOfBoundsException("Index can not be negative.");
		}
		if(index >= length) {
			throw new IndexOutOfBoundsException("Index can not be bigger than length");
		}
		
		return data[offset + index];
	}
	
	/**
	 * New array of all character that this string represents.
	 * 
	 * @return new char array with all characters from this string.
	 */
	public char[] toCharArray() {
		char[] array = new char[length];
		copyFromTo(data, array, offset, 0, length);
		return array;
	}
	
	/**
	 * From this CString creates new String.
	 * New String will have same value as this CString.
	 * 
	 * @return new String with same values as this CString.
	 */
	public String toString() {
		return new String(data, offset, length);
	}
	
	/**
	 * Returns index of first occurrence of char or -1 if not found.
	 * 
	 * @param c		char being looked for.
	 * @return first index of given char.
	 */
	public int indexOf(char c) {
		for(int index = offset, end = length + offset; index < end; index++) {
			if(data[index] == c) {
				return index - offset;
			}
		}
		return -1;
	}
	
	/**
	 *  Returns true if this string begins with given string,
	 *  false otherwise.
	 * 
	 * @param s		prefix.
	 * @return true if this string starts with the given prefix.
	 */
	public boolean startsWith(CString s) {
		//didn't put this if in hasStringAt because of s.length in endsWith
		if(s == null) {
			throw new IllegalArgumentException("Null string is not valid argument.");
		}
		return hasStringAt(s, 0);
	}
	
	/**
	 *  Returns true if this string ends with given string,
	 *  false otherwise.
	 * 
	 * @param s		suffix.
	 * @return true if this string ends with the given suffix.
	 */
	public boolean endsWith(CString s) {
		//didn't put this if in hasStringAt because of s.length
		if(s == null) {
			throw new IllegalArgumentException("Null string is not valid argument.");
		}
		return hasStringAt(s, length - s.length);
	}
	
	/**
	 * Checks from startingIndex till startingIndex+cString.length 
	 * if chars are equal in this.data and cString.data.
	 * 
	 * Returns true if this string has given string at given index,
	 * false otherwise.
	 * 
	 * @param s					string.
	 * @param startingIndex		index in data.
	 * @return true if this string has given string at given index.
	 */
	private boolean hasStringAt(CString s, int startingIndex) {
		if(s.length > this.length) {
			return false;
		}
		
		for(int index = 0; index < s.length; index++) {
			if(s.charAt(index) != this.charAt(startingIndex + index)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns true if this string contains given string at any position, 
	 * false otherwise
	 * 
	 * @param s		string to search for.
	 * @return true if this string contains given string, false otherwise
	 */
	public boolean contains(CString s) {
		if(s == null) {
			throw new IllegalArgumentException("Null string is not valid argument.");
		}
		if(s.length > this.length) {
			return false;
		}
		
		return indexOfSequence(s, offset, offset + length) != -1;
	}
	
	/**
	 * Returns new CString which represents a part of original string. 
	 * Position endIndex does not belong to the substring. 
	 * 
	 * Valid arguments are:
	 * startIndex>=0, endIndex>=startIndex, end<=this.length. 
	 * Invalid argument will result with IndexOutOfBoundsException 
	 * or IllegalArgumentException(if start bigger than end).
	 * 
	 * Its complexity is O(1).
	 * 
	 * @param startIndex	the starting index, inclusive.
	 * @param endIndex		the ending index, exclusive.
	 * @return the specified substring.
	 */
	public CString substring(int startIndex, int endIndex) {
		if(startIndex < 0) {
			throw new IndexOutOfBoundsException("Starting index can not be negative.");
		}
		if(endIndex > length) {
			throw new IndexOutOfBoundsException
						("Ending index can not be bigger than actual string length.");
		}
		if(startIndex > endIndex) {
			throw new IllegalArgumentException
						("Starting index can not be bigger than ending index.");
		}
		
		
		return new CString(data, offset + startIndex, endIndex - startIndex, true);
	}
	
	/**
	 * Returns new CString which represents starting part of original string 
	 * and is of length n.
	 *  
	 * Throws IndexOutOfBoundsException if n is negative or too big.
	 * 
	 * @param n		length of new string.
	 * @return new string with described properties.
	 */
	public CString left(int n) {
		if(n < 0) {
			throw new IndexOutOfBoundsException("Length can not be negative.");
		}
		if(n > length) {
			throw new IndexOutOfBoundsException
						("Length of new string can not be bigger than original string.");
		}
		
		return substring(0, n);
	}
	
	/**
	 * Returns new CString which represents ending part of original string 
	 * and is of length n.
	 *  
	 * Throws IndexOutOfBoundsException if n is negative or too big.
	 * 
	 * @param n		length of new string.
	 * @return new string with described properties.
	 */
	public CString right(int n) {
		if(n < 0) {
			throw new IndexOutOfBoundsException("Length can not be negative.");
		}
		if(n > length) {
			throw new IndexOutOfBoundsException
						("Length of new string can not be bigger than original string.");
		}
		
		return substring(length - n, length);
	}
	
	/**
	 * Creates a new CString which is 
	 * concatenation of current and given string.
	 * 
	 * @param s		given string.
	 * @return concatenation of this and given string.
	 */
	public CString add(CString s) {
		if(s == null) {
			throw new IllegalArgumentException("Given string can not be null");
		}
		
		char[] result = new char[s.length + this.length];
		
		copyFromTo(data, result, this.offset, 0, this.length);
		copyFromTo(s.data, result, s.offset, this.length, s.length);
		
		return new CString(result, 0, s.length + this.length, true);
	}
	
	/**
	 * Creates a new CString in which each occurrence 
	 * of old character is replaces with new character.
	 * 
	 * @param oldChar	old character.
	 * @param newChar	new character.
	 * @return new string after replacement.
	 */
	public CString replaceAll(char oldChar, char newChar) {
		return replaceAll(new CString(new char[] {oldChar}), 
						  new CString(new char[] {newChar}));
	}
	
	/**
	 * Creates a new CString in which each occurrence 
	 * of old substring is replaces with the new substring.
	 * 
	 * @param oldStr	old substring.
	 * @param newStr	new substring.
	 * @return new string after replacement.
	 */
	public CString replaceAll(CString oldStr, CString newStr) {
		if(oldStr == null || newStr == null) {
			throw new IllegalArgumentException("Old and/or new string can not be null.");
		}
		
		/*
		 * My algoritham is this:
		 * -find all indexes where oldStr in data is and store them in arrayOfIndexes
		 * -then go one by one char in data and if index is in arrayOfIndexes add newStr
		 * to result and skip oldStr in data
		 */
		
		int[] arrayOfIndexes = new int[0];
		int step = offset;
		int end = offset + length;
		int index = indexOfSequence(oldStr, step, end);
		
		while(index != -1) {
			arrayOfIndexes = addToIntArray(arrayOfIndexes, index);
			step = index + 1;
			index = indexOfSequence(oldStr, step, end);
		}
		
		char[] resultData = new char[length + 
		                             (newStr.length - oldStr.length )*arrayOfIndexes.length];
	
		int indexResult = 0;	//index for resultData
		int indexData = offset;	//index for class variable data
		int indexIndexes = 0;	//index for arrayOfIndexes
		
		while(indexResult < resultData.length) {
			if(indexIndexes < arrayOfIndexes.length 
					&& arrayOfIndexes[indexIndexes] == indexData) {
				for(int i = 0; i < newStr.length; i++) {
					resultData[indexResult + i] = newStr.charAt(i);
				}
				indexResult += newStr.length;
				indexData += oldStr.length;
				indexIndexes++;
				
			} else {
				resultData[indexResult] = data[indexData];
				indexResult++;
				indexData++;
			}
		}
		
		return new CString(resultData, 0, resultData.length, true);
		//could use CString(resultData) too, but that would remake same char array
	}
	
	/**
	 * Adds a single integer to an int array.
	 * 
	 * @param array		adds to this array.
	 * @param number	this number is added.
	 * @return array with added number.
	 */
	private int[] addToIntArray(int[] array, int number) {
		/*
		 * yes, this will be slow if we have to do this work million times,
		 * but not many calls are expected
		 */
		int[] newArray = new int[array.length + 1];
		for(int i = 0; i < array.length; i++) {
			newArray[i] = array[i];
		}
		newArray[array.length] = number;
		return newArray;
	}
	
	/**
	 * Finds index where sequence of chars starts.
	 * Searching between index startingIndex and endingIndex, 
	 * those indexes are actual internal character array indexes.
	 * 
	 * @param sequence			sequence being searched.
	 * @param startingIndex		search starts here.
	 * @param endingIndex		search ends here.
	 * @return index where sequence starts, or -1 if no such sequence.
	 */
	private int indexOfSequence(CString sequence, int startingIndex, int endingIndex) {
		int savedOffset = offset;
		int savedLength = length;
		offset = startingIndex;
		length = endingIndex - startingIndex;
		
		int index = -1;
		
		try {
			while(length >= sequence.length) {
				if(startsWith(sequence)) {
					index = offset;
					break;
				}
				offset++;
				length--;
			}
		} finally { //because if anything bad happens we must restore offset and length
			offset = savedOffset;
			length = savedLength;
		}
		
		return index;
	}
	
	/**
	 * Copies all chars from array copyFrom to array copyTo.
	 * In array copyFrom chars are copied startin from index [indexFrom].
	 * In array copyTo chars are "pasted" starting from index [indexTo].
	 * Number of chars copied from copyFrom to copyTo is defined by howMany.
	 * 
	 * NOTE: before calling this method, make sure that arrays are big enough, 
	 * this method will not check for valid arguments.
	 * 
	 * @param copyFrom		array where chars being copied are.
	 * @param copyTo		array where copied chars are being "pasted".
	 * @param indexFrom		from this index copying from copyFrom starts.
	 * @param indexTo		starting index where chars are "pasted" in copyTo.
	 * @param howMany		this is how many chars are being copied/pasted.		
	 */
	private void copyFromTo(char[] copyFrom, char[] copyTo, 
							int indexFrom, int indexTo, int howMany) {
		/*
		 * will not check if arguments are valid because this is a private method 
		 * and I will make sure I pass valid arguments
		*/
		
		for(int index = 0; index < howMany; index++) {
			copyTo[indexTo + index] = copyFrom[indexFrom + index]; 
		}
	}
}
