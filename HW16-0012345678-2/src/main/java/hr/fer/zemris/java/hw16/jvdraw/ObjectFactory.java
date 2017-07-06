package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;

import hr.fer.zemris.java.hw16.jvdraw.color.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.Circle;
import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.Line;

/**
 * Object which are instance of this class knows how to create
 * all possible {@link GeometricalObject}s.
 * To create new object use method {@link #generate()}.
 * To change which object will be generated use {@link #setType(Type)}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public final class ObjectFactory implements ColorChangeListener {
	
	/**
	 * Enumeration explaining which 
	 * objects this factory can generate.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (1.7.2016.)
	 */
	public enum Type {
		/** Generated object will be null. */
		NOTHING,
		/** Generated object will be circle. */
		CIRCLE,
		/** Generated object will be line. */
		LINE,
		/** Generated object will be filled circle. */
		FILLED_CIRCLE;
	}
	
	/** Total number of generated lines. */
	private int numberOfLines = 0;
	/** Total number of generated circles. */
	private int numberOfCircles = 0;
	/** Total number of generated filled circles. */
	private int numberOfFilled = 0;
	
	/** By default null will be generated. */
	private Type type = Type.NOTHING;
	/** Provider which has foreground color used to create new object. */
	private IColorProvider foreground;
	/** Provider which has background color used to create new object. */
	private IColorProvider background;
	
	/**
	 * Initializes new object factory and
	 * registers itself to both given JColorAreas.
	 * 
	 * @param foreground	provider for foreground color.
	 * @param background	provider for background color.
	 */
	public ObjectFactory(JColorArea foreground, JColorArea background) {
		this.foreground = foreground;
		this.background = background;
		foreground.addColorChangeListener(this);
		background.addColorChangeListener(this);
	}
	
	/**
	 * Set {@link Type} which makes {@link #generate()} 
	 * generate different objects.
	 * 
	 * @param type		type to use when generating new object.
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * Generates new object.
	 * All newly creates object will have point set to 0.
	 * <br>
	 * To change which object will be generated
	 * use method {@link #setType(Type)}.
	 * 
	 * @return newly created object.
	 */
	public GeometricalObject generate() {
		switch (type) {
		case CIRCLE:
			numberOfCircles++;
			return new Circle("Circle#"+numberOfCircles, foreground.getCurrentColor());
		case FILLED_CIRCLE:
			numberOfFilled++;
			return new FilledCircle("FCircle#"+numberOfFilled, 
					foreground.getCurrentColor(), background.getCurrentColor());
		case LINE:
			numberOfLines++;
			return new Line("Line#"+numberOfLines, foreground.getCurrentColor());
		case NOTHING:
		default:
			return null;
		}
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		if(source == foreground) {
			foreground = source;
		} else {
			background = source;
		}
	}
}
