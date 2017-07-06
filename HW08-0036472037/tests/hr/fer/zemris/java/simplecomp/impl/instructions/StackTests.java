package hr.fer.zemris.java.simplecomp.impl.instructions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import hr.fer.zemris.java.simplecomp.impl.instructions.Creator.Type;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
@RunWith(MockitoJUnitRunner.class)
public class StackTests {
	
	@Mock
	private Computer computer;
	
	@Before
	public void createMockComputer() {
		Registers registersMock = mock(Registers.class);
		Memory memoryMock = mock(Memory.class);	
		when(computer.getMemory()).thenReturn(memoryMock);
		when(computer.getRegisters()).thenReturn(registersMock);
		
		when(registersMock.getProgramCounter()).thenReturn(1);
		when(registersMock.getRegisterValue(0)).thenReturn("r0");
		when(registersMock.getRegisterValue(Registers.STACK_REGISTER_INDEX)).thenReturn(100); //stack location
		when(memoryMock.getLocation(100)).thenReturn(null); //current pointer on top of the stack (nothing there)
		when(memoryMock.getLocation(101)).thenReturn(55); //actual top of the stack
	}
	
	@Test
	public void testPush() {
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 0));
		
		InstrPush push = new InstrPush(list);
		
		assertFalse(push.execute(computer)); 	//executing push should return false
		
		verify(computer.getRegisters(), times(1)).getRegisterValue(Registers.STACK_REGISTER_INDEX); //find top of the stack
		verify(computer.getRegisters(), times(1)).getRegisterValue(0); 	//take value from r0
		verify(computer.getMemory(), times(1)).setLocation(100, "r0");	//put value from r0 on top of the stack
		verify(computer.getRegisters(), times(1)).setRegisterValue(Registers.STACK_REGISTER_INDEX, 99);	//change stack head
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPushIndirectAddressing() {
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, ((1 << 24) | 1)));	// register 1, indirect, offset 0
		
		 new InstrPush(list);
	}
	
	@Test
	public void testPop() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 0));
		
		InstrPop pop = new InstrPop(list);
		
		assertFalse(pop.execute(computer)); 	//executing pop should return false
		
		verify(computer.getRegisters(), times(1)).getRegisterValue(Registers.STACK_REGISTER_INDEX); //find top of the stack
		verify(computer.getMemory(), times(1)).getLocation(101);	//get what's on top of the stack
		verify(computer.getRegisters(), times(1)).setRegisterValue(0, 55);	//store what's on top in r0
		verify(computer.getRegisters(), times(1)).setRegisterValue(Registers.STACK_REGISTER_INDEX, 101);	//change stack head
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPopNoArguments() {
		List<InstructionArgument> list = new ArrayList<>();
		
		 new InstrPop(list);
	}
	
	@Test
	public void testCall() {
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.NUMBER, 500));
		
		InstrCall call = new InstrCall(list);
		
		assertFalse(call.execute(computer)); 	//executing call should return false
		
		verify(computer.getRegisters(), times(1)).getRegisterValue(Registers.STACK_REGISTER_INDEX); //find top of the stack
		verify(computer.getRegisters(), times(1)).getProgramCounter(); //get current PC
		verify(computer.getMemory(), times(1)).setLocation(100, 1);	//store current PC on stack
		verify(computer.getRegisters(), times(1)).setRegisterValue(Registers.STACK_REGISTER_INDEX, 99);	//change stack head
		verify(computer.getRegisters(), times(1)).setProgramCounter(500);	//change PC
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCallRegister() {
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 500));
		
		new InstrCall(list);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCallText() {
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.STRING, 500));
		
		new InstrCall(list);
	}
	
	@Test
	public void testRet() {
		List<InstructionArgument> list = new ArrayList<>();
		
		InstrRet call = new InstrRet(list);
		
		assertFalse(call.execute(computer)); 	//executing call should return false
		
		verify(computer.getRegisters(), times(1)).getRegisterValue(Registers.STACK_REGISTER_INDEX); //find top of the stack
		verify(computer.getMemory(), times(1)).getLocation(101); //get address from top of the stack
		verify(computer.getRegisters(), times(1)).setRegisterValue(Registers.STACK_REGISTER_INDEX, 101); //change stack head
		verify(computer.getRegisters(), times(1)).setProgramCounter(55); //change PC to address from stack
	}
	
	@Test(expected = RuntimeException.class)
	public void testRetText() {
		List<InstructionArgument> list = new ArrayList<>();
		
		InstrRet call = new InstrRet(list);
		
		when(computer.getMemory().getLocation(101)).thenReturn("text");		//change stack after creating instruction
		
		call.execute(computer);	//should throw because on top of the stack is text, text can't be PC
	}
}
