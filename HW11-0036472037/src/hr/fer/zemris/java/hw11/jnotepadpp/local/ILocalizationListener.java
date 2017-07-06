package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Listeners for {@link ILocalizationProvider}.
 * Localization listeners are notified of
 * localization change.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public interface ILocalizationListener {
	
	/**
	 * Called by the Subject when the selected language changes.
	 */
	void localizationChanged();
}
