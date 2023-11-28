import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClinicVisualizer {

	public static void main(String[] args) {
		Map<Integer, Set<Integer>> city = new HashMap<>() {
			{
				this.put(1, new HashSet<Integer>() {
					{
						this.add(2);
						this.add(3);
						this.add(4);
					}
				});

				this.put(2, new HashSet<Integer>() {
					{
						this.add(1);
						this.add(3);
					}
				});

				this.put(3, new HashSet<Integer>() {
					{
						this.add(1);
						this.add(2);
						this.add(4);
					}
				});

				this.put(4, new HashSet<Integer>() {
					{
						this.add(1);
						this.add(3);
						this.add(5);
					}
				});

				this.put(5, new HashSet<Integer>() {
					{
						this.add(4);
					}
				});

				this.put(6, new HashSet<Integer>());

				for (int i = 7; i < 100; i++) {
					this.put(i, new HashSet<Integer>());
				}
			}
		};

		Set<Integer> clinicLocations = new HashSet<>() {
			{
				add(1);
				add(5);
				add(6);
			}
		};

		ClinicPlacer clinicPlacer = new ClinicPlacer(city, clinicLocations);
		ClinicVisualizer clinicVisualizer = new ClinicVisualizer(clinicPlacer);

		clinicVisualizer.run();

	}

	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 600;

	private final ClinicPlacer clinicPlacer;
	private final JFrame frame;

	public ClinicVisualizer(ClinicPlacer clinicPlacer) {
		this.clinicPlacer = clinicPlacer;

		this.frame = new JFrame("Clinic Placement Visualizer");
		this.frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.frame.setLayout(new BorderLayout());

		this.frame.add(new VisualizerPanel(), BorderLayout.CENTER);
	}

	public void run() {
		this.frame.setVisible(true);
	}

	public class VisualizerPanel extends JPanel {

		private static final int PADDING = 20;
		private static final int CLINIC_RADIUS = 15;

		private final Map<Integer, Point> locations;

		public VisualizerPanel() {
			super();

			this.locations = new HashMap<>();

			this.placeNodes();
		}

		public void placeNodes() {
			Set<Integer> locationSet = clinicPlacer.getCity().keySet();

			int offset = PADDING + CLINIC_RADIUS;

			int contentWidth = SCREEN_WIDTH - (2 * offset);
			int contentHeight = SCREEN_HEIGHT - (2 * offset) - 40;

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
}