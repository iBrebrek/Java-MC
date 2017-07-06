package hr.fer.zemris.java.graphics;

import java.util.Scanner;

import hr.fer.zemris.java.graphics.raster.BWRaster;
import hr.fer.zemris.java.graphics.raster.BWRasterMem;
import hr.fer.zemris.java.graphics.shapes.Circle;
import hr.fer.zemris.java.graphics.shapes.Ellipse;
import hr.fer.zemris.java.graphics.shapes.GeometricShape;
import hr.fer.zemris.java.graphics.shapes.Rectangle;
import hr.fer.zemris.java.graphics.shapes.Square;
import hr.fer.zemris.java.graphics.shapes.Triangle;
import hr.fer.zemris.java.graphics.views.SimpleRasterView;

/**
 * Program that draws shapes on system out.
 * Program needs 1 or 2 arguments. 
 * If 1 argument then that's value of both width and height of raster,
 * if 2 arguments then first argument is width and second is height.
 * <p>
 * When program starts a single number is required.
 * That number defines how many commands will follow.
 * Every command but flip also requires some integer values.
 * Possible commands are: 
 * FLIP - toggles flip mode
 * SQUARE - draws square, needs top left corner and size
 * RECTANGLE - draws rectangle, needs top left corner, width and height,
 * CIRCLE - draws circle, needs center and radius
 * ELLIPSE - draws ellipse, needs center, horizontal and vertical radius
 * TRIANGLE - draws triangle, needs all 3 points
 * </p>
 * 
 * @author Ivica Brebrek
 * @version 1.0  (5.4.2016.)
 */
public class Demo {

	/**
	 * Entry point for this program.
	 * 
	 * @param args	see class documentation.
	 */
	public static void main(String[] args) {

		BWRaster raster = createRaster(args);
		
		System.out.println("Read class documentation too see what commands you can use.");
		System.out.println("How many commands are you planning to give?");
		
		int size = Reader.nextInt();
		if(size < 0) {
			System.err.println("Sorry, can not make negative number of shapes.");
			System.exit(-1);
		}
		
		if(size > 0) {
			System.out.println("Write each command in a new line, "
						+ "if you write more in the same line every but first will be ignored.");
			System.out.println("You said you were going to give " + size + " commands, "
					+ "if you give more than that " + (size+1) + ". and others will be ignored.");
		}
		
		GeometricShape[] shapes = new GeometricShape[size];
		
		for(int index = 0; index < shapes.length; index++) {
			String newLine = Reader.nextLine();
			while(newLine.trim().isEmpty()) newLine = Reader.nextLine();
			shapes[index] = createShape(newLine);
		}
		
		boolean flip = false;
		
		for(GeometricShape shape : shapes) {
			if(shape == null) {
				flip = !flip;
				if(flip) raster.enableFlipMode();
				else raster.disableFlipMode();
				
			} else {
				shape.draw(raster);
			}
		}
		
		Reader.close();
		
		new SimpleRasterView().produce(raster);
	}
	
	/**
	 * From arguments reads dimensions and creates BWRaster.
	 * 
	 * @param args	program's arguments.
	 * @return new raster.
	 */
	private static BWRaster createRaster(String[] args) {
		int width = 0;
		int height = 0;
		
		try {
			if(args.length == 1) {
				width = height = Integer.parseInt(args[0]);
			} else if (args.length == 2){
				width = Integer.parseInt(args[0]);
				height = Integer.parseInt(args[1]);
			} else {
				System.err.println("Invalid number of arguments.");
				System.exit(-1);
			}
		}catch(NumberFormatException exc) {
				System.err.println("Given argument is not integer.");
				System.exit(-1);
		}
		
		BWRaster raster = null;
		try {
			raster = new BWRasterMem(width, height);
		} catch(IllegalArgumentException exc) {
			System.err.println(exc.getMessage());
			System.exit(-1);
		}
		
		return raster;
	}
	
	/**
	 * From line creates geometric shape, if possible.
	 * 
	 * @param line		line being interpreted into shape.
	 * @return new geometric shape.
	 */
	private static GeometricShape createShape(String line) {
		final String triangle = "triangle";
		final String circle = "circle";
		final String ellipse = "ellipse";
		final String square = "square";
		final String rectangle = "rectangle";
		final String flip = "flip";
		
		String[] args = line.split("\\s+");
		
		try {
			switch(args[0].toLowerCase()) {
				case triangle:
					return new Triangle(Integer.parseInt(args[1]), Integer.parseInt(args[2]), 
									Integer.parseInt(args[3]), Integer.parseInt(args[4]), 
									Integer.parseInt(args[5]), Integer.parseInt(args[6]));
				
				case flip:
					return null;
				
				case circle:
					return new Circle(Integer.parseInt(args[1]), Integer.parseInt(args[2]),
								  Integer.parseInt(args[3]));
				
				case ellipse:
					return new Ellipse(Integer.parseInt(args[1]), Integer.parseInt(args[2]), 
								   Integer.parseInt(args[3]), Integer.parseInt(args[4]));
			
				case square:
					return new Square(Integer.parseInt(args[1]), Integer.parseInt(args[2]),
								  Integer.parseInt(args[3]));
				
				case rectangle: 
					return new Rectangle(Integer.parseInt(args[1]), Integer.parseInt(args[2]), 
						   			 Integer.parseInt(args[3]), Integer.parseInt(args[4]));
			
				default:
					System.err.println(args[0] + " is unknown command.");
					System.exit(-1);
			}
			
		} catch(NumberFormatException numberExc){
			System.err.println("Integer was expected but non-integer was given.");
			System.exit(-1);
		} catch(IndexOutOfBoundsException argsTooShort) {
			System.err.println("Not enough arguments for that shape.");
			System.exit(-1);
		} catch(Exception exc) {
			System.err.println(exc.getMessage());
			System.exit(-1);
		}
		
		return null; //because compiler still asks for return even with exit...
	}
	
	
	/**
	 * Nested class used to read from system in.
	 */
	private static class Reader {
		/** Scanner to read input */
		static Scanner input = new Scanner(System.in);
		
		/**
		 * Closes scanner.
		 */
		static void close() {
			input.close();
		}
		
		/**
		 * Reads next integer.
		 * 
		 * @return read integer.
		 */
		static int nextInt() {
			try {
				return input.nextInt();
			} catch(Exception exc) {
				System.err.println("Invalid argument: integer was expected.");
				System.exit(-1);
				return -1; //because compiler still asks for return even with exit...
			}
		}
		
		/**
		 * Reads next line.
		 * 
		 * @return next line as string.
		 */
		static String nextLine() {
			try {
				return input.nextLine();
			} catch(Exception exc) {
				System.err.println("Invalid argument: text was expected.");
				System.exit(-1);
				return null; //because compiler still asks for return even with exit...
			}
		}
	}

}
