package hr.fer.zemris.java.gui.charts;

import java.util.Collections;
import java.util.List;

/**
 * Class used to store every bar chart data.
 * Stores list of all column values,
 * axis descriptions and numbers,
 * start, end and step of y axis.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.5.2016.)
 */
public class BarChart {
	/** All column values. */
	private final List<XYValue> list;
	/** Description of x axis. */
	private final String xText;
	/** Description of y axis. */
	private final String yText;
	/** First number on y axis. */
	private final int minY;
	/** Last number on y axis. */
	private final int maxY;
	/** Steps between first and last number on y axis. */
	private final int stepY;
	
	/**
	 * Initializes all data needed for one bar chart.
	 * 
	 * If {@code maxY-minY} can't be divided by {@code stepY}
	 * stepY will be set to next higher integer that will divide.
	 * 
	 * @param list		column values.
	 * @param xText		x axis description.
	 * @param yText		y axis description.
	 * @param minY		first number on y axis.
	 * @param maxY		last number on y axis.
	 * @param stepY		steps between first and last number on y axis.
	 * 
	 * @throws IllegalArgumentException		if {@code minY > maxY}.
	 */
	public BarChart(List<XYValue> list, String xText, String yText, int minY, int maxY, int stepY) {
		if(minY > maxY) {
			throw new IllegalArgumentException("Minimum can't be bigger than maximum.");
		}
		this.list = list;
		Collections.sort(this.list, (a, b) -> a.getX()-b.getX());
		this.xText = xText;
		this.yText = yText;
		this.minY = minY;
		this.maxY = maxY;
		
		int dif = maxY - minY;
		if(stepY > dif) {
			this.stepY = dif;
		} else {
			while(dif % stepY != 0) {
				stepY++;
			}
			this.stepY = stepY;
		}
	}

	/**
	 * Get list of column values.
	 * 
	 * @return column values.
	 */
	public List<XYValue> getList() {
		return list;
	}

	/**
	 * X axis description.
	 * 
	 * @return x axis description.
	 */
	public String getxText() {
		return xText;
	}
	
	/**
	 * Y axis description.
	 * 
	 * @return y axis description.
	 */
	public String getyText() {
		return yText;
	}

	/**
	 * Get first number to be shown on y axis.
	 * 
	 * @return first number on y axis.
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Get last number to be shown on y axis.
	 * 
	 * @return last number on y axis.
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Get steps between first and last number to be shown on y axis.
	 * 
	 * @return y axis step.
	 */
	public int getStepY() {
		return stepY;
	}
}
