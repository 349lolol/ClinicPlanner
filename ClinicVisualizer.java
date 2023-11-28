import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClinicVisualizer {

	public static void main(String[] args) {
		Map<Integer, Set<Integer>> city = new HashMap<>() {{
			this.put(1, new HashSet<Integer>() {{
				this.add(2);
				this.add(3);
				this.add(4);
			}});

			this.put(2, new HashSet<Integer>() {{
				this.add(1);
				this.add(3);
			}});

			this.put(3, new HashSet<Integer>() {{
				this.add(1);
				this.add(2);
				this.add(4);
			}});

			this.put(4, new HashSet<Integer>() {{
				this.add(1);
				this.add(3);
				this.add(5);
			}});

			this.put(5, new HashSet<Integer>() {{
				this.add(4);
			}});

			this.put(6, new HashSet<Integer>());
		}};

		Set<Integer> clinicLocations = new HashSet<>() {{
			add(1);
			add(5);
			add(6);
		}};

		ClinicPlacer clinicPlacer = new ClinicPlacer(city, clinicLocations);

	}

	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 800;

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
		private static final int CLINIC_RADIUS = 5;

		private final Map<Integer, Point> locations;
		private final int maxRadius;

		public VisualizerPanel() {
			super();

			this.locations = new HashMap<>();

			// Calculate max radius
			Dimension size = this.getSize();

			Dimension contentSize = new Dimension(
				(int) size.getWidth() - 2 * PADDING,
				(int) size.getHeight() - 2 * PADDING
			);

			this.maxRadius = (int) Math.max(contentSize.getWidth(), contentSize.getHeight());

			this.placeNodes();
		}

		public void placeNodes() {
			Set<Integer> locationSet = clinicPlacer.getCity().keySet();

			for (int location : locationSet) {
				// Generate random polar coordinate
				double theta = Math.random() * 2 * Math.PI;
				double radius = Math.random() * (maxRadius - CLINIC_RADIUS) + CLINIC_RADIUS;

				// Convert to rectangular coordinates
				int x = (int) (radius * Math.cos(theta));
				int y = (int) (radius * Math.sin(theta));

				// Translate the point such that the center of the screen is the origin
				this.locations.put(location, new Point(x + SCREEN_WIDTH / 2, y + SCREEN_HEIGHT / 2));
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setColor(Color.RED);

			for (Map.Entry<Integer, Point> entry : this.locations.entrySet()) {
				Point point = entry.getValue();
				int x = (int) point.getX();
				int y = (int) point.getY(); 

				g.fillOval(x - CLINIC_RADIUS, y - CLINIC_RADIUS, CLINIC_RADIUS * 2, CLINIC_RADIUS * 2);
			}
		}
	}
}