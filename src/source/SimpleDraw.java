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
	private Point mousePosition;
	private static final String NAME = "SimpleDraw";
	private static final String SAVE = "Save";
	private static final String CLEAR = "Clear";
	private static final String COLOR = "Color";

	public SimpleDraw() {
		super(NAME);
		// Not resize-able, will break drawing if resized at the moment.
		setResizable(false);
		// Attempt to mimic the system look and feel
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
		colorButton = new JButton(COLOR);
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
		setSize(512, 564);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Sets the window to be center screen
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
			chooseColor();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mousePosition != null) {
			int x = e.getX();
			int y = e.getY();
			// TODO Fix this bad fix for brush/mouse offset
			// the "- 16" fixes some inherent offset from the JFrame boiler itself, will break if window is resized.
			g2d.drawLine(mousePosition.x, mousePosition.y - 16, x, y - 16);
			mousePosition = new Point(x, y);
			getContentPane().repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = new Point(e.getX(), e.getY());
	}
	

	public void saveCanvas () {
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
	
	public void chooseColor() {
		Color initialcolor = g2d.getColor();    
    	Color color = JColorChooser.showDialog(this,COLOR,initialcolor);
    	g2d.setPaint(color);  
	}
}