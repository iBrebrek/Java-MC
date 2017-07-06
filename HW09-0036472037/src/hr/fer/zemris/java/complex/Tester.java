package hr.fer.zemris.java.complex;


/**
 * A simple class to test {@code ComplexPolynomial} and {@code ComplexRootedPolynomial}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.5.2016.)
 */
public class Tester {
	
	/**
	 * Entry point.
	 * @param args not used.
	 */
	public static void main(String[] args) {
		
		ComplexPolynomial poly = new ComplexPolynomial(new Complex(1, -1), Complex.ONE,  new Complex(2,2) ,Complex.ZERO, Complex.IM_NEG);
		Complex z = new Complex(5, -3.3);
		System.out.println("order: "+poly.order());
		System.out.println(poly + " , z="+z +"  result: "+poly.apply(z));
		System.out.println("poly*poly = " + poly.multiply(poly));
		System.out.println("poly' = " + poly.derive());
		
		System.out.println("----------------------------");
		
		ComplexRootedPolynomial roots = new ComplexRootedPolynomial(Complex.ONE, Complex.ZERO, new Complex(3, -3.5));
		System.out.println(roots);
		System.out.println("To poly: " + roots.toComplexPolynom());
		System.out.println("Apply z:  " + roots.apply(z));
	}
}
