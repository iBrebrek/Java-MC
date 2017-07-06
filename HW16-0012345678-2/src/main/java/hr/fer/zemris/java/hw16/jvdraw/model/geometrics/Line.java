package hr.fer.zemris.java.hw16.jvdraw.model.geometrics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Geometrical object representing a single line.
 * 
 * For more information on what this class 
 * can do see {@link GeometricalObject}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public class Line extends GeometricalObject {	
	/** Starting point of line. */
	private Point start;
	/** Ending point of line. */
	private Point end;
	
	/** Temporal data to be saved if user chooses so. */
	private final TempData temp = new TempData();
	
	/**
	 * Initializes new line with name and line color,
	 * and starting and ending point.
	 * 
	 * @param name			object name.
	 * @param foreground	line color.
	 * @param start			starting point.
	 * @param end			ending point.
	 */
	public Line(String name, Color foreground, Point start, Point end) {
		super(name, foreground);
		this.start = start;
		this.end = end;
	}

	/**
	 * Initializes new line with name and line color. 
	 * Both points will be (0,0).
	 * 
	 * @param name			object name.
	 * @param foreground	line color.
	 */
	public Line(String name, Color foreground) {
		this(name, foreground, new Point(0,0), new Point(0,0));
	}
	
	@Override
	protected void paintObject(Graphics g) {
		g.drawLine(start.x, start.y, end.x, end.y);
	}

	@Override
	public void onStartChange(Point start) {
		this.start = start;
	}

	@Override
	public void onEndChange(Point end) {
		this.end = end;
	}	
	
	@Override
	protected void addEditOptions(JPanel panel) {
		panel.add(new JLabel("First point x:"));
		temp.startX.setValue(start.x);
		panel.add(new JSpinner(temp.startX));
		
		panel.add(new JLabel("First point y:"));
		temp.startY.setValue(start.y);
		panel.add(new JSpinner(temp.startY));
		
		panel.add(new JLabel("Second point x:"));
		temp.endX.setValue(end.x);
		panel.add(new JSpinner(temp.endX));
		
		panel.add(new JLabel("Second point y:"));
		temp.endY.setValue(end.y);
		panel.add(new JSpinner(temp.endY));
	}

	@Override
	protected void saveChanges() {
		start.x = (int)temp.startX.getValue();
		start.y = (int)temp.startY.getValue();
		end.x = (int)temp.endX.getValue();
		end.y = (int)temp.endY.getValue();
	}
	
	@Override
	public String asText() {
		return String.format(
			"LINE %d %d %d %d %d %d %d", 
			start.x, start.y, 
			end.x, end.y, 
			foreground.getRed(), foreground.getGreen(), foreground.getBlue()
		);
	}
	
	@Override
	public GeometricalObject shiftBy(int x, int y) {
		Point s = new Point(start.x+x, start.y+y);
		Point e = new Point(end.x+x, end.y+y);
		return new Line(name, foreground, s, e);
	}

	@Override
	public Point getMin() {
		return new Point(
			start.x < end.x ? start.x : end.x,
			start.y < end.y ? start.y : end.y
		);
	}

	@Override
	public Point getMax() {
		return new Point(
			start.x > end.x ? start.x : end.x,
			start.y > end.y ? start.y : end.y
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
		/** Model for spinner used to pick X of starting point. */
		SpinnerNumberModel startX = new SpinnerNumberModel(
			Integer.valueOf(0), 
			Integer.valueOf(Integer.MIN_VALUE),
			Integer.valueOf(Integer.MAX_VALUE), 
			Integer.valueOf(1)
		);
		/** Model for spinner used to pick Y of starting point. */
		SpinnerNumberModel startY = new SpinnerNumberModel(
			Integer.valueOf(0), 
			Integer.valueOf(Integer.MIN_VALUE),
			Integer.valueOf(Integer.MAX_VALUE), 
			Integer.valueOf(1)
		);
		/** Model for spinner used to pick X of ending point. */
		SpinnerNumberModel endX = new SpinnerNumberModel(
			Integer.valueOf(0), 
			Integer.valueOf(Integer.MIN_VALUE),
			Integer.valueOf(Integer.MAX_VALUE), 
			Integer.valueOf(1)
		);
		/** Model for spinner used to pick Y of ending point. */
		SpinnerNumberModel endY = new SpinnerNumberModel(
			Integer.valueOf(0), 
			Integer.valueOf(Integer.MIN_VALUE),
			Integer.valueOf(Integer.MAX_VALUE), 
			Integer.valueOf(1)
		);
	}
}
