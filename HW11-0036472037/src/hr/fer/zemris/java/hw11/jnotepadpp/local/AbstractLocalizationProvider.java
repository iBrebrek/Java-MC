package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that implements {@link ILocalizationProvider} 
 * and adds it the ability to register, de-register
 * and inform ({@link #fire()}) listeners.
 * It does not implement {@link #getString(String)}. 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	
	/** List of all registered listeners. */
	private List<ILocalizationListener> listeners = new ArrayList<>();

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		if(listener == null) return; //no need to add null
		if(listeners.contains(listener)) return; //don't add twice
		listeners = new ArrayList<>(listeners); //in case someone was iterating
		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners = new ArrayList<>(listeners); //in case someone was iterating
		listeners.remove(listener);
	}
	
	/**
	 * Informs all registers listeners 
	 * that localization change happened.
	 */
	public void fire() {
		for(ILocalizationListener l : listeners) {
			l.localizationChanged();
		}
	}
}
