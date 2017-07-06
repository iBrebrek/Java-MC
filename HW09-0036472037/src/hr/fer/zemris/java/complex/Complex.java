package hr.fer.zemris.java.complex;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;


/**
 * Immutable model of complex number
 * 
 * @author Ivica Brebrek
 * @version 1.0  (9.5.2016.)
 */
public class Complex {	
	/** Complex number: Re{z} = 0, Im{z} = 0 */
	public static final Complex ZERO = new Complex(0,0);
	/** Complex number: Re{z} = 1, Im{z} = 0 */
	public static final Complex ONE = new Complex(1,0);
	/** Complex number: Re{z} = -1, Im{z} = 0 */
	public static final Complex ONE_NEG = new Complex(-1,0);
	/** Complex number: Re{z} = 0, Im{z} = 1 */
	public static final Complex IM = new Complex(0,1);
	/** Complex number: Re{z} = 0, Im{z} = -1 */
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/** Angle in polar form. */
	private double angle;
	/** Radius in polar form. */
	private double radius;
	
	/**
	 * Sets both real and imaginary part to 0. 
	 * Same as calling {@code Complex.ZERO}
	 */
	public Complex() {
		//did not write this(0,0) just to avoid calculating angle and radius
		angle = 0;
		radius = 0;
	}
	
	/**
	 * Constructor that initializes both real and imaginary part of complex number.
	 * 
	 * @param re 			real part of complex number
	 * @param im		imaginary part of complex number
	 */
	public Complex(double re, double im) {
		angle = getAngle(re, im);
		radius = getRadius(re, im);
	}
	
	/**
	 * With real and imaginary part calculates angle in polar form.
	 * 
	 * @param real			real part of complex number
	 * @param imaginary		imaginary part of complex number
	 * 
	 * @return angle in polar form
	 */
	private static double getAngle(double real, double imaginary) {
		return Math.atan2(imaginary, real);
	}
	
	/**
	 * With real and imaginary part calculates radius in polar form.
	 * 
	 * @param real			real part of complex number
	 * @param imaginary		imaginary part of complex number
	 * 
	 * @return radius in polar form
	 */
	private static double getRadius(double real, double imaginary) {
		return Math.sqrt(real*real + imaginary*imaginary);
	}
	
	/**
	 * Puts given angle between -PI and PI.
	 * 
	 * @param angle		angle to adjust
	 * @return (-PI, PI) equivalent to {@code angle}
	 */
	private static double adjustAngle(double angle) {
		while(angle < -Math.PI) {
			angle += 2*Math.PI;
		}
		while(angle > Math.PI) {
			angle -= 2*Math.PI;
		}
		return angle;
	}
	
	/**
	 * Returns module of this complex number.
	 * 
	 * @return module of complex number.
	 */
	public double module() {
		//sqrt((r*cos)^2+(r*sin)^2)=sqrt(r^2(1))=sqrt(r^2)=r
		return radius;
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
	public Complex multiply(Complex c) {
		checkIfNull(c);
		
		Complex complexNumber = new Complex();
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
	public Complex divide(Complex c) {
		checkIfNull(c);
		if(c.radius == 0) {
			throw new IllegalArgumentException("Can not divide by 0.");
		}
		
		Complex complexNumber = new Complex();
		complexNumber.radius = this.radius / c.radius;
		complexNumber.angle = adjustAngle(this.angle - c.angle);
		return complexNumber;
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
	public Complex add(Complex c) {
		checkIfNull(c);
		return new Complex(this.getReal() + c.getReal(),
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
	public Complex sub(Complex c)  {
		checkIfNull(c);
		return new Complex(this.getReal() - c.getReal(),
				 		   this.getImaginary() - c.getImaginary());
	}
	
	/**
	 * Returns new complex number equivalent to 
	 * negation of this complex number.
	 * 
	 * -this = returned complex number
	 * 
	 * @return negation of this complex number.
	 */
	public Complex negate() {
		//TODO negiraj samo im ili oboje?
		return Complex.ZERO.sub(this);
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
	public Complex power(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("N must be 0 or bigger.");
		}
		
		Complex complexNumber = new Complex();
		complexNumber.radius = Math.pow(this.radius, n);
		complexNumber.angle = adjustAngle(n * this.angle);
		return complexNumber;
	}
	
	/**
	 * Creates new complex numbers that are equal to 
	 * the given root of this complex number.
	 * 
	 * Roots must positive natural number.
	 * If invalid root is given IllegalArgumentException is thrown.
	 * 
	 * @param n		number of roots
	 * @return result of root (new complex numbers)
	 */
	public List<Complex> root(int n) {
		if(n < 1) {
			throw new IllegalArgumentException("N must be 1 or bigger.");
		}
		
		double roots = n; //because int is not nice for dividing
		double r = Math.pow(radius, 1 / roots);
		double fi = angle / roots;
		double fragment = 2*Math.PI / roots;
		
		List<Complex> allRoots = new ArrayList<>();
		
		for(int i = 0; i < n; i++) {
			allRoots.add(
				new Complex(r * Math.cos(fi+fragment*i), 
						 	r * Math.sin(fi+fragment*i)
			));
		}
		
		return allRoots;
	}

	@Override
	public String toString() {
		String re = formatDecimal(getReal());
		String im = formatDecimal(getImaginary());
		
		StringBuilder sb = new StringBuilder();
		sb.append(re);
		if(!im.isEmpty()) {
			if(!im.startsWith("-") && !re.isEmpty()) {
				sb.append("+");
			}
			if(!im.equals("1") || !im.equals("-1")) {
				sb.append(im);
			}
			sb.append("i");
		}
	
		String result = sb.toString();
		if(result.isEmpty()) return "0";
		return result;
	}
	
	/**
	 * Converts {@code double} to {@code String}, round to 2 decimals.
	 * 
	 * 1.01 returns "1.01"
	 * 1.000 returns "1"
	 * 1.0001 returns "1"
	 * 0 returns ""  - empty string
	 * -0.005 returns ""  - empty string
	 * -10000.01 returns "-10000.01"
	 * 
	 * Simply put: 
	 * rounds given number to 2 decimals,
	 * and returns it as string, 
	 * but if result from rounding is 0 returns empty string.
	 * 
	 * @param decimal	decimal number being formated to string
	 * @return string representation of given number, but empty string if {@code decimal} is 0.
	 */
	private String formatDecimal(double decimal) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat formatter = new DecimalFormat("#.##", otherSymbols);
		String number = formatter.format(decimal);
		if(number.equals("0") || number.equals("-0")) return "";
		return number;
	}

	/**
	 * Used to calculate real part of this complex number. 
	 * @return real part
	 */
	//needed because we store only radius and angle
	private double getReal() {
		return radius * Math.cos(angle);
	}
	
	/**
	 * Used to calculate imaginary part of this complex number. 
	 * @return imaginary part
	 */
	//needed because we store only radius and angle
	private double getImaginary() {
		return radius * Math.sin(angle);
	}
	
	/**
	 * If given reference is null throws IllegalArgumentException.
	 * 
	 * @param c 	reference being checked
	 */
	private static void checkIfNull(Object c) {
		if(c == null) {
			throw new IllegalArgumentException("Complex number can not be null.");
		}
	}
}
