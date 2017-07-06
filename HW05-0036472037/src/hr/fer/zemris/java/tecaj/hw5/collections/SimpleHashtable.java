package hr.fer.zemris.java.tecaj.hw5.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;


/**
 * This hash map lets you store values and keys as pairs.
 * Keys can not be null, wile values can be null.
 * 
 * @param <K> 	type of keys.
 * @param <V> 	type of values.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (9.4.2016.)

 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K,V>> {
	
	/** Default size for table TableEntry<K,V> */
	private static final int DEFAULT_TABLE_SIZE = 16;
	
	/** When this capacity is reached number of slots is increased. */
	private static final double MAX_CAPACITY = 0.75;
	
	/** When MAX_CAPACITY is reached, number of slots is multiplied by this. */
	private static final int SLOT_MULTIPLIER = 2;
	
	/** Slots of hash table. */
	private TableEntry<K,V>[] table;
	
	/** Total number of pairs in this hash table. */
	private int size;
	
	/** Counter for adding/deleting entries. */
	private int modificationCount;
	
	/**
	 * Returns next 2^n.
	 * 2^n will be equal or higher than given number.
	 * 
	 * @param number	given number.
	 * @return 2^n.
	 */
	private static int powerOf2(int number) {
		int result = 1;
        while(number > result) {
            result *= 2;
        }
        return result;
	}
	
	/**
	 * Initializes new SimpleHashtable<K,V>.
	 * This hash table will have 2^n slots, where 2^n >= tableSize.
	 * 
	 * @param tableSize		initial number of slots in hash table.
	 * 
	 * @throws IllegalArgumentException if tableSize is less than 1.
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int tableSize) {
		if(tableSize < 1) {
			throw new IllegalArgumentException("Size can not be lower than 1.");
		}

		table = (TableEntry<K,V>[])new TableEntry[powerOf2(tableSize)]; //not really sure if casting is needed
	}
	
	/**
	 * Default constructor sets number of slots to default size.
	 */
	public SimpleHashtable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	/**
	 * Adds new pair to hash table.
	 * If key already exists its value is updated.
	 * If key does not exists pair is added to the end of a slot.
	 * 
	 * @param key		key of a pair.
	 * @param value		value of a pair.
	 * 
	 * @throws IllegalArgumentException if key is null.
	 */
	public void put(K key, V value) {
		if(key == null) {
			throw new IllegalArgumentException("Key can not be null.");
		}

		int slot = pickSlot(key);
		TableEntry<K, V> newEntry = new TableEntry<>(key, value);
		TableEntry<K, V> currentEntry = table[slot];
		
		if(currentEntry == null) {
			table[slot] = newEntry;
			increaseSize();;
			return;
		}
		
		while(currentEntry.next != null) {
			if(currentEntry.key.equals(key)) {
				break;
			}
			currentEntry = currentEntry.next;
		}
		
		if(currentEntry.key.equals(key)) {
			currentEntry.value = value;
			
		} else { //this means that we are at the last entry in a slot and we did not find the key
			currentEntry.next = newEntry;
			increaseSize();
		}
	}
	
	/**
	 * Returns value that is paired with a given key, or null if key was not found.
	 * 
	 * @param key	key of a pair.
	 * @return value of a pair.
	 */
	public V get(Object key) {
		TableEntry<K, V> entry = findEntry(key);
		
		if(entry == null) {
			return null;
		} else {
			return entry.value;
		}
	}
	
	/**
	 * Returns total number of entries in this hash table.
	 * 
	 * @return number of entries.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Checks if this hash table has given key.
	 * 
	 * @param key	key being searched.
	 * @return true if key is found, false if not found.
	 */
	public boolean containsKey(Object key) {
		return findEntry(key) != null;
	}
	
	/**
	 * Checks if this hash table has given value.
	 * 
	 * @param value		value being searched.
	 * @return true if value is found, false if not found.
	 */
	public boolean containsValue(Object value) {
		
		for(TableEntry<K, V> entry : this) {
			if(value == null) {
				if(entry.value == null) return true;
			} else {
				if(value.equals(entry.value)) return true;
			}
		}

		return false;
	}
	
	/**
	 * Removes pair that has given key.
	 * Does nothing if no such pair/key.
	 * 
	 * @param key	pair with this key is removed.
	 */
	public void remove(Object key) {
		if(key == null) return;
		
		int slot = pickSlot(key);
		TableEntry<K, V> currentEntry = table[slot];
		
		if(currentEntry == null) return;
		
		if(currentEntry.key.equals(key)) {
			table[slot] = currentEntry.next;
			size--;
			modificationCount++;
			return;
		} 
		
		while(currentEntry.next != null) {
			if(currentEntry.next.key.equals(key)) {
				currentEntry.next = currentEntry.next.next;
				size--;
				modificationCount++;
				return;
			}
			currentEntry = currentEntry.next;
		}
	}
	
	/**
	 * Checks if hash table is empty.
	 * 
	 * @return true if empty, false if not empty.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * 
	 * @return "[entry1, entry2, ....]"
	 */
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "[", "]");
		
		for(TableEntry<K, V> entry : this) {
			joiner.add(entry.toString());
		}
		
		return joiner.toString();
	}
	
	/**
	 * Clears whole hash table. Number of slots stays the same.
	 */
	public void clear() {
		for(int index = 0; index < table.length; index++) {
			table[index] = null;
		}
		size = 0;
	}
	
	/**
	 * Increases size by one and if needed increases number of slots.
	 * Number of slots will be increased when capacity is at X%.
	 * X is constant defined in this class - MAX_CAPACITY.
	 */
	@SuppressWarnings("unchecked")
	private void increaseSize() {
		size++;
		modificationCount++;
		double capacity = (double)size / table.length;
		if(capacity >= MAX_CAPACITY) {
			TableEntry<K,V>[] tempTable = Arrays.copyOf(table, table.length);
			
			table = (TableEntry<K,V>[])new TableEntry[table.length * SLOT_MULTIPLIER];
			size = 0;
			
			for(TableEntry<K,V> entry : tempTable) {
				TableEntry<K,V> tempEntry = entry;
				while(tempEntry != null) {
					put(tempEntry.key, tempEntry.value);
					tempEntry = tempEntry.next;
				}
			}
		}
	}
	
	/**
	 * Searches for an entry represented by a given key.
	 * Returns null if not found.
	 * 
	 * @param key	key of a pair.
	 * @return entry in a table.
	 */
	private TableEntry<K, V> findEntry(Object key) {
		if(key == null) return null;
		
		TableEntry<K, V> currentEntry = table[pickSlot(key)];
		
		while(currentEntry != null) {
			if(currentEntry.key.equals(key)) {
				return currentEntry;
			}
			currentEntry = currentEntry.next;
		}
		return null;
	}
	
	/**
	 * Checks in which slot key is/should be.
	 * 
	 * @param key	checked key.
	 * @return slot for given key.
	 */
	private int pickSlot(Object key) {
		return Math.abs(key.hashCode()) % table.length;
	}
	
	
	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl(modificationCount);
	}
	

	/**
	 * Represents one slot in simple hash table.
	 * 
	 * @param <K> 	type of keys.
	 * @param <V> 	type of values.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (9.4.2016.)
	 */
	public static class TableEntry<K, V> {
		
		/** Key of a pair. */
		private K key;
		/** Value of a pair. */
		private V value;
		/** Next pair in the same slot. */
		private TableEntry<K, V> next;
		
		/**
		 * Initializes key and value of a pair.
		 * 
		 * @param key		key of a pair.
		 * @param value		value of a pair.
		 */
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * Getter for value of a pair.
		 * 
		 * @return	value of a pair.
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Setter for value of a pair.
		 * 
		 * @param value		value of a pair.
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		/**
		 * Getter for key of a pair.
		 * 
		 * @return key of a pair.
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * @see java.lang.Object#toString()
		 * 
		 * @return "key=value"
		 */
		@Override
		public String toString() {
			return key + "=" + value;
		}
	}
	
	/**
	 * Iterator implementation for simple hash table.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (9.4.2016.)
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {
		
		/** Remembers modificationCount from when this object was created. */
		private int cachedModifications;
		/** Next slot where entries are being searched. */
		private int slotNumber = 0;
		/** Number of entries that were generated by next(). */
		private int entriesCount = 0;
		/** Last generated entry. */
		private TableEntry<K, V> currentEntry;
		
		/**
		 * Caches number of modifications.
		 * 
		 * @param cachedModifications	number of current modifications.
		 */
		private IteratorImpl(int cachedModifications) {
			this.cachedModifications = cachedModifications;
		}
		
		
		/**
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext(){
			if(cachedModifications != modificationCount) {
				throw new ConcurrentModificationException
								("Iteration is stopped because unknown modification happened.");
			}
			
			return entriesCount < size;
		}
		
		/**
		 * @see java.util.Iterator#next()
		 */
		@Override
		public SimpleHashtable.TableEntry<K, V> next() {
			if(!hasNext()) {
				throw new NoSuchElementException("All entries were already iterated.");
			}
			
			if(currentEntry != null) {
				currentEntry = currentEntry.next;
			}
			
			while(currentEntry == null) {
				currentEntry = table[slotNumber];
				slotNumber++;
			}
			
			entriesCount++;
			return currentEntry;
			
		}
		
		/**
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			if(!containsKey(currentEntry.key)) {
				throw new IllegalStateException
							("Entry is already removed, or next() hasn't been called yet.");
			}
			
			//It's impossible that SimpleHastable.remove can't remove, so no need to check anything
			
			SimpleHashtable.this.remove(currentEntry.key);
			cachedModifications++; //because SimpleHastable.remove will increase it's counter
			entriesCount--;
		}
	}
}
