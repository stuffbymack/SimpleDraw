package source;


import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class MinimalColorChooser extends JColorChooser {

	private static final long serialVersionUID = 1L;
	private static final Color[] COLORS = {
            Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE,
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA,
            new Color(128, 0, 0), new Color(128, 128, 0), new Color(0, 128, 0), new Color(0, 128, 128),
            new Color(0, 0, 128), new Color(128, 0, 128), new Color(255, 51, 51), new Color(255, 153, 51),
            new Color(255, 255, 51), new Color(102, 204, 0), new Color(51, 153, 255), new Color(204, 51, 255),
            new Color(204, 0, 0), new Color(204, 102, 0), new Color(204, 204, 0), new Color(0, 204, 0),
            new Color(0, 204, 204), new Color(51, 51, 255), new Color(204, 0, 204), new Color(153, 153, 153)
    };

    public MinimalColorChooser(Color initialColor) {
        super(initialColor);
        setChooserPanels(new AbstractColorChooserPanel[] {new MinimalChooserPanel()});
    }

    private class MinimalChooserPanel extends AbstractColorChooserPanel {
     
		private static final long serialVersionUID = 1L;

		public void buildChooser() {
            JPanel panel = new JPanel(new GridLayout(4, 8, 2, 2));
            for (Color color : COLORS) {
                JButton button = new JButton();
                button.setBackground(color);
                button.setPreferredSize(new Dimension(16, 16));
                button.addActionListener(e -> getColorSelectionModel().setSelectedColor(color));
                panel.add(button);
            }
            add(panel);
        }

        public String getDisplayName() {
            return "Minimal";
        }

        public Icon getSmallDisplayIcon() {
            return null;
        }

        public Icon getLargeDisplayIcon() {
            return null;
        }

		@Override
		public void updateChooser() {

		}
    }
}

