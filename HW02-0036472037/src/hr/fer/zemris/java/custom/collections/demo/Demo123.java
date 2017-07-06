package hr.fer.zemris.java.custom.collections.demo;

import java.util.Arrays;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.Processor;

/**
 * Demonstration for first 3 tasks
 * (Collection, LinkedListIndexedCollection and ArrayIndexedCollection).
 * 
 * Modified version of CollectionsDemo.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (21.3.2016.)
 */
public class Demo123 {

	/**
	 * Starting method for this program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		ArrayIndexedCollection col = new ArrayIndexedCollection(2);
		col.add(new Integer(20));
		col.add("New York");
		col.add("Šan Franšćiđžo"); 
		System.out.println(col.contains("New York")); // writes: true
		col.remove(1); // removes "New York"; shifts "San Francisco" to position 1
		System.out.println(col.get(1)); // writes: "San Francisco"
		System.out.println(col.size()); // writes: 2
		col.add("Los Angeles");
		col.insert(0, 0);
		col.insert("Last", col.size());
		
		for(int i = 0; i<10; i++) {
			col.insert(i, col.size()-1);
		}
		
		LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(col);
		col.insert("First", 0);
		col.insert("Third", 2);
		col2.insert("First", 0);
		col2.insert("Third", 2);
		
		class P extends Processor {
			public void process(Object o) {
				System.out.println(o);
			}
		};
		
		System.out.println("col1 elements:");
		col.forEach(new P());
		
		System.out.println("col1 elements again:");
		System.out.println(Arrays.toString(col.toArray()));
		
		System.out.println("col2 elements:");
		col2.forEach(new P());
		
		System.out.println("col2 elements again:");
		System.out.println(Arrays.toString(col2.toArray()));
		
		System.out.println(col.contains(col2.get(4))); // true
		System.out.println(col2.contains(col.get(4))); // true
		
		// removes 20 from collection (at position 0).
		System.out.println("\n"+col.remove(new Integer(20)));  //true
		System.out.println(col2.remove(new Integer(20))); //true
		System.out.println(col.remove(new Integer(20))); //false
		System.out.println(col2.remove(new Integer(20))); //false
		System.out.println(col.size()); //16
		System.out.println(col2.size()); //16
		
		System.out.println(Arrays.toString(col.toArray()));
		System.out.println(Arrays.toString(col2.toArray()));
		System.out.println("Index of 0: " + col.indexOf(0));
		System.out.println("Index of 0: " + col2.indexOf(0));
		
		col.clear();
		col2.clear();
		System.out.println(col.size()); //0
		System.out.println(col.size()); //0
		col.forEach(new P());
		col2.forEach(new P());
		System.out.println(Arrays.toString(col.toArray()));
		System.out.println(Arrays.toString(col2.toArray()));
	}

}
