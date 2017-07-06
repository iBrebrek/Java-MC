package hr.fer.zemris.java.graphics;



import hr.fer.zemris.java.graphics.raster.BWRaster;
import hr.fer.zemris.java.graphics.raster.BWRasterMem;
import hr.fer.zemris.java.graphics.shapes.Rectangle;
import hr.fer.zemris.java.graphics.views.RasterView;
import hr.fer.zemris.java.graphics.views.SimpleRasterView;

/**
 *	Simple demonstration given in this homework.
 */
public class FirstTest {

	/**
	 * Entry point for this program.
	 * @param args not used.
	 */
	public static void main(String[] args) {

		Rectangle rect1 = new Rectangle(0, 0, 4, 4);
		Rectangle rect2 = new Rectangle(1, 1, 2, 2);
		BWRaster raster = new BWRasterMem(6, 5);
		raster.enableFlipMode();
		rect1.draw(raster);
		rect2.draw(raster);
		RasterView view = new SimpleRasterView();
		view.produce(raster);
		view.produce(raster);
		System.out.println();
		RasterView view2 = new SimpleRasterView('X','_');
		view2.produce(raster);		
	}

}
