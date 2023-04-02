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

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class MinimalColorChooser extends JColorChooser {

	private static final long serialVersionUID = 1L;
	private static final String FRAME_NAME = ":)";
	private static final Color[] COLORS = { new Color(0, 0, 0), // black
			new Color(70, 70, 70), // dark grey
			new Color(120, 120, 120), // grey
			new Color(153, 0, 48), // dark red
			new Color(237, 28, 36), // bright red
			new Color(255, 126, 0), // orange
			new Color(255, 194, 14), // dark yellow
			new Color(255, 242, 0), // bright yellow
			new Color(168, 230, 29), // bright green
			new Color(34, 177, 76), // dark green
			new Color(0, 183, 239), // bright teal
			new Color(77, 109, 243), // bright blue
			new Color(47, 54, 153), // dark blue
			new Color(111, 49, 152), // purple
			new Color(255, 255, 255), // white
			new Color(220, 220, 220), // light grey
			new Color(180, 180, 180), // lighter grey
			new Color(156, 90, 60), // brown
			new Color(255, 163, 177), // pink
			new Color(229, 170, 122), // light brown
			new Color(245, 228, 156), // pale yellow
			new Color(255, 249, 189), // pale peach
			new Color(211, 249, 188), // light green
			new Color(157, 187, 97), // olive green
			new Color(153, 217, 234), // light blue
			new Color(112, 154, 209), // pale blue
			new Color(84, 109, 142), // darker blue
			new Color(181, 165, 213) // lavender
	};

	public MinimalColorChooser(Color initialColor) {
		super(initialColor);
		setChooserPanels(new AbstractColorChooserPanel[] { new MinimalChooserPanel() });
	}

	private class MinimalChooserPanel extends AbstractColorChooserPanel {

		private static final long serialVersionUID = 1L;

		public void buildChooser() {
			JPanel panel = new JPanel(new GridLayout(2, 14, 2, 2));
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
			return FRAME_NAME;
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
