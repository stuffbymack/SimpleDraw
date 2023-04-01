package source;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class SimpleDraw extends JFrame implements MouseMotionListener, MouseListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private BufferedImage canvas;
	private Graphics2D g2d;
	private JButton saveButton;
	private JButton clearButton;
	private JButton colorButton;
	private MinimalColorChooser colorChooser;
	private JDialog colorDialog;
	private Point mousePosition;
	private boolean mousePressed;
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
		//Extra clear button magic for right click fill with color behavior
		clearButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    clearCanvas();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                	colorCanvas();
                }
            }
        });
		colorButton.addActionListener(this);
		// JFrame parameters
		addMouseMotionListener(this);
		addMouseListener(this);
		setSize(524, 584);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Sets the window to be center screen
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//The clear button is not handled here because it has added functionality for left/right click
		if (e.getSource() == saveButton) {
			saveCanvas();
		} else if (e.getSource() == colorButton) {
			openColorWindow();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mousePosition != null && mousePressed) {
			// This can be better, it gets the color every drag event
			g2d.setPaint(colorChooser.getColor());
			int x = e.getX();
			int y = e.getY();
			// TODO Fix this bad fix for brush/mouse offset
			// 29 = (584 - 512) / 2 - (Brush / 2)
			// 6 = ?
			g2d.drawLine(mousePosition.x - 6, mousePosition.y - 29, x - 6, y - 29);
			mousePosition = new Point(x, y);
			getContentPane().repaint();
		}
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		// Draws a single circle when clicked for full action of mouse
		g2d.setPaint(colorChooser.getColor());
		g2d.fillOval(mousePosition.x - 13, mousePosition.y - 36, 15, 15);
		getContentPane().repaint();
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
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(lastSavedFilePath));
            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getPath();
                if (!filePath.endsWith(".png")) {
                    filePath += ".png";
                }
                ImageIO.write(canvas, "png", new File(filePath));
                lastSavedFilePath = filePath;
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