package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton class that extends {@link AbstractLocalizationProvider}.
 * Constructor sets the language to "en" by default. 
 * It also loads the resource bundle for 
 * this language and stores reference to it. 
 * Method {@link #getString(String)} uses loaded 
 * resource bundle to translate the requested key.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	
	/** Single object used as a localization provider. */
	private static final LocalizationProvider provider = new LocalizationProvider();
	
	/** Translations for current language. */
	private ResourceBundle bundle;
	
	/** 
	 * Constructor is invisible to others. 
	 * Default language is English.
	 */
	private LocalizationProvider() {
		setLanguage("en");
	}
	
	/**
	 * Get provider.
	 * An instance of this singleton class.
	 * 
	 * @return localization provider.
	 */
	public static LocalizationProvider getInstance() {
		return provider;
	}
	
	/**
	 * Sets localization to given language.
	 * 
	 * @param language		newly set localization.
	 */
	public void setLanguage(String language) {
		bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw11.jnotepadpp.local.lang",
				Locale.forLanguageTag(language));
		fire();
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}
}
