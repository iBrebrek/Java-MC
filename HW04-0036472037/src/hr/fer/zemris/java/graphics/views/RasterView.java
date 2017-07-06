package hr.fer.zemris.java.graphics.views;

import hr.fer.zemris.java.graphics.raster.BWRaster;

/**
 * Classes which implement this interface will be 
 * responsible for visualization of created images.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (4.4.2016.)
 */
public interface RasterView {

	/**
	 * Somewhere prints given raster.
	 * 
	 * @param raster		black and white raster.
	 * @return object.
	 */
	Object produce(BWRaster raster);
}
