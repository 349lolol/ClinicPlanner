import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

/** 
 * Visualizes the city and the clinics on a GUI
 * @author Harry Xu
 * @version 1.0 - November 28th 2023
 */
public class ClinicVisualizer {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;

	private final JFrame frame;

	public ClinicVisualizer(ClinicPlacer clinicPlacer) {
		this.frame = new JFrame("Clinic Placement Visualizer");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set the size of the "inner frame" to the specified size
		this.frame.getContentPane().setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.frame.pack();

		this.frame.setLayout(new BorderLayout());

		this.frame.add(new VisualizerPanel(clinicPlacer), BorderLayout.CENTER);
	}

	public void run() {
		this.frame.setVisible(true);
	}
}