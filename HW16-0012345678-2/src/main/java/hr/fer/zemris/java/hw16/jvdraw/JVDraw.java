package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw16.jvdraw.color.ColorStatus;
import hr.fer.zemris.java.hw16.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.GeometricsModel;
import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.model.list.DrawingObjectListModel;

/**
 * This program offers user to draw and open geometrical objects.
 * <br>
 * This application offers user to paint lines,
 * circles and filled circle.
 * For every object he can change foreground
 * and/or background color.
 * <br>
 * Application has list of all object on the right.
 * To delete any object simply select it (multiple
 * selection is available) and press DEL.
 * <br>
 * Application offers menu which allows user
 * to open other geometrical objects,
 * save current canvas or
 * extract current canvas as
 * png, gif or jpg.
 * <br>
 * When application is being closed,
 * if canvas was modified user is aksed if
 * he wants to keep changes.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public class JVDraw extends JFrame {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Model containing all object which are painted on canvas and displayed on list. */
	private final DrawingModel model = new GeometricsModel();
	/** Object which contains all actions used by this application. */
	private final JVDrawActions actions = new JVDrawActions(this, model);
	
	/**
	 * Initializes this window.
	 * Window is centered on the screen.
	 * Calls {@link #initGUI()}.
	 */
	public JVDraw() {
		setTitle("JVDraw");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				actions.exitProgramAction.actionPerformed(null);
			}
		});
		
		initGUI();
	}

	/**
	 * Initializes GUI.
	 * Initializes canvas and list and calls
	 * {@link #createToolbar(JColorArea, JColorArea, ObjectFactory)}
	 * and {@link #createMenu()}.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JColorArea foreground = new JColorArea(Color.BLACK);
		JColorArea background = new JColorArea(Color.WHITE);
		
		ObjectFactory factory = new ObjectFactory(foreground, background);
		JDrawingCanvas canvas = new JDrawingCanvas(model, factory);

		cp.add(canvas, BorderLayout.CENTER);
		JList<GeometricalObject> list = new JList<>(new DrawingObjectListModel(model));
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(100, 100));
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					for(GeometricalObject o : list.getSelectedValuesList()) {
						model.delete(o);
					}
					list.clearSelection();
				}
			}
		});
		list.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        if (evt.getClickCount() == 2) {
		            int index = list.locationToIndex(evt.getPoint());
		            model.edit(JVDraw.this, model.getObject(index));
		        }
		    }
		});
		
		cp.add(scrollPane, BorderLayout.LINE_END);
		cp.add(new ColorStatus(foreground, background), BorderLayout.PAGE_END);
		
		createToolbar(foreground, background, factory);
		createMenu();
	}

	/**
	 * Creates toolbar for this program.
	 * Toolbar has color choosers for 
	 * foreground and background color,
	 * and toggle buttons to choose
	 * which object will be painted on canvas.
	 * 
	 * @param foreground	color area used to choose foreground color.
	 * @param background	color area used to choose background color.
	 * @param factory factory which knows how to create objects.
	 */
	private void createToolbar(JColorArea foreground, JColorArea background, ObjectFactory factory) {
		JToolBar toolBar = new JToolBar("JVDraw - Toolbar");	
		toolBar.add(foreground);
		toolBar.add(background);
		toolBar.addSeparator();
		
		ButtonGroup group = new ButtonGroup() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setSelected(ButtonModel model, boolean selected) {
				if (selected) {
					super.setSelected(model, selected);
				} else {
					factory.setType(ObjectFactory.Type.NOTHING);
					clearSelection();
				}
			}
		};
		
		JToggleButton line = new JToggleButton("Line");
		JToggleButton circle = new JToggleButton("Circle");
		JToggleButton fCircle = new JToggleButton("Filled Circle");
		
		group.add(line);
		group.add(circle);
		group.add(fCircle);
		
		line.addActionListener(l -> {
			if(line.isSelected()) {
				factory.setType(ObjectFactory.Type.LINE);
			}
		});
		circle.addActionListener(l -> {
			if(circle.isSelected()) {
				factory.setType(ObjectFactory.Type.CIRCLE);
			}
		});
		fCircle.addActionListener(l -> {
			if(fCircle.isSelected()) {
				factory.setType(ObjectFactory.Type.FILLED_CIRCLE);
			}
		});
		
		toolBar.add(line);
		toolBar.add(circle);
		toolBar.add(fCircle);
		
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	/**
	 * Creates all menus used by this program.
	 * Menu items are: open, save, save as, exit, extract.
	 */
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		fileMenu.add(new JMenuItem(actions.openCanvasAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(actions.saveCanvasAction));
		fileMenu.add(new JMenuItem(actions.saveAsCanvasAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(actions.extractCanvasAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(actions.exitProgramAction));
		
		setJMenuBar(menuBar); 
	}
	
	/**
	 * Entry point.
	 * 
	 * @param args		not used.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> 
			new JVDraw().setVisible(true)
		);
	}
}