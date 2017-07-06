package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Creates 2 equal lists containing prime numbers.
 * On button click next prime number is added to lists.
 * 
 * Program takes no arguments.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (17.5.2016.)
 */
public class PrimDemo extends JFrame {
	/** For serialization. */
	private static final long serialVersionUID = 2552614965736703542L;

	/**
	 * Creates window "List of primes".
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(100, 100);
		setSize(300, 200);
		setTitle("List of primes");

		initGUI();
	}
	
	/**
	 * Creates GUI for this window.
	 * 2 scrollable lists and button.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		
		JList<Integer> leftList = new JList<>(model);
		JList<Integer> rightList = new JList<>(model);
		
		JPanel lists = new JPanel(new GridLayout(1, 2));
		lists.add(new JScrollPane(leftList));
		lists.add(new JScrollPane(rightList));
		
		JButton next = new JButton("Next");
		next.addActionListener(e -> model.next());
		
		cp.add(lists, BorderLayout.CENTER);
		cp.add(next, BorderLayout.PAGE_END);
	}

	/**
	 * Entry point.
	 * 
	 * @param args		not used.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new PrimDemo().setVisible(true);
		});
	}
}
