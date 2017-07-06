package hr.fer.zemris.java.simplecomp.impl.instructions;

import org.junit.Test;
import org.junit.runner.RunWith;
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
public class MoveTests {
	
	private Computer createMockComputer() {
		Computer computerMock = mock(Computer.class);
		Registers registersMock = mock(Registers.class);
		Memory memoryMock = mock(Memory.class);	
		when(computerMock.getMemory()).thenReturn(memoryMock);
		when(computerMock.getRegisters()).thenReturn(registersMock);
		
		when(memoryMock.getLocation(20)).thenReturn("Offset");
		when(registersMock.getRegisterValue(0)).thenReturn("Register");
		when(registersMock.getRegisterValue(1)).thenReturn(20); //for offset
		when(registersMock.getRegisterValue(2)).thenReturn(40); //for offset
		
		return computerMock;
	}
	
	@Test
	public void testMoveNumberToRegister() {
		Computer computerMock = createMockComputer();
		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 1));
		list.add(Creator.createArgument(Type.NUMBER, 10));
		
		InstrMove move = new InstrMove(list);	//no exceptions should happen
		
		assertFalse(move.execute(computerMock));	//executing move should return false
		
		verify(computerMock, times(0)).getMemory();		//should not touch memory
		
		verify(computerMock.getRegisters(), times(1)).setRegisterValue(1, 10);
	}
	
	@Test
	public void testMoveNumberToIndirectAddress() {
		Computer computerMock = createMockComputer();
		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, ((1 << 24) | 1)));	// register 1, indirect, offset 0
		list.add(Creator.createArgument(Type.NUMBER, 10));
		
		InstrMove move = new InstrMove(list);	//no exceptions should happen
		
		move.execute(computerMock);
		
		verify(computerMock.getRegisters(), times(1)).getRegisterValue(1);
		
		verify(computerMock.getRegisters(), times(0)).setRegisterValue(1, 10); // should not change register
		verify(computerMock.getMemory(), times(1)).setLocation(20, 10);  //value in r1 is 20, so put number 10 on address 20
	}
	
	@Test
	public void testMoveRegisterToRegister() {
		Computer computerMock = createMockComputer();
		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 1));
		list.add(Creator.createArgument(Type.REGISTER, 0));
		
		InstrMove move = new InstrMove(list);	//no exceptions should happen
		
		move.execute(computerMock);
		
		verify(computerMock, times(0)).getMemory();			//should not touch memory
		
		verify(computerMock.getRegisters(), times(1)).getRegisterValue(0);
		verify(computerMock.getRegisters(), times(1)).setRegisterValue(1, "Register");
	}
	
	@Test
	public void testMoveRegisterToIndirectAddress() {
		Computer computerMock = createMockComputer();
		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, ((1 << 24) | 1)));	// register 1, indirect, offset 0
		list.add(Creator.createArgument(Type.REGISTER, 0));
		
		InstrMove move = new InstrMove(list);	//no exceptions should happen
		
		move.execute(computerMock);
				
		verify(computerMock.getRegisters(), times(1)).getRegisterValue(1);
		verify(computerMock.getRegisters(), times(1)).getRegisterValue(0);
		
		verify(computerMock.getRegisters(), times(0)).setRegisterValue(1, "Register"); // should not change register 1
		verify(computerMock.getMemory(), times(1)).setLocation(20, "Register");  //value in r0 is "Register", so put it on address 20
	}
	
	@Test
	public void testMoveIndirectAddressToRegister() {
		Computer computerMock = createMockComputer();
		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 0));
		list.add(Creator.createArgument(Type.REGISTER, ((1 << 24) | 1)));	// register 1, indirect, offset 0
		
		InstrMove move = new InstrMove(list);	//no exceptions should happen
		
		move.execute(computerMock);
				
		verify(computerMock.getRegisters(), times(1)).getRegisterValue(1);
		verify(computerMock.getMemory(), times(1)).getLocation(20);
		
		verify(computerMock.getRegisters(), times(1)).setRegisterValue(0, "Offset"); //from address 20 to r0
	}
	
	@Test
	public void testMoveIndirectAddressToIndirectAddress() {
		Computer computerMock = createMockComputer();
		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, ((1 << 24) | 2)));	// register 2, indirect, offset 0
		list.add(Creator.createArgument(Type.REGISTER, ((1 << 24) | 1)));	// register 1, indirect, offset 0
		
		InstrMove move = new InstrMove(list);	//no exceptions should happen
		
		move.execute(computerMock);
				
		verify(computerMock.getRegisters(), times(1)).getRegisterValue(1);
		verify(computerMock.getRegisters(), times(1)).getRegisterValue(2);
		verify(computerMock.getMemory(), times(1)).getLocation(20);
		
		verify(computerMock.getRegisters(), times(0)).setRegisterValue(2, "Offset"); //don't change register
		verify(computerMock.getMemory(), times(1)).setLocation(40, "Offset"); //from address 20 to address 40
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveTooManyArguments() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 1));
		list.add(Creator.createArgument(Type.REGISTER, 1));	
		list.add(Creator.createArgument(Type.REGISTER, 1));	
		
		new InstrMove(list);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveStringToRegister() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.REGISTER, 1));
		list.add(Creator.createArgument(Type.STRING, "..."));
		
		new InstrMove(list);	//should throw because move does not support strings
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveRegisterToNumber() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.NUMBER, 1));
		list.add(Creator.createArgument(Type.REGISTER, 0));
		
		new InstrMove(list);	//should throw because first must be register or indirect address
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMoveRegisterToString() {		
		List<InstructionArgument> list = new ArrayList<>();
		list.add(Creator.createArgument(Type.STRING, "1"));
		list.add(Creator.createArgument(Type.REGISTER, 0));
		
		new InstrMove(list);	//should throw because first must be register or indirect address
	}
}
