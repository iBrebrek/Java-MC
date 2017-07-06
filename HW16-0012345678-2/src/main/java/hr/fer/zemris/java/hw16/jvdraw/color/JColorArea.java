package hr.fer.zemris.java.hw16.jvdraw.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;

/**
 * This graphics component enables user 
 * to pick color when component is clicked.
 * 
 * {@link IColorProvider} is implemented 
 * to enable getting selected color.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public class JColorArea extends JButton implements IColorProvider {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Width of this component. */
	private static final int WIDTH = 15;
	/** Height of this component. */
	private static final int HEIGHT = 15;
	
	/** Currently selected color. */
	private Color selectedColor;
	
	/** Listeners for when color is changed. */
	private List<ColorChangeListener> listeners;
	
	/**
	 * Initializes new color area with given initial color.
	 * 
	 * @param initialColor		starting color for this color area.
	 */
	public JColorArea(Color initialColor) {
		if(initialColor == null) {
			selectedColor = Color.WHITE;
		} else {
			selectedColor = initialColor;
		}
		listeners = new ArrayList<>();
		
		setBackground(selectedColor);
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
								
		addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseClicked(MouseEvent e) {
				Color color = JColorChooser.showDialog(null, "Select color.", selectedColor);
				if(color == null) {
					return;
				}
				Color oldColor = selectedColor;
				selectedColor = color;
				setBackground(selectedColor);
				for(ColorChangeListener l : listeners) {
					l.newColorSelected(JColorArea.this, oldColor, selectedColor);
				}
			}
		});
	}
	
	/**
	 * Adds listener for when color is changed.
	 * 
	 * @param l		listener to be registered.
	 */
	public void addColorChangeListener(ColorChangeListener l) {
		listeners = new ArrayList<>(listeners);
		listeners.add(l);
	}
	
	/**
	 * Removes listener.
	 * 
	 * @param l		listener to be removed.
	 */
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners = new ArrayList<>(listeners);
		listeners.remove(l);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}	
	
	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}
}
