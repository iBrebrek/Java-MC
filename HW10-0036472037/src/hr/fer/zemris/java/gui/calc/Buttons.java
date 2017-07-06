package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * Class that offers only static factory methods.
 * Factory methods create calculator buttons.
 * Each method takes {@link IStack} and/or {@link IScreen} as argument.
 * Every button will have style from {@link Style}.
 * Every button already has defined text and action listener.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (15.5.2016.)
 */
public class Buttons {
	
	/** Can't be reached, can't be touched. */
	private Buttons() {
	}
	
	/**
	 * Creates button that on click adds ADD operation to stack.
	 * Button text is "+".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will push current number from {@link IScreen#read()} to {@code IStack}
	 * clear {@code IScreen} and push add operation to {@code IStack}.
	 * 
	 * @param screen		screen where numbers are shown.
	 * @param stack			stack where number and operation is pushed.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton add(IScreen screen, IStack stack) {
		return createButton("+", 
				doubleNumberOperation(screen, stack, (a,b) -> a+b)
		);
	}
	
	/**
	 * Creates button that on click adds MINUS operation to stack.
	 * Button text is "-".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will push current number from {@link IScreen#read()} to {@code IStack}
	 * clear {@code IScreen} and push minus operation to {@code IStack}.
	 * 
	 * @param screen		screen where numbers are shown.
	 * @param stack			stack where number and operation is pushed.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton minus(IScreen screen, IStack stack) {
		return createButton("-", 
				doubleNumberOperation(screen, stack, (a,b) -> a-b)
		);
	}
	
	/**
	 * Creates button that on click adds MULTIPY operation to stack.
	 * Button text is "*".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will push current number from {@link IScreen#read()} to {@code IStack}
	 * clear {@code IScreen} and push multipy operation to {@code IStack}.
	 * 
	 * @param screen		screen where numbers are shown.
	 * @param stack			stack where number and operation is pushed.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton multiply(IScreen screen, IStack stack) {
		return createButton("*", 
				doubleNumberOperation(screen, stack, (a,b) -> a*b)
		);
	}
	
	/**
	 * Creates button that on click adds DIVIDE operation to stack.
	 * Button text is "/".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will push current number from {@link IScreen#read()} to {@code IStack}
	 * clear {@code IScreen} and push divide operation to {@code IStack}.
	 * 
	 * @param screen		screen where numbers are shown.
	 * @param stack			stack where number and operation is pushed.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton divide(IScreen screen, IStack stack) {
		return createButton("/", 
				doubleNumberOperation(screen, stack, (a,b) -> a/b)
		);
	}
	
	/**
	 * Creates button that changes sing of shown number.
	 * Button text is "+/-".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will change sing of number from {@link IScreen#read()}.
	 * 
	 * @param screen		screen where number is shown.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton sign(IScreen screen) {
		return createButton("+/-", 
				singleNumberOperation(screen, x -> x*(-1))
		);
	}
	
	/**
	 * Creates button that on click shows number.
	 * Button text is equals to {@code number}.
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, depending on {@link IScreen#isEmpty()}, button will
	 * write itself to screen if screen is empty, or
	 * add itself to an end of current number if screen is not empty.
	 * 
	 * Example, let's say {@code number} is 7:
	 * screen empty: on click screen will show "7",
	 * if "33" shown on screen, after click screen will show "337";
	 * 
	 * @param number 		number to be shown.
	 * @param screen		screen where number is shown.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton number(int number, IScreen screen) {
		return createButton(String.valueOf(number), e -> {
			screen.show(screen.isEmpty() ? String.valueOf(number) : (screen.read()+number));
		});
	}
	
	/**
	 * Creates button that removes currently shown number on screen.
	 * Button text is "clr".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will show "0" on {@code screen} and clear said screen.
	 * 
	 * @param screen		screen being cleared.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton clear(IScreen screen) {
		return createButton("clr", e -> {
			screen.show("0");
			screen.clear();
		});
	}

	/**
	 * Creates button that removes everything from stack and screen.
	 * Button text is "res".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will clear everything from {@code stack} 
	 * and write "0" to {@code screen}.
	 * 
	 * @param screen		screen being cleared.
	 * @param stack 		stack being emptied.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton reset(IScreen screen, IStack stack) {
		return createButton("res", e -> {
			screen.show("0");
			screen.clear();
			while(!stack.isEmpty()) {
				stack.pop(); //pop everything -> clear stack
			}	
		});
	}

	/**
	 * Creates button that calculates everything from stack and writes result to screen.
	 * Button text is "=".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, 
	 * button will take everything from {@code stack} (it will also remove stack),
	 * calculate everything (starting from first to last pushed on stack),
	 * and write result on screen.
	 * Also, after click, new number will rewrite result (it won't be added to end).
	 * 
	 * @param screen		screen where result is written.
	 * @param stack 		stack containing all elements needed for calculation.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton equals(IScreen screen, IStack stack) {
		return createButton("=", e -> {
			stack.push(screen.read()); //push last number
			try {
				if(stack.size() > 2) {
					screen.show(calculate(stack));
				}
			} catch (Exception exc) {
				screen.show("ERROR"); //not sure if this can happen, was changing a lot of code so left this alone
			}
			while(!stack.isEmpty()) { //clear it, because we could do 3===== and that would push a lot on stack.
				stack.pop();
			}
			screen.clear();
		});
	}
	
	/**
	 * Creates button that puts decimal point at the end of number.
	 * Button text is ".".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will add decimal point at 
	 * the end of number from {@link IScreen#read()}.
	 * If said number already contains point 
	 * click will produce alter sound.
	 * 
	 * @param screen		screen where result is written.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton point(IScreen screen) {
		return createButton(".", e -> {
			if(screen.isEmpty()) {
				screen.show("0.");
				return;
			}
			String string = screen.read();
			boolean hasPoint = string.contains(".");
			if(hasPoint) {
				Toolkit.getDefaultToolkit().beep();
			} else {
				screen.show(string+".");
			}
		});
	}
	
	/**
	 * Creates button that pushes what's on screen to given stack.
	 * Button text is "push".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will push from {@code screen} to {@stack stack}.
	 * 
	 * @param screen		{@link IScreen#read()} will be pushed.
	 * @param stack 		where to push.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton push(IScreen screen, IStack stack) {
		return createButton("push", e -> {
			stack.push(screen.read());
		});
	}
	
	/**
	 * Creates button that pop what's on stack to given screen.
	 * Button text is "pop".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will pop from {@stack stack} to {@code screen}.
	 * If stack is empty, warning dialog will be shown.
	 * 
	 * @param screen		where to write poped element.
	 * @param stack 		from where to pop.
	 * @param frame 		where to show warning dialog if unable to pop.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton pop(IScreen screen, IStack stack, Component frame) {
		return createButton("pop", e -> {
			try {
				screen.show(stack.pop().toString());
			} catch(Exception exc) {
				JOptionPane.showMessageDialog(
						frame,
					    "Stack is empty, unable to pop.",
					    "Empty stack",
					    JOptionPane.WARNING_MESSAGE);
			}
		});
	}
	
	/**
	 * Creates button that calculates reciprocal value of currently shown number.
	 * Button text is "1/x".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate reciprocal value of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton reciprocal(IScreen screen) {
		return createButton("1/x", 
				singleNumberOperation(screen, x -> 1.0 / x)
		);
	}
	
	/**
	 * Creates button that calculates sine of currently shown number.
	 * Button text is "sin".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate sine of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton sine(IScreen screen) {
		return createButton("sin", 
				singleNumberOperation(screen, x -> Math.sin(x))
		);
	}
	
	/**
	 * Creates button that calculates cosine of currently shown number.
	 * Button text is "cos".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate cosine of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton cosine(IScreen screen) {
		return createButton("cos", 
				singleNumberOperation(screen, x -> Math.cos(x))
		);
	}
	
	/**
	 * Creates button that calculates tangent of currently shown number.
	 * Button text is "tan".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate tangent of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton tangent(IScreen screen) {
		return createButton("tan", 
				singleNumberOperation(screen, x -> Math.tan(x))
		);
	}
	
	/**
	 * Creates button that calculates cotanget of currently shown number.
	 * Button text is "ctg".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate cotanget of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton cotanget(IScreen screen) {
		return createButton("ctg", 
				singleNumberOperation(screen, x -> 1.0 / Math.tan(x))
		);
	}
	
	/**
	 * Creates button that calculates logarithm of currently shown number.
	 * Button text is "log".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate logarithm of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton logarithm(IScreen screen) {
		return createButton("log", 
				singleNumberOperation(screen, x -> Math.log10(x))
		);
	}
	
	/**
	 * Creates button that calculates natural logarithm of currently shown number.
	 * Button text is "ln".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate natural logarithm of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton naturalLogarithm(IScreen screen) {
		return createButton("ln", 
				singleNumberOperation(screen, x -> Math.log(x))
		);
	}
	
	/**
	 * Creates button that calculates arc sine of currently shown number.
	 * Button text is "asin".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate arc sine of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton arcSine(IScreen screen) {
		return createButton("asin", 
				singleNumberOperation(screen, x -> Math.asin(x))
		);
	}
	
	/**
	 * Creates button that calculates arc cosine of currently shown number.
	 * Button text is "acos".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate arc cosine of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton arcCosine(IScreen screen) {
		return createButton("acos", 
				singleNumberOperation(screen, x -> Math.acos(x))
		);
	}
	
	/**
	 * Creates button that calculates arc tangent of currently shown number.
	 * Button text is "atan".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate arc tangent of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton arcTangent(IScreen screen) {
		return createButton("atan", 
				singleNumberOperation(screen, x -> Math.atan(x))
		);
	}
	
	/**
	 * Creates button that calculates arc cotanget of currently shown number.
	 * Button text is "actg".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate arc cotanget of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton arcCotanget(IScreen screen) {
		return createButton("actg", 
				singleNumberOperation(screen, x -> 1.0 / Math.atan(x))
		);
	}
	
	/**
	 * Creates button that calculates 10^ of currently shown number.
	 * Button text is "10^".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate 10^ of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton tenPow(IScreen screen) {
		return createButton("10^", 
				singleNumberOperation(screen, x -> Math.pow(10, x))
		);
	}
	
	/**
	 * Creates button that calculates e^ of currently shown number.
	 * Button text is "e^".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will read number from {@code screen},
	 * calculate e^ of that number,
	 * and write result to {@code screen}.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result (it won't be added to end).
	 * 
	 * @param screen		from where to read and where to write.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton ePow(IScreen screen) {
		return createButton("e^", 
				singleNumberOperation(screen, x -> Math.pow(Math.E, x))
		);
	}
	
	/**
	 * Creates button that on click adds x^n operation to stack.
	 * Button text is "x^n".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will push current number from {@link IScreen#read()} to {@code IStack}
	 * clear {@code IScreen} and push x^n operation to {@code IStack}.
	 * 
	 * @param screen		screen where numbers are shown.
	 * @param stack			stack where number and operation is pushed.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton xPowN(IScreen screen, IStack stack) {
		return createButton("x^n", 
				doubleNumberOperation(screen, stack, (a,b) -> Math.pow(a, b))
		);
	}
	
	/**
	 * Creates button that on click adds n√x operation to stack.
	 * Button text is "n√x".
	 * Style used for this button can be found at {@link Style}.
	 * 
	 * On click, button will push current number from {@link IScreen#read()} to {@code IStack}
	 * clear {@code IScreen} and push n√x operation to {@code IStack}.
	 * 
	 * @param screen		screen where numbers are shown.
	 * @param stack			stack where number and operation is pushed.
	 * @return new {@code JButton} with described properties.
	 */
	public static JButton nRootX(IScreen screen, IStack stack) {
		return createButton("n√x", 
				doubleNumberOperation(screen, stack, (a,b) -> Math.pow(Math.E, Math.log(a)/b))
		);
	}
	
