package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Text editor that allows user to work 
 * with multiple documents at the same time. 
 * 
 * <p>This editor allows following actions:</p>
 * <ul>
 * 	<li>creating a new blank document</li>
 * 	<li>opening existing document</li>
 * 	<li>saving/saving-as document</li>
 * 	<li>close document shown it a tab </li>
 * 	<li>cut/copy/paste text</li>
 * 	<li>statistical info (number of characters and lines)</li>
 * 	<li>exiting application</li>
 * 	<li>changing application language (en, de and hr)</li>
 * 	<li>changing cases (to lower/upper and invert)</li>
 * 	<li>sorting selected lines (ascending or descending)</li>
 * 	<li>removing duplicate lines</li>
 * </ul>
 * 
 * <p>Program has status bar that is shown 
 * at the bottom of window. @see StatusBar</p>
 * 
 * <p>When file is unmodified tab icon will be green disk
 * and if file is modified tab icon will be red disk.
 * When tab or application is being closed user is asked 
 * if he wants to save modified files.</p>
 * 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (23.5.2016.)
 */
public class JNotepadPP extends JFrame {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Tab icon when editor is unmodified. */
	private final ImageIcon GREEN_ICON = createImageIcon("icons/green16.png");
	/** Tab icon when editor is modified. */
	private final ImageIcon RED_ICON = createImageIcon("icons/red16.png");
	
	/** Localization provider for this frame. */
	private final FormLocalizationProvider localizationProvider = 
			new FormLocalizationProvider(
					LocalizationProvider.getInstance(), 
					this
	);
	
	/** Status bar shown at the bottom. It it status of currently opened tab. */
	private final StatusBar statusBar = new StatusBar(this, localizationProvider);
	
	/** All tabs. Inside each tab is {@link JScrollPane} and inside it is {@link MyEditor}. */
	private JTabbedPane tabPanel;
	
	/**
	 * Initializes everything needed for {@link JNotepadPP} frame.
	 * See class documentation for more information.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null); //center of the screen
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}
		});
		
		localizationProvider.addLocalizationListener(() -> {
			final String newFileKey = "new_file";
			String newFile = localizationProvider.getString(newFileKey);
			for(int i = 0, max = tabPanel.getTabCount(); i < max; i++) {
				if(getEditorAt(i).filePath == null) {
					tabPanel.setTitleAt(i, newFile);
					tabPanel.setToolTipTextAt(i, newFile);
				}
			}
		});
		
		initGUI();
	}
	
	/**
	 * Creates layout and in that layout 
	 * adds tab panel and status bar.
	 * In first tab adds new empty file.
	 * Calls {@link #createActions()},
	 * {@link #createMenus()} and
	 * {@link #createToolBars()}.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		Container center = new Container();
		center.setLayout(new BorderLayout());
		center.add(statusBar, BorderLayout.PAGE_END);
		
		tabPanel = new JTabbedPane();
		tabPanel.addChangeListener(e -> {
			String tip = tabPanel.getToolTipTextAt(
					tabPanel.getSelectedIndex());
			JNotepadPP.this.setTitle(
					tip == null ?
					"JNotepad++" : tip+" - JNotepad++");
			
			MyEditor editor = getEditorAt(tabPanel.getSelectedIndex());
			editor.requestFocus(); //so we don't need to click inside editor after changing tab
			editor.caretUpdate(null); //to update status bar
			statusBar.setLength(editor.getDocument().getLength());
		});
		
		newDocumentAction.actionPerformed(null); //to start with empty editor
		
		center.add(tabPanel, BorderLayout.CENTER);
		cp.add(center, BorderLayout.CENTER);
		
		createActions();
		createMenus();
		createToolBars();
	}
	
	/**
	 * Standard new action.
	 * Opens new tab with empty editor.
	 * Tab name will be "new_file".
	 */
	private Action newDocumentAction = new LocalizableAction("New", "New_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;
		
		private final String newFileKey = "new_file";
		private String newFile;

		@Override
		public void actionPerformed(ActionEvent e) {
			MyEditor newEditor = new MyEditor(null);
			tabPanel.addTab(newFile, GREEN_ICON, newEditor.getScrollPane(), newFile);
			tabPanel.setSelectedComponent(newEditor.getScrollPane());
		}
		
		@Override
		protected void languageChanged(ILocalizationProvider provider) {
			super.languageChanged(provider);
			newFile = provider.getString(newFileKey);
		};
	};
	
