package source;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SimpleDraw extends JFrame implements MouseMotionListener, MouseListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private BufferedImage canvas;
	private Graphics2D graphics;
	private JButton saveButton;
	private JButton clearButton;
	private JButton colorButton;
	private MinimalColorChooser colorChooser;
	private JDialog colorDialog;
	private Point mousePosition;
	private boolean mousePressed;
	private JPanel buttonPanel;
	private static final String FRAME_NAME = "SimpleDraw";
	private static final String SAVE_BUTTON = "Save";
	private static final String CLEAR_BUTTON = "Clear";
	private static final String PALETTE_BUTTON = "Palette";
	private static final int CANVAS_SIZE = 512;
	private static final int BRUSH_SIZE = 15;

	public SimpleDraw() {
		super(FRAME_NAME);
		setupColorChooser();
		setupColorDialog();
		setupResizable();
		setupLookAndFeel();
		setupCanvas();
		setupGraphics();
		setupButtons();
		setupContentPane();
		setupListeners();
		setupJFrame();
	}

	private void setupColorChooser() {
		colorChooser = new MinimalColorChooser(Color.BLACK);
		colorChooser.setPreviewPanel(new JPanel());
	}

	private void setupColorDialog() {
		colorDialog = new JDialog();
		colorDialog.add(colorChooser);
		colorDialog.pack();
		colorDialog.setResizable(false);
		colorDialog.setTitle(PALETTE_BUTTON);
	}

	private void setupResizable() {
		setResizable(false);
	}

	private void setupLookAndFeel() {
		updateLookAndFeel();
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
		colorButton = new JButton(PALETTE_BUTTON);
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(saveButton);
		buttonPanel.add(colorButton);
		buttonPanel.add(clearButton);
		saveButton.addActionListener(this);
		colorButton.addActionListener(this);
		clearButton.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					clearCanvas();
				} else if (SwingUtilities.isRightMouseButton(e)) {
					colorCanvas();
				}
			}
		});
	}

	private void setupContentPane() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabel(new ImageIcon(canvas)), BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
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
		} else if (e.getSource() == colorButton) {
			openColorWindow();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		graphics.setPaint(colorChooser.getColor());
		graphics.fillOval(mousePosition.x - 13, mousePosition.y - 36, BRUSH_SIZE, BRUSH_SIZE);
		getContentPane().repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mousePosition != null && mousePressed) {
			graphics.setPaint(colorChooser.getColor());
			int x = e.getX();
			int y = e.getY();
			// TODO Fix this bad fix for brush/mouse offset
			// 29 = (584 - 512) / 2 - (Brush / 2)
			// 6 = ?
			graphics.drawLine(mousePosition.x - 6, mousePosition.y - 29, x - 6, y - 29);
			mousePosition = new Point(x, y);
			getContentPane().repaint();
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
		getContentPane().repaint();
	}

	public void colorCanvas() {
		graphics.setPaint(colorChooser.getColor());
		graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		getContentPane().repaint();
	}

	public void openColorWindow() {
		if (!colorDialog.isVisible()) {
			colorDialog.setLocationRelativeTo(this);
			int offsetX = 120;
			int offsetY = 600;
			Point referenceLocation = this.getLocationOnScreen();
			colorDialog.setLocation(referenceLocation.x + offsetX, referenceLocation.y + offsetY);
			colorDialog.setVisible(true);
		} else {
			colorDialog.requestFocus();
		}
	}

	public void updateLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}