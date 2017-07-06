package hr.fer.zemris.java.hw16.jvdraw.model.geometrics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;

import hr.fer.zemris.java.hw16.jvdraw.color.JColorArea;

/**
 * Geometrical object representing a single filled circle.
 * 
 * For more information on what this class 
 * can do see {@link GeometricalObject}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public class FilledCircle extends Circle {
	/** Color inside of circle. */
	private Color background;
	
	/** Data which will be saved if user chooses so. */
	private final TempData temp = new TempData();
	
	/**
	 * Initializes new filled circle with all properties.
	 * 
	 * @param name			object name.
	 * @param foreground	outline color.
	 * @param background	fill color.
	 * @param center		center of circle.
	 * @param radius		radius of circle.
	 */
	public FilledCircle(String name, Color foreground, Color background, Point center, int radius) {
		super(name, foreground, center, radius);
		this.background = background;
	}
	
	/**
	 * Initializes name and colors of this circle.
	 * Center and radius are set to 0.
	 * 
	 * @param name			object name.
	 * @param foreground	outline color.
	 * @param background	fill color.
	 */
	public FilledCircle(String name, Color foreground, Color background) {
		this(name, foreground, background, new Point(0,0), 0);
	}

	@Override
	protected void paintObject(Graphics g) {
		int x = center.x - radius;
		int y = center.y - radius;
		g.drawOval(x, y, radius*2, radius*2);
		g.setColor(background);
		g.fillOval(x, y, radius*2, radius*2);
	}
	
	@Override
	protected void addEditOptions(JPanel panel) {
		super.addEditOptions(panel);
		panel.add(new JLabel("Background:"));
		temp.background = new JColorArea(background);
		panel.add(temp.background);
	}

	@Override
	protected void saveChanges() {
		super.saveChanges();
		background = temp.background.getCurrentColor();
	}
	
	@Override
	public String asText() {
		return String.format(
			"FCIRCLE %d %d %d %d %d %d %d %d %d", 
			center.x, center.y, radius, 
			foreground.getRed(), foreground.getGreen(), foreground.getBlue(),
			background.getRed(), background.getGreen(), background.getBlue()
		);
	}
	
	@Override
	public GeometricalObject shiftBy(int x, int y) {
		Point c = new Point(center.x+x, center.y+y);
		return new FilledCircle(name, foreground, background, c, radius);
	}
	
	/**
	 * Class used to store data which
	 * will be saved if user chooses so.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (1.7.2016.)
	 */
	private static class TempData {
		/** Color area used to pick object background color. */
		JColorArea background = null;
	}
}
