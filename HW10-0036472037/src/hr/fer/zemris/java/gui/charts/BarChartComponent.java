package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JComponent;

/**
 * Component showing bar chart.
 * Component will take information from object {@code BarChart}
 * and use it's data to draw that chart.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (16.5.2016.)
 */
public class BarChartComponent extends JComponent {
	/** For serialization. */
	private static final long serialVersionUID = 8445190848376584528L;
	
	/** Number of pixels between text and numbers. */
	private static final int GAP_AFTER_TEXT = 15;
	/** NUmber of pixels between numbers and graph. */
	private static final int GAP_AFTER_NUMBERS = 10;
	/** Number of pixels after graph (right and top side). */
	private static final int GAP_AT_THE_END = 20;
	
	/** Color of grid on drawn chart. */
	private static final Color GRID_COLOR = new Color(239, 223, 194);
	/** Color of columns on drawn chart. */
	private static final Color COLUMN_COLOR = new Color(244, 119, 72);
	/** Color of y and x axis on drawn chart. */
	private static final Color AXIS_COLOR = new Color(186, 185, 185);
	
	/** Chart being drawn. */
	private BarChart chart;
	
	/** Height of text, both description and numbers. */
	private int textHeight;
	/** Width of biggest number. */
	private int maxNumberWidth;
	/** Y coordinate of the left text. */
	private int yLeftText;
	/** X coordinate of the bottom text. */
	private int xBottomText;
	/** X coordinate of left-bottom graph point. */
	private int chartStartX;
	/** Y coordinate of left-bottom graph point. */
	private int chartStartY;
	/** X coordinate of right-top graph point. */
	private int chartEndX;
	/** Y coordinate of right-top graph point. */
	private int chartEndY;
	/** Number of rows (and numbers on y axis). */
	private int numberOfRows;
	/** Height of each row. */
	private int rowHeight;
	/** Number of columns (and numbers on x axis). */
	private int numberOfColumns;
	/** Width of each column. */
	private int columnWidth;
//	private int actualStep; //maybe needed to scale?

	/**
	 * Initializes chart data that will be drawn.
	 * 
	 * @param chart		chart being drawn.
	 * 
	 * @throws IllegalArgumentException 	if chart is null.
	 */
	public BarChartComponent(BarChart chart) {
		if(chart == null) {
			throw new IllegalArgumentException("Chart can not be null.");
		}
		this.chart = chart;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		measure(g2d);
		drawDescription(g2d);
		drawNumbers(g2d);
		drawGrid(g2d);        
		drawColumns(g2d);
	}
	
	/**
	 * On given graphics draws graph columns.
	 * 
	 * @param g2d		where to draw.
	 */
	private void drawColumns(Graphics2D g2d) {
		Color saved = g2d.getColor(); //remember color
		g2d.setColor(COLUMN_COLOR);
		List<XYValue> list = chart.getList();
		
		
		for(int c = 0; c < list.size(); c++) {
			Double yColumn = (double)(list.get(c).getY()-chart.getMinY())/chart.getStepY()*rowHeight;
			g2d.fillRect(
					chartStartX + c*columnWidth, 
					chartStartY - yColumn.intValue(), 
					columnWidth-1, 
					yColumn.intValue()
			);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(
					chartStartX + c*columnWidth + columnWidth,
					chartStartY - yColumn.intValue() + 5,
					5, 
					yColumn.intValue() - 5
				);
				g2d.setColor(COLUMN_COLOR);
		}
		
		g2d.setColor(saved);
	}

