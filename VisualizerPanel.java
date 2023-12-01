import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VisualizerPanel extends JPanel {
	private static final int PADDING = 20;
	private static final int CLINIC_RADIUS = 15;

	private static final Color EDGE_COLOR = new Color(0, 0, 255, 50);
	private static final Color SELECTED_EDGE_COLOR = new Color(189, 0, 255, 50);

	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 40;

	private static final int BORDER_OFFSET = PADDING + CLINIC_RADIUS;

	private static final int CONTENT_WIDTH = ClinicVisualizer.SCREEN_WIDTH - (2 * BORDER_OFFSET);
	private static final int CONTENT_HEIGHT = ClinicVisualizer.SCREEN_HEIGHT - (2 * BORDER_OFFSET);

	private final ClinicPlacer clinicPlacer;
	private final Map<Integer, Point> locations;
	private Point currentDragPoint;
	private int anchorNode;
	private int selectedNode;

	private final Button addNodeButton;
	private final Button deleteNodeButton;
	private final Button deleteEdgeButton;

	public VisualizerPanel(ClinicPlacer clinicPlacer) {
		super();

		this.clinicPlacer = clinicPlacer;
		this.locations = new HashMap<>();

		// -1 for not selected
		this.selectedNode = -1;
		this.anchorNode = -1;

		this.placeNodes();

		// Add UI elements
		this.addNodeButton = new Button(
				new Rectangle(
						ClinicVisualizer.SCREEN_WIDTH - BUTTON_WIDTH - PADDING,
						ClinicVisualizer.SCREEN_HEIGHT - BUTTON_HEIGHT - PADDING,
						BUTTON_WIDTH,
						BUTTON_HEIGHT),
				"Add Node");

		this.deleteNodeButton = new Button(
				new Rectangle(
						ClinicVisualizer.SCREEN_WIDTH - BUTTON_WIDTH - PADDING,
						ClinicVisualizer.SCREEN_HEIGHT - (2 * BUTTON_HEIGHT) - (int) (1.5 * PADDING),
						BUTTON_WIDTH,
						BUTTON_HEIGHT),
				"Delete Node");

		this.deleteEdgeButton = new Button(
				new Rectangle(
						ClinicVisualizer.SCREEN_WIDTH - BUTTON_WIDTH - PADDING,
						ClinicVisualizer.SCREEN_HEIGHT - (2 * BUTTON_HEIGHT) - (int) (1.5 * PADDING),
						BUTTON_WIDTH,
						BUTTON_HEIGHT),
				"Delete Connection");

		this.addMouseListener(new PanelMouseListener());
		this.addMouseMotionListener(new PanelMouseMotionListener());
	}

	public void placeNodes() {
		Set<Integer> locationSet = clinicPlacer.getCity().keySet();

		for (int node : locationSet) {
			this.locations.put(node, this.generatePoint());
		}
	}

	public Point generatePoint() {
		boolean isValid = false;
		int x = 0;
		int y = 0;

		// Generate coordinates
		while (!isValid) {
			x = (int) (Math.random() * CONTENT_WIDTH) + BORDER_OFFSET;
			y = (int) (Math.random() * CONTENT_HEIGHT) + BORDER_OFFSET;

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

		return new Point(x, y);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.drawEdges(g);
		this.drawNodes(g);
		this.drawOverlay(g);
	}

	private void drawNodes(Graphics g) {
		Font font = new Font("Arial", Font.BOLD, 12);

		g.setFont(font);
		((Graphics2D) g).setStroke(new BasicStroke(2));

		// Get the FontMetrics
		FontMetrics metrics = g.getFontMetrics(font);

		for (Map.Entry<Integer, Point> entry : this.locations.entrySet()) {
			int nodeNumber = entry.getKey();
			String nodeString = Integer.toString(nodeNumber);
			Point point = entry.getValue();

			int x = (int) point.getX();
			int y = (int) point.getY();

			int topCornerX = x - CLINIC_RADIUS;
			int topCornerY = y - CLINIC_RADIUS;

			// Determine the X coordinate for the text
			int fontCornerX = topCornerX + (CLINIC_RADIUS * 2 - metrics.stringWidth(nodeString)) / 2;

			int fontCornerY = topCornerY + ((CLINIC_RADIUS * 2 - metrics.getHeight()) / 2) + metrics.getAscent();

			g.setColor(Color.LIGHT_GRAY);
			g.fillOval(x - CLINIC_RADIUS, y - CLINIC_RADIUS, CLINIC_RADIUS * 2, CLINIC_RADIUS * 2);

			g.setColor(Color.BLACK);

			if (selectedNode == nodeNumber) {

				((Graphics2D) g).setStroke(new BasicStroke(4));

				g.drawOval(x - CLINIC_RADIUS - 1, y - CLINIC_RADIUS - 1, CLINIC_RADIUS * 2 + 2, CLINIC_RADIUS * 2 + 2);

				((Graphics2D) g).setStroke(new BasicStroke(2));
			} else {
				g.drawOval(x - CLINIC_RADIUS, y - CLINIC_RADIUS, CLINIC_RADIUS * 2, CLINIC_RADIUS * 2);
			}

			g.drawString(nodeString, fontCornerX, fontCornerY);
		}
	}

	private void drawEdges(Graphics g) {
		Map<Integer, Set<Integer>> city = clinicPlacer.getCity();

		g.setColor(EDGE_COLOR);
		((Graphics2D) g).setStroke(new BasicStroke(5));

		for (Map.Entry<Integer, Set<Integer>> entry : city.entrySet()) {
			int currentNode = entry.getKey();
			Point currentPoint = this.locations.get(currentNode);

			for (int neighborNode : entry.getValue()) {
				Point neighborPoint = this.locations.get(neighborNode);

				boolean selectedEdge = (neighborNode == selectedNode) || (currentNode == selectedNode);

				if (selectedEdge) {
					g.setColor(SELECTED_EDGE_COLOR);
				}

				g.drawLine(
					(int) currentPoint.getX(),
					(int) currentPoint.getY(),
					(int) neighborPoint.getX(),
					(int) neighborPoint.getY()
				);

				if (selectedEdge) {
					g.setColor(EDGE_COLOR);
				}
			}
		}

		if ((anchorNode != -1) && (currentDragPoint != null)) {
			g.setColor(SELECTED_EDGE_COLOR);

			Point anchorPoint = this.locations.get(anchorNode);

			g.drawLine(
				(int) anchorPoint.getX(),
				(int) anchorPoint.getY(),
				(int) currentDragPoint.getX(),
				(int) currentDragPoint.getY()
			);

			g.setColor(EDGE_COLOR);
		}
	}

	private void drawOverlay(Graphics g) {
		this.addNodeButton.draw(g);

		if (selectedNode != -1) {
			this.deleteNodeButton.draw(g);
		}
	}

	private class PanelMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			Point clickedPoint = e.getPoint();

			// Buttons
			if (addNodeButton.getBounds().contains(clickedPoint)) {
				addNodeButton.setPressed(true);
				repaint();

				int value;

				try {
					value = Integer.parseInt(JOptionPane.showInputDialog("Input city number: "));
				} catch (NumberFormatException exception) {
					JOptionPane.showMessageDialog(null, "Not a positive integer.");
					return;
				}

				if (clinicPlacer.getCity().containsKey(value)) {
					JOptionPane.showMessageDialog(null, "City already exists");
					return;
				}

				clinicPlacer.addCity(value);
				locations.put(value, generatePoint());

				repaint();
				addNodeButton.setPressed(false);

				return;
			}

			if ((selectedNode != -1) && (deleteNodeButton.getBounds().contains(clickedPoint))) {
				deleteNodeButton.setPressed(true);
				repaint();
				
				clinicPlacer.deleteNode(selectedNode);
				locations.remove(selectedNode);
				selectedNode = -1;
				
				deleteNodeButton.setPressed(false);
				repaint();
				return;
			}

			// Selecting entities
			if (SwingUtilities.isLeftMouseButton(e)) {
				for (Map.Entry<Integer, Point> entry : locations.entrySet()) {
					int currentNode = entry.getKey();
					Point currentPoint = entry.getValue();

					if (Util.distance(clickedPoint, currentPoint) <= CLINIC_RADIUS) {
						selectedNode = currentNode;

						repaint();
						return;
					}
				}
			}

			selectedNode = -1;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point clickedPoint = e.getPoint();

			if (anchorNode != -1) {
				for (Map.Entry<Integer, Point> entry : locations.entrySet()) {
					int currentNode = entry.getKey();
					Point currentPoint = entry.getValue();

					if (Util.distance(clickedPoint, currentPoint) <= CLINIC_RADIUS) {
						clinicPlacer.createConnection(anchorNode, currentNode);
						break;
					}
				}

				anchorNode = -1;
				repaint();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

	}

	private class PanelMouseMotionListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			Point clickedPoint = e.getPoint();

			// Dragging entities
			if (SwingUtilities.isLeftMouseButton(e)) {
				for (Map.Entry<Integer, Point> entry : locations.entrySet()) {
					int currentNode = entry.getKey();
					Point currentPoint = entry.getValue();

					if (Util.distance(clickedPoint, currentPoint) <= CLINIC_RADIUS) {
						selectedNode = currentNode;

						repaint();
						return;
					}
				}

				if (selectedNode != -1) {
					locations.put(selectedNode, clickedPoint);
				}
			}

			
			// Creating Connections
			if (SwingUtilities.isRightMouseButton(e)) {
				currentDragPoint = e.getPoint();

				for (Map.Entry<Integer, Point> entry : locations.entrySet()) {
					int currentNode = entry.getKey();
					Point currentPoint = entry.getValue();

					if ((Util.distance(clickedPoint, currentPoint) <= CLINIC_RADIUS) && (anchorNode == -1)) {
						anchorNode = currentNode;

						repaint();
						return;
					}
				}
			
			}

			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}

	}
}