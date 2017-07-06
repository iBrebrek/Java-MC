package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.model.GeometricsModel;
import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.Circle;
import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.model.geometrics.Line;

/**
 * All actions used by {@link JVDraw}.
 * All actions are final, therefore, they can't be changed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.7.2016.)
 */
public class JVDrawActions {
	/** Frame which uses actions. */
	private final JFrame frame;
	/** Model containting all objects. */
	private final DrawingModel model;
	
	/** Path where jvd file is located. */
	private Path currentJvdPath = null;
	/** <true>true</code> if canvas was modified. */
	private boolean modified = false;

	/**
	 * Initializes new action provider.
	 * Adds listener to given model.
	 * 
	 * @param frame		frame which uses actions.
	 * @param model		model with all geometrical objects.
	 */
	public JVDrawActions(JFrame frame, DrawingModel model) {
		this.frame = frame;
		this.model = model;
		
		model.addDrawingModelListener(new DrawingModelListener() {
			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				modified = true;
			}
			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				modified = true;
			}
			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				modified = true;
			}
		});
	}

	/**
	 * Action which opens file and paints every geometrical object 
	 * from that file. Or displays error dialog if that is not possible.
	 */
	final Action openCanvasAction = new AbstractAction("Open") {
		/** For serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter jvdFilter = new FileNameExtensionFilter(
					"jvd files (*.jvd)", "jvd"
			);
			fc.setFileFilter(jvdFilter);
			fc.setDialogTitle("Open goemetrical objects.");
			if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File fileName = fc.getSelectedFile();
			Path filePath = fileName.toPath();
			if (!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(
					frame, 
					"File " + fileName.getAbsolutePath() + " doesn't exist!",
					"Error while opening", 
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			List<String> lines = null;
			try {
				lines = Files.readAllLines(filePath);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(
					frame, 
					"Unable to read file " + fileName.getAbsolutePath(),
					"Error while reading", 
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			currentJvdPath = filePath;
			model.clear();
			if (lines == null || lines.isEmpty()) {
				return;
			}
			int nLines = 0;
			int nCircles = 0;
			int nFilled = 0;
			for (String line : lines) {
				String[] elements = line.split("\\s+");
				if (elements.length == 0) {
					continue;
				}

				switch (elements[0].toUpperCase()) {
				case "LINE":
					if (elements.length != 8) {
						continue;
					}
					Point start = getPoint(elements[1], elements[2]);
					Point end = getPoint(elements[3], elements[4]);
					Color lineColor = getColor(elements[5], elements[6], elements[7]);
					if (start == null || end == null || lineColor == null) {
						continue;
					}
					model.add(new Line("Line#" + (++nLines), lineColor, start, end));
					break;

				case "CIRCLE":
					if (elements.length != 7) {
						continue;
					}
					Point cCenter = getPoint(elements[1], elements[2]);
					Integer cRadius = getInt(elements[3]);
					Color cBorder = getColor(elements[4], elements[5], elements[6]);
					if (cCenter == null || cRadius == null || cBorder == null) {
						continue;
					}
					model.add(new Circle("Circle#" + (++nCircles), cBorder, cCenter, cRadius));
					break;

				case "FCIRCLE":
					if (elements.length != 10) {
						continue;
					}
					Point fCenter = getPoint(elements[1], elements[2]);
					Integer fRadius = getInt(elements[3]);
					Color fBorder = getColor(elements[4], elements[5], elements[6]);
					Color fFill = getColor(elements[7], elements[8], elements[9]);
					if (fCenter == null || fRadius == null || fBorder == null || fFill == null) {
						continue;
					}
					model.add(new FilledCircle("FCircle#" + (++nFilled), fBorder, fFill, fCenter, fRadius));
					break;

				default:
					continue; // nothing to do if line is weird
				}
			}
			modified = false;
		}

		private Integer getInt(String number) {
			try {
				return Integer.parseInt(number);
			} catch (Exception exc) {
				return null;
			}
		}

		private Point getPoint(String sX, String sY) {
			try {
				int x = Integer.parseInt(sX);
				int y = Integer.parseInt(sY);
				return new Point(x, y);
			} catch (Exception exc) {
				return null;
			}
		}

		private Color getColor(String sR, String sG, String sB) {
			try {
				int r = Integer.parseInt(sR);
				int g = Integer.parseInt(sG);
				int b = Integer.parseInt(sB);
				return new Color(r, g, b);
			} catch (Exception exc) {
				return null;
			}
		}
	};

	/**
	 * Action used to save canvas to file.
	 * If painting on canvas in not already located
	 * this method is same as {@link #saveAsCanvasAction}.
	 */
	final Action saveCanvasAction = new AbstractAction("Save") {
		/** For serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentJvdPath == null) {
				saveAsCanvasAction.actionPerformed(null);
				return;
			}
			String result = "";
			for (int i = 0, size = model.getSize(); i < size; i++) {
				result += model.getObject(i).asText();
				result += "\n";
			}
			try {
				Files.write(currentJvdPath, result.getBytes(StandardCharsets.UTF_8));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(
					frame, 
					"Unable to save file.", 
					"Error", 
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			JOptionPane.showMessageDialog(frame, currentJvdPath + " is saved.", "Information",
					JOptionPane.INFORMATION_MESSAGE);
			modified = false;
		}
	};

	/**
	 * Saves current canvas on choosen location.
	 * If that is not possible error is displayed.
	 */
	final Action saveAsCanvasAction = new AbstractAction("Save As") {
		/** For serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter jvdFilter = new FileNameExtensionFilter(
					"jvd files (*.jvd)", "jvd"
			);
			jfc.setFileFilter(jvdFilter);
			jfc.setDialogTitle("Save as");
			if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(
					frame, 
					"Nothing saved", 
					"Warning", 
					JOptionPane.WARNING_MESSAGE
				);
				return;
			}
			String pathString = jfc.getSelectedFile().toString();
			if (!pathString.contains(".")) {
				pathString += ".jvd";
			}
			Path filePath = Paths.get(pathString);
			if (Files.exists(filePath)) {
				int answer = JOptionPane.showConfirmDialog(
					frame,
					"That file already exists.\nDo you want to replace it?", 
					"Overwrite",
					JOptionPane.YES_NO_OPTION
				);
				if (answer != JOptionPane.YES_OPTION) {
					return;
				}
			}
			currentJvdPath = filePath;
			saveCanvasAction.actionPerformed(null);
		}
	};

	/**
	 * Exits program.
	 * If canvas was changed user is asked
	 * if he wants to save all changes.
	 * Saving is done by using {@link #saveCanvasAction}.
	 */
	final Action exitProgramAction = new AbstractAction("Exit") {
		/** For serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (modified) {
				int answer = JOptionPane.showConfirmDialog(
					frame,
					"Canvas was modified.\nDo you want to keep changes?", 
					"Confirm close",
					JOptionPane.YES_NO_CANCEL_OPTION
				);
				if (answer == JOptionPane.YES_OPTION) {
					saveCanvasAction.actionPerformed(null);
				} else if (answer != JOptionPane.NO_OPTION) {
					return;
				}
			}
			frame.dispose();
		}
	};
	
	/**
	 * Enables user to extract picture from canvas to 
	 * png, gif or jpg file.
	 */
	final Action extractCanvasAction = new AbstractAction("Extract") {
		/** For serialization. */
		private static final long serialVersionUID = 1L;
		
		final String[] extensions = {"png", "gif", "jpg"};

		@Override
		public void actionPerformed(ActionEvent e) {
			if(model.getSize() == 0) {
				JOptionPane.showMessageDialog(
					frame, 
					"Canvas is empty, there is nothing to extract.", 
					"Information", 
					JOptionPane.INFORMATION_MESSAGE
				);
				return;
			}
			Object selected = JOptionPane.showInputDialog(
				frame, 
				"Which extension do you want to use?", 
				"Extraction", 
				JOptionPane.DEFAULT_OPTION, 
				null, 
				extensions, 
				extensions[0]
			);
			if(selected == null) {
				JOptionPane.showMessageDialog(
					frame, 
					"Extraction canceled.", 
					"Information", 
					JOptionPane.INFORMATION_MESSAGE
				);
				return;
			}
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Save extracted file");
			if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(
					frame, 
					"Nothing saved", 
					"Warning", 
					JOptionPane.WARNING_MESSAGE
				);
				return;
			}
			File file = new File(jfc.getSelectedFile()+"."+selected);
			try {
				createImage(file, selected.toString());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(
					frame, 
					"Unable to save file.", 
					"Error", 
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			JOptionPane.showMessageDialog(
				frame, 
				file + " is saved.", 
				"Information",
				JOptionPane.INFORMATION_MESSAGE
			);
		}
		
		private void createImage(File file, String extension) throws IOException {
			Point max = new Point(0, 0);
			Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
			for(int i = 0, size = model.getSize(); i < size; i++) {
				Point currentMax = model.getObject(i).getMax();
				if(currentMax.x > max.x) {
					max.x = currentMax.x;
				}
				if(currentMax.y > max.y) {
					max.y = currentMax.y;
				}
				Point currentMin = model.getObject(i).getMin();
				if(currentMin.x < min.x) {
					min.x = currentMin.x;
				}
				if(currentMin.y < min.y) {
					min.y = currentMin.y;
				}
			}
			
			int shiftX = 0 - min.x;
			int shiftY = 0 - min.y;
			DrawingModel tempModel = new GeometricsModel();
			for(int i = 0, size = model.getSize(); i < size; i++) {
				tempModel.add(model.getObject(i).shiftBy(shiftX, shiftY));
			}
			
			BufferedImage image = new BufferedImage(
				max.x - min.x, 
				max.y - min.y, 
				BufferedImage.TYPE_3BYTE_BGR
			);
			Graphics2D g = image.createGraphics();
			g.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON
			);
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, max.x-min.x, max.y-min.y);
			for(int i = 0, size = tempModel.getSize(); i < size; i++) {
				tempModel.getObject(i).paint(g);
			}
			g.dispose();
			ImageIO.write(image, extension, file);
		}
	};
}
