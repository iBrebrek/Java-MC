package hr.fer.zemris.java.hw16.jvdraw.model;

/**
 * Interface which needs to be implemented
 * when listening {@link DrawingModel}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public interface DrawingModelListener {

	/**
	 * Notified when new object is added to listened model.
	 * 
	 * @param source	source with added object.
	 * @param index0	index of first changed object.
	 * @param index1	index of last changed object.
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);
	
	/**
	 * Notified when object is removed from listened model.
	 * 
	 * @param source	source with removed object.
	 * @param index0	index of first changed object.
	 * @param index1	index of last changed object.
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);
	
	/**
	 * Notified when object is changed in listened model.
	 * 
	 * @param source	source with changed object.
	 * @param index0	index of first changed object.
	 * @param index1	index of last changed object.
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
