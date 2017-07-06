package hr.fer.zemris.java.hw11.jnotepadpp;

import javax.swing.JMenu;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Same as {@link JMenu} but instead of fixed name
 * it has key which is used to get name based on 
 * current application language.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public class LJMenu extends JMenu {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	/** Key for menu text. */
	private String key;

	/**
	 * Initializes key for name of this menu.
	 * Registers itself to given localization provider.
	 * 
	 * @param key		key used to "translate" menu text.
	 * @param lp		localization provider that this registers to.
	 */
	public LJMenu(String key, ILocalizationProvider lp) {
		if(key == null || lp == null) {
			throw new IllegalArgumentException("LJMenu does not accept nulls.");
		}
		this.key = key;
		languageChanged(lp);
		lp.addLocalizationListener(() -> languageChanged(lp));
	}
	
	/**
	 * Updates text of this menu.
	 * By update, we mean translate. 
	 * 
	 * @param provider		object that can translate.
	 */
	private void languageChanged(ILocalizationProvider provider) {
		String name = provider.getString(key);
		setText(name);
	}
}
