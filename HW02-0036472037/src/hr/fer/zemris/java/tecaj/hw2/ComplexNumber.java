package hr.fer.zemris.java.tecaj.hw2;

/**
 * Represents an unmodifiable complex number.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (21.3.2016.)
 */
public class ComplexNumber {
	/** Angle in polar form. */
	private double angle;
	
	/** Radius in polar form. */
	private double radius;
	
	/**
	 * Constructor that initializes both real and imaginary part of complex number.
	 * 
	 * @param real 			real part of complex number
	 * @param imaginary		imaginary part of complex number
	 */
	public ComplexNumber(double real, double imaginary) {
		angle = getAngle(real, imaginary);
		radius = getRadius(real, imaginary);
	}
	
	/**
	 * Default constructor invisible to user.
	 * Used in some methods because public constructor was too complex.
	 */
	private ComplexNumber() {
	}

	/**
	 * With real and imaginary part calculates angle in polar form.
	 * 
	 * @param real		real part of complex number
	 * @param imaginary	imaginary part of complex number
	 * 
	 * @return angle in polar form
	 */
	private static double getAngle(double real, double imaginary) {
		return adjustAngle(Math.atan2(imaginary, real));
	}
	
	/**
	 * Puts given angle between 0 and 2PI.
	 * Because getAngle() must return angle between 0-2PI.
	 * 
	 * @param angle		angle to adjust
	 * @return 0-2PI equivalent to angle
	 */
	private static double adjustAngle(double angle) {
		double twoPI = 2*Math.PI;
		while(angle < 0) {
			angle += twoPI;
		}
		while(angle > twoPI) {
			angle -= twoPI;
		}
		return angle;
	}
	
	/**
	 * With real and imaginary part calculates radius in polar form.
	 * 
	 * @param real		real part of complex number
	 * @param imaginary	imaginary part of complex number
	 * 
	 * @return radius in polar form
	 */
	private static double getRadius(double real, double imaginary) {
		return Math.sqrt(real*real + imaginary*imaginary);
	}

	/**
	 * Creates complex number with given real number and sets imaginary part to 0.
	 * 
	 * @param real	real part of complex number
	 * @return new complex number
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}
	
	/**
	 * Creates complex number with given imaginary number and sets real part to 0.
	 * 
	 * @param imaginary		imaginary part off complex number
	 * @return new complex number
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	
	/**
	 * Creates new complex number with given magnitude and angle.
	 * 
	 * @param magnitude		radius in polar form
	 * @param angle			angle in polar form
	 * @return new complex number
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		ComplexNumber c = new ComplexNumber();
		c.angle = adjustAngle(angle);
		c.radius = magnitude;
		return c;
	}
	
	/**
	 * Converts given string to new complex number.
	 * 
	 * Examples of s: "-3", "5-i", "7i"
	 * 
	 * If s does not represent any complex number
	 * NumberFormatException is thrown. 
	 * 
	 * @param s	string representing complex number
	 * @return complex number
	 */
	public static ComplexNumber parse(String s) {
		checkIfNull(s);

		s = s.replace("\"", "").replaceAll("\\s+", "");
		int indexOfLastSign = Math.max(s.lastIndexOf("+"), s.lastIndexOf("-"));
		double real;
		double imaginary;
		
		if(indexOfLastSign < 1) { //-1 if no signs, 0 if sign is first
			real = stringToReal(s);
			imaginary= stringToImaginary(s);
		} else { 
			real = stringToReal(s.substring(0, indexOfLastSign));
			imaginary = stringToImaginary(s.substring(indexOfLastSign));
		}
		
		return new ComplexNumber(real, imaginary);
	}
	
	/**
	 * Converts string containing one part of complex number to real part.
	 * 
	 * @param s 	string being converted
	 * @return decimal number
	 */
	private static double stringToReal(String s) {
		if(s.contains("i")) {
			return 0;
		}
		
		return tryToParse(s);
	}
	
	/**
	 * Converts string containing one part of complex number to imaginary part.
	 * 
	 * @param s 	string being converted
	 * @return decimal number
	 */
	private static double stringToImaginary(String s) {
		if(!s.contains("i")) {
			return 0;
		}
		
		s = s.replace("i", "");
		if(s.equals("-")) {
			//example: if starting string was "3-i"
			return -1;
		}
		
		return tryToParse(s);
	}

	/**
	 * Tries to parse string to double.
	 * Throws NumberFormatException if not possible.
	 * 
	 * @param s 	string being parsed
	 * @return decimal number
	 */
	private static double tryToParse(String s) {
		try {
			return Double.parseDouble(s);
			
		} catch(NumberFormatException | NullPointerException exception) {
			throw new NumberFormatException
				("Given string can not be converted to complex number.");
		}
	}
	
	/**
	 * Getter for real part of this complex number.
	 * @return real part
	 */
	public double getReal() {
		return radius * Math.cos(angle);
	}
	
