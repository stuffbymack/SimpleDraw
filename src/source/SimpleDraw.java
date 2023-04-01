package source;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SimpleDraw extends JFrame implements MouseMotionListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private BufferedImage canvas;
	private Graphics2D g2d;
	private JButton saveButton;
	private JButton clearButton;
	private JButton colorButton;
	private MinimalColorChooser colorChooser;
	private JDialog colorDialog;
	private Point mousePosition;
	private static final String NAME = "SimpleDraw";
	private static final String SAVE = "Save";
	private static final String CLEAR = "Clear";
	private static final String PALETTE = "Palette";

	public SimpleDraw() {
		super(NAME);
		colorChooser = new MinimalColorChooser(Color.BLACK);
        colorChooser.setPreviewPanel(new JPanel()); // disable the preview panel
        colorDialog = new JDialog();
        colorDialog.add(colorChooser);
        colorDialog.pack();
        colorDialog.setResizable(false);
        colorDialog.setTitle(PALETTE);
        // Not resize-able, will break drawing if resized at the moment.
		setResizable(false);
		// Attempt to mimic the system look and feel
		updateLookAndFeel();
		// Build 2d canvas object in JFrame
		canvas = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		g2d = canvas.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		// Set the canvas background to white
		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		g2d.setPaint(Color.BLACK);
		// Button factory
		JPanel buttonPanel = new JPanel(new FlowLayout());
		saveButton = new JButton(SAVE);
		clearButton = new JButton(CLEAR);
		colorButton = new JButton(PALETTE);
		buttonPanel.add(saveButton);
		buttonPanel.add(colorButton);
		buttonPanel.add(clearButton);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabel(new ImageIcon(canvas)), BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		saveButton.addActionListener(this);
		clearButton.addActionListener(this);
		colorButton.addActionListener(this);
		// JFrame parameters
		addMouseMotionListener(this);
		setSize(524, 584);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Sets the window to be center screen
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {
			saveCanvas();
		} else if (e.getSource() == clearButton) {
			clearCanvas();
		} else if (e.getSource() == colorButton) {
			openColorWindow();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mousePosition != null) {
			// This can be better, it gets the color every drag event
			g2d.setPaint(colorChooser.getColor());
			int x = e.getX();
			int y = e.getY();
			// TODO Fix this bad fix for brush/mouse offset
			// 29 = (584 - 512) / 2 - (Brush / 2)
			// 8 = 524 - 512
			g2d.drawLine(mousePosition.x - 8, mousePosition.y - 29, x - 8, y - 29);
			mousePosition = new Point(x, y);
			getContentPane().repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = new Point(e.getX(), e.getY());
	}

	public void saveCanvas() {
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(chooser.getFileSystemView().getDefaultDirectory());
			int result = chooser.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				String filePath = chooser.getSelectedFile().getPath();
				if (!filePath.endsWith(".png")) {
					filePath += ".png";
				}
				ImageIO.write(canvas, "png", new File(filePath));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void clearCanvas() {
		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		g2d.setPaint(Color.BLACK);
		getContentPane().repaint();
	}
	
	public void colorCanvas() {
		g2d.setPaint(colorChooser.getColor());
		g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		g2d.setPaint(Color.BLACK);
		getContentPane().repaint();
	}

	public void openColorWindow() {
        if (!colorDialog.isVisible()) {
		colorDialog.setVisible(true);
        colorDialog.setLocationRelativeTo(this);
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