	/**
	 * Standard open action.
	 * Asks used to pick a file.
	 * If file doesn't exists user is informed and action is over.
	 * If it is unable to read that file user is informed and action is over.
	 * When text from chosen file is read, new tab with
	 * editor containing said text is opened.
	 */
	private Action openDocumentAction = new LocalizableAction("Open", "Open_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;
		
		private final String openFileKey = "Open_file";
		private String openFile;
		private final String fileKey = "File";
		private String file;
		private final String notExistsKey = "does_not_exists";
		private String notExists;
		private final String errorKey = "Error";
		private String error;
		private final String errorReadingKey = "Error_while_reading_file";
		private String errorReading;
		
		@Override
		protected void languageChanged(ILocalizationProvider provider) {
			super.languageChanged(provider);
			openFile = provider.getString(openFileKey);
			file = provider.getString(fileKey);
			notExists = provider.getString(notExistsKey);
			error = provider.getString(errorKey);
			errorReading = provider.getString(errorReadingKey);
		};

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(openFile);
			if(fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File fileName = fc.getSelectedFile();
			Path filePath = fileName.toPath();
			if(!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(
						JNotepadPP.this, 
						file+fileName.getAbsolutePath()+" "+notExists+"!",
						error,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			byte[] bytes;
			try {
				bytes = Files.readAllBytes(filePath);
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(
						JNotepadPP.this, 
						errorReading+" "+fileName.getAbsolutePath(),
						error,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String text = new String(bytes, StandardCharsets.UTF_8);
			MyEditor editor = new MyEditor(text, filePath);
			tabPanel.addTab(
					filePath.getFileName().toString(), 
					GREEN_ICON, 
					editor.getScrollPane(), 
					fileName.getAbsolutePath());
			tabPanel.setSelectedComponent(editor.getScrollPane());
		}
	};
	
	/**
	 * Standard save action.
	 * Saves to already defined file for shown text.
	 * (that file path can be shown by holding mouse over tab.)
	 * If editor does not have it's file this action becomes 
	 * same as action "Save As".
	 * After saving is done, text will be considered unmodified.
	 */
	private Action saveDocumentAction = new LocalizableAction("Save", "Save_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;
		
		private final String errorCopyingKey = "Error_while_copying_to_file";
		private String errorCopying;
		private final String errorKey = "Error";
		private String error;
		private final String isSavedKey = "is_saved";
		private String isSaved;
		private final String informationKey = "Information";
		private String information;
		
		@Override
		protected void languageChanged(ILocalizationProvider provider) {
			super.languageChanged(provider);
			errorCopying = provider.getString(errorCopyingKey);
			error = provider.getString(errorKey);
			isSaved = provider.getString(isSavedKey);
			information = provider.getString(informationKey);
		};

		@Override
		public void actionPerformed(ActionEvent e) {
			MyEditor editor = getEditorAt(tabPanel.getSelectedIndex());
			Path currentFilePath = editor.filePath;
			if(currentFilePath == null) {
				saveAsDocumentAction.actionPerformed(null);
				return;
			}
			byte[] data = editor.getText().getBytes(StandardCharsets.UTF_8);
			try {
				Files.write(currentFilePath, data);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(
						JNotepadPP.this, 
						errorCopying+" "+currentFilePath,
						error,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(
					JNotepadPP.this, 
					currentFilePath+" "+isSaved, 
					information,
					JOptionPane.INFORMATION_MESSAGE);
			tabPanel.setIconAt(tabPanel.indexOfComponent(editor.getScrollPane()), GREEN_ICON);
			editor.modified = false;
		}
	};
	
	/**
	 * Standard save as action.
	 * Asks user where he wants to save text inside currently shown editor.
	 * If chosen file already exists user is asked if he wants to continue.
	 * After saving is done, text will be considered unmodified.
	 */
	private Action saveAsDocumentAction = new LocalizableAction("Save_as", "Save_as_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;
		
		private final String saveDocumentKey = "Save_document";
		private String saveDocument;
		private final String nothingSavedKey = "Nothing_saved";
		private String nothingSaved;
		private final String warningKey = "Warning";
		private String warning;
		private final String fileExitsKey = "File_already_exists";
		private String fileExits;
		private final String askToReplaceKey = "Ask_to_replace";
		private String askToReplace;
		private final String confirmSaveAsKey = "confirm_save_as";
		private String confirmSaveAs;
		
		@Override
		protected void languageChanged(ILocalizationProvider provider) {
			super.languageChanged(provider);
			saveDocument = provider.getString(saveDocumentKey);
			nothingSaved = provider.getString(nothingSavedKey);
			warning = provider.getString(warningKey);
			fileExits = provider.getString(fileExitsKey);
			askToReplace = provider.getString(askToReplaceKey);
			confirmSaveAs = provider.getString(confirmSaveAsKey);
		};

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle(saveDocument);
			if(jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(
						JNotepadPP.this, 
						nothingSaved,
						warning,
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			Path filePath = jfc.getSelectedFile().toPath();
			if(Files.exists(filePath)) {
				int answer = JOptionPane.showConfirmDialog(
						JNotepadPP.this, 
						fileExits+"\n"+askToReplace,
						confirmSaveAs,
						JOptionPane.YES_NO_OPTION);
				if(answer != JOptionPane.YES_OPTION) {
					return;
				}
			}
			MyEditor editor = getEditorAt(tabPanel.getSelectedIndex());
			editor.setPath(filePath);
			saveDocumentAction.actionPerformed(null);
		}
	};
	
	/**
	 * Closes currently shown tab.
	 * If closed was the only tab then
	 * new empty editor will be opened.
	 * If text inside editor was modified in 
	 * any way, user will be asked if he wants
	 * to keep changes before closing.
	 */
	private Action closeTabAction = new LocalizableAction("Close", "Close_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;
		
		private final String wasModifiedKey = "was_modified";
		private String wasModified;
		private final String askToKeepKey = "Ask_to_keep";
		private String askToKeep;
		private final String confirmCloseKey = "Confirm_close";
		private String confirmClose;
		
		@Override
		protected void languageChanged(ILocalizationProvider provider) {
			super.languageChanged(provider);
			wasModified = provider.getString(wasModifiedKey);
			askToKeep = provider.getString(askToKeepKey);
			confirmClose = provider.getString(confirmCloseKey);
		};

		@Override
		public void actionPerformed(ActionEvent e) {
			MyEditor editor = getEditorAt(tabPanel.getSelectedIndex());
			if(editor.modified) {
				int answer = JOptionPane.showConfirmDialog(
						JNotepadPP.this, 
						tabPanel.getTitleAt(tabPanel.getSelectedIndex())+" "+wasModified+".\n"+askToKeep,
						confirmClose,
						JOptionPane.YES_NO_CANCEL_OPTION);
				if(answer == JOptionPane.YES_OPTION) {
					saveAsDocumentAction.actionPerformed(null);
					remove();
				} else if(answer == JOptionPane.NO_OPTION) {
					remove();
				} else {
					return;
				}
			} else {
				remove();
			}
		}
		
		private void remove() {
			Component component = tabPanel.getSelectedComponent();
			if(tabPanel.getTabCount() == 1) {
				newDocumentAction.actionPerformed(null);
			}
			tabPanel.remove(component);
		}
	};
	
	/**
	 * Standard cut action.
	 * Cuts selected part of the text.
	 * Multiple selection is not supported.
	 * If nothing is selected nothing is cut.
	 * Cut is equivalent to copy but cut also deletes selected text.
	 */
	private Action cutSelectedPartAction = new LocalizableAction("Cut", "Cut_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			getEditorAt(tabPanel.getSelectedIndex()).cut();
		}
	};
	
	/**
	 * Standard copy action.
	 * Copies selected part of the text.
	 * Multiple selection is not supported.
	 * If nothing is selected nothing is copied.
	 */
	private Action copySelectedPartAction = new LocalizableAction("Copy", "Copy_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			getEditorAt(tabPanel.getSelectedIndex()).copy();
		}
	};
	
	/**
	 * Standard paste action.
	 * Pasts to caret location or over marked text.
	 */
	private Action pasteSelectedPartAction = new LocalizableAction("Paste", "Paste_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {			
			getEditorAt(tabPanel.getSelectedIndex()).paste();
		}
	};
	
	/**
	 * Shows dialog with statistical information of currently shown editor.
	 * Statistical information is:
	 * "Your document has X characters, Y non-blank characters and Z lines.",
	 * where X, Y and Z are integer numbers.
	 */
	private Action viewStatistic = new LocalizableAction("Stats", "Stats_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;
		
		private final String dialogStatsTextKey = "Dialog_stats_formated_text";
		private String dialogStatsText;
		private final String informationKey = "Information";
		private String information;
		
		@Override
		protected void languageChanged(ILocalizationProvider provider) {
			super.languageChanged(provider);
			dialogStatsText = provider.getString(dialogStatsTextKey);
			information = provider.getString(informationKey);
		};

		@Override
		public void actionPerformed(ActionEvent e) {
			MyEditor editor = getEditorAt(tabPanel.getSelectedIndex());
			int chars = 0;
			int lines = 1;
			for(char c : editor.getText().toCharArray()) {
				if(c == '\n') lines++;
				else if(!Character.isWhitespace(c)) chars++;
			}
			JOptionPane.showMessageDialog(
					JNotepadPP.this, 
					String.format(
						dialogStatsText, 
						editor.getText().length(), chars, lines),
					information,
					JOptionPane.INFORMATION_MESSAGE);
		}
	};
	
	/**
	 * Exits whole application.
	 * Exiting is done by closing one by one tab.
	 * If there were modified files while exiting,
	 * user is asked if he wants to keep any changes.
	 * To cancel exiting user should click "cancel"
	 * on any of shown dialogs.
	 * If there were no modifications before exiting,
	 * user is not asked anything and application
	 * is simply closed.
	 */
	private Action exitAction = new LocalizableAction("Exit", "Exit_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int numberOfTabs = tabPanel.getTabCount();
			Component last = null; //will be used to check if last tab was removed (because we can never have 0)
			for(int expectedNumber = numberOfTabs; expectedNumber > 0; expectedNumber--) {
				if(expectedNumber != tabPanel.getTabCount()) {
					//user declined to close one tab, therefore, declined to exit application
					return;
				}
				if(expectedNumber == 1) {
					last = tabPanel.getComponent(0);
				}
				closeTabAction.actionPerformed(null);
			}
			if(tabPanel.getComponentAt(0) != last) {
				dispose();
			}
		}
	};
	
	/**
	 * Same part for all toUppercase, toLowercase and invert.
	 * They will only have to define how to change case of selected string.
	 * If nothing is selected nothing is done.
	 * 
	 * @param caseChanger		function that will change case of selected string.
	 */
	private void actionForCaseChange(Function<String, String> caseChanger) {
		MyEditor editor = getEditorAt(tabPanel.getSelectedIndex());
		Document doc = editor.getDocument();
		int length = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		if(length == 0) return;
		int offset = Math.min(
				editor.getCaret().getDot(), 
				editor.getCaret().getMark());
		try {
			String text = doc.getText(offset, length);
			doc.remove(offset, length);
			doc.insertString(offset, caseChanger.apply(text), null);
		} catch(BadLocationException ex) {
			//nothing smart to do here...
			ex.printStackTrace();
		}
	}
	
	/**
	 * Inside currently shown editor 
	 * changes every selected character to upper case.
	 * If no characters are selected nothing is done.
	 */
	private Action toUppercaseAction = new LocalizableAction("To_upper", "To_upper_info", localizationProvider) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			actionForCaseChange(String::toUpperCase);
		}
	};
	
	/**
	 * Inside currently shown editor 
	 * changes every selected character to lower case.
	 * If no characters are selected nothing is done.
	 */
	private Action toLowercaseAction = new LocalizableAction("To_lower", "To_lower_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			actionForCaseChange(String::toLowerCase);
		}
	};
	
