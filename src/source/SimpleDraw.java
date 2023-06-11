/*
 *     Copyright 2023 Mack Fisher @ http://stuffbymack.info
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package source;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimpleDraw extends JFrame implements MouseMotionListener, MouseListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private BufferedImage canvas;
	private Graphics2D graphics;
	private JButton saveButton;
	private JButton clearButton;
	private MinimalColorChooser colorChooser;
	private Point mousePosition;
	private boolean mousePressed;
	private JPanel buttonPanel;
	private JLabel labelPanel;
	private static final String FRAME_NAME = "SimpleDraw";
	private static final String SAVE_BUTTON = "Save";
	private static final String CLEAR_BUTTON = "Clear";
	private static final int CANVAS_SIZE = 512;
	private static final int BRUSH_SIZE = 15;

	public SimpleDraw() {
		super(FRAME_NAME);
		setupColorChooser();
		setupLookAndFeel();
		setupCanvas();
		setupGraphics();
		setupButtons();
		setupLabel();
		setupContentPane();
		setupListeners();
		setupJFrame();
	}

	private void setupColorChooser() {
		colorChooser = new MinimalColorChooser(Color.BLACK);
		colorChooser.setPreviewPanel(new JPanel());
	}

	private void setupLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void setupCanvas() {
		canvas = new BufferedImage(CANVAS_SIZE, CANVAS_SIZE, BufferedImage.TYPE_INT_ARGB);
	}

	private void setupGraphics() {
		graphics = canvas.createGraphics();
		graphics.setColor(Color.BLACK);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setStroke(new BasicStroke(BRUSH_SIZE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	private void setupButtons() {
		saveButton = new JButton(SAVE_BUTTON);
		clearButton = new JButton(CLEAR_BUTTON);
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(saveButton);
		buttonPanel.add(clearButton);
		saveButton.addActionListener(this);
		clearButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					clearCanvas();
				} else if (SwingUtilities.isRightMouseButton(e)) {
					colorCanvas();
				}
			}
		});
	}

	private void setupLabel() {
		labelPanel = new JLabel(new ImageIcon(canvas));
	}

	private void setupContentPane() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(labelPanel, BorderLayout.CENTER);
		// Create a panel for the color chooser
		JPanel colorPanel = new JPanel(new BorderLayout());
		colorPanel.add(colorChooser, BorderLayout.NORTH);
		// Add the button panel and color panel to the main panel
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(buttonPanel, BorderLayout.NORTH);
		mainPanel.add(colorPanel, BorderLayout.SOUTH);
		// Add the main panel to the content pane
		getContentPane().add(mainPanel, BorderLayout.SOUTH);
	}

	private void setupListeners() {
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	private void setupJFrame() {
		setSize(524, 584);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {
			saveCanvas();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		graphics.setPaint(colorChooser.getColor());
		int offsetY = ((this.getHeight() - CANVAS_SIZE) / 2) - 39;
		int offsetX = ((this.getWidth() - CANVAS_SIZE) / 2) + 7;
		graphics.fillOval(mousePosition.x - offsetX, mousePosition.y - offsetY, BRUSH_SIZE, BRUSH_SIZE);
		labelPanel.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mousePosition != null && mousePressed) {
			graphics.setPaint(colorChooser.getColor());
			int x = e.getX();
			int y = e.getY();
			int offsetY = ((this.getHeight() - CANVAS_SIZE) / 2) - 46;
			int offsetX = (this.getWidth() - CANVAS_SIZE) / 2;
			graphics.drawLine(mousePosition.x - offsetX, mousePosition.y - offsetY, x - offsetX, y - offsetY);
			mousePosition = new Point(x, y);
			labelPanel.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = new Point(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	private String lastSavedFilePath = System.getProperty("user.dir");

	public void saveCanvas() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(lastSavedFilePath));
			int result = fileChooser.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				String selectedFilePath = fileChooser.getSelectedFile().getPath();
				if (!selectedFilePath.endsWith(".png")) {
					selectedFilePath += ".png";
				}
				try (FileOutputStream fos = new FileOutputStream(selectedFilePath)) {
					ImageIO.write(canvas, "png", fos);
					lastSavedFilePath = selectedFilePath;
				} catch (FileNotFoundException ex) {
					System.err.println("Error: Unable to save file - File not found.");
				} catch (IOException ex) {
					System.err.println("Error: Unable to save file - IO Exception.");
				} catch (Exception ex) {
					System.err.println("Error: Unable to save file.");
				}
			}
		} catch (Exception ex) {
			System.err.println("Error: Unable to open file chooser.");
		}
	}

	public void clearCanvas() {
		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		labelPanel.repaint();
	}

	public void colorCanvas() {
		graphics.setPaint(colorChooser.getColor());
		graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		labelPanel.repaint();
	}
}
