import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
	private final String outputFile;
	private final JFrame frame;

	public ClinicVisualizer(ClinicPlacer clinicPlacer, String outputFile) {
		this.clinicPlacer = clinicPlacer;
		this.outputFile = outputFile;

		this.frame = new JFrame("Clinic Placement Visualizer");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set the size of the "inner frame" to the specified size
		this.frame.getContentPane().setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.frame.pack();

		this.frame.addWindowListener(new GUIWindowListener());

		this.frame.setLayout(new BorderLayout());

		this.frame.add(new VisualizerPanel(this.clinicPlacer), BorderLayout.CENTER);
	}

	public void run() {
		this.frame.setVisible(true);
	}

	private class GUIWindowListener implements WindowListener {
		@Override
		public void windowOpened(WindowEvent e) { }

		@Override
		public void windowClosing(WindowEvent e) { 
			try {
				BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));
				output.write(clinicPlacer.toString());
				output.close();
			} catch (IOException exception) {
				System.out.println("Error occurred whilst saving to file \"" + outputFile + "\".");
			}
		}

		@Override
		public void windowClosed(WindowEvent e) { }

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