	/**
	 * Inside currently shown editor 
	 * inverts lower case to upper case and upper case
	 * to lower case of every selected character.
	 * If no characters are selected nothing is done.
	 */
	private Action invertCaseAction = new LocalizableAction("Invert", "Invert_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			actionForCaseChange(text -> {
				char[] chars = text.toCharArray();
				for(int i = 0; i < chars.length; i++) {
					char c = chars[i];
					if(Character.isLowerCase(c)) {
						chars[i] = Character.toUpperCase(c);
					} else if(Character.isUpperCase(c)) {
						chars[i] = Character.toLowerCase(c);
					}
				}
				return new String(chars);
			});
		}
	};
	
	/**
	 * Changes language of application to English.
	 * JFileChooser dialogs, and button labels
	 * which are displayed by JOptionPane are
	 * not affected by this action.
	 */
	private Action toEnglish = new LocalizableAction("English", "English_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};
	
	/**
	 * Changes language of application to German.
	 * JFileChooser dialogs, and button labels
	 * which are displayed by JOptionPane are
	 * not affected by this action.
	 */
	private Action toGerman = new LocalizableAction("German", "German_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	};
	
	/**
	 * Changes language of application to Croatian.
	 * JFileChooser dialogs, and button labels
	 * which are displayed by JOptionPane are
	 * not affected by this action.
	 */
	private Action toCroatian = new LocalizableAction("Croatian", "Croatian_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};
	
	/**
	 * Same part for action unique, ascending and descending.
	 * They will only have to define how to change selected lines.
	 * If nothing is selected nothing is done.
	 * 
	 * @param linesChanger		function that will change selected lines
	 */
	private void actionForLinesSelection(Function<String[], String[]> linesChanger) {
		MyEditor editor = getEditorAt(tabPanel.getSelectedIndex());
		Document doc = editor.getDocument();
		int start = Math.min(
				editor.getCaret().getDot(), 
				editor.getCaret().getMark()
		);
		int end = Math.max(
				editor.getCaret().getDot(), 
				editor.getCaret().getMark()
		);
		try {
			int firstLine = editor.getLineOfOffset(start);
			int lastLine = editor.getLineOfOffset(end);
			if (firstLine == lastLine) return; // nothing to do if only 1 line selected
			int offset = editor.getLineStartOffset(firstLine);
			int length = editor.getLineEndOffset(lastLine) - offset;
		
			String text = doc.getText(offset, length);
			doc.remove(offset, length);
			String[] resultLines = linesChanger.apply(text.split("\n"));
			String resultText = "";
			for(String line : resultLines) {
				resultText += line;
				resultText += "\n";
			}
			doc.insertString(offset, resultText, null);
		} catch(BadLocationException ex) {
			//nothing smart to do here...
			ex.printStackTrace();
		}
	}
	
	/**
	 * Ascending sort of selected lines.
	 * Sorts only the selected lines of text 
	 * using rules of currently defined language. 
	 * If user selection spans only part of 
	 * some line, whole line is affected. 
	 * If nothing is selected nothing is done.
	 */
	private Action ascendingSortAction = new LocalizableAction("Ascending", "Sorting_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;
		
		private final String localeKey = "locale";
		private Collator comparator;
		
		@Override
		protected void languageChanged(ILocalizationProvider provider) {
			super.languageChanged(provider);
			Locale locale = new Locale(provider.getString(localeKey));
			comparator = Collator.getInstance(locale);
		};

		@Override
		public void actionPerformed(ActionEvent e) {
			actionForLinesSelection(lines -> {
				Arrays.sort(lines, (a,b) -> comparator.compare(a, b));
				return lines;
			});
		}
	};
	
	/**
	 * Descending sort of selected lines.
	 * Sorts only the selected lines of text 
	 * using rules of currently defined language. 
	 * If user selection spans only part of 
	 * some line, whole line is affected. 
	 * If nothing is selected nothing is done.
	 */
	private Action descendingSortAction = new LocalizableAction("Descending", "Sorting_info", localizationProvider) {

		private static final long serialVersionUID = 1L;
		
		private final String localeKey = "locale";
		private Collator comparator;
		
		@Override
		protected void languageChanged(ILocalizationProvider provider) {
			super.languageChanged(provider);
			Locale locale = new Locale(provider.getString(localeKey));
			comparator = Collator.getInstance(locale);
		};

		@Override
		public void actionPerformed(ActionEvent e) {
			actionForLinesSelection(lines -> {
				Arrays.sort(lines, (a,b) -> comparator.compare(b, a));
				return lines;
			});
		}
	};
	
	/**
	 * Removes from selection all lines which are duplicates 
	 * (only the first occurrence is retained).
	 * If user selection spans only part of 
	 * some line, whole line is affected. 
	 * If nothing is selected nothing is done.
	 */
	private Action uniqueAction = new LocalizableAction("Unique", "Unique_info", localizationProvider) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			actionForLinesSelection(lines -> {
				List<Integer> toRemove = new ArrayList<>();
				for(int i = 0; i < lines.length; i++) {
					for(int j = i+1; j < lines.length; j++) {
						if(lines[i].equals(lines[j])) {
							if(!toRemove.contains(j)) {
								toRemove.add(j);
							}
						}
					}
				}
				int size = lines.length - toRemove.size();
				String[] result = new String[size];
				int index = 0;
				for(int i = 0; i < lines.length; i++) {
					if(toRemove.contains(i)) continue;
					result[index] = lines[i];
					index++;
				}
				return result;
			});
		}
	};
	
	/**
	 * Initializes accelerator and mnemonic of 
	 * every {@code Action} defined in this class.
	 */
	private void createActions() {
		newDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_N);
		
		openDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_O);
		
		saveDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_S);
		
		saveAsDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control alt S"));
		saveAsDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_A);
		
		closeTabAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control W"));
		closeTabAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_C);
		
		cutSelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control X"));
		cutSelectedPartAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_T);
		
		copySelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control C"));
		copySelectedPartAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_C);
		
		pasteSelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control V"));
		pasteSelectedPartAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_P);
		
		viewStatistic.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control I"));
		viewStatistic.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_I);
		
		exitAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("alt F4"));
		exitAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_X);
		
		toLowercaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("ctrl alt L"));
		toLowercaseAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_L);
		
		toUppercaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("ctrl alt U"));
		toUppercaseAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_U);
		
		invertCaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("ctrl alt I"));
		invertCaseAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_I);
		
		toEnglish.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("ctrl alt E"));
		toEnglish.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_E);
		
		toGerman.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("ctrl alt D"));
		toGerman.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_D);
		
		toCroatian.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("ctrl alt H"));
		toCroatian.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_H);
		
		uniqueAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("ctrl shift U"));
		uniqueAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_U);
		
		ascendingSortAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("ctrl shift A"));
		ascendingSortAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_A);
		
		descendingSortAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("ctrl shift D"));
		descendingSortAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_D);
	}
	
	/**
	 * Creates all menus on menu bar.
	 * Every action defined in this 
	 * class can be found in menus.
	 * Menu bar is grouped into 5 menus:
	 * File, Edit, View, Tools and Languages.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new LJMenu("File", localizationProvider);
		menuBar.add(fileMenu);
		
		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.add(new JMenuItem(closeTabAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));
		
		JMenu editMenu = new LJMenu("Edit", localizationProvider);
		menuBar.add(editMenu);
		
		editMenu.add(new JMenuItem(cutSelectedPartAction));
		editMenu.add(new JMenuItem(copySelectedPartAction));
		editMenu.add(new JMenuItem(pasteSelectedPartAction));		
		
		JMenu viewMenu = new LJMenu("View", localizationProvider);
		menuBar.add(viewMenu);
		
		viewMenu.add(new JMenuItem(viewStatistic));
		
		JMenu toolsMenu = new LJMenu("Tools", localizationProvider);
		menuBar.add(toolsMenu);
		
		JMenu casesMenu = new LJMenu("Change_case", localizationProvider);
		toolsMenu.add(casesMenu);

		JMenuItem toLower = new JMenuItem(toLowercaseAction);
		JMenuItem toUpper = new JMenuItem(toUppercaseAction);
		JMenuItem invert = new JMenuItem(invertCaseAction);
		casesMenu.add(toLower);
		casesMenu.add(toUpper);
		casesMenu.add(invert);
		
		JMenu sortMenu = new LJMenu("Sort", localizationProvider);
		sortMenu.add(new JMenuItem(ascendingSortAction));
		sortMenu.add(new JMenuItem(descendingSortAction));
		toolsMenu.add(sortMenu);
		toolsMenu.add(new JMenuItem(uniqueAction));

		casesMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				boolean isSelected = 
						getEditorAt(tabPanel.getSelectedIndex())
						.getSelectedText() != null;
				toLower.setEnabled(isSelected);
				toUpper.setEnabled(isSelected);
				invert.setEnabled(isSelected);
			}
			@Override
			public void menuDeselected(MenuEvent e) {
			}
			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});
		
		JMenu langMenu = new LJMenu("Languages", localizationProvider);
		menuBar.add(langMenu);
		
		langMenu.add(new JMenuItem(toEnglish));
		langMenu.add(new JMenuItem(toGerman));
		langMenu.add(new JMenuItem(toCroatian));
		
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Creates all tools on tool bar.
	 * Tool bar is floatable.
	 * Tool bar has some(not all) defined actions:
	 * New, Open, Save, Save As, Close, 
	 * Cut, Copy, Paste, 
	 * Statistical Info, Exit
	 */
	private void createToolBars() {
		JToolBar toolBar = new JToolBar("JNotepad++");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(newDocumentAction));
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveAsDocumentAction));
		toolBar.add(new JButton(closeTabAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(cutSelectedPartAction));
		toolBar.add(new JButton(copySelectedPartAction));
		toolBar.add(new JButton(pasteSelectedPartAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(viewStatistic));
		toolBar.addSeparator();
		toolBar.add(new JButton(exitAction));
		
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Create {@code ImageIcon} from given icon path.
	 * If it it unable to create icon whole window 
	 * will be disposed and problem will be written
	 * on {@link System#err}.
	 * 
	 * @param iconPath		path where icon should be.
	 * @return image icon from given path.
	 */
	private ImageIcon createImageIcon(String iconPath) {
		InputStream is = this.getClass().getResourceAsStream(iconPath);
		if(is == null) {
			System.err.println("Could not load icon : " + iconPath + ".\n" 
								+ "Add icon there and start again.");
			dispose();		
		}
		byte[] buffer = new byte[4096];
	    int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			while ((bytesRead = is.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
		} catch(IOException exc) {
			System.err.println("Unable to get icon " + iconPath + ".");
			dispose();		
		}
		return new ImageIcon(output.toByteArray());
	}
	
	/**
	 * Get {@code MyEditor} from tab at given tab index.
	 * Indexes count from 0.
	 * 
	 * @param tabIndex		tab where editor is.
	 * @return editor at given tab index.
	 */
	private MyEditor getEditorAt(int tabIndex) {
		return (MyEditor)((JScrollPane)tabPanel.getComponentAt(tabIndex)).getViewport().getView();
	}


	/**
	 * This is editor used in every tab.
	 * Each editor is its own listener for document and caret change.
	 * When document change happens, flag modified will be set to {@code true},
	 * tab icon will be set to red disk icon and status length will be updated.
	 * When caret change happens status bar(line, column and selection) will be updated.
	 * <p>
	 * DO NOTE: each editor is inside it's own tab, BUT, editor is not component inside tab.
	 * Scroll pane is added in tab, and this editor is added in scroll pane.
	 * This way, editor will show scroll bars if needed.
	 * To get scroll pane in which is editor, use method {@link MyEditor#getScrollPane()}.
	 * </p>
	 * Every time you save text inside an editor to a new location you 
	 * should update file path using method {@link MyEditor#setPath(Path)}. 
	 * If you don't do so, tab title and tip won't be updated.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (23.5.2016.)
	 */
	private class MyEditor extends JTextArea implements DocumentListener, CaretListener {
		/** For serialization. */
		private static final long serialVersionUID = 1L;
		/** {@code true} if any modification happened. */
		private boolean modified;
		/** File path where text in this editor is located on disk, null if not on disk. */
		private Path filePath;
		/** Scroll pane is added in tab, and this editor is added in scroll pane. */
		private JScrollPane parent;
		
		/**
		 * Initializes editor containing given text.
		 * Given file path should be a path to file with 
		 * given text (that constraint won't be checked).
		 * 
		 * @param text			what text will initially be shown in editor.
		 * @param filePath		path where text inside editor is located on disk.
		 */
		public MyEditor(String text, Path filePath) {
			super(text);
			this.filePath = filePath;
			parent = new JScrollPane(this);
			getDocument().addDocumentListener(this);
			addCaretListener(this);
		}
		
		/**
		 * Initialize everything needed for editor 
		 * and creates editor with empty text.
		 * 
		 * @param filePath		path where text inside editor is located on disk, 
		 * 						can be {@code null} if it's not saved.
		 */
		public MyEditor(Path filePath) {
			this("", filePath);
		}

		/**
		 * Change file path where text inside this editor is saved on disk.
		 * Changing path will change window and tab title and also tab tip.
		 * 
		 * @param currentFilePath		file path where text in document is saved.
		 */
		public void setPath(Path currentFilePath) {
			filePath = currentFilePath;
			int index = tabPanel.indexOfComponent(parent);
			tabPanel.setTitleAt(index, filePath.getFileName().toString());
			tabPanel.setToolTipTextAt(index, filePath.toAbsolutePath().toString());
			if(tabPanel.getSelectedIndex() == index) {
				JNotepadPP.this.setTitle(filePath.toAbsolutePath().toString()+" - JNoptepad++");
			}
		}
		
		/**
		 * Get scroll panel in which is this editor.
		 * 
		 * @return parent of this component.
		 */
		public JScrollPane getScrollPane() {
			return parent;
		}

		@Override
		public void caretUpdate(CaretEvent e) {
			int line = 1;
            int column = 1;
			int position = getCaretPosition();
			try {
				line = getLineOfOffset(position);
				column = position - getLineStartOffset(line);
			} catch (BadLocationException e1) {
				//what can we do here?
				e1.printStackTrace();
			}
            line += 1;
            int mark = getCaret().getMark();
            int selection = Math.abs(mark - position);
            statusBar.updateCaretStatus(line, column, selection);
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			editorModified();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			editorModified();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			editorModified();
		}
		
		/**
		 * Sets modified, changes icon to red 
		 * and keeps updating length in status bar.
		 */
		private void editorModified() {
			modified = true;
			int index = tabPanel.indexOfComponent(parent);
			tabPanel.setIconAt(index, RED_ICON);
			statusBar.setLength(getDocument().getLength());
		}
	}

	/**
	 * Entry point.
	 * 
	 * @param args		not used.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}
}
