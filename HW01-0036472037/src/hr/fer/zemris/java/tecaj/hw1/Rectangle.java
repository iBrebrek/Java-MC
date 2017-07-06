package hr.fer.zemris.java.tecaj.hw1;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Program koji računa površinu i opseg pravokutnika.
 * Od korisnika, red po red, traži unos širine i visine.
 * Drugi način je prilikom pozivanja zadati 2 argumenta (�irinu i visinu).
 * Dozvoljen je samo unos brojeva.
 * 
 * @author Ivica B.
 * @version 1.0
 *
 */
public class Rectangle {

	/**
	 * Metoda koja pokreće izvođenje programa.
	 * @param args	argumenti programa Rectangle
	 * @throws Exception ako nije moguće čitati s komandnije linije, 
	 * 					 ili ako je uneseno nešto što nije broj
	 */
	public static void main(String[] args) throws Exception {
		double width = 0d;
		double height = 0d;
		
		switch(args.length) {
			case 0:
				width = doubleFromLine("width");
				height = doubleFromLine("height");
				break;
			case 2:
				width = Double.parseDouble(args[0].trim());
				height = Double.parseDouble(args[1].trim());
				if(width < 0 || height < 0) {
					System.out.println("Both width and height must be positive.");
					return;
				}
				break;
			default:
				System.out.println("Invalid number of arguments was provided.");
				return;
		}
			
		System.out.printf(
				"You have specified a rectangle with width %.1f and height %.1f. "
				+ "Its area is %.1f and its perimeter is %.1f.", 
				width, height, width*height, 2*width + 2*height
		);

	}

	/**
	 * Metoda koja s komandne linije čita broj i pretvara ga u double.
	 * @param name ime onoga što se traži od korisnika
	 * @return broj s komandne linije
	 * @throws Exception ako nije moguće čitati s komandnije linije, 
	 * 					 ili ako je uneseno nešto što nije broj
	 */
	private static double doubleFromLine(String name) throws Exception {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in)
		);
		while (true) {
			System.out.println("Please provide " + name + ": ");
			String line = reader.readLine().trim();
			if(line.isEmpty()) {
				System.out.println("You wrote nothing!");
				continue;
			}
			double number = Double.parseDouble(line);
			if(number < 0) {
				System.out.println("Negative number is not acceptable.");
				continue;
			}
			return number;
		}
	}

}
