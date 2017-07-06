package hr.fer.zemris.java.raytracer.model;

/**
 * Graphical object sphere used as static object in scene.
 * It has center, radius and diffuse and reflective component coefficients.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.5.2016.)
 */
public class Sphere extends GraphicalObject {
	
	/** Used for comparing substitution of 2 doubles. */
	private static final double ACCEPTABLE_DEVIANCE = 0.001;
	
	/** Center of this Sphere. */
	private final Point3D center;
	/** Sphere's radius. */
	private final double radius;
	/** Diffuse component coefficient, color:red. */
	private final double kdr;
	/** Diffuse component coefficient, color:green. */
	private final double kdg;
	/** Diffuse component coefficient, color:blue. */
	private final double kdb;
	/** Reflective component coefficient, color:red. */
	private final double krr;
	/** Reflective component coefficient, color:green. */
	private final double krg;
	/** Reflective component coefficient, color:blue. */
	private final double krb;
	/** Reflective component coefficient, surface roughness. */
	private final double krn;
	
	/**
	 * Initializes all needed data for a new Sphere.
	 * 
	 * @param center	3d center point.
	 * @param radius	Sphere radius.
	 * @param kdr		Diffuse component coefficient, color:red.
	 * @param kdg		Diffuse component coefficient, color:green
	 * @param kdb		Diffuse component coefficient, color:red.
	 * @param krr		Reflective component coefficient, color:red.
	 * @param krg		Reflective component coefficient, color:green.
	 * @param krb		Reflective component coefficient, color:blue.
	 * @param krn		Reflective component coefficient, surface roughness.
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, 
					double krr, double krg, double krb,double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}
	
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		double a = ray.direction.scalarProduct(ray.direction);
		double b = ray.direction.scalarMultiply(2).scalarProduct(ray.start.sub(this.center));
		double c = ray.start.sub(this.center).scalarProduct(ray.start.sub(this.center)) - radius * radius;

		double determinant = b*b - 4*a*c;
		if (determinant < 0) {
			return null;
		}

		double lambda1 = (-b + Math.sqrt(determinant)) / (2*a);
		double lambda2 = (-b - Math.sqrt(determinant)) / (2*a);

		if (Math.abs(lambda1 - lambda2) > ACCEPTABLE_DEVIANCE && lambda1 > 0 && lambda2 > 0) {
			double lambda = Math.min(lambda1, lambda2);
			return new SphereIntersection(ray, lambda, true);
		} else if (Math.abs(lambda1 - lambda2) > ACCEPTABLE_DEVIANCE) {
			RayIntersection first = new SphereIntersection(ray, lambda1, false);
			RayIntersection second = new SphereIntersection(ray, lambda2, false);

			if (first.getDistance() > second.getDistance()) {
				return second;
			}
			return first;
		} else if (Math.abs(lambda1 - lambda2) < ACCEPTABLE_DEVIANCE) { 
			return new SphereIntersection(ray, lambda1, true);
		}

		return null;
	}
	
	/**
	 * Intersection of ray and sphere.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (11.5.2016.)
	 */
	private class SphereIntersection extends RayIntersection {

		/**
		 * From ray and lambda calculates intersection point and distance.
		 * 
		 * @param ray		ray for this intersection.
		 * @param lambda	lambda in this intersection.
		 * @param outer		if intersection is outer(or inner).
		 */
		protected SphereIntersection(Ray ray, double lambda, boolean outer) {
			super(ray.start.add(ray.direction.scalarMultiply(lambda)), 
					ray.start.add(ray.direction.scalarMultiply(lambda)).sub(ray.start).norm(), 
					outer);
		}
		
		@Override
		public Point3D getNormal() {
			return getPoint().sub(center);
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}
		
	}
}