	/**
	 * On given graphics draws graph grid.
	 * And x-y axis.
	 * 
	 * @param g2d		where to draw.
	 */
	private void drawGrid(Graphics2D g2d) {
		Color saved = g2d.getColor(); //remember color
        g2d.setColor(GRID_COLOR);
        
        int columnX = chartStartX + columnWidth;
        for(int x = 0; x < numberOfColumns; x++, columnX += columnWidth) {
        	g2d.drawLine(columnX, chartStartY, columnX, chartEndY);
        }
        
        int rowY = chartStartY - rowHeight;
        for(int y = 0; y < numberOfRows; y++, rowY -= rowHeight) {
        	g2d.drawLine(chartStartX, rowY, chartEndX, rowY);
        }
        
        g2d.setColor(AXIS_COLOR);
        g2d.drawLine(chartStartX, chartStartY, chartStartX, GAP_AT_THE_END/2);
        g2d.fillPolygon(new int[] {chartStartX, chartStartX-GAP_AT_THE_END/4, chartStartX+GAP_AT_THE_END/4},
                	    new int[] {0, GAP_AT_THE_END/2, GAP_AT_THE_END/2}, 3);
        g2d.drawLine(chartStartX, chartStartY, chartEndX+GAP_AT_THE_END/2, chartStartY);
        g2d.fillPolygon(new int[] {getWidth(), getWidth()-GAP_AT_THE_END/2, getWidth()-GAP_AT_THE_END/2},
        	    		new int[] {chartStartY, chartStartY-GAP_AT_THE_END/4, chartStartY+GAP_AT_THE_END/4}, 3);
        
        g2d.setColor(saved);
	}

	/**
	 * Writes number on x and y axis.
	 * 
	 * @param g2d		where to draw.
	 */
	private void drawNumbers(Graphics2D g2d) {
		List<XYValue> list = chart.getList();
		FontMetrics metrics = g2d.getFontMetrics();
        
        int bottomNumberX = chartStartX + columnWidth/2;
        int bottomNumberY = chartStartY + textHeight + GAP_AFTER_NUMBERS;
        for(int x = 0; x < numberOfColumns; x++, bottomNumberX += columnWidth) {
        	String number = String.valueOf(list.get(x).getX());
        	g2d.drawString(number, bottomNumberX+metrics.stringWidth(number)/2, bottomNumberY);
        }
        
        int leftNumberX = chartStartX - GAP_AFTER_NUMBERS;
        int leftNumberY = chartStartY;
        for(int y = chart.getMinY(); y <= chart.getMaxY(); y+=chart.getStepY(), leftNumberY -= rowHeight) {
        	String number = String.valueOf(y);
        	g2d.drawString(number, leftNumberX - metrics.stringWidth(number), leftNumberY);
        }
	}

	/**
	 * On given graphics writes description for both x and y axis.
	 * 
	 * @param g2d		where to draw.
	 */
	private void drawDescription(Graphics2D g2d) {
		AffineTransform defaultAt = g2d.getTransform();
        // rotates the coordinate by 90 degree counterclockwise
        AffineTransform at = new AffineTransform();
        at.rotate(- Math.PI / 2);
        g2d.setTransform(at);
        g2d.drawString(chart.getyText(), -yLeftText, textHeight);
        g2d.setTransform(defaultAt);
        g2d.drawString(chart.getxText(), xBottomText, getHeight()-5);  
	}

	/**
	 * Measures given graphics, measures will be used to draw every element.
	 * This must be called before drawing.
	 * 
	 * @param g2d		what to measure.
	 */
	private void measure(Graphics2D g2d) {
		Dimension dimension = getSize();
		FontMetrics metrics = g2d.getFontMetrics();
		textHeight = metrics.getHeight();
		maxNumberWidth = metrics.stringWidth(String.valueOf(chart.getMaxY()));
		int heightTillGraph = textHeight + GAP_AFTER_TEXT + textHeight + GAP_AFTER_NUMBERS;
		int widthTillGraph = textHeight + GAP_AFTER_TEXT + maxNumberWidth + GAP_AFTER_NUMBERS;
		
		yLeftText = dimension.height - heightTillGraph - GAP_AT_THE_END;
		yLeftText = yLeftText / 2 + metrics.stringWidth(chart.getyText()) / 2;
		yLeftText += GAP_AT_THE_END;
		
		xBottomText = dimension.width - widthTillGraph - GAP_AT_THE_END;
		xBottomText = xBottomText / 2 - metrics.stringWidth(chart.getxText()) / 2;
		xBottomText += heightTillGraph;
		
		chartStartX = widthTillGraph;
		chartStartY = dimension.height - heightTillGraph;
		chartEndX = dimension.width - GAP_AT_THE_END;
		chartEndY = GAP_AT_THE_END;
		
		//just in case someone changed data
		numberOfRows = (chart.getMaxY()-chart.getMinY()) / chart.getStepY();
		numberOfColumns = chart.getList().size();
		rowHeight = (chartStartY-chartEndY) / numberOfRows;
		columnWidth = (chartEndX-chartStartX) / numberOfColumns;
	}
}
