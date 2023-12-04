import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

/** 
 * Visualizes the city and the clinics on a GUI
 * @author Harry Xu
 * @version 1.0 - November 28th 2023
 */
public class ClinicVisualizer {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;

	private final ClinicPlacer clinicPlacer;
	private final JFrame frame;

	public ClinicVisualizer(ClinicPlacer clinicPlacer) {
		this.clinicPlacer = clinicPlacer;

		this.frame = new JFrame("Clinic Placement Visualizer");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set the size of the "inner frame" to the specified size
		this.frame.getContentPane().setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.frame.pack();

		this.frame.setLayout(new BorderLayout());

		this.frame.add(new VisualizerPanel(this.clinicPlacer), BorderLayout.CENTER);
	}

	public void run() {
		this.frame.setVisible(true);
	}

	
	private class WindowCloseListener implements WindowListener {
		@Override
		public void windowOpened(WindowEvent e) { }

		@Override
		public void windowClosing(WindowEvent e) { }

		@Override
		public void windowClosed(WindowEvent e) {
			Map<Integer, Set<Integer>> city = clinicPlacer.getCity();
			Set<Integer> clinics = clinicPlacer.getClinicLocations();

			for (Map.Entry<Integer, Set<Integer>> entry : city.entrySet()) {
				int location = entry.getKey();
				Set<Integer> neighbors = entry.getValue();
				String line = Integer.toString(location);

				if (clinics.contains(location)) {
					line += " C";
				}

				line += ":";

				for (int neighbor : neighbors) {
					line += (neighbor + ",");
				}

				// Removing trailing comma
				if (neighbors.size() != 0) {
					line = line.substring(0, line.length() - 1);
				}
				
			}
		}

		@Override
		public void windowIconified(WindowEvent e) { }

		@Override
		public void windowDeiconified(WindowEvent e) { }

		@Override
		public void windowActivated(WindowEvent e) { }

		@Override
		public void windowDeactivated(WindowEvent e) { }
	}
}