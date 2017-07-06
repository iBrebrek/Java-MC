package hr.fer.zemris.java.webserver;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

@SuppressWarnings("javadoc")
public class RequestContextTest {
	
	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	RequestContext emptyRC = null; //has no parameters or cookies
	RequestContext normalRC = null; //has parameters and cookies

	@Before
	public void initRequest() {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("keyA", "valueA");
		parameters.put("keyB", "valueB");
		Map<String,String> persistentParameters = new HashMap<>();
		persistentParameters.put("X", "1");
		persistentParameters.put("Y", "2");
		List<RCCookie> outputCookies = new ArrayList<>();
		outputCookies.add(new RCCookie("name", "value", null, null, null));
		outputCookies.add(new RCCookie("name", "value", 200, null, "/"));
		normalRC = new RequestContext(stream, parameters, persistentParameters, outputCookies);
		emptyRC = new RequestContext(stream, null, null, null);
	}
	
	@After
	public void closeStream() {
		try {
			stream.close();
		} catch (IOException ignore) {
		}
	}
	
	@Test
	public void testGetParameter() {
		String expected1 = "valueB";
		String expected2 = null;
		String actual1 = normalRC.getParameter("keyB");
		String actual2 = emptyRC.getParameter("keyB");
		assertEquals(expected1, actual1);
		assertEquals(expected2, actual2);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testGetParameterNames() {
		normalRC.getParameterNames().remove("keyA");
	}
	
	@Test
	public void testGetPersistentParameter() {
		String expected1 = "1";
		String expected2 = null;
		String actual1 = normalRC.getPersistentParameter("X");
		String actual2 = emptyRC.getPersistentParameter("X");
		assertEquals(expected1, actual1);
		assertEquals(expected2, actual2);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testGetPersistentParameterNames() {
		normalRC.getPersistentParameterNames().remove("X");
	}
	
	@Test
	public void testSetPersistentParameter() {
		String expected = "newY";
		normalRC.setPersistentParameter("Y", expected);
		String actual = normalRC.getPersistentParameter("Y");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRemovePersistentParameter() {
		normalRC.removePersistentParameter("X");
		String actual = normalRC.getPersistentParameter("X");
		assertNull(actual);
	}
	
	@Test
	public void testGetTemporaryParameter() {
		normalRC.setTemporaryParameter("a", "temp");
		String expected1 = "temp";
		String expected2 = null;
		String actual1 = normalRC.getTemporaryParameter("a");
		String actual2 = emptyRC.getTemporaryParameter("a");
		assertEquals(expected1, actual1);
		assertEquals(expected2, actual2);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testGetTemporaryParameterNames() {
		normalRC.getTemporaryParameterNames().add("should fail");
	}
	
	@Test
	public void testSetTemporaryParameter() {
		String expected = "temp";
		normalRC.setTemporaryParameter("a", expected);
		String actual = normalRC.getTemporaryParameter("a");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRemoveTemporaryParameter() {
		normalRC.setTemporaryParameter("a", "temp");
		normalRC.removeTemporaryParameter("a");
		String actual = normalRC.getPersistentParameter("a");
		assertNull(actual);
	}
	
	@Test
	public void testWriteString() {
		try {
			String abc = "abc";
			emptyRC.write(abc);
			String expected = expectedHeaderDefault(abc);
			String actual = stream.toString();
			assertEquals(expected, actual);
		} catch (IOException ignore) {
		}
	}
	
	@Test
	public void testWriteBytes() {
		try {
			String abc = "abc";
			emptyRC.write(abc.getBytes());
			byte[] expected = expectedHeaderDefault(abc).getBytes();
			byte[] actual = expectedHeaderDefault(abc).getBytes();
			assertArrayEquals(expected, actual);
		} catch (IOException ignore) {
		}
	}

	private String expectedHeaderDefault(String text) {
		//everything is at deafult value
		return "HTTP/1.1 200 OK\r\n"+
				"Content-Type: text/html; charset=UTF-8\r\n"+
				"\r\n"+
				text;
	}
	
	@Test
	public void testWriteBiggerChangedHeader() {
		String expected = "HTTP/1.1 250 FINE\r\n"+
				"Content-Type: text/something; charset=ASCII\r\n"+
				"Set-Cookie: name=\"value\"\r\n"+
				"Set-Cookie: name=\"value\"; Path=/; Max-Age=200\r\n"+
				"Set-Cookie: a=\"1\"; Domain=home; Path=C:/; Max-Age=0\r\n"+
				"\r\n"+
				"first"+"second";
		try {
			normalRC.setEncoding("ASCII");
			normalRC.setMimeType("text/something");
			normalRC.setStatusCode(250);
			normalRC.setStatusText("FINE");
			normalRC.addRCCookie(new RCCookie("a", "1", 0, "home", "C:/"));
			normalRC.write("first"); //will also create header
			normalRC.write("second"); //will only add
		} catch (IOException ignore) {
		} 
		
		String actual = stream.toString();
		assertEquals(expected, actual);
	}
	
	@Test(expected = RuntimeException.class)
	public void testSetEncodingAfterHeader() {
		try {
			normalRC.write("abc");
			normalRC.setEncoding("UTF-8");
		} catch (IOException ignore) {
		}
	}
	
	@Test(expected = RuntimeException.class)
	public void testSetStatusCodeAfterHeader() {
		try {
			normalRC.write("abc");
			normalRC.setStatusCode(555);
		} catch (IOException ignore) {
		}
	}
	
	@Test(expected = RuntimeException.class)
	public void testSetStatusTextAfterHeader() {
		try {
			normalRC.write("abc");
			normalRC.setStatusText("text");
		} catch (IOException ignore) {
		}
	}
	
	@Test(expected = RuntimeException.class)
	public void testSetMimeTypeAfterHeader() {
		try {
			normalRC.write("abc");
			normalRC.setMimeType("MIME");
		} catch (IOException ignore) {
		}
	}
	
	@Test(expected = RuntimeException.class)
	public void testAddCookieAfterHeader() {
		try {
			normalRC.write("abc");
			normalRC.addRCCookie(new RCCookie("a", "b", 0, null, null));
		} catch (IOException ignore) {
		}
	}
}

