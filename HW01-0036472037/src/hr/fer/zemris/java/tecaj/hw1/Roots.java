package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program koji računa korijen kompleksnoga broja.
 * Potrebna su 3 argumenata.
 * Prvi argument je realni dio, 
 * drugi argument je imaginarni dio,
 * treći argument je korijen.
 * Korijen mora biti prirodan broj veći od 1.
 * 
 * @author Ivica B.
 * @version 1.0
 */
public class Roots {

	/**
	 * Metoda koja pokreće glavni program.
	 * @param args argumenti programa
	 */
	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("Wrong number of arguments.");
			return;
		}
		
        double re = Double.parseDouble(args[0]);
        double im = Double.parseDouble(args[1]);
        double root = Integer.parseInt(args[2]);
        
        if(re == 0 && im == 0) {
        	System.out.println("Result is 0.");
        	return;
        }
        if(root < 2) {
        	System.out.println("Root must be natural number greater than 1.");
        	return;
        }
        
        double fi = getFi(re, im);
        double partialFi = fi / root;
        double z = Math.sqrt(re*re + im*im);
        double r = Math.pow(z, 1/root);
        double partsOfCircle = 2*Math.PI / root;
        
        System.out.printf("Re = %.2f, Im = %.2f.%nYou requested calculation of %d. root. Solutions are:%n", 
        				re, im, (int)root
        );
        
        for (int i = 0; i < root; i++) {
            re = r * Math.cos(partialFi + partsOfCircle*i);
            im = r * Math.sin(partialFi + partsOfCircle*i);
            if(im < 0) {
                System.out.printf("%d) %.2f - %.2fi%n", i+1, re, Math.abs(im));
            }else{
            	System.out.printf("%d) %.2f + %.2fi%n", i+1, re, im);
            }
        }
	}

	/**
	 * Računa kut fi kompleksnog broja.
	 * Kut je izražen u radijanima.
	 * @param re realni dio
	 * @param im imaginarni dio, bez i
	 * @return kut u radijanima
	 */
	private static double getFi(double re, double im) {
		if(re > 0) {
			return Math.atan(im / re);
		}
		if(re < 0) {
			if(im < 0) {
				return (Math.atan(im / re) - Math.PI);
			} else {
				return (Math.atan(im / re) + Math.PI);
			}
		}
		//znaci da je re 0
		if(im > 0) {
			return Math.PI / 2;
		}else if(im < 0){
			return -Math.PI / 2;
		}
		//i re i im su 0, a 0/0 je neodredeno
		return Double.NaN;
	}
}
