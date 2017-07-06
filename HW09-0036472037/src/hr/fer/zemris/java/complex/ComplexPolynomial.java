package hr.fer.zemris.java.complex;

/**
 * Immutable model of coefficient-based complex polynomial .
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.5.2016.)
 */
public class ComplexPolynomial {
	
	/** Coefficient for each x^n. */
	private Complex[] polynom;
	
	/**
	 * Initializes this polynomial.
	 * 
	 * If polynomial is: a*x^2 + b
	 * factors would be: b, 0, a
	 * 
	 * factor[n] is {@code Complex} next to x^n
	 * 
	 * @param factors		factors of this polynomial.
	 */
	public ComplexPolynomial(Complex ...factors) {
		for(Complex factor : factors) {
			if(factor == null) {
				throw new IllegalArgumentException("There is a null factor, that is not acceptable.");
			}
		}
		polynom = factors;
	}

	/**
	 * Returns order of this polynomial.
	 * For example: (7+2i)z^3+2z^2+5z+1 returns 3
	 * 
	 * @return order of this polynomial.
	 */
	public short order() {
		return (short) (polynom.length==0 ? 0 : polynom.length-1);
	}
	
	/**
	 * Multiplies two polynomials.
	 * 
	 * @param p		multiplier.
	 * @return new {@code ComplexPolynomial} equal to {@code this*p}
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		if(p == null) {
			throw new IllegalArgumentException("Complex polynomial can not be null");
		}
		Complex[] result = new Complex[p.order() + this.order() + 1];
		
		for(int i = 0; i < result.length; i++) {
			result[i] = Complex.ZERO;
		}
		
		for(int first = 0; first < polynom.length; first++) {
			for(int sec = 0; sec < p.polynom.length; sec++) {
				result[first+sec] = result[first+sec].add(polynom[first].multiply(p.polynom[sec]));
			}
		}
		
		return new ComplexPolynomial(result);
	}
	
	/**
	 * Computes first derivative of this polynomial.
	 * For example: 7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	 * 
	 * @return first derivation.
	 */
	public ComplexPolynomial derive() {
		Complex[] result = new Complex[polynom.length==1 ? 1 : polynom.length-1];
		
		for(int i = 1; i < polynom.length; i++) {
			result[i-1] = polynom[i].multiply(new Complex(i, 0));
		}
		
		return new ComplexPolynomial(result);
	}
	
	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z		substituted complex number.
	 * @return result of substitution.
	 */
	public Complex apply(Complex z) {
		if(z == null) {
			throw new IllegalArgumentException("Complex number can not be null");
		}
		Complex result = Complex.ZERO;
		
		for(int i = 0; i < polynom.length; i++) {
			result = result.add(polynom[i].multiply(z.power(i)));
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = polynom.length-1; i > 0; i--) {
			String complex = polynom[i].toString();
			if(complex.equals("0")) continue;
			boolean brackets = complex.indexOf("+")!=-1 || complex.indexOf("-")!=-1;
			if(brackets) sb.append("(");
			sb.append(complex);
			if(brackets) sb.append(")");
			sb.append("z^");
			sb.append(i);
			sb.append(" + ");
		}
		
		String z0 = polynom[0].toString();
		if(z0.equals(0)) {
			sb.setLength(sb.length() - 3); //remove last " + "
		} else {
			boolean brackets = z0.indexOf("+")!=-1 || z0.indexOf("-")!=-1;
			if(brackets) sb.append("(");
			sb.append(z0);
			if(brackets) sb.append(")");
		}
		
		return sb.toString();
	}
}
