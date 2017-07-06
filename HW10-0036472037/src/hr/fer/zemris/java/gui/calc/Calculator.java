package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;

/**
 * This program creates new simple calculator.
 * Biggest and first component is screen.
 * Screen can be edited only by clicking buttons.
 * 
 * Calculations are done sequentially, it's easier to give example:
 * 3+2*5= instead of 3+(2*5) it will be (3+2)*5, there is no priority.
 * 
 * All buttons from {@link Buttons} are in this calculator.
 * For more information about each button read methods in {@link Buttons},
 * one method is one button.
 * 
 * Style used in this calculator can be seen here: {@link Style}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (15.5.2016.)
 */
public class Calculator extends JFrame implements IScreen {
	/** For serialization. */
	private static final long serialVersionUID = -1192022052128833397L;

	/**
	 * Stack used for {@code Calculator}.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (15.5.2016.)
	 */
	private static class Stack implements IStack {
		/** Simulates stack. Always add/remove from last index. */
		private List<Object> list = new ArrayList<>();

		@Override
		public void push(Object value) {
			list.add(value);
		}

		@Override
		public Object peek() {
			return list.get(list.size()-1);
		}

		@Override
		public Object pop() {
			Object o = peek();
			list.remove(list.size()-1);
			return o;
		}

		@Override
		public boolean isEmpty() {
			return list.isEmpty();
		}

		@Override
		public int size() {
			return list.size();
		}
	}
	
	/** Stack used for calculations (almost everything). */
	private final Stack stack = new Stack();
	/** Stack used ONLY for calculator button pop and push. */
	private final Stack internStack = new Stack();
	/** Screen of this calculator. */
	private final JLabel screen;
	/** Flag used to know if screen is empty. */
	private boolean aboutToClear = true; //name isn't isEmpty because after clear() shown text stays the same.
	
	/** Action done when check box "inv" is clicked. */
	private final ActionListener inverseListener = new ActionListener() {
		private boolean showInverse = true;
		
		private JButton asin = Buttons.arcSine(Calculator.this);
		private JButton tenPow = Buttons.tenPow(Calculator.this);
		private JButton acos = Buttons.arcCosine(Calculator.this);
		private JButton epow = Buttons.ePow(Calculator.this);
		private JButton atan = Buttons.arcTangent(Calculator.this);
		private JButton nroot = Buttons.nRootX(Calculator.this, stack);
		private JButton actg = Buttons.arcCotanget(Calculator.this);
		private JButton sin = Buttons.sine(Calculator.this);
		private JButton log = Buttons.logarithm(Calculator.this);
		private JButton cos = Buttons.cosine(Calculator.this);
		private JButton ln = Buttons.naturalLogarithm(Calculator.this);
		private JButton tan = Buttons.tangent(Calculator.this);
		private JButton xpow = Buttons.xPowN(Calculator.this, stack);
		private JButton ctg = Buttons.cotanget(Calculator.this);
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Container cp = getContentPane();
			showInverse = !showInverse;
			
			if(showInverse) {
				cp.remove(sin);
				cp.add(asin, "2,2");
				cp.remove(log);
				cp.add(tenPow, "3,1");
				cp.remove(cos);
				cp.add(acos, "3,2");
				cp.remove(ln);
				cp.add(epow, "4,1");
				cp.remove(tan);
				cp.add(atan, "4,2");
				cp.remove(xpow);
				cp.add(nroot, "5,1");
				cp.remove(ctg);
				cp.add(actg, "5,2");
			} else {
				cp.remove(asin);
				cp.add(sin, "2,2");
				cp.remove(tenPow);
				cp.add(log, "3,1");
				cp.remove(acos);
				cp.add(cos, "3,2");
				cp.remove(epow);
				cp.add(ln, "4,1");
				cp.remove(atan);
				cp.add(tan, "4,2");
				cp.remove(nroot);
				cp.add(xpow, "5,1");
				cp.remove(actg);
				cp.add(ctg, "5,2");
			}
			
			cp.revalidate();
			cp.repaint();
		}
	};
	
	/**
	 * Creates new calculator described in class documentation.
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Calculator");
		setLocation(20, 20);
		setSize(500, 200);
		screen = new JLabel("0"); //weird, can't put that inside initGUI but can here (just because screen is final)
		initGUI();
	}

	/**
	 * Creates every GUI element for calculator.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(5));
		
		screen.setHorizontalAlignment(SwingConstants.RIGHT);
		screen.setOpaque(true);
		screen.setBackground(Style.SCREEN_BACKGROUND);
		screen.setBorder(BorderFactory.createCompoundBorder(
				Style.BORDER, 
				BorderFactory.createEmptyBorder(10, 10, 10, 10) //it's like padding
		));
		screen.setFont(Style.SCREEN_FONT);
		screen.setForeground(Color.BLACK);
		cp.add(screen, "1,1");
		
		cp.add(Buttons.equals(this, stack), "1,6");
		cp.add(Buttons.clear(this), "1,7");
		cp.add(Buttons.reciprocal(this), "2,1");
		cp.add(Buttons.number(7, this), "2,3");
		cp.add(Buttons.number(8, this), "2,4");
		cp.add(Buttons.number(9, this), "2,5");
		cp.add(Buttons.divide(this, stack), "2,6");
		cp.add(Buttons.reset(this, stack), "2,7");
		cp.add(Buttons.number(4, this), "3,3");
		cp.add(Buttons.number(5, this), "3,4");
		cp.add(Buttons.number(6, this), "3,5");
		cp.add(Buttons.multiply(this, stack), "3,6");
		cp.add(Buttons.push(this, internStack), "3,7");
		cp.add(Buttons.number(1, this), "4,3");
		cp.add(Buttons.number(2, this), "4,4");
		cp.add(Buttons.number(3, this), "4,5");
		cp.add(Buttons.minus(this, stack), "4,6");
		cp.add(Buttons.pop(this, internStack, this), "4,7");
		cp.add(Buttons.number(0, this), "5,3");
		cp.add(Buttons.sign(this), "5,4");
		cp.add(Buttons.point(this), "5,5");
		cp.add(Buttons.add(this, stack), "5,6");
		
		JCheckBox inv = new JCheckBox("inv");
		inv.setOpaque(true);
		inv.setBackground(Style.BUTTONS_BACKGROUND);
		inv.setHorizontalAlignment(SwingConstants.CENTER);
		inv.setBorder(Style.BORDER);
		inv.setBorderPainted(true);
		inv.setFont(Style.BUTTONS_FONT);
		inv.setForeground(Color.BLACK);
		inv.addActionListener(inverseListener);
		cp.add(inv, "5,7");
		
		inverseListener.actionPerformed(null);
	}

	/**
	 * Entry point for this program.
	 * Creates and shows new {@code Calculator}.
	 * 
	 * @param args		not used.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));	
	}

	@Override
	public void show(String text) {
		screen.setText(text);
		aboutToClear = false;
	}

	@Override
	public String read() {
		return screen.getText();
	}

	@Override
	public boolean isEmpty() {
		return aboutToClear;
	}

	@Override
	public void clear() {
		aboutToClear = true;
	}
}
