package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.elems.IElementVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class used to execute smart script nodes.
 * Instance of this class requires {@link DocumentNode}
 * and {@link RequestContext}.
 * Once {@link #execute()} is called ending will
 * execute every node starting from given document node.
 * <p>Supported operations are: '+', '-', '*' and '/'.</p>
 * <p>Supported functions are:</p>
 * <ul>
 * 	<li>sin(x) - sine, x is in degrees</li>
 * 	<li>decfmt(x,f) - returns string, x is representation of number, f is string used to format</li>
 * 	<li>dup() - duplicates current top value from stack</li>
 * 	<li>swap() - replaces the order of two topmost items on stack</li>
 *  <li>setMimeType(x) - takes string x and calls requestContext.setMimeType(x)</li>
 *  <li>paramGet(name, defValue) - pushes to stack param value with name to stack, or defValue if no such param name</li>
 *  <li>pparamGet(name, defValue) - same as paramGet but persistent parameter</li>
 *  <li>tparamGet(name, defValue) - same as paramGet temporary parameter </li>
 *  <li>pparamSet(value, name) - stores a value into persistent parameter name</li>
 *  <li>tparamSet(value, name) - stores a value into temporary parameter name</li>
 *  <li>pparamDel(name) - removes association for name (persistent parameter)</li>
 *  <li>tparamDel(name) - removes association for name (temporary parameter)</li>
 * </ul>
 * 
 * @author Ivica Brebrek
 * @version 1.0  (31.5.2016.)
 */
public class SmartScriptEngine {

	/** Node where visiting will start. */
	private DocumentNode documentNode;
	/** Context in which this is executed. */
	private RequestContext requestContext;
	/** Stack used to store some elements. Mostly variables. */
	private ObjectMultistack multistack = new ObjectMultistack();
	
	/**
	 * Initializes this smart script engine.
	 * It requires document node, from where
	 * node visiting will start, and request
	 * context that will be used.
	 * 
	 * @param documentNode			starting node.
	 * @param requestContext		context.
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		if(documentNode == null || requestContext == null) {
			throw new IllegalArgumentException("Document node and request context must not be null.");
		}
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Executes this smart script engine.
	 * In other words, starts visiting all
	 * nodes starting from given document node.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
	
	/**
	 * Visitor used to visit nodes.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				throw new RuntimeException("Can not write to output stream.");
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			Object end = node.getEndExpression().asText();
			Object step = node.getStepExpression() == null ?
					1 : node.getStepExpression().asText();
			String variable = node.getVariable().asText();
			
			multistack.push(
					variable, 
					new ValueWrapper(node.getStartExpression().asText())
			);
			
			while(multistack.peek(variable).numCompare(end) <= 0) {
				iterate(node);
				multistack.peek(variable).increment(step);
			}
			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			ObjectStack temp = new ObjectStack();
			ElementVisitor elementVisitor = new ElementVisitor(temp);
			
			for(Element element : node.getElements()) {
				element.accept(elementVisitor);
			}
			
			ObjectStack temp2 = new ObjectStack();
			for(int i = temp.size(); i > 0; i--) {
				temp2.push(temp.pop());
			}
			try {
				for (int i = temp2.size(); i > 0; i--) {
					requestContext.write(String.valueOf(temp2.pop()));
				}
			} catch (IOException e) {
				throw new RuntimeException("Can not write to output stream.");
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			iterate(node);
		}
		
		private void iterate(Node node) {
			for(int i = 0, max = node.numberOfChildren(); i < max; i++) {
				node.getChild(i).accept(this);
			}
		}
	};
	
	/**
	 * Class used to visit elements.
	 * Class has single field {@link #stack}
	 * which will have results from visiting elements.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (31.5.2016.)
	 */
	private class ElementVisitor implements IElementVisitor {
		/** Stack used to store some elements/results. */
		private final ObjectStack stack;

		/**
		 * Initialize new element visitor.
		 * 
		 * @param stack		stack to store some elements/results.
		 */
		public ElementVisitor(ObjectStack stack) {
			this.stack = stack;
		}

		@Override
		public void visitVariable(ElementVariable variable) {
			try {
				stack.push(multistack.peek(variable.asText()).getValue());
			} catch(EmptyStackException exc) {
				throw new RuntimeException(variable.asText() + " is undefined.");
			}
		}
		
		@Override
		public void visitConstant(Element constant) {
			stack.push(constant.getValue());
		}
		
		@Override
		public void visitOperator(ElementOperator operator) {
			Object second = stack.pop();
			ValueWrapper first = new ValueWrapper(stack.pop());
			
			switch (operator.asText()) {
			case "+":
				first.increment(second);
				break;
			case "-":
				first.decrement(second);
				break;
			case "*":
				first.multiply(second);
				break;
			case "/":
				first.divide(second);
				break;
			default:
				throw new RuntimeException(operator.asText() + " is unsupported operator.");
			}
			stack.push(first.getValue()); //because first will change its value, so it is result
		}
		
		@Override
		public void visitFunction(ElementFunction function) {
			switch ((String)function.getValue()) {
			case "sin":
				ValueWrapper wrapper = new ValueWrapper(stack.pop());
				wrapper.increment(0.0); //to make it be double
				stack.push(Math.sin(Math.toRadians((double)wrapper.getValue())));
				break;

			case "decfmt":
				DecimalFormat formatter = new DecimalFormat(String.valueOf(stack.pop()));
				Object number = stack.pop();
				String formatted = formatter.format(number);
				stack.push(formatted);
				break;
				
			case "dup":
				stack.push(stack.peek());
				break;
				
			case "swap":
				Object a = stack.pop();
				Object b = stack.pop();
				stack.push(a);
				stack.push(b);
				break;
				
			case "setMimeType":
				requestContext.setMimeType(String.valueOf(stack.pop()));
				break;
				
			case "paramGet":
				String defValue = String.valueOf(stack.pop());
				String paramName = String.valueOf(stack.pop());
				String actualValue = requestContext.getParameter(paramName);
				stack.push(actualValue == null ? defValue : actualValue);
				break;
			
			case "pparamGet":
				String pdefValue = String.valueOf(stack.pop());
				String pparamName = String.valueOf(stack.pop());
				String pactualValue = requestContext.getPersistentParameter(pparamName);
				stack.push(pactualValue == null ? pdefValue : pactualValue);
				break;
			
			case "pparamSet":
				String ppName = String.valueOf(stack.pop());
				String ppValue = String.valueOf(stack.pop());
				requestContext.setPersistentParameter(ppName, ppValue);
				break;
				
			case "pparamDel":
				String ppDelName = String.valueOf(stack.pop());
				requestContext.removePersistentParameter(ppDelName);
				break;
			
			case "tparamGet":
				String tdefValue = String.valueOf(stack.pop());
				String tparamName = String.valueOf(stack.pop());
				String tactualValue = requestContext.getTemporaryParameter(tparamName);
				stack.push(tactualValue == null ? tdefValue : tactualValue);
				break;
			
			case "tparamSet":
				String tName = String.valueOf(stack.pop());
				String tValue = String.valueOf(stack.pop());
				requestContext.setTemporaryParameter(tName, tValue);
				break;
			
			case "tparamDel":
				String tDelName = String.valueOf(stack.pop());
				requestContext.removeTemporaryParameter(tDelName);
				break;
			
			default:
				throw new RuntimeException(function.getValue() + " is unsupported function.");
			}
		}
	};
}
