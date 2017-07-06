package hr.fer.zemris.java.tecaj.hw07.crypto;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
public class CryptoHexBytesTests {
	
	@Test
	public void testBytesToHexZeroes() {
		byte[] bytes = new byte[4]; //4 zeroes
		
		String expected = "00000000";  //8 zeroes
		String actual = Crypto.bytesToHex(bytes);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testBytesToHexMax() {
		byte[] bytes = {127, 127}; //01111111, 01111111
		
		String expected = "7f7f"; 
		String actual = Crypto.bytesToHex(bytes);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testBytesToHexMin() {
		byte[] bytes = {-128, -128}; //10000000, 10000000
		
		String expected = "8080"; 
		String actual = Crypto.bytesToHex(bytes);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testHexToBytesZeroes() {
		String hex = "00000000";
		
		String expected = "0000";
		String actual = "";
		for(byte b : Crypto.hextobyte(hex)) {
			actual +=b;
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testHexToByteFF() {
		String hex = "ffff";
		
		String expected = "-1-1"; 
		String actual = "";
		for(byte b : Crypto.hextobyte(hex)) {
			actual +=b;
		}
		
		assertEquals(expected, actual);
	}	
	
	@Test
	public void testHexToBytesMax() {
		String hex = "7f7f";
		
		String expected = "127127"; 
		String actual = "";
		for(byte b : Crypto.hextobyte(hex)) {
			actual +=b;
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testHexToBytesMin() {
		String hex = "8080";
		
		String expected = "-128-128"; 
		String actual = "";
		for(byte b : Crypto.hextobyte(hex)) {
			actual +=b;
		}
		
		assertEquals(expected, actual);
	}
}
