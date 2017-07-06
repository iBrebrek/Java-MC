package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.GeometricalObject;

/**
 * Class which implements interface {@link DrawingModel}.
 * For more information see that interface.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public class GeometricsModel implements DrawingModel {
	
	/** All geometric objects in this model. */
	private final List<GeometricalObject> objects = new ArrayList<>();
	
	/** All listeners listening to this model. */
	private List<DrawingModelListener> listeners = new ArrayList<>();

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		int index = objects.size();
		objects.add(object);
		fire(index, index);
	}
	
	@Override
	public void delete(GeometricalObject object) {
		int index = objects.size();
		objects.remove(object);
		fire(index, index);
	}
	
	@Override
	public void clear() {
		objects.clear();
		fire(0, 0);
	}
	
	@Override
	public void edit(Component parent, GeometricalObject object) {
		int index = objects.size();
		object.edit(parent);
		fire(index, index);
	}
	
	/**
	 * Notifies all listeners.
	 * 
	 * @param index1 	index of first changed object.
	 * @param index2	index of last changed object.
	 */
	private void fire(int index1, int index2) {
		for(DrawingModelListener l : listeners) {
			l.objectsAdded(this, index1, index2);
		}
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners = new ArrayList<>(listeners);
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners = new ArrayList<>(listeners);
		listeners.remove(l);
	}
}
