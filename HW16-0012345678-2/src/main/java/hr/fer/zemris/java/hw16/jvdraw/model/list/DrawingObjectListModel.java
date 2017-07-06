package hr.fer.zemris.java.hw16.jvdraw.model.list;

import javax.swing.AbstractListModel;
import javax.swing.JList;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.GeometricalObject;

/**
 * Model used in {@link JList}.
 * Model contains all {@link GeometricalObject}s 
 * that have to be displayed on list.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Model containing all objects. */
	private final DrawingModel model;
	
	/**
	 * Initializes new list model and 
	 * registers itself to given model.
	 * 
	 * @param model		drawing model.
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.model = model;
		
		model.addDrawingModelListener(new DrawingModelListener() {
			
			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				fireIntervalRemoved(this, index0, index1);
			}
			
			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				fireContentsChanged(this, index0, index1);
			}
			
			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				fireIntervalAdded(this, index0, index1);
			}
		});
	}

	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}
}
