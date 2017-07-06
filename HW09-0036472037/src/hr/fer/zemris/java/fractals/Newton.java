package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.java.complex.Complex;
import hr.fer.zemris.java.complex.ComplexPolynomial;
import hr.fer.zemris.java.complex.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * Draws fractals derived from Newton-Raphson iteration.
 * The program asks user to enter roots
 * and then it starts fractal viewer and display the fractal.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.5.2016.)
 */
public class Newton {
	
	/** "Command" to stop input. */
	private static String DONE = "done";
	
	/** Derivation used in FractalWorker. */
	private static ComplexPolynomial derived;
	/** Polynomial used in FractalWorker. */
	private static ComplexPolynomial polynomial;
	/** Root polynomial used in FractalWorker. */
	private static ComplexRootedPolynomial rooted;
	
	//those constants are here instead of in FractalWorker because here are easier to find.
	/** Maximum iterations used in FractalWorker. **/
	private static final int MAX_ITERATIONS = Integer.MAX_VALUE;
	/** Convergence threshold used in FractalWorker. **/
	private static final double CONVERGENCE_THRESHOLD = 0.001;
	/** Convergence threshold used in FractalWorker. **/
	private static final double ROOT_THRESHOLD = 0.002;
	
	/**
	 * Entry point.
	 * 
	 * @param args		not used.
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter '"+DONE+"' when done.");
		
		List<Complex> roots = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);

		while(true) {
			System.out.print("Root "+(roots.size()+1)+"> ");
			String line = scanner.nextLine();
			if(line == null) break;
			line = line.trim();
			if(line.isEmpty()) continue;
			if(line.equalsIgnoreCase(DONE)) break;
			try {
				roots.add(Parser.parse(line));
			} catch(NumberFormatException stopProgram) {
				System.out.println(stopProgram.getMessage() + " Write again.");
				continue;
			}
		}
		
		scanner.close();
		System.out.println("Image of fractal will appear shortly. Thank you.");

		rooted = new ComplexRootedPolynomial(roots.toArray(new Complex[roots.size()]));
		polynomial = rooted.toComplexPolynom();
		derived = polynomial.derive();
		
		FractalViewer.show(new FractalProducer());
	}
	
	/**
	 * Class which draws fractal. Work is done parallel.
	 * Work is represented by class {@code FractalTask}.
	 */
	private static class FractalProducer implements IFractalProducer {
		/** Executor service used in fractal production .*/
		private ExecutorService executorService;
		/** Number of available processors for this program. */
		private int numberOfAvailableProcessors;
		