	/**
	 * Creates action listener that changes value written on screen.
	 * Currently shown value is applied to {@code operation} and 
	 * result is shown back to screen.
	 * 
	 * If number is being written after this operation,
	 * new number will overwritte result.
	 * 
	 * @param screen		screen where to read and write.
	 * @param operation		operation to be applied.
	 * @return decsribed action listener.
	 */
	private static ActionListener singleNumberOperation(IScreen screen, Function<Double, Double> operation) {
		return e -> {
			double num = Double.parseDouble(screen.read());
			String toBeWritten = String.valueOf(operation.apply(num));
			int pointIndex = toBeWritten.indexOf(".");
			if(pointIndex != -1) {
				if(toBeWritten.substring(pointIndex).equals(".0")) {
					toBeWritten = toBeWritten.substring(0, pointIndex);
				}
			}
			screen.show(toBeWritten);
			screen.clear();
		};
	}
	
	/**
	 * Creates action listener that adds operation to stack.
	 * 
	 * Pushes current number from {@link IScreen#read()} to {@code IStack}
	 * clears {@code IScreen} and pushs operation to {@code IStack}.
	 * 
	 * In case there are 2 operations one after another 
	 * second one will replace first.
	 * 
	 * @param screen		screen where numbers are shown.
	 * @param stack			stack where numbers and operations are pushed.
	 * @param operation 	operation to be pushed on stack.
	 * @return new {@code ActionListener} with described properties.
	 */
	private static ActionListener doubleNumberOperation(IScreen screen, IStack stack, BiFunction<Double,Double,Double> operation) {
		return e -> {
			if(!stack.isEmpty() && screen.isEmpty()) { //this would happen if 2 operations in row
				stack.pop();
			} else {
				stack.push(screen.read());
			}
			if(stack.size() > 2) {
				String result = calculate(stack);
				screen.show(result);
				stack.push(result);
			}
			stack.push(operation);
			screen.clear();
		};
	}
	
