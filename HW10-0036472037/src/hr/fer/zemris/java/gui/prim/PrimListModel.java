package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Model containing prime numbers.
 * Initially model contains only number 1.
 * Class offers method next which will
 * add next prime number to the list.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (17.5.2016.)
 */
public class PrimListModel implements ListModel<Integer> {
	/** All generated prime numbers. */
	private List<Integer> primes = new ArrayList<>();
	/** All observers observing this model. */
	private List<ListDataListener> listeners = new ArrayList<>();
	
	/**
	 * Initializes list of prime numbers.
	 * List initially contains number 1.
	 */
	public PrimListModel() {
		primes.add(1);
	}

	@Override
	public int getSize() {
		return primes.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return primes.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Generates next prime number and notifies 
	 * observers about the change.
	 */
	public void next() {
		int next = primes.isEmpty() ? 1 : primes.get(primes.size()-1) + 1;
		while (true) {
			boolean isPrime = true;
			for(int i = 1; i < primes.size(); i++) { //going from index 1 to skip number 1
				if(next % primes.get(i) == 0) {
					isPrime = false;
					break;
				}
			}
			if(isPrime) {
				primes.add(next);
				break;
			}
			next++;
		}
		
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, getSize()-1, getSize()-1);
		for(ListDataListener l : listeners) {
			l.intervalAdded(event);
		}
	}
}
