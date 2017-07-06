package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * Wrapper that stores {@code Object} and allows some arithmetic method and numerical comparison.
 * To use comparison and arithmetic methods values must be {@code Integer} or {@code Double}. 
 * {@code String} is also acceptable but it must be convertible to {@code Integer} or {@code Double}.
 * If {@code Object} is {@code null} reference, in arithmetic method it will be treated as a {@code Integer(0)}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (17.4.2016.)
 */
public class ValueWrapper {
	/** Value stored in this object. */
	private Object value;

	/**
	 * Initial stored value in this object.
	 * 
	 * @param value		initial value.
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Getter for value stored in this object.
	 * 
	 * @return current value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setter for value stored in this object.
	 * 
	 * @param value		new value.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Increases stored value by {@code incValue}.
	 * 
	 * @param incValue 		by this is increased stored value.
	 * 
	 * @throws RuntimeException if stored value or {@code incValue} do not meet requirements specified in class documentation.
	 */
	public void increment(Object incValue) {
		value = calculate(examineValue(value), examineValue(incValue), (x, y) -> x + y);
	}
	
	/**
	 * Decreases stored value by {@code decValue}.
	 * 
	 * @param decValue 		by this is decreased stored value.
	 * 
	 * @throws RuntimeException if stored value or {@code decValue} do not meet requirements specified in class documentation.
	 */
	public void decrement(Object decValue) {
		value = calculate(examineValue(value), examineValue(decValue), (x, y) -> x - y);
	}
	
	/**
	 * Multiplies stored value by {@code mulValue}.
	 * 
	 * @param mulValue 		by this is multiplied stored value.
	 * 
	 * @throws RuntimeException if stored value or {@code mulValue} do not meet requirements specified in class documentation.
	 */
	public void multiply(Object mulValue) {
		value = calculate(examineValue(value), examineValue(mulValue), (x, y) -> x * y);
	}
	
	/**
	 * Divides stored value by {@code divValue}.
	 * 
	 * @param divValue 		by this is divided stored value.
	 * 
	 * @throws RuntimeException if stored value or {@code divValue} do not meet requirements specified in class documentation.
	 */
	public void divide(Object divValue) {
		value = calculate(examineValue(value), examineValue(divValue), (x, y) -> x / y);
	}
	
	/**
	 * Compares stored value and {@code withValue}.
	 * Returns an integer less than zero if currently stored value is smaller than argument, 
	 * an integer greater than zero if currently stored value is larger than argument 
	 * or an integer 0 if they are equal.
	 * 
	 * @param withValue 		by this is divided stored value.
	 * @return {@code 0} if stored value and argument are the same.
	 * 			Less than {@code 0} if stored value is smaller than argument.
	 * 			Greater than {@code 0} if stored value is larger than argument.
	 * 
	 * @throws RuntimeException if stored value or {@code withValue} do not meet requirements specified in class documentation.
	 */
	public int numCompare(Object withValue) {
		Object result = calculate(examineValue(value), examineValue(withValue), (x, y) -> x - y);
		if(result instanceof Integer) {
			return ((Integer) result).intValue();
		} else {
			return ((Double) result).compareTo(0.0);
		}
	}
	
	/**
	 * Calculates {@code first} and {@code second} using given {@code BiFunction}.
	 * Returns {@code Object} that is result of {@code function}.
	 * 
	 * {@code first} and {@code second} must be either {@code Integer} or {@code Double}.
	 * 
	 * @param first			first operator in {@code function}.
	 * @param second		second operator in {@code function}.
	 * @param function		function used to calculate result.
	 * @return {@code Double} if at least one given operator is double, 
	 * 		   {@code Integer} if both operators are integers.
	 */
	private Object calculate(Object first, Object second, BiFunction<Double, Double, Double> function) {
		
		Double result = function.apply(toDouble(first), toDouble(second));
		
		if(first instanceof Double || second instanceof Double) {
			return result;
		}
		else {
			return new Integer(result.intValue());
		}
	}
	
	/**
	 * Returns {@code Double} of a given {@code number}.
	 * {@code number} is expected to be either {@code Integer} or {@code Double}.
	 * 
	 * @param number		{@code Integer} or {@code Double}.
	 * @return {@code Double} representation of {@code number}.
	 */
	private Double toDouble(Object number) {
		if(number instanceof Double) return (Double) number;
		//so it must be Integer
		return ((Integer) number).doubleValue();
	}
	
	/**
	 * Checks if {@code value} is valid for arithmetic method and numerical comparison.
	 * 
	 * Converts {@code String} to {@code Integer} or {@code Double}.
	 * {@code Integer} and {@code Double} are just returned.
	 * 
	 * @param value		{@code Object} being validated.
	 * @return {@code Integer} or {@code Double}.
	 * 
	 * @throws RuntimeException if {@code value} does not meet requirements specified in class documentation.
	 */
	private Object examineValue(Object value) {
		if(value == null) return new Integer(0);
		if(value instanceof Integer) return value;
		if(value instanceof Double) return value;
		if(value instanceof String) {
			try{
				return Integer.parseInt((String)value);
			} catch (Exception notInteger) {
				try {
					return Double.parseDouble((String) value); 
				} catch (Exception notDouble) {
					throw new RuntimeException("String must be either integer or decimal.");
				}
			}
		}

		throw new RuntimeException("Value must be null or instance of String, Integer or Double.");
	}
}
