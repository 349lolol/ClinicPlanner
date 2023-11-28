import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

		Map<Integer, Set<Integer>> graph = new HashMap<>();

		// Construct nodes first
		for (String line : lines) {
			String[] splitLine = line.split(":");

			int node = Integer.parseInt(splitLine[0].trim());

			if (!graph.containsKey(node)) {
				graph.put(node, new HashSet<>());
			}

			if (splitLine.length != 2) {
				continue;
			}

			String[] neighbors = splitLine[1].trim().split(",");

			for (String neighborAsString : neighbors) {
				int neighbor = Integer.parseInt(neighborAsString.trim());

				graph.get(node).add(neighbor);

				if (!graph.containsKey(neighbor)) {
					graph.put(neighbor, new HashSet<>());
				}
			}
		}

		System.out.println(graph);
    }
}