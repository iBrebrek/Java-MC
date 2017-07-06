package hr.fer.zemris.java.simplecomp.impl.instructions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import hr.fer.zemris.java.simplecomp.impl.ComputerImpl;
import hr.fer.zemris.java.simplecomp.impl.instructions.InstrLoad;
import hr.fer.zemris.java.simplecomp.impl.instructions.Creator.Type;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
@RunWith(MockitoJUnitRunner.class)
public class LoadTests {
	
	@Test
	public void testLoadVerifyTimes() {
		Computer computerMock = mock(Computer.class);
		Registers registersMock = mock(Registers.class);
		Memory memoryMock = mock(Memory.class);	
		when(computerMock.getMemory()).thenReturn(memoryMock);
		when(computerMock.getRegisters()).thenReturn(registersMock);
		
		when(memoryMock.getLocation(10)).thenReturn("TestLoad");
		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 1));	// register 1
		list.add(Creator.createArgument(Type.NUMBER, 10));		// address 10
		
		InstrLoad load = new InstrLoad(list);	//no exceptions should happen
		
		load.execute(computerMock);
		
		verify(computerMock, atLeast(1)).getMemory();
		verify(computerMock, atLeast(1)).getRegisters();
		
		verify(memoryMock, times(1)).getLocation(10);
		verify(registersMock, times(1)).setRegisterValue(1, "TestLoad");
	}
	
	@Test
	public void testLoadActualValue() {
		Computer computer = new ComputerImpl(20, 5);	
		computer.getMemory().setLocation(10, "TestLoad");
		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 1));	// register 1
		list.add(Creator.createArgument(Type.NUMBER, 10));		// address 10
		
		InstrLoad load = new InstrLoad(list);	//no exceptions should happen
		
		assertFalse(load.execute(computer));	//executing load should return false
		
		String actual = (String)computer.getRegisters().getRegisterValue(1);
		assertEquals("TestLoad", actual);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLoadTooManyArguments() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 1));
		list.add(Creator.createArgument(Type.NUMBER, 1));	
		list.add(Creator.createArgument(Type.NUMBER, 1));	
		
		new InstrLoad(list);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLoadFirstArgIndirectAddressing() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, ((1 << 24) | 1)));	// register 1, indirect, offset 0
		list.add(Creator.createArgument(Type.NUMBER, 1));	
		
		new InstrLoad(list);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLoadFirstArgNumber() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.NUMBER, 1));	
		list.add(Creator.createArgument(Type.NUMBER, 1));	
		
		new InstrLoad(list);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLoadFirstArgString() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.STRING, "1"));
		list.add(Creator.createArgument(Type.NUMBER, 1));
		
		new InstrLoad(list);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLoadSecondArgString() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 1));
		list.add(Creator.createArgument(Type.STRING, "1"));	
		
		new InstrLoad(list);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLoadSecondArgRegister() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 1));	
		list.add(Creator.createArgument(Type.REGISTER, 1));
		
		new InstrLoad(list);
	}
}
