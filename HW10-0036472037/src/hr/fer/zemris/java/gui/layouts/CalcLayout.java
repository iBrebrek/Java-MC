package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.function.Function;

/**
 * Layout manager typically used for calculators.
 * It has 5 rows, 7 columns, and first component is big as 5 others,
 * every other component has same width and same height.
 * 
 * In constructor can be defined gap.
 * Gap is number of pixels between each column and each row.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (15.5.2016.)
 */
public class CalcLayout implements LayoutManager2 {
	
	/** Fixed number of rows in this layout. */
	private static final int ROWS = 5;
	/** Fixed number of columns in this layout. */
	private static final int COLUMNS = 7;
	/** How many times first component's WIDTH is bigger than other components. */
	private static final int FIRST_SIZE = 5;
	
	/** Pixels between columns and rows. */
	private final int gap;
	/** All components added to this layout. */
	private final Component[] allComponents = new Component[ROWS*COLUMNS - FIRST_SIZE+1];
    
	/**
	 * Initializes unmodified space between columns and rows.
	 * Given gap is actually number of pixels.
	 * 
	 * @param gap	space between columns/rows.
	 */
	public CalcLayout(int gap) {
		this.gap = gap;
	}
	
	/**
	 * Sets unmodified space between columns and rows to 0.
	 */
	public CalcLayout() {
		this(0);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, RCPosition.fromString(name));
	}
	
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if(comp == null) {
			throw new IllegalArgumentException("Component should not be null.");
		}
		if(constraints == null || !(constraints instanceof RCPosition)) {
			if(constraints instanceof String) {
				constraints = RCPosition.fromString((String)constraints);
			} else {
				throw new IllegalArgumentException("Constraints must be RCPosition non-null object.");
			}
		}
		
		RCPosition position = (RCPosition) constraints;
		int row = position.getRow();		
		int column = position.getColumn();	//because it's boring to call getters
		
		if(column < 1 || column > COLUMNS) {
			throw new IndexOutOfBoundsException("Column must be between 1 and " + COLUMNS);
		}
		if(row < 1 || row > ROWS) {
			throw new IndexOutOfBoundsException("Row must be between 1 and " + ROWS);
		}
		if(row == 1 && (column > 1 && column < (FIRST_SIZE+1))) {
			throw new IndexOutOfBoundsException("In first row columns from 2 to " + (FIRST_SIZE+1) + " are invalid.");
		}
		
		int index = 0;
		
		if(row != 1 || column != 1) { //if not big one
			index = (row-1)*COLUMNS + column - FIRST_SIZE;
		}
		
		if(allComponents[index] != null) {
			throw new RuntimeException("Position ("+row+","+column+") is already taken.");
		}
		
		//finally we can add.....
		allComponents[index] = comp;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		for(int i = 0; i < allComponents.length; i++) {
			if(comp.equals(allComponents[i])) {
				allComponents[i] = null;
			}
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return minMaxPref(parent, c->c.getPreferredSize());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return minMaxPref(parent, c->c.getMinimumSize());
	}
	
	@Override
	public Dimension maximumLayoutSize(Container target) {
		return minMaxPref(target, c->c.getMaximumSize());
	}
	
	/**
	 * All common things to calculate min, max and preferred layout size.
	 * 
	 * @param parent			container where everything is.
	 * @param dimensionGetter	function which will return {@code Dimension} used to calculated wished size.
	 * @return dimension of whole layout.
	 */
	private Dimension minMaxPref(Container parent, Function<Component, Dimension> dimensionGetter) {
		int width = 0;
		int height = 0;
		
		//first one is special
		if(allComponents[0] != null) {
			Dimension first = dimensionGetter.apply(allComponents[0]);
			if(first != null) {
				width = first.width/FIRST_SIZE - gap*4;
				height = first.height;
			}
		}
		
		for(int i = 1; i < allComponents.length; i++) {
			if(allComponents[i] == null) continue;
			Dimension current = dimensionGetter.apply(allComponents[i]);
			if(current == null) continue; //because he doesn't care
			if(current.getWidth() > width) {
				width = current.width;
			}
			if(current.getHeight() > height) {
				height = current.height;
			}
		}
		
		//don't forget gap
		width += (COLUMNS-1) * gap;
		height += (ROWS-1) * gap;
		
		//don't forget border
		Insets insets = parent.getInsets();
		width += insets.left + insets.right;
		height += insets.top + insets.bottom;
		
		return new Dimension(width, height);
	}
	
	@Override
	public void invalidateLayout(Container target) {
		//nothing is cached
		//TODO or is it?
	}

	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();

		int totalGapsWidth = (COLUMNS - 1) * gap;
        int widthWOInsets = parent.getWidth() - (insets.left + insets.right);
        int widthOnComponent = (widthWOInsets - totalGapsWidth) / COLUMNS;
        int extraWidthAvailable = (widthWOInsets - (widthOnComponent * COLUMNS + totalGapsWidth)) / 2;

        int totalGapsHeight = (ROWS - 1) * gap;
        int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
        int heightOnComponent = (heightWOInsets - totalGapsHeight) / ROWS;
        int extraHeightAvailable = (heightWOInsets - (heightOnComponent * ROWS + totalGapsHeight)) / 2;
        
        int firstWidth = widthOnComponent*FIRST_SIZE + gap*(FIRST_SIZE-1);
        //first component in first row
        if(allComponents[0] != null) {
        	allComponents[0].setBounds(
        			insets.left + extraWidthAvailable, 
        			insets.top + extraHeightAvailable, 
        			firstWidth, 
        			heightOnComponent
        	);
        }
        //finish first row
        for(int i = 1; i <= COLUMNS-FIRST_SIZE; i++) {
        	if(allComponents[i] == null) continue;
        	allComponents[i].setBounds(
        			insets.left + extraWidthAvailable + firstWidth + widthOnComponent*(i-1) + gap*i,
        			insets.top + extraHeightAvailable, 
        			widthOnComponent, 
        			heightOnComponent
        	);
        }
        //everything BUT first row
        int y = insets.top + extraHeightAvailable + heightOnComponent + gap; //too big to put it in for
		for (int i = COLUMNS-FIRST_SIZE, r = 1; r < ROWS; r++, y += heightOnComponent + gap) {
			for (int c = 0, x = insets.left + extraWidthAvailable; c < COLUMNS; c++, x += widthOnComponent + gap) {
				i++;
				if (allComponents[i] != null) {
					allComponents[i].setBounds(x, y, widthOnComponent, heightOnComponent);
				}
			}
		}
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}
}