	/**
	 * Getter for imaginary part of this complex number.
	 * @return imaginary part
	 */
	public double getImaginary() {
		return radius * Math.sin(angle);
	}
	
	/**
	 * Getter for magnitude of this complex number.
	 * @return magnitude in polar form
	 */
	public double getMagnitude() {
		return radius;
	}
	
	/**
	 * Getter for angle of this complex number.
	 * @return angle in polar form
	 */
	public double getAngle() {
		return angle;
	}
	
	/**
	 * Creates new complex number that is equal to
	 * sum of given complex number and this complex number.
	 * 
	 * this + c = returned complex number
	 * 
	 * @param c 	complex number being added
	 * @return new complex number(sum of this and given complex number)
	 */
	public ComplexNumber add(ComplexNumber c) {
		checkIfNull(c);
		return new ComplexNumber(this.getReal() + c.getReal(),
								 this.getImaginary() + c.getImaginary());
	}

	/**
	 * Creates new complex number that is equal to
	 * subtraction of this complex number and given complex number
	 * 
	 * this - c = returned complex number
	 * 
	 * @param c		complex number being subtracted
	 * @return result of subtraction (new complex number)
	 */
	public ComplexNumber sub(ComplexNumber c)  {
		checkIfNull(c);
		return new ComplexNumber(this.getReal() - c.getReal(),
				 				 this.getImaginary() - c.getImaginary());
	}
	
	/**
	 * Creates new complex number that is equal to 
	 * multiplication of this and given complex number.
	 * 
	 * this * c = returned complex number
	 * 
	 * @param c		current complex number is multiplied by c
	 * @return result of multiplication (new complex number)
	 */
	public ComplexNumber mul(ComplexNumber c) {
		checkIfNull(c);
		
		ComplexNumber complexNumber = new ComplexNumber();
		complexNumber.radius = this.radius * c.radius;
		complexNumber.angle = adjustAngle(this.angle + c.angle);
		return complexNumber;
	}
	
	/**
	 * Creates new complex number that is equal to
	 * division of this and given complex number.
	 * 
	 * this / c = returned complex number
	 * 
	 * @param c		current complex number is divided by c
	 * @return result of division (new complex number)
	 */
	public ComplexNumber div(ComplexNumber c) {
		checkIfNull(c);
		if(c.radius == 0) {
			throw new IllegalArgumentException("Can not divide by 0.");
		}
		
		ComplexNumber complexNumber = new ComplexNumber();
		complexNumber.radius = this.radius / c.radius;
		complexNumber.angle = adjustAngle(this.angle - c.angle);
		return complexNumber;
	}

	/**
	 * Returns new complex number that is equal to the value of 
	 * the this complex number raised to the power of the given argument.
	 * 
	 * this ^ n = returned complex number
	 * 
	 * @param n		exponent
	 * @return result (new complex number)
	 */
	public ComplexNumber power(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("N must be 0 or bigger.");
		}
		
		ComplexNumber complexNumber = new ComplexNumber();
		complexNumber.radius = Math.pow(this.radius, n);
		complexNumber.angle = adjustAngle(n * this.angle);
		return complexNumber;
	}
	
	/**
	 * Creates new complex number that is equal to 
	 * the given root of this complex number.
	 * 
	 * Roots must positive natural number.
	 * If invalid root is given IllegalArgumentException is thrown.
	 * 
	 * @param n		number of root
	 * @return result of root (new complex number)
	 */
	public ComplexNumber[] root(int n) {
		if(n < 1) {
			throw new IllegalArgumentException("N must be 1 or bigger.");
		}
		
		double roots = n; //because int is not nice for dividing
		double r = Math.pow(radius, 1 / roots);
		/*
		 * order was incorrect (compared to Wolfram) because angle is 0-2PI
		 * so this way we put it between -PI and PI and it fixes order
		 * 
		 * if order does not matter then it should be:
		 * fi = angle / roots;
		 */
		double fi = (angle > Math.PI ? angle-2*Math.PI : angle) / roots;
		double fragment = 2*Math.PI / roots;
		
		ComplexNumber[] allRoots = new ComplexNumber[n];
		
		for(int i = 0; i < n; i++) {
			allRoots[i] = 
					new ComplexNumber(r * Math.cos(fi + fragment*i), 
									  r * Math.sin(fi + fragment*i));
		}
		
		return allRoots;
	}
	
	/**
	 * Returns a string representation of this object.
	 * Numbers are always decimal numbers with 3 digits after decimal point.
	 */
	@Override
	public String toString() {
		double imaginary = getImaginary();
		String sign;
		
		if (imaginary < 0) {
			sign = "-";
		} else {
			sign = "+";
		}
		
		return String.format("%.3f %s %.3fi", getReal(), sign, Math.abs(imaginary));
	}
	
	/**
	 * If given reference is null throws IllegalArgumentException.
	 * 
	 * @param object 	reference being checked
	 */
	private static void checkIfNull(Object object) {
		if(object == null) {
			throw new IllegalArgumentException("Complex number can not be null.");
		}
	}
}
