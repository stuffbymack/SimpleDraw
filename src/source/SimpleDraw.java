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
    private Point mousePosition;
    
    public SimpleDraw() {
        super("SimpleDraw");
        setResizable(false);
        setLocation(700, 250);
        
        canvas = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Set the canvas background to white
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setPaint(Color.BLACK);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        saveButton = new JButton("Save");
        clearButton = new JButton("Clear");
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JLabel(new ImageIcon(canvas)), BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        saveButton.addActionListener(this);
        clearButton.addActionListener(this);
        addMouseMotionListener(this);
        
        setSize(512, 564);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
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
        } else if (e.getSource() == clearButton) {
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            g2d.setPaint(Color.BLACK);
            getContentPane().repaint();
        }
    }
    
    public void mouseDragged(MouseEvent e) {
        if (mousePosition != null) {
            int x = e.getX();
            int y = e.getY();
            g2d.drawLine(mousePosition.x, mousePosition.y - 16, x, y -16);
            mousePosition = new Point(x, y);
            getContentPane().repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        mousePosition = new Point(e.getX(), e.getY());
    }

    public static void main(String[] args) {
        new SimpleDraw();
    }
}