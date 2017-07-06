package hr.fer.zemris.java.tecaj.hw5.collections.demo;


import java.util.ConcurrentModificationException;
import java.util.Iterator;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;

/**
 * All given examples for task 3 are copy/pasted here, with slight upgrades.
 */
public class SecondTest {

	/**
	 * Entry point for this program.
	 * 
	 * @param args  not used.
	 */
	public static void main(String[] args) {
		// create collection:
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		for(SimpleHashtable.TableEntry<String,Integer> pair : examMarks) {
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		}
		
		
		for(SimpleHashtable.TableEntry<String,Integer> pair1 : examMarks) {
			for(SimpleHashtable.TableEntry<String,Integer> pair2 : examMarks) {
				System.out.printf(
						"(%s => %d) - (%s => %d)%n",
						pair1.getKey(), pair1.getValue(),
						pair2.getKey(), pair2.getValue()
				);
			}
		}
		
		
		
		
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			if(pair.getKey().equals("Ivana")) {
					iter.remove(); // this is OK
			}
		}
		
		
		
		try {
			Iterator<SimpleHashtable.TableEntry<String,Integer>> iter2 = examMarks.iterator();
			while(iter2.hasNext()) {
				SimpleHashtable.TableEntry<String,Integer> pair = iter2.next();
				if(pair.getKey().equals("Ante")) {
					iter2.remove();
					iter2.remove();
				}
			}
		} catch(IllegalStateException exc) {
			System.err.println("\nIllegal State Exception\n");
		}
		
		
		
		
		try {
			Iterator<SimpleHashtable.TableEntry<String,Integer>> iter3 = examMarks.iterator();
			while(iter3.hasNext()) {
				SimpleHashtable.TableEntry<String,Integer> pair = iter3.next();
				if(pair.getKey().equals("Jasna")) {
					examMarks.remove("Jasna");
				}
			}
		} catch(ConcurrentModificationException exc) {
			System.err.println("\nConcurrent Modification Exception\n");
		}
	}
}
