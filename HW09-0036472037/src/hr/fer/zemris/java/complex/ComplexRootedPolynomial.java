package hr.fer.zemris.java.complex;

/**
 * Immutable model of root-based complex polynomial.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.5.2016.)
 */
public class ComplexRootedPolynomial {
	
	/** Roots. (z-root)-> in roots is only root, without -. */
	private Complex[] roots;
	
	/**
	 * Initializes this polynomial.
	 * 
	 * If polynomial is: (z-a)(z-b)(z-c)
	 * roots would be: a, b, c
	 * 
	 * @param roots		all roots for this polynomial.
	 */
	public ComplexRootedPolynomial(Complex ...roots) {
		for(Complex root : roots) {
			if(root == null) {
				throw new IllegalArgumentException("There is a null root, that is not acceptable.");
			}
		}
		this.roots = roots;
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
		return toComplexPolynom().apply(z);
	}

	/**
	 * Converts this {@code ComplexRootedPolynomial} to new {@code ComplexPolynomial}.
	 * 
	 * @return equivalent {@code ComplexPolynomial} to this. 
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(roots[0].negate(), Complex.ONE);	
		for(int i = 1; i < roots.length; i++) {
			result = result.multiply(new ComplexPolynomial(roots[i].negate(), Complex.ONE));
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Complex root : roots) {
			sb.append("[z-(")
			  .append(root.toString())
			  .append(")]");
		}
		return sb.toString();
	}

	/**
	 * Finds index of closest root for given complex number z that is within threshold.
	 * If there is not such roots, returns -1.
	 * 
	 * @param z				searches closest root for this complex number.
	 * @param threshold		looking for complex numbers within this threshold.
	 * @return index of closest roots, or -1 if not found.
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		if(z == null) {
			throw new IllegalArgumentException("Complex number can not be null");
		}
		int index = -1;
		double maxDistance = threshold;
		
		for(int i = 0; i < roots.length; i++) {
			double currentDistance = z.sub(roots[i]).module();
			if(currentDistance < maxDistance) {
				index = i;
				maxDistance = currentDistance;
			}
		}
		
		return index;
	}
}
