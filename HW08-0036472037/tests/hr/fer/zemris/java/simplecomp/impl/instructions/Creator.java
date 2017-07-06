package hr.fer.zemris.java.simplecomp.impl.instructions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Class used to create {@code InstructionArgument}s for mockito tests.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (4.5.2016.)
 */
class Creator {
	
	/**
	 * Possible types of {@code InstructionArgument}.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (4.5.2016.)
	 */
	enum Type {
		/** Argument is register. */
		REGISTER, 
		/** Argument is text. */
		STRING, 
		/** Argument is number (this is also memory location). */
		NUMBER
	}
	
	/**
	 * Creates mock up of {@code InstructionArgument} with given type and value.
	 * 
	 * @param type		type of argument.
	 * @param value		value stored in argument.
	 * @return new mock up argument.
	 */
	static InstructionArgument createArgument(Type type, Object value) {
		InstructionArgument argument = mock(InstructionArgument.class);
		when(argument.isRegister()).thenReturn(type == Type.REGISTER);
		when(argument.isString()).thenReturn(type == Type.STRING);
		when(argument.isNumber()).thenReturn(type == Type.NUMBER);
		when(argument.getValue()).thenReturn(value);
		return argument;
	}
}
