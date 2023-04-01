package source;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SimpleDraw extends JFrame implements MouseListener, MouseMotionListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel canvas, buttonPanel;
    private int brushSize = 15;
    private int lastX, lastY;
    private Image bufferImage;
    private Graphics bufferGraphics;
    private JButton clearButton, saveButton;
    private JFileChooser fileChooser;
    private FileNameExtensionFilter pngFilter;

    public SimpleDraw() {
    	setTitle("SimpleDraw");
        setSize(512, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);

        // Panel for drawing canvas
        canvas = new JPanel() {
			private static final long serialVersionUID = 2L;
			public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bufferImage != null) {
                    g.drawImage(bufferImage, 0, 0, null);
                }
            }
        };
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        add(canvas);

        // Panel just for buttons
        buttonPanel = new JPanel(new BorderLayout());
        
        // Clear button created for erasing the canvas
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearCanvas());
        buttonPanel.add(clearButton, BorderLayout.NORTH);

        // Save button added for exporting the canvas
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveCanvas());
        buttonPanel.add(saveButton, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.NORTH);

        fileChooser = new JFileChooser();
        pngFilter = new FileNameExtensionFilter("PNG files", "png");
        fileChooser.setFileFilter(pngFilter);

        setVisible(true);

        bufferImage = createImage(getWidth(), getHeight());
        bufferGraphics = bufferImage.getGraphics();
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
    }

    private void clearCanvas() {
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        canvas.getGraphics().drawImage(bufferImage, 0, 0, null);
    }

    private void saveCanvas() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.endsWith(".png")) {
                filePath += ".png";
            }
            try {
                ImageIO.write((RenderedImage) bufferImage, "png", new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        drawBrush(lastX, lastY, e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        drawBrush(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
    }
    
    private void drawBrush(int x1, int y1, int x2, int y2) {
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillOval(x2 - brushSize / 2, y2 - brushSize / 2, brushSize, brushSize);
        canvas.getGraphics().drawImage(bufferImage, 0, 0, null);
    }
    
    // Useless methods needed for implementation.
    public void actionPerformed(ActionEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseDragExited(MouseEvent e) {}
    public void mouseDragEntered(MouseEvent e) {}

    public static void main(String[] args) {
        new SimpleDraw();
    }
}
