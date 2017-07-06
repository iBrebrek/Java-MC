package hr.fer.zemris.java.gui.layouts;


/**
 * Stores read-only row and column ordinal number.
 * 
 * Class offers single constructor which takes both row and column.
 * Object can be also made with method {@link #fromString(String)}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.5.2016.)
 */
public class RCPosition {
	/** Row ordinal number. */
	private final int row;
	/** Column ordinal number. */
	private final int column;
	
	/**
	 * Creates new {@code RCPosition} from given string.
	 * Example of valid strings:
	 * "1,1"
	 * "-5,-700"
	 * In other word, string must be:
	 * "[int],[int]"
	 * 
	 * @param string	representation of {@code RCPosition}.
	 * @return new {@code RCPosition}.
	 * 
	 * @throws IllegalArgumentException if {@code string} is not representation of {@code RCPosition}.
	 */
	public static RCPosition fromString(String string) {
		if(string == null) {
			throw new IllegalArgumentException("Name can not be null.");
		}
		
		String[] positionName = string.split(",");
		
		try {
			if(positionName.length != 2) throw new Exception(); //it will be re-thrown in catch.
			
			return new RCPosition(
					Integer.parseInt(positionName[0]), 
					Integer.parseInt(positionName[1])
			);
			
		} catch(Exception exc) {
			throw new IllegalArgumentException(string + " is not a position.");
		}
	}
	
	/**
	 * Initializes ordinal number for row and column.
	 * 
	 * @param row		ordinal number of row.
	 * @param column	ordinal number of column.
	 */
	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	/**
	 * Ordinal number of row.
	 * 
	 * @return ordinal number of row.
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Ordinal number of column.
	 * 
	 * @return ordinal number of column.
	 */
	public int getColumn() {
		return column;
	}
}
