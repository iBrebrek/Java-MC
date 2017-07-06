package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Status bar has 3 parts equally big.
 * <li>
 * First, left, part shows length, total 
 * number of characters, of currently shown text.
 * </li><li>
 * Second, middle, part shows current position of caret.
 * First is line of caret, then column of caret and
 * lastly number of selected characters.
 * </li><li>
 * Third, last, part shows current time. 
 * Time format: year/month/day hour:min:sec
 * </li>
 * <p>
 * Constructor takes frame and localization provider.
 * Frame is used only to listen when it is closed,
 * when given frame is closed status bar stops
 * thread that updates time every second.
 * Localization provider is used to change static 
 * words used in status bar, such as "length, line..".
 * </p>
 * <p>
 * Status bar updates time every second, but
 * does not update length and caret position
 * by itself. Object using this status bar
 * has to call {@link #setLength(int)} and
 * {@link #updateCaretStatus(int, int, int)}
 * every time status bar should update numbers.
 * </p>
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public class StatusBar extends JPanel {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Key word in properties for length . */
	private final String lengthKey = "length";
	/** Current translation of length. */
	private String lengthString;
	/** Key word in properties for line . */
	private final String lineKey = "line";
	/** Current translation of line. */
	private String lineString;
	/** Key word in properties for column . */
	private final String columnKey = "column";
	/** Current translation of selection. */
	private String columnString;
	/** Key word in properties for selection . */
	private final String selectionKey = "selection";
	/** Current translation of selection. */
	private String selectionString;
	
	/** Number of characters. */
	private int length = 0;
	/** Line in which is caret. */
	private int line = 1;
	/** Column in which is caret. */
	private int column = 1;
	/** Number of selected characters. */
	private int selection = 0;
	
	/** {@code true} only when frame given in constructor is closed. */
	private volatile boolean applicationOver;
	
	/** Label used to display left part of status bar. */
	private final JLabel labelLength = new JLabel();
	/** Label used to display middle part of status bar. */
	private final JLabel labelCaret = new JLabel();
	/** Label used to display right part of status bar. */
	private final JLabel labelTime = new JLabel();
	
	/** Format in which time will be displayed. */
	private final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
							.withLocale(Locale.getDefault())
							.withZone(ZoneId.systemDefault());
	
	/**
	 * Creates GUI of status bar.
	 * Default length and selection are 0
	 * while default line and column are 1.
	 * <p>
	 * Starts thread that will update time
	 * every second until given frame is closed.
	 * </p>
	 * 
	 * @param frame 	frame on which is added on close listener.
	 * @param lp		localization provider that this registers to.
	 */
	public StatusBar(JFrame frame, ILocalizationProvider lp) {	
		if(lp == null || frame == null) {
			throw new IllegalArgumentException("Frame and localization provider can not be null.");
		}
		languageChanged(lp);
		lp.addLocalizationListener(() -> languageChanged(lp));
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				applicationOver = true;
			};
		});
		
		new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(1000);
				} catch(Exception ex) {}
				if(applicationOver) break;
				SwingUtilities.invokeLater(() -> {
					updateTime();
				});
			}
		}).start();
		
		initGUI();
	}
	
	/**
	 * Initializes GUI.
	 * Left and middle element have left horizontal
	 * alignment while right element has right alignment.
	 * Every element is equally big.
	 */
	private void initGUI() {
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY), 
				BorderFactory.createEmptyBorder(3, 3, 3, 3))
		);
		
		labelLength.setHorizontalAlignment(SwingConstants.LEFT);
		labelCaret.setHorizontalAlignment(SwingConstants.LEFT);
		labelTime.setHorizontalAlignment(SwingConstants.RIGHT);
		
		setLayout(new GridLayout(1, 3));
		
		labelCaret.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 1, 0, 1, Color.LIGHT_GRAY), 
				BorderFactory.createEmptyBorder(0, 5, 0, 5))
		);
		
		add(labelLength);
		add(labelCaret);
		add(labelTime);
		
		updateLength();
		updateCaret();
		updateTime();
	}
	
	/**
	 * Every time language is changed this method is called.
	 * It will change label names.
	 * 
	 * @param lp		localization provider that can "translate".
	 */
	private void languageChanged(ILocalizationProvider lp) {
		lengthString = lp.getString(lengthKey);
		lineString = lp.getString(lineKey);
		columnString = lp.getString(columnKey);
		selectionString = lp.getString(selectionKey);
		updateCaret();
		updateLength();
	}
	
	/**
	 * Sets length which will be shown on status bar.
	 * 
	 * @param length		total number of characters.
	 */
	public void setLength(int length) {
		this.length = length;
		updateLength();
	}
	
	/**
	 * Sets caret status which will be displayed on status bar.
	 * 
	 * @param line			line in which is caret.
	 * @param column		column in which is caret.
	 * @param selection		number of selected characters.
	 */
	public void updateCaretStatus(int line, int column, int selection) {
		this.line = line;
		this.column = column;
		this.selection = selection;
		updateCaret();
	}

	/**
	 * Updates displayed time.
	 */
	private void updateTime() {
		labelTime.setText(formatter.format(Instant.now()));
	}
	
	/**
	 * Updates displayed length.
	 */
	private void updateLength() {
		labelLength.setText(lengthString+":"+length);
	}

	/**
	 * Updates displayed caret status
	 * (line, column and selection).
	 */
	private void updateCaret() {
		labelCaret.setText(lineString+":"+line+"  "
					+columnString+":"+column+"  "
					+selectionString+":"+selection);
	}
	
}
