package hr.fer.zemris.java.tecaj.hw5.collections;

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable.TableEntry;

@SuppressWarnings("javadoc")
public class SimpleHashtableTest {
	
	SimpleHashtable<Integer, String> map;
	
	//this is done before every @test
	@Before
	public void makeMap() {
		map = new SimpleHashtable<>(3);
		map.put(1, "a");
		map.put(2, "b");
		map.put(3, "c");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidSizeInConstructor() {
		new SimpleHashtable<>(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPutNullKey() {
		new SimpleHashtable<>().put(null, new Object());
	}
	
	@Test
	public void testPutExisting() {
		int size = map.size();
		map.put(1, "aa");
		map.put(1, "bb");
		
		assertEquals("bb", map.get(1));
		assertEquals(size, map.size());
	}
	
	@Test
	public void testPutNullValue() {
		int size = map.size();
		map.put(11, null);
		
		assertEquals(size + 1, map.size());
	}
	
	@Test
	public void testGetNotExists() {
		
		assertNull(map.get(new Integer(55)));
	}
	
	@Test
	public void testGetExisting() {
		
		String test = "test55";
		map.put(1, test);
		
		assertEquals("Should be the same", test, map.get(1));
	}
	
	@Test
	public void testRemove() {
		
		map.put(17, "");
		int size = map.size();
		map.remove(17); //removes
		map.remove(null); //does nothing
		map.remove(8888); //does nothing
		
		assertEquals("Size should decrease by 1", size-1, map.size());
	}
	
	@Test
	public void testContainsKey() {
		
		assertFalse(map.containsKey(null));
		assertFalse(map.containsKey(new String("3")));
		map.put(5, "1");
		assertTrue(map.containsKey(5));
		map.remove(5);
		assertFalse(map.containsKey(5));
	}
	
	@Test
	public void testContainsValue() {
		assertTrue(map.containsValue("a"));
		map.put(1, null); //null overwrites a
		assertTrue(map.containsValue(null));
		assertFalse(map.containsValue("a"));
	}
	
	@Test
	public void testToString() {
		assertEquals("[1=a, 2=b, 3=c]", map.toString());
		
		SimpleHashtable<Long, Double> empty = new SimpleHashtable<>();
		assertEquals("[]", empty.toString());
	}
	
	@Test
	public void testClear() {
		map.clear();
		assertTrue(map.isEmpty());
	}
	
	@Test
	public void testIteration() {
		//in map we have 1=a, 2=b, 3=c
		String expected = "aaabacbabbbccacbcc";
		String result = "";
		for(TableEntry<Integer, String> first : map) {
			for(TableEntry<Integer, String> second : map) {
				result += first.getValue() + second.getValue();
			}
		}
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testNextNextNext() {
		int size = 0;
		try {
			Iterator<TableEntry<Integer, String>> iterator = map.iterator();
			while(true) {
				iterator.next();
				size++;
			}
		} catch (NoSuchElementException ignore) {
			//now we also test if this will be thrown
		}
		
		assertEquals(map.size(), size);
	}
	
	@Test
	public void testRemoveOK() {
		int expectedSize = map.size() - 1;
		
		Iterator<TableEntry<Integer, String>> iterator = map.iterator();
		while(iterator.hasNext()) {
			SimpleHashtable.TableEntry<Integer, String> pair = iterator.next();
			if(pair.getKey().equals(1)) {
				iterator.remove(); 
			}
		}
		
		assertEquals(expectedSize, map.size());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testRemoveRemove() {
		Iterator<TableEntry<Integer, String>> iterator = map.iterator();
		while(iterator.hasNext()) {
			SimpleHashtable.TableEntry<Integer, String> pair = iterator.next();
			if(pair.getKey().equals(1)) {
				iterator.remove(); 
				iterator.remove(); 
			}
		}
	}
	
	@Test(expected = ConcurrentModificationException.class)
	public void testIteratorRemoveOutside() {
		Iterator<TableEntry<Integer, String>> iterator = map.iterator();
		while(iterator.hasNext()) {
			SimpleHashtable.TableEntry<Integer, String> pair = iterator.next();
			if(pair.getKey().equals(1)) {
				map.remove(1);
			}
		}
	}
	
}
