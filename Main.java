import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        
        /*
         * Read in each node, add it to map - later
         * read again to find neighbours - later
         * 
         * adjacency list - done
         * set of covered
         * set of locations
         * map of nodes to their count
        */

		// Get all lines
		List<String> lines = new ArrayList<>();

		BufferedReader input = new BufferedReader(new FileReader("input.txt"));

		String currentLine = input.readLine();

		while (currentLine != null) {
			lines.add(currentLine);
			currentLine = input.readLine();
		}

		input.close();

		Map<Integer, Set<Integer>> city = new HashMap<>();
		Set<Integer> clinics = new HashSet<>();

		// Construct nodes first
		for (String line : lines) {
			String[] splitLine = line.split(":");
			String currentNode = splitLine[0];

			int node;

			if (currentNode.contains("C")) {
				String nodeNumber = currentNode.split(" ")[0];
				node = Integer.parseInt(nodeNumber.trim());
				clinics.add(node);
			} else {
				node = Integer.parseInt(currentNode.trim());
			}


			if (!city.containsKey(node)) {
				city.put(node, new HashSet<>());
			}

			if (splitLine.length != 2) {
				continue;
			}

			String[] neighbors = splitLine[1].trim().split(",");

			for (String neighborAsString : neighbors) {
				int neighbor = Integer.parseInt(neighborAsString.trim());

				city.get(node).add(neighbor);

				if (!city.containsKey(neighbor)) {
					city.put(neighbor, new HashSet<>());
				}
			}
		}
		
		ClinicPlacer clinicPlacer = new ClinicPlacer(city, clinics);
		ClinicVisualizer clinicVisualizer = new ClinicVisualizer(clinicPlacer);

		clinicVisualizer.run();
    }
}