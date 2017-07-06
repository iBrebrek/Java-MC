package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import static java.lang.Integer.parseInt;

/**
 * Demonstrates class ObjectStack.
 * 
 * Expression is in postfix representation.
 * Example 1: "8 2 /" is 8/2
 * Example 2: "-1 8 2 / +" is 8/2 + -1
 * Every element in "" must be separated by at least one space. 
 * 
 * Supported operators are: +, -, /, * and %.
 * All number are integers (both inputs and results).
 * 
 * Program should have only one argument and one exactly argument, see example 1 and 2.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (21.3.2016.)
 */
public class StackDemo {
	
	/**
	 * Defines what kind of object it is.
	 */
	enum Type {
		/** Object is decimal number */
		DECIMAL,
		/** Object is an integer */
		INTEGER,
		/** Object is supported operator */
		OPERATOR,
		/** Object is something that isn't in any of first 3 groups */
		OTHER
	}
	

	/**
	 * Starting method for this program.
	 * 
	 * @param args 	read class documentation
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("One and only one argument is needed. "
								+ "Write whole expression in \" \".");
			return;
		}
		
		String[] expression = args[0].replace("\"", "")
									 .trim()
									 .replaceAll("\\s+", " ")
									 .split(" ");
		
		if(expression.length < 1 || expression[0].isEmpty()) {
			System.err.println("Empty expression was given.");
			return;
		}
		
		ObjectStack stack = new ObjectStack();
		
		for(String element : expression) {
			switch(getType(element)) {
				case INTEGER:
					stack.push(element);
					break;
					
				case DECIMAL:
					System.err.println("Decimal numbers are not supported, only integers are.");
					return;
					
				case OPERATOR:
					stack.push(element);
					try {
						stack.push(calculate(stack.pop(), stack.pop(), stack.pop()));
					} catch (EmptyStackException emptyStack) { 
						System.err.println
							("There has to be at least 2 numbers before operator.");
						return;
					} catch (IllegalArgumentException exception) {
						System.err.println(exception.getMessage());
						return;
					}
					break;
					
				case OTHER:
					System.err.println("This element is meaningless: " + element);
					return;
			}
		}
		
		if(stack.size() != 1) {
			System.err.println("Invalid expression was given.");
		} else {
			System.out.println("Expression evaluates to " + stack.pop() + ".");
		}
	}
	
	
	
	/**
	 * Checks what's stored in given string.
	 * 
	 * @param string	string being checked
	 * @return string's type
	 */
	private static Type getType(String string) {  
		if(string.matches("[-+]?\\d+")) {
			return Type.INTEGER;
			
		} else if(string.matches("[-+]?\\d*\\.?\\d+")) {
			return Type.DECIMAL;
			
		} else if(string.matches("[+-[*]/%]")){
			return Type.OPERATOR;
			
		} else {
			return Type.OTHER;
		}
	}  

	/**
	 * Calculates rightNumber and leftNumber with operator.
	 * Numbers are integers.
	 * Operators can be +, -, *, / and %.
	 * 
	 * @param operator		math operator
	 * @param rightNumber	number before operator
	 * @param leftNumber	number after operator
	 * @return result of (leftNumber operator rightNumber)
	 */
	private static Object calculate(Object operator, Object rightNumber, Object leftNumber) {
		int first = parseInt(leftNumber.toString());
		int second = parseInt(rightNumber.toString());
		
		switch (operator.toString()) {
			case "+":
				return first + second;
				
			case "-":
				return first - second;
				
			case "*":
				return first * second;
				
			case "/":
				if(second == 0) {
					throw new IllegalArgumentException("Unable to divide by 0.");
				}
				return first / second;
				
			case "%":
				if(second == 0) {
					throw new IllegalArgumentException("Unable to divide by 0.");
				}
				return first % second;
				
			default:
				throw new IllegalArgumentException
					("Only +, -, *, / and % are supported operators.");
		}
	}
}
