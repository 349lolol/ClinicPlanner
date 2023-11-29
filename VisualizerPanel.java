import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

public class VisualizerPanel extends JPanel {

		private static final int PADDING = 20;
		private static final int CLINIC_RADIUS = 15;

		private final ClinicPlacer clinicPlacer;
		private final Map<Integer, Point> locations;
		private int currentNode;

		public VisualizerPanel(ClinicPlacer clinicPlacer) {
			super();

			this.clinicPlacer = clinicPlacer;
			this.locations = new HashMap<>();

			// -1 for not selected
			this.currentNode = -1;

			this.placeNodes();
		}

		public void placeNodes() {
			Set<Integer> locationSet = clinicPlacer.getCity().keySet();

			int offset = PADDING + CLINIC_RADIUS;

			int contentWidth = ClinicVisualizer.SCREEN_WIDTH - (2 * offset);
			int contentHeight = ClinicVisualizer.SCREEN_HEIGHT - (2 * offset) - 40;

			for (int node : locationSet) {
				boolean isValid = false;
				int x = 0;
				int y = 0;

				// Generate coordinates
				while (!isValid) {
					x = (int) (Math.random() * contentWidth) + offset;
					y = (int) (Math.random() * contentHeight) + offset;

					boolean invalidPair = false;

					for (Map.Entry<Integer, Point> location : this.locations.entrySet()) {
						Point neighborPoint = location.getValue();

						if (Util.distance(x, y, neighborPoint.getX(), neighborPoint.getY()) < 2 * CLINIC_RADIUS) {
							invalidPair = true;
							break;
						}
					}

					if (!invalidPair) {
						isValid = true;
					}
				}

				this.locations.put(node, new Point(x, y));
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			this.drawEdges(g);
			this.drawNodes(g);
		}

		private void drawNodes(Graphics g) {
			Font font = new Font("Arial", Font.BOLD, 12);

			g.setFont(font);
			((Graphics2D) g).setStroke(new BasicStroke(2));

			// Get the FontMetrics
			FontMetrics metrics = g.getFontMetrics(font);
			
			for (Map.Entry<Integer, Point> entry : this.locations.entrySet()) {
				String nodeNumber = Integer.toString(entry.getKey());
				Point point = entry.getValue();

				int x = (int) point.getX();
				int y = (int) point.getY();

				int topCornerX = x - CLINIC_RADIUS;
				int topCornerY = y - CLINIC_RADIUS;

				// Determine the X coordinate for the text
				int fontCornerX = topCornerX + (CLINIC_RADIUS * 2 - metrics.stringWidth(nodeNumber)) / 2;
				
				int fontCornerY = topCornerY + ((CLINIC_RADIUS * 2 - metrics.getHeight()) / 2) + metrics.getAscent();
				
				g.setColor(Color.LIGHT_GRAY);
				g.fillOval(x - CLINIC_RADIUS, y - CLINIC_RADIUS, CLINIC_RADIUS * 2, CLINIC_RADIUS * 2);
				
				g.setColor(Color.BLACK);
				g.drawOval(x - CLINIC_RADIUS, y - CLINIC_RADIUS, CLINIC_RADIUS * 2, CLINIC_RADIUS * 2);

				g.drawString(nodeNumber, fontCornerX, fontCornerY);
			}
		}
		
		private void drawEdges(Graphics g) {
			Map<Integer, Set<Integer>> city = clinicPlacer.getCity();

			g.setColor(new Color(0, 0, 255, 50));
			((Graphics2D) g).setStroke(new BasicStroke(5));

			for (Map.Entry<Integer, Set<Integer>> entry : city.entrySet()) {
				int currentNode = entry.getKey();
				Point currentPoint = this.locations.get(currentNode);

				for (int neighborNode : entry.getValue()) {
					Point neighborPoint = this.locations.get(neighborNode);

					g.drawLine(
						(int) currentPoint.getX(),
						(int) currentPoint.getY(),
						(int) neighborPoint.getX(),
						(int) neighborPoint.getY()
					);
				}
			}
		}
	}