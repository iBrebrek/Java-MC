package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * Every object of this class has multiple stacks.
 * Each stack is stored under one String key.
 * Null values are allowed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (17.4.2016.)
 */
public class ObjectMultistack {
	/** Stacks stored by a String key. */
	private Map<String, MultistackEntry> stackMap = new HashMap<>();
	
	/**
	 * On top of the stack {@code name} puts value {@code ValueWrapper}.
	 * 
	 * @param name				name of a stack.
	 * @param valueWrapper		value that will be on the top of the stack.
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		MultistackEntry topEntry = stackMap.get(name);
		MultistackEntry newEntry = new MultistackEntry(valueWrapper, topEntry);
		stackMap.put(name, newEntry);
	}
	
	/**
	 * Removes top value of a stack {@code name}, and returns that value.
	 *  
	 * @param name		name of the stack.
	 * @return value from top of the stack {@code name}.
	 * 
	 * @throws EmptyStackException if stack {@code name} is empty.
	 */
	public ValueWrapper pop(String name) {
		MultistackEntry topEntry = getTop(name);
		if(topEntry.next == null) { 
			stackMap.remove(name);
		} else {
			stackMap.put(name, topEntry.next);
		}
		return topEntry.value;
	}
	
	/**
	 * Returns top value of a stack {@code name}.
	 *  
	 * @param name		name of the stack.
	 * @return value from top of the stack {@code name}.
	 * 
	 * @throws EmptyStackException if stack {@code name} is empty.
	 */
	public ValueWrapper peek(String name) {
		return getTop(name).value;
	}
	
	/**
	 * Checks if stack {@code name} is empty.
	 * 
	 * @param name		name of the stack.
	 * @return {@code true} if that stack is empty.
	 */
	public boolean isEmpty(String name) {
		MultistackEntry topEntry = stackMap.get(name);
		return topEntry == null;
	}
	
	/**
	 * Returns top {@code MultistackEntry} from the stack {@code name}.
	 * 
	 * @param name		name of the stack.
	 * @return entry on top of the stack.
	 * 
	 * @throws EmptyStackException if stack {@code name} is empty.
	 */
	private MultistackEntry getTop(String name) {
		MultistackEntry topEntry = stackMap.get(name);
		if(topEntry == null) {
			throw new EmptyStackException(name + " is an empty stack.");
		}
		return topEntry;
	}
	
	/**
	 * Single node used for a single stack in {@code ObjectMultistack}.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (17.4.2016.)
	 */
	private static class MultistackEntry {
		/** Value stored in this node. */
		private ValueWrapper value;
		/** Next node. */
		private MultistackEntry next;
		
		/**
		 * Initializes node's value and next node.
		 * Set next to null if there is no next node.
		 * 
		 * @param value		node's value.
		 * @param next		next node.
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = value;
			this.next = next;
		}
	}
}
