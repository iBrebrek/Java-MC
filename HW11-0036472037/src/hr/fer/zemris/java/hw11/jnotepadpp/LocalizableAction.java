package hr.fer.zemris.java.hw11.jnotepadpp;


import javax.swing.AbstractAction;
import javax.swing.Action;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Class derived from {@link AbstractAction}.
 * It is same as abstract action, BUT
 * it saves keys to translate action name 
 * and description. 
 * It will register itself to provider given
 * in constructor, and each time language
 * is changed action will update its
 * name and description.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public abstract class LocalizableAction extends AbstractAction {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	/** Key for action name. */
	private final String keyName;
	/** Key for action description. */
	private final String keyDescription;
	
	/**
	 * Initializes key for name and description of this action.
	 * Registers itself to given localization provider.
	 * On notification from provider it will call {@link #languageChanged(ILocalizationProvider)}.
	 * 
	 * @param keyName				key used to "translate" action name.
	 * @param keyDescription		key used to "translate" action description.
	 * @param lp					localization provider that this registers to.
	 */
	public LocalizableAction(String keyName, String keyDescription, ILocalizationProvider lp) {
		if(keyName == null || keyDescription == null || lp == null) {
			throw new IllegalArgumentException("LocalizableAction doesn't accepts nulls.");
		}
		this.keyName = keyName;
		this.keyDescription = keyDescription;
		languageChanged(lp);
		lp.addLocalizationListener(() -> languageChanged(lp));
	}
	
	/**
	 * Updates name and description of this action.
	 * By update, we mean translate. 
	 * 
	 * @param provider		object that can translate.
	 */
	protected void languageChanged(ILocalizationProvider provider) {
		String name = provider.getString(keyName);
		String description = provider.getString(keyDescription);
		putValue(Action.NAME, name);
		putValue(Action.SHORT_DESCRIPTION, description);
	}
}
