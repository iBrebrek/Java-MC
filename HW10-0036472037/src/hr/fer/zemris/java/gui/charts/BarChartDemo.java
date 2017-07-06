package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Creates window that contains label of file with data
 * and {@code BarChartComponent}.
 * Program takes single argument- path to file.
 * Creates chart from given file and creates window with that chart.
 * If argument is incorrect program is terminated.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (17.5.2016.)
 */
public class BarChartDemo extends JFrame {
	/** For serialization. */
	private static final long serialVersionUID = -2095813712870952876L;

	/**
	 * Creates window that contains label of file with data
	 * and {@code BarChartComponent}.
	 * 
	 * @param model		which chart to show.
	 * @param filePath 	file with data, used only as a text.
	 */
	public BarChartDemo(BarChart model, String filePath) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Bar chart");
		setLocation(20, 20);
		setSize(700, 500);
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		JLabel label = new JLabel(Paths.get(filePath).toAbsolutePath().toString());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		cp.add(label, BorderLayout.PAGE_START);
		cp.add(new BarChartComponent(model), BorderLayout.CENTER);
	}

	/**
	 * Entry point.
	 * 
	 * @param args		file-path.
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Program takes single argument- file path.");
			System.exit(-1);
		}
		
		try (BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new BufferedInputStream(
									new FileInputStream(args[0]))
							,StandardCharsets.UTF_8))) {
			
			String xAxis = br.readLine();
			String yAxis = br.readLine();
			
			List<XYValue> values = new ArrayList<>();
			for(String value : br.readLine().split("\\s+")) {
				String [] split = value.split(",");
				values.add(new XYValue(
						Integer.parseInt(split[0]),
						Integer.parseInt(split[1])
				));
			}
			
			int min = Integer.parseInt(br.readLine());
			int max = Integer.parseInt(br.readLine());
			int step = Integer.parseInt(br.readLine());
			
			BarChart model = new BarChart(
					values,
					xAxis, 
					yAxis,
					min,
					max,
					step
			);
			
			SwingUtilities.invokeLater(() -> {
				new BarChartDemo(model, args[0]).setVisible(true);
			});
			
		} catch (FileNotFoundException e) {
			System.err.println("Given file path is incorrect.");
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			System.err.println("File contains invalid data.");
		} catch (IOException e) {
			System.err.println("Unable to read file.");
		} catch (IllegalArgumentException info) {
			System.err.println(info.getMessage());
		} catch (Exception e) {
			System.err.println("Unable to create file.");
		}
	}
}
