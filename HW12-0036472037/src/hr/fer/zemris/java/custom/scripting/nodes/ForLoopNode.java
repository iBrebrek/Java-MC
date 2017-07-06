package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * A node representing a single for-loop construct.
 * 
 * For loop is written in tags {$ FOR variable start end step $} 
 * and it has to be closed with {$END$}.
 * 
 * Variable must be an instance of ElementVariable, while other 3 can instance of any Element.
 * Only step can be null.
 * If the above conditions are not met for loop is invalid, 
 * therefore, it can not be created.
 * 
 * Once elements are initializes they can not be changed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class ForLoopNode extends Node {
	
	/** Variable used in for loop */
	private final ElementVariable variable;
	
	/** Starting value of variable */
	private final Element startExpression;
	
	/** Last value of variable */
	private final Element endExpression;
	
	/** Value of steps from first to last variable value */
	private final Element stepExpression;

	/**
	 * Initializes all node's properties.
	 * All properties are read-only.
	 * Only step expression can be null,
	 * if any other is null IllegalArgumentException will be thrown.
	 * 
	 * Variable must be instance of ElementVariable.
	 * Any non-null element must be number, variable or string.
	 * 
	 * @param variable			variable in for loop.
	 * @param startExpression	first value of variable.
	 * @param endExpression		last value of variable.
	 * @param stepExpression	increments of variable.
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, 
					Element endExpression,Element stepExpression) {
		if(variable == null || startExpression == null || endExpression == null) {
			throw new IllegalArgumentException("Only step expression can be null.");
		}

		if(!isValid(startExpression)) {
			throw new IllegalArgumentException
				("Start expression in for loop must be string, number or variable.");
		}
		if(!isValid(endExpression)) {
			throw new IllegalArgumentException
				("End expression in for loop must be string, number or variable.");
		}
		if(stepExpression != null && !isValid(stepExpression)) {
			throw new IllegalArgumentException
				("Step expression in for loop must be string, number, variable or undefined(null)");
		}
		
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}
	
	/**
	 * Checks if element is variable, string, integer or decimal.
	 * 
	 * @param element element being checked.
	 * @return true if element is instance of correct class.
	 */
	private boolean isValid(Element element) {
		return element instanceof ElementVariable 
				|| element instanceof ElementString
				|| element instanceof ElementConstantInteger
				|| element instanceof ElementConstantDouble;
	}

	/**
	 * Returns for loop variable.
	 * 
	 * @return variable used in for loop.
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Returns starting expression of variable.
	 * 
	 * @return first value of variable.
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Returns ending expression of variable.
	 * 
	 * @return last value of variable.
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Returns steps of variable in for loop,
	 * or null if there are no steps.
	 * 
	 * @return steps of for loop.
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{$");
		sb.append(" FOR ")
			.append(variable.asText()).append(" ")
			.append(startExpression.asText()).append(" ")
			.append(endExpression.asText()).append(" ");
		
		if(stepExpression != null) {
			sb.append(stepExpression.asText()).append(" ");
		}
		sb.append("$}");
		
		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
}
