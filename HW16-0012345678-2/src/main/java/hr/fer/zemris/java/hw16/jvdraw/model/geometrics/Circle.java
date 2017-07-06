package hr.fer.zemris.java.hw16.jvdraw.model.geometrics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Geometrical object representing a single circle.
 * 
 * For more information on what this class 
 * can do see {@link GeometricalObject}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public class Circle extends GeometricalObject {
	/** Circle center point. */
	protected Point center;
	/** Circle radius. */
	protected int radius;
	
	/** Data which will be saved if user chooses so. */
	private TempData temp = new TempData();

	/**
	 * Initializes new circle with all needed properties.
	 * 
	 * @param name			object name.
	 * @param foreground	outline color.
	 * @param center		center of circle.
	 * @param radius		radius of circle.
	 */
	public Circle(String name, Color foreground, Point center, int radius) {
		super(name, foreground);
		this.center = center;
		this.radius = radius;
	}
	
	/**
	 * Initializes name and outline color of this circle.
	 * Center and radius are set to 0.
	 * 
	 * @param name			object name.
	 * @param foreground	outline color.
	 */
	public Circle(String name, Color foreground) {
		this(name, foreground, new Point(0,0), 0);
	}
	
	@Override
	public void onStartChange(Point start) {
		center = start;
	}
	
	@Override
	protected void paintObject(Graphics g) {
		int x = center.x - radius;
		int y = center.y - radius;
		g.drawOval(x, y, radius*2, radius*2);
	}

	@Override
	public void onEndChange(Point end) {
		radius = Double.valueOf(Math.sqrt(Math.pow(center.x-end.x, 2) + Math.pow(center.y-end.y, 2))).intValue();
	}
	
	@Override
	protected void addEditOptions(JPanel panel) {
		panel.add(new JLabel("Center x:"));
		temp.centerX.setValue(center.x);
		panel.add(new JSpinner(temp.centerX));
		
		panel.add(new JLabel("Center y:"));
		temp.centerY.setValue(center.y);
		panel.add(new JSpinner(temp.centerY));
		
		panel.add(new JLabel("Radius:"));
		temp.radius.setValue(radius);
		panel.add(new JSpinner(temp.radius));
	}

	@Override
	protected void saveChanges() {
		center.x = (int)temp.centerX.getValue();
		center.y = (int)temp.centerY.getValue();
		radius = (int)temp.radius.getValue();
	}
	
	@Override
	public String asText() {
		return String.format(
			"CIRCLE %d %d %d %d %d %d", 
			center.x, center.y, radius, 
			foreground.getRed(), foreground.getGreen(), foreground.getBlue()
		);
	}
	
	@Override
	public GeometricalObject shiftBy(int x, int y) {
		Point c = new Point(center.x+x, center.y+y);
		return new Circle(name, foreground, c, radius);
	}

	@Override
	public Point getMin() {
		return new Point(
			center.x-radius,
			center.y-radius
		);
	}

	@Override
	public Point getMax() {
		return new Point(
			center.x+radius,
			center.y+radius
		);
	}
	
	/**
	 * Class used to store data which
	 * will be saved if user chooses so.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (1.7.2016.)
	 */
	private static class TempData {
		/** Model for spinner used to pick X of center point. */
		SpinnerNumberModel centerX = new SpinnerNumberModel(
			Integer.valueOf(0), 
			Integer.valueOf(Integer.MIN_VALUE),
			Integer.valueOf(Integer.MAX_VALUE), 
			Integer.valueOf(1)
		);
		/** Model for spinner used to pick Y of center point. */
		SpinnerNumberModel centerY = new SpinnerNumberModel(
			Integer.valueOf(0), 
			Integer.valueOf(Integer.MIN_VALUE),
			Integer.valueOf(Integer.MAX_VALUE), 
			Integer.valueOf(1)
		);
		/** Model for spinner used to pick radius. */
		SpinnerNumberModel radius = new SpinnerNumberModel(
			Integer.valueOf(0), 
			Integer.valueOf(0),
			Integer.valueOf(Integer.MAX_VALUE), 
			Integer.valueOf(1)
		);
	}
}
