package hr.fer.zemris.java.graphics.shapes;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Abstract representation of any geometric that can be looked at as an ellipse.
 * Examples would be circle and ellipse.
 * 
 * This is not same as ellipse because this class can not change radius.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (3.4.2016.)
 */
abstract class NonStretchableEllipse extends GeometricShape {
	/* 
	 * same question as in RectangularQuadrangle, 
	 * is there a better solution?
	 */
	
	/** x coordinate of the center. */
	protected int centerX;
	/** y coordinate of the center. */
	protected int centerY;
	/** Horizontal radius. */
	protected int horizontalRadius;
	/** Vertical radius. */
	protected int verticalRadius;

	/**
	 * Initializes center point and horizontal and vertical radius of this shape.
	 * 
	 * @param centerX			x coordinate of the center.
	 * @param centerY			y coordinate of the center.
	 * @param horizontalRadius	horizonal radius.
	 * @param verticalRadius	vertical radius.
	 */
	protected NonStretchableEllipse(int centerX, int centerY, 
								  int horizontalRadius, int verticalRadius) {
		
		if(horizontalRadius < 1 || verticalRadius < 1) {
			throw new IllegalArgumentException("Radius can not be lower than 1.");
		}
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.horizontalRadius = horizontalRadius;
		this.verticalRadius = verticalRadius;
	}
	
	/**
	 * Getter for x coordinate of center point.
	 * 
	 * @return x coordinate of the center.
	 */
	public int getCenterX() {
		return centerX;
	}

	/**
	 * Setter for x coordinate of center point.
	 * 
	 * @param centerX	x coordinate of the center.
	 */
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	/**
	 * Getter for y coordinate of center point.
	 * 
	 * @return y coordinate of the center.
	 */
	public int getCenterY() {
		return centerY;
	}

	/**
	 * Setter for y coordinate of center point.
	 * 
	 * @param centerY	y coordinate of the center.
	 */
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		/*
		 * My idea was this:
		 * -for every pixel you need some value (to move to that pixel)
		 * -sum of valueX and valueX must be 1 (basically, we interpolate it)
		 * -if one axis has less pixels, then that axis needs more value (in other words:
		 * 	if there are more x-s than y-s, x is easier so it requires less value)
		 * -now find out how many x-s and y-s were passed
		 * -every x/y required certain value, get total value used to get there
		 * -max value is any value needed to go full left, or full top, etc..
		 * -if we used more value than max value then we are out of the circle
		 */
		
		double sum = horizontalRadius + verticalRadius;
		double valueX = verticalRadius / sum;
		double valueY = horizontalRadius / sum;
		
		int distanceX = Math.abs(centerX - x);
		int distanceY = Math.abs(centerY - y);
		
		double valueDone = valueX*distanceX + valueY*distanceY;
		
		/*
		 * valueX*horzintal should be same as valueY*vertical, but double 
		 * rounds weirdly and in one test case it made a mistake around 16-th decimal place.
		 * So taking minimum of those two fixes that problem.
		 */
		double maxValue = Math.min(valueX*horizontalRadius, valueY*verticalRadius);
		
		return valueDone < maxValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(BWRaster r) {
		
		double sum = horizontalRadius + verticalRadius;
		double valueX = verticalRadius / sum;
		double valueY = horizontalRadius / sum;

		double maxValue = Math.min(valueX*horizontalRadius, valueY*verticalRadius);
		
		int maxX = Math.min(horizontalRadius + centerX, r.getWidth()); 
		int maxY = Math.min(verticalRadius + centerY, r.getHeight());
		int minX = Math.max(centerX - horizontalRadius, 0); //because raster starts at (0,0)
		int minY = Math.max(centerY - verticalRadius, 0);
		
		class Painter {
			/*
			 * This local class is used to turn on all the pixels within this shape.
			 * 
			 * To turn on the pixels call method startPainting(), nothing else.
			 * 
			 * -startPainting starts at center and sends work to every adjacent pixel
			 * -paintLeft sends work to next upper, lower and left pixel
			 * -paintRight sends work to next right, upper and lower pixel
			 * -paintUp sends work to next upper pixel
			 * -paintDown sends work to next lower pixel
			 * 
			 * This way no pixel will be visited more than once.
			 * 
			 * Each method, of those 5, will check if it is within boundaries of min and max x/y 
			 * and if it has low enough value. If all is true then that pixel is turned on.
			 * If any of those conditions is not true it doesn't go any further.
			 */
			
			void startPainting() {
				paintLeft(valueX, centerX - 1, centerY);
				paintRight(valueX, centerX + 1, centerY);
				paintUp(valueY, centerX, centerY + 1);
				paintDown(valueY, centerX, centerY - 1);
				if(notInRaster(centerX, centerY)) return;
				r.turnOn(centerX, centerY);
				
			}
			
			void paintLeft(double currentValue, int x, int y) {
				if(notInRaster(x, y)) return;
				if(currentValue >= maxValue) return;
				r.turnOn(x, y);
				paintLeft(currentValue + valueX, x - 1, y);
				paintDown(currentValue + valueY, x, y - 1);
				paintUp(currentValue + valueY, x, y + 1);
			}
			
			void paintRight(double currentValue, int x, int y) {
				if(notInRaster(x, y)) return;
				if(currentValue >= maxValue) return;
				r.turnOn(x, y);
				paintRight(currentValue + valueX, x + 1, y);
				paintDown(currentValue + valueY, x, y - 1);
				paintUp(currentValue + valueY, x, y + 1);
			}
			
			void paintUp(double currentValue, int x, int y) {
				// x doesn't matter here, but we need it to turn the pixel on
				if(notInRaster(x, y)) return;
				if(currentValue >= maxValue) return;
				r.turnOn(x, y);
				paintUp(currentValue + valueY, x, y + 1);
			}
			
			void paintDown(double currentValue, int x, int y) {
				// x doesn't matter here, but we need it to turn the pixel on
				if(notInRaster(x, y)) return;
				if(currentValue >= maxValue) return;
				r.turnOn(x, y);
				paintDown(currentValue + valueY, x, y - 1);
			}
			
			boolean notInRaster(int x, int y) {
				if(x < minX) return true;
				if(x >= maxX) return true;
				if(y < minY) return true;
				if(y >= maxY) return true;
				return false;
			}
		}
		
		new Painter().startPainting();
	}
}
