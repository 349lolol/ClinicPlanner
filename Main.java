import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) throws IOException {
		String filename = JOptionPane.showInputDialog("Enter input file (with extension): ");

		// Read from file
		BufferedReader input = new BufferedReader(new FileReader(filename));

		List<String> lines = new ArrayList<>();

		String currentLine = input.readLine();
		
		while (currentLine != null) {
			lines.add(currentLine);
			currentLine = input.readLine();
		}
		
		input.close();
		
		// Process file
		ClinicPlacer clinicPlacer = new ClinicPlacer();

		for (String line : lines) {
			// Construct nodes
			String[] splitLine = line.split(":");
			String currentNode = splitLine[0];

			int node;

			// Mark as clinics 
			if (currentNode.contains("C")) {
				String nodeNumber = currentNode.split(" ")[0];
				node = Integer.parseInt(nodeNumber.trim());
				clinicPlacer.addClinic(node);
			} else {
				node = Integer.parseInt(currentNode.trim());
			}


			if (!clinicPlacer.containsCity(node)) {
				clinicPlacer.addCity(node);
			}

			if (splitLine.length != 2) {
				continue;
			}

			String[] neighbors = splitLine[1].trim().split(",");

			// Construct edges
			for (String neighborAsString : neighbors) {
				int neighbor = Integer.parseInt(neighborAsString.trim());

				if (!clinicPlacer.containsCity(neighbor)) {
					clinicPlacer.addCity(neighbor);
				}

				clinicPlacer.createConnection(node, neighbor);

			}
		}
		
		// Visualize graph
		ClinicVisualizer clinicVisualizer = new ClinicVisualizer(clinicPlacer);

		clinicVisualizer.run();
    }
}