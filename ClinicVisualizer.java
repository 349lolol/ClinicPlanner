import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;


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

	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;

	private final JFrame frame;

	public ClinicVisualizer(ClinicPlacer clinicPlacer) {
		this.frame = new JFrame("Clinic Placement Visualizer");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.frame.setLayout(new BorderLayout());

		this.frame.add(new VisualizerPanel(clinicPlacer), BorderLayout.CENTER);
	}

	public void run() {
		this.frame.setVisible(true);
	}
}