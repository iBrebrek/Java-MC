package hr.fer.zemris.java.hw16.jvdraw.model.geometrics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.jvdraw.color.JColorArea;

/**
 * This is an abstract class of all geometrical 2D objects which can draw itself on canvas.
 * <br>
 * For information about painting see: 
 * {@link #paint(Graphics)} and {@link #paintObject(Graphics)}.
 * And for painting with mouse see:
 * {@link #onStartChange(Point)} and {@link #onEndChange(Point)}.
 * <br>
 * For information about modifying object see:
 * {@link #edit(Component)}, {@link #addEditOptions(JPanel)}
 * and {@link #saveChanges()}.
 * <br>
 * For information about saving object as text see:
 * {@link #asText()}.
 * <br>
 * For information about shifting object see:
 * {@link #shiftBy(int, int)}.
 * 
 * <br>
 * Other than that, this class also offers
 * {@link #getMax()} and {@link #getMin()}
 * to retrieves extremes of this 2D object.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public abstract class GeometricalObject {
	/** Object name. */
	protected String name;
	/** Object foreground color. */
	protected Color foreground;
	
	/** Data which will be saved if user chooses so. */
	private final TempData temp = new TempData();
	
	/**
	 * Initializes new geometrical object
	 * with given name and foreground color.
	 * 
	 * @param name			object name.
	 * @param foreground	foreground color.
	 */
	public GeometricalObject(String name, Color foreground) {
		this.name = name;
		this.foreground = foreground;
	}
	
	/**
	 * Paints itself to give given graphics.
	 * It is not needed to set foreground 
	 * color because method {@link #paint(Graphics)}
	 * already did so.
	 * 
	 * @param g		where to paint itself.
	 */
	protected abstract void paintObject(Graphics g);
	
	/**
	 * On given panel adds GUI with which
	 * user can change object's properties.
	 * 
	 * Only offer GUI for newly added properties.
	 * 
	 * Given panel has grid layout with 2 columns
	 * and n number of rows.
	 * 
	 * @param panel		panel on which GUI should be added.	
	 */
	protected abstract void addEditOptions(JPanel panel);
	
	/**
	 * Save changes stored in {@link TempData}.
	 */
	protected abstract void saveChanges();
	
	/**
	 * Called when object drawing is started on canvas.
	 * This is called on first mouse click.
	 * 
	 * @param start		coordinates of first mouse click.
	 */
	public abstract void onStartChange(Point start);
	
	/**
	 * Called when object drawing is finished on canvas.
	 * This is called on second mouse click.
	 * 
	 * @param end		coordinates of second mouse click.
	 */
	public abstract void onEndChange(Point end);
	
	/**
	 * Return string which is used to 
	 * store this object in jvd file.
	 * 
	 * @return this object for jvd file.
	 */
	public abstract String asText();
	
	/**
	 * Shift this object by given x and given y.
	 * This object must stay the same.
	 * Returned object is newly created object.
	 * 
	 * Shifting is done by adding given x and y
	 * to existing points.
	 * 
	 * @param x			shift to right-left.
	 * @param y			shift to up-down.
	 * @return new object which is result of shifting.
	 */
	public abstract GeometricalObject shiftBy(int x, int y);
	
	/**
	 * Get minimal coordinates of this object.
	 * 
	 * @return minimal coordinates of this object.
	 */
	public abstract Point getMin();
	
	/**
	 * Get maximum coordinates of this object.
	 * 
	 * @return maximum coordinates of this object.
	 */
	public abstract Point getMax();
	
	/**
	 * Paints this object to the given graphics. 
	 * When finished, given graphics will have
	 * same color it had when method was called.
	 * 
	 * This method will call method
	 * {@link #paintObject(Graphics)},
	 * which will do actual painting.
	 * 
	 * @param g		where to draw this object.
	 */
	public final void paint(Graphics g) {
		Color saved = g.getColor();
		g.setColor(foreground);
		paintObject(g);
		g.setColor(saved);
	}
	
	/**
	 * Opens dialog center in given parent.
	 * Dialog offers edit of all properties
	 * of this object.
	 * 
	 * Changes are saved only if OK is clicked
	 * in dialog.
	 * 
	 * @param parent		where to center dialog.
	 */
	public final void edit(Component parent) {
		int option = JOptionPane.showConfirmDialog(
			parent, 
			editingDialog(), 
			"Edit geometrical object", 
			JOptionPane.OK_CANCEL_OPTION, 
			JOptionPane.PLAIN_MESSAGE
		);
		if(option == JOptionPane.OK_OPTION) {
			name = temp.tempName.getText();
			foreground = temp.tempColor.getCurrentColor();
			saveChanges();
		}
	}
	
	/**
	 * Crates panel where will be GUI for editing.
	 * 
	 * Calls {@link #addEditOptions(JPanel)} so
	 * all classes which extend this one can
	 * add GUI components.
	 * 
	 * @return panel which will be placed in dialog.
	 */
	private Object editingDialog() {
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Name: "));
		temp.tempName = new JTextField(name);
		panel.add(temp.tempName);
		panel.add(new JLabel("Foreground: "));
		temp.tempColor = new JColorArea(foreground);
		panel.add(temp.tempColor);
		addEditOptions(panel);
		return panel;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Class used to store data which
	 * will be saved if user chooses so.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (1.7.2016.)
	 */
	private static class TempData {
		/** Text field used to pick object name. */
		JTextField tempName = null;
		/** Color area used to pick object foreground color. */
		JColorArea tempColor = null;
	}
}
