package hr.fer.zemris.java.hw16.jvdraw.color;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Label which displays currently selected foreground and background.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public class ColorStatus extends JLabel implements ColorChangeListener {
	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Color provider for foreground. */
	private final IColorProvider foreground;
	/** Color provider for background. */
	private final IColorProvider background;
	
	/**
	 * Initializes new color status and
	 * registers itself to both JColorAreas provided.
	 * 
	 * @param foreground	provider for foreground color.
	 * @param background	provider for background color.
	 */
	public ColorStatus(JColorArea foreground, JColorArea background) {
		setColor(foreground.getCurrentColor(), background.getCurrentColor());
		this.foreground = foreground;
		this.background = background;
		foreground.addColorChangeListener(this);
		background.addColorChangeListener(this);
		setHorizontalAlignment(SwingConstants.RIGHT);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	/**
	 * Displays label with currently selected color.
	 * 
	 * @param f		foreground color.
	 * @param b		background color.
	 */
	private void setColor(Color f, Color b) {
		StringBuilder sb = new StringBuilder();
		sb.append("Foreground color: (");
		sb.append(f.getRed()).append(", ");
		sb.append(f.getBlue()).append(", ");
		sb.append(f.getGreen()).append("), ");
		sb.append("background color: (");
		sb.append(b.getRed()).append(", ");
		sb.append(b.getBlue()).append(", ");
		sb.append(b.getGreen()).append(").");
		setText(sb.toString());
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		if(oldColor.equals(newColor)) {
			return;
		}
		if(source == foreground) {
			setColor(newColor, background.getCurrentColor());
		} else {
			setColor(foreground.getCurrentColor(), newColor);
		}
	}
}