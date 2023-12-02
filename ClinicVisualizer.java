import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;


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