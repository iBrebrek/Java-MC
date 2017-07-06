package hr.fer.zemris.java.tecaj.hw2.demo;

import java.util.Arrays;

import hr.fer.zemris.java.tecaj.hw2.ComplexNumber;

/**
 * Program that demonstrates class ComplexNumber.
 * 
 * No arguments are needed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (22.3.2016.)
 */
public class ComplexDemo {

	/**
	 * First called method when program is started.
	 * Shows a demonstration of ComplexNumber.
	 * 
	 * @param args 	not used
	 */
	public static void main(String[] args) {

		System.out.println(ComplexNumber.parse("9  + 5.9999999i"));
		System.out.println(ComplexNumber.parse("-5.55+5.99i"));
		
		ComplexNumber c1 = new ComplexNumber(5, 5);
		ComplexNumber c2 = ComplexNumber.parse("-2.77-555i");
		ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57))
		.div(c2).power(3).root(2)[1];
		System.out.println(c3);

		ComplexNumber c5 = new ComplexNumber(-9, -3.6);
		ComplexNumber c4 = ComplexNumber.parse("-2.5+7.5i");
		System.out.println(c5.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57)));
		System.out.println(c5.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57)).div(c4));
		System.out.println(c5.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57)).div(c4).power(3));
		System.out.println(Arrays.toString(c5.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57)).div(c4).power(3).root(5)));



	}

}
