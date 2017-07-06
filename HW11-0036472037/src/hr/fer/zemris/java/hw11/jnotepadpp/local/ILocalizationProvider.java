package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Object which are instances of classes that
 * implement this interface will be able to 
 * give the translations for given keys. 
 * For this reason there is a declared method 
 * {@link #getString(String)}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public interface ILocalizationProvider {

	/**
	 * Registers listeners for when localization is changed.
	 * 
	 * @param listener		listener being registered.
	 */
	void addLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Removes object from registered listeners.
	 * 
	 * @param listener		listener being removed.
	 */
	void removeLocalizationListener(ILocalizationListener listener);
	
	/**
	 * It takes a key and gives back the localization.
	 * Returned string depends on current localization.
	 * 
	 * @param key		key for text.
	 * @return text belonging to given key.
	 */
	String getString(String key);
}
