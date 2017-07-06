package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Class derived from {@link LocalizationProviderBridge}.
 * In its constructor it registers itself to its {@link JFrame}.
 * When frame is opened, it calls {@link #connect()} and when
 * frame is closed, it calls {@link #disconnect()}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Registers itself to given frame.
	 * When frame is opened, it calls {@link #connect()} and when 
	 * frame is closed, it calls {@link #disconnect()}.
	 * 
	 * @param provider		provider being "decorated".
	 * @param frame			frame on which to register.
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		if(frame == null) {
			throw new IllegalArgumentException("Frame can not be null.");
		}
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
	}
}
