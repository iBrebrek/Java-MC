package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Main program for second homework problem.
 * Draws 2 Spheres with 2 light sources. 
 * Uses Fork-Join framework and RecursiveAction for calculations.
 * Output is same as in RayCaster. 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.5.2016.)
 */
public class RayCasterParallel {
	/** Ambient component for all colors. */
	private static final short AMBIENT_COMPONENT = 15;
	/** Used for comparing substitution of 2 doubles. */
	private static final double ACCEPTABLE_DEVIANCE = 0.001;
	/** Used in {@code RecursiveAction}.*/
	private static final int THRESHOLD = 100;

	/**
	 * Entry point.
	 * 
	 * @param args		not used.
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), 
				new Point3D(10, 0, 0),
				new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 
				20, 20);
	}

	/**
	 * Creates new {@code IRayTracerProducer} which is able 
	 * to create scene snapshots by using ray-tracing technique.
	 * 
	 * @return	new {@code IRayTracerProducer} 	
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {
				
				//all those final variables will be used in local class TracingTask
				final short[] red = new short[width * height];
				final short[] green = new short[width * height];
				final short[] blue = new short[width * height];
				
				final Point3D OG = view.sub(eye).modifyNormalize();
				final Point3D yAxis = viewUp.normalize().modifySub(OG.scalarMultiply(OG.scalarProduct(viewUp.normalize())));
				final Point3D xAxis = OG.vectorProduct(yAxis).normalize();
				
				final Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal/2)).add(yAxis.scalarMultiply(vertical/2));
				final Scene scene = RayTracerViewer.createPredefinedScene();
				
				/*
				 * 
				 * No need to write comment for local class, 
				 * but just so you can find RecursiveAction faster.
				 * 
				 */
				class TracingTask extends RecursiveAction {
					private static final long serialVersionUID = 1L;
					final int minY;
					final int maxY;

					TracingTask(int minY, int maxY) {
						this.minY = minY;
						this.maxY = maxY;
					}

					protected void compute() {
						if (maxY - minY < THRESHOLD) {
							short[] rgb = new short[3];
							int offset = minY * width;
							for (int y = minY; y < maxY; y++) {
								for (int x = 0; x < width; x++) {
									Point3D screenPoint = screenCorner
											.add(xAxis.scalarMultiply(horizontal * x / (width - 1)))
											.modifySub(yAxis.scalarMultiply(vertical * y / (height - 1)));
									Ray ray = Ray.fromPoints(eye, screenPoint);

									tracer(scene, ray, rgb, eye); 
									
									red[offset] = rgb[0] > 255 ? 255 : rgb[0];
									green[offset] = rgb[1] > 255 ? 255 : rgb[1];
									blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

									offset++;
								}
							}
						} else {
							int mid = (minY + maxY) / 2;
							invokeAll(new TracingTask(minY, mid), new TracingTask(mid, maxY));
						}
					}
				}
				
				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new TracingTask(0, height));
				pool.shutdown();
				
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}

			/**
			 * Using ray-tracing fills {@code rgb}.
			 * 
			 * @param scene		scene containing objects and light sources.
			 * @param ray		observed ray.
			 * @param rgb		red-green-blue values for each pixel.
			 * @param eye		position of observer.
			 */
			private void tracer(Scene scene, Ray ray, short[] rgb, Point3D eye) {
				RayIntersection intersection = getIntersection(scene, ray);
				if(intersection == null) {
					rgb[0] = rgb[1] = rgb[2] = 0;	//no intersection - in the dark
					return;
				}
				
				rgb[0] = rgb[1] = rgb[2] = AMBIENT_COMPONENT; // there is an intersection - at least has ambient component
				
				for(LightSource lightSource : scene.getLights()) {
					Ray ray2 = Ray.fromPoints(lightSource.getPoint(), intersection.getPoint());
					RayIntersection intersection2 = getIntersection(scene, ray2);
					
					if(intersection2 == null) continue; // nothing to do if no intersection
					
					double first = intersection.getPoint().sub(lightSource.getPoint()).norm();
					double second = intersection2.getPoint().sub(lightSource.getPoint()).norm();
					if (Math.abs(first - second) > ACCEPTABLE_DEVIANCE) continue; //it is obscured
					
					//diffuse component
					Point3D l = lightSource.getPoint().sub(intersection2.getPoint()).normalize();
					Point3D n = intersection2.getNormal().normalize();
					double cosTheta = Math.max(n.scalarProduct(l), 0);
					
					rgb[0] += lightSource.getR() * intersection2.getKdr() * cosTheta;
					rgb[1] += lightSource.getG() * intersection2.getKdg() * cosTheta;
					rgb[2] += lightSource.getB() * intersection2.getKdb() * cosTheta;
					
					//reflective component
					Point3D r = n.scalarMultiply(2).scalarMultiply(l.scalarProduct(n)).sub(l).normalize();
					Point3D v = eye.sub(intersection2.getPoint()).normalize();
					double cosAplhaN = Math.pow(Math.max(r.scalarProduct(v), 0), intersection2.getKrn());
					
					rgb[0] += lightSource.getR() * intersection2.getKrr() * cosAplhaN;
					rgb[1] += lightSource.getG() * intersection2.getKrg() * cosAplhaN;
					rgb[2] += lightSource.getB() * intersection2.getKrb() * cosAplhaN;
				}
			}

			/**
			 * Searches for closest intersection of given ray within given scene.
			 * 
			 * @param scene		scene containing objects and light sources.
			 * @param ray		observed ray.
			 * @return closest intersection or {@code null} if no intersections.
			 */
			private RayIntersection getIntersection(Scene scene, Ray ray) {
				RayIntersection intersection = null;
				for (GraphicalObject s : scene.getObjects()) {
					RayIntersection current = s.findClosestRayIntersection(ray);
					if (current != null && 
							(intersection == null || 
							current.getDistance() < intersection.getDistance())) {
						intersection = current;
					}
				}
				return intersection;
			}
		};
	}
}
