package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * A decorator for some other {@link ILocalizationProvider}.
 * This class offers two additional methods: 
 * {@link #connect()} and {@link #disconnect()}.
 * Listens for localization changes so when it 
 * receives the notification, it will notify 
 * all listeners that are registered as its listeners.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	/** {@code true} if connected to {@code provider}. */
	private boolean connected;
	/** Listener used to listen {@code provider}. */
	private final ILocalizationListener listener = () -> fire();
	/** Provider of which this class is being decorator. */
	private final ILocalizationProvider provider;
	
	/**
	 * Initializes provider bridge.
	 * On method {@link #getString(String)} call
	 * given provider will be asked to get string.
	 * 
	 * @param provider		provider being "decorated".
	 */
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		if(provider == null) {
			throw new IllegalArgumentException("Localization provider can not be null.");
		}
		this.provider = provider;
	}

	/**
	 * Connects this to provider given in constructor.
	 */
	public void connect() {
		if(connected) return;
		connected = true;
		provider.addLocalizationListener(listener);		
	}
	
	/**
	 * Disconnects this from provider given in constructor.
	 */
	public void disconnect() {
		if(!connected) return;
		connected = false;
		provider.removeLocalizationListener(listener);
	}

	@Override
	public String getString(String key) {
		return provider.getString(key);
	}

}