	/**
	 * Pops last 3 elements from stack and calculates is.
	 * If stack is [1, +, 2], where 2 is top of the stack,
	 * calculation would be 1+2 and returned value would be "3".
	 * 
	 * @param stack		where are numbers and operations.
	 * @return result of calculation.
	 */
	private static String calculate(IStack stack) {
		double second = Double.parseDouble(stack.pop().toString());
		@SuppressWarnings("unchecked")
		BiFunction<Double, Double, Double> operation = (BiFunction<Double, Double, Double>) stack.pop();
		double first = Double.parseDouble(stack.pop().toString());
		
		Double result = operation.apply(first, second);
		
		String toBeWritten = String.valueOf(result);
		int pointIndex = toBeWritten.indexOf(".");
		if(pointIndex != -1) {
			if(toBeWritten.substring(pointIndex).equals(".0")) {
				toBeWritten = String.valueOf(result.intValue());
			}
		}
		return toBeWritten;
	}
	
	/**
	 * Creates new {@code JButton} with given {@code text},
	 * given {@code listener} and style from {@link Style}.
	 * 
	 * @param text			text on new button.
	 * @param listener		action listener of new button.
	 * @return new {@code JButton} with described properties.
	 */
	private static JButton createButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.setOpaque(true);
		button.setBackground(Style.BUTTONS_BACKGROUND);
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setBorder(Style.BORDER);
		button.setFont(Style.BUTTONS_FONT);
		button.setForeground(Color.BLACK);
		button.addActionListener(listener);
		
		return button;
	}
}
