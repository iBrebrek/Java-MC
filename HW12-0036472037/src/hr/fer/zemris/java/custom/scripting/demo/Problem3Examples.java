package hr.fer.zemris.java.custom.scripting.demo;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * In this program are all 4 demonstrations given for problem 3.
 * In order to change between those demonstrations simply change 
 * constant {@link #WHICH_EXAMPLE_TO_DISPLAY}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (31.5.2016.)
 */
public class Problem3Examples {
	
	/**
	 * Determines which example will be executed.
	 * 
	 * In order to change which example will be 
	 * executed manually change this constant.
	 */
	private static final int WHICH_EXAMPLE_TO_DISPLAY = 1;
	
	/**
	 * Entry point.
	 * 
	 * @param args		not used.
	 */
	public static void main(String[] args) {
		try {
			switch (WHICH_EXAMPLE_TO_DISPLAY) {
			case 1:
				firstExample();
				break;
			case 2:
				secondExample();
				break;
			case 3:
				thirdExample();
				break;
			case 4:
				fourthExample();
				break;
			default:
				System.err.println("No such example.");
			}
		} catch (SmartScriptParserException parserError) {
			System.err.println("Unable to parse given file.");
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
		}
	}

	/**
	 * Retrieves whole text from given file path.
	 * 
	 * @param string		path to the file.
	 * @return text inside the file.
	 */
	private static String readFromDisk(String string) {
		try {
			return new String(
					Files.readAllBytes(Paths.get(string)), 
					StandardCharsets.UTF_8);
		} catch (Exception exc) {
			System.err.println("Can not read file.");
			System.exit(-1);
			return ""; //never happens, but compiler is complaining
		}
	}
	
	/**
	 * Executes first given example for problem 3.
	 */
	private static void firstExample() {
		String documentBody = readFromDisk("examples/osnovni.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}
	
	/**
	 * Executes second given example for problem 3.
	 */
	private static void secondExample() {
		String documentBody = readFromDisk("examples/zbrajanje.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}
	
	/**
	 * Executes third given example for problem 3.
	 */
	private static void thirdExample() {
		String documentBody = readFromDisk("examples/brojPoziva.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(
				System.out, 
				parameters, 
				persistentParameters, 
				cookies)
		;
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(), rc
		).execute();
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
	}
	
	/**
	 * Executes fourth given example for problem 3.
	 */
	private static void fourthExample() {
		String documentBody = readFromDisk("examples/fibonacci.smscr");
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}

}
