package hr.fer.zemris.java.custom.scripting.elems;

/**
 * This interface defines methods 
 * that are used once object that 
 * is an instance of specific
 * element class is visited.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (31.5.2016.)
 */
public interface IElementVisitor {
	
	/**
	 * What will be done once instance of 
	 * {@link ElementConstantDouble}, 
	 * {@link ElementConstantInteger} or 
	 * {@link ElementString} is visited.
	 * 
	 * @param constant		constant element being visited.
	 */
	void visitConstant(Element constant);

	/**
	 * What will be done once instance 
	 * of {@link ElementVariable} is visited.
	 * 
	 * @param variable		{@link ElementVariable} being visited.
	 */
	void visitVariable(ElementVariable variable);
	
	/**
	 * What will be done once instance 
	 * of {@link ElementOperator} is visited.
	 * 
	 * @param operator		{@link ElementOperator} being visited.
	 */
	void visitOperator(ElementOperator operator);
	
	/**
	 * What will be done once instance 
	 * of {@link ElementFunction} is visited.
	 * 
	 * @param function		{@link ElementFunction} being visited.
	 */
	void visitFunction(ElementFunction function);
}