		/**
		 * Initializes executor service.
		 */
		public FractalProducer() {
			numberOfAvailableProcessors = Runtime.getRuntime().availableProcessors();
			executorService = Executors.newFixedThreadPool(
					numberOfAvailableProcessors, 
					(runnable)-> { //DaemonicThreadFactory
						Thread newThread = new Thread(runnable); 
						newThread.setDaemon(true); 
						return newThread;
					}
			);
		} 
		
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
					long requestNo, IFractalResultObserver observer) {
			
			short[] data = new short[width*height];
			int numberOfJobs = 8 * numberOfAvailableProcessors;
			int rangeOfY = height / numberOfJobs;
			
			List<Future<Void>> results = new ArrayList<Future<Void>>();
			
			for(int i = 0; i < numberOfJobs; i++) {
				int yMin = i*rangeOfY;
				int yMax;
				if(i == numberOfJobs-1) {
					yMax = height - 1; //rangeOfY is Integer so dividing might leave some pixels unattended
				} else {
					yMax = yMin + rangeOfY-1;
				}
				
				FractalTask task = new FractalTask(reMin, reMax, imMin, imMax, width, height, yMin, yMax, data);
				results.add(executorService.submit(task));
			}
			
			for(Future<Void> result : results) {
				while (true) {
					try {
						result.get();
						break;
					} catch (InterruptedException | ExecutionException ignorable) {
					}
				}
			}
			observer.acceptResult(data, (short)(polynomial.order()+1), requestNo);
		}
	}
	
	/**
	 * Class which calculates data for drawing fractal.
	 */
	private static class FractalTask implements Callable<Void> {
		/** Minimum imaginary value. */
		double reMin;
		/** Maximum real value. */
		double reMax;
		/** Minimum imaginary value. */
		double imMin;
		/** Maximum imaginary value. */ 
		double imMax;
		/** Width of the drawing window. */
		int width;
		/** Height of the drawing window. */
		int height;
		/** Minimum value of Y-axis. */
		int yMin;
		/** Maximum value of Y-axis. */
		int yMax;
		/** Data for calculation results. */
		short[] data;
		
		/**
		 * Creates new computation class with provided arguments.
		 * 
		 * @param reMin		minimum real value.
		 * @param reMax		maximum real value.
		 * @param imMin		minimum imaginary value.
		 * @param imMax		maximum imaginary value.
		 * @param width		width of the drawing window.
		 * @param height	height of the drawing window.
		 * @param yMin		minimum value of Y-axis.
		 * @param yMax		maximum value of Y-axis.
		 * @param data		data for calculation results.
		 */
		public FractalTask(double reMin, double reMax, double imMin, double imMax, 
					int width, int height, int yMin, int yMax, short[] data) {
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.data = data;
		}

		@Override
		public Void call() throws Exception {
			int offset = yMin*width;
			for(int y = yMin; y <= yMax; y++) {
				for(int x = 0; x < width; x++) {
					double cre = x / (width-1.0) * (reMax - reMin) + reMin;
					double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
					Complex zn = new Complex(cre, cim);
					Complex zn1 = new Complex();
					
					int iterations = 0;
					double module = 0;
					
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex fraction = numerator.divide(denominator);
						zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						zn = zn1;						
						iterations++;
					} while (module>CONVERGENCE_THRESHOLD && iterations<MAX_ITERATIONS);
					
					int index = rooted.indexOfClosestRootFor(zn1, ROOT_THRESHOLD);
					if(index == -1) {
						data[offset++] = 0;
					} else {
						data[offset++] = (short) (index+1);
					}
				}
			}
			return null;
		}
	}	
	
	/**
	 * Used to parse {@code String} into {@code Complex}.
	 * Throws NumberFormatException if unable to parse.
	 */
	private static class Parser {
		/**
		 * Converts given string to new complex number.
		 * 
		 * Examples of s: "-3", "5-i", "7i"
		 * 
		 * @param s	string representing complex number
		 * @return complex number
		 */
		public static Complex parse(String s) {
			s = s.replace("\"", "").replaceAll("\\s+", "");
			int indexOfLastSign = Math.max(s.lastIndexOf("+"), s.lastIndexOf("-"));
			double real;
			double imaginary;
			
			if(indexOfLastSign < 1) { //-1 if no signs, 0 if sign is first
				real = stringToReal(s);
				imaginary= stringToImaginary(s);
			} else { 
				real = stringToReal(s.substring(0, indexOfLastSign));
				imaginary = stringToImaginary(s.substring(indexOfLastSign));
			}
			
			return new Complex(real, imaginary);
		}
		
		/**
		 * Converts string containing one part of complex number to real part.
		 * 
		 * @param s 	string being converted
		 * @return decimal number
		 */
		private static double stringToReal(String s) {
			if(s.contains("i")) return 0;
			return tryToParse(s);
		}
		
		/**
		 * Converts string containing one part of complex number to imaginary part.
		 * 
		 * @param s 	string being converted
		 * @return decimal number
		 */
		private static double stringToImaginary(String s) {
			if(!s.contains("i")) return 0;
			s = s.replace("i", "");
			if(s.isEmpty()) return 1;
			if(s.equals("-")) return -1;
			return tryToParse(s);
		}

		/**
		 * Tries to parse string to double.
		 * Shuts down if can't parse.
		 * 
		 * @param s 	string being parsed
		 * @return decimal number
		 */
		private static double tryToParse(String s) {
			try {
				return Double.parseDouble(s);
			} catch(NumberFormatException | NullPointerException exception) {
				throw new NumberFormatException("Given string can not be converted to complex number.");
			}
		}
	}
}
