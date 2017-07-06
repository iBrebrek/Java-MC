package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.Component;

import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.GeometricalObject;

/**
 * Stores {@link GeometricalObject}s and has 
 * listener listening when this model is changed.
 * 
 * To see all possibilities see {@link GeometricalObject}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public interface DrawingModel {
	
	/**
	 * Retrieves total number of geometrical objects in this model.
	 * 
	 * @return total number of geometrical objects in this model.
	 */
	int getSize();
	
	/**
	 * Retrieves geometrical object at given index.
	 * 
	 * @param index			index at which is wanted object.
	 * @return object at given index.
	 * 
	 * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	GeometricalObject getObject(int index);
	
	/**
	 * Adds geometrical object to this model.
	 * 
	 * @param object	object being added to model.
	 */
	void add(GeometricalObject object);
	
	/**
	 * Removes given geometrical object from this model.
	 * 
	 * @param object	object being removed from model.
	 */
	void delete(GeometricalObject object);
	
	/**
	 * Opens GUI which will let user edit given object,
	 * for more information see {@link GeometricalObject#edit(Component)}.
	 * GUI is centered inside of given parent.
	 * 
	 * @param parent	where to center GUI.
	 * @param object	object being edited.
	 */
	void edit(Component parent, GeometricalObject object);
	
	/**
	 * Removes ALL geomtrical object from this model.
	 */
	void clear();
	
	/**
	 * Adds listener to this model.
	 * All listeners will be notified when
	 * any object is added/removed/edited.
	 * 
	 * @param l		listener being added.
	 */
	void addDrawingModelListener(DrawingModelListener l);
	
	/**
	 * Removes listener from this model.
	 * 
	 * @param l		listener being removed.
	 */
	void removeDrawingModelListener(DrawingModelListener l);
}
