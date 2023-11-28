import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class ClinicPlacer {
    private final Map<Integer, HashSet<Integer>> city;
    private final Set<Integer> clinicLocations;

    public ClinicPlacer() {
        this.city = new HashMap<>();
        this.clinicLocations = new HashSet<>();
    }

	public void run() {

	}
    
    public Set<Integer> computeCoveredLocations() {
        Set<Integer> coveredLocations = new HashSet<>();
        
        for (int location : this.clinicLocations) {
            coveredLocations.add(location);
            coveredLocations.addAll(city.get(location));
        }

        return coveredLocations;
    }

    public Set<Integer> computeUncoveredLocations(){
        Set<Integer> uncoveredNeighbors = this.city.keySet();

        uncoveredNeighbors.removeAll(this.computeCoveredLocations());

        return uncoveredNeighbors;
    }

    public Map<Integer, Integer> computeWeights(Set<Integer> uncoveredNeighbors) {
        Map<Integer, Integer> weights = new HashMap<>();

        for (int node : this.city.keySet()) {
            Set<Integer> neighbors = this.city.get(node);

			int count = 0;

			for (int neighbor : neighbors) {
				if (uncoveredNeighbors.contains(neighbor)) {
					count++;
				}
			}

			weights.put(node, count);
        }
        
		return weights;
    }

    //adds one connection
    public void createConnection(int node1, int node2) {
        if ((this.city.containsKey(node1)) && (this.city.containsKey(node2))) {
            this.city.get(node1).add(node2);
            this.city.get(node2).add(node1);
        }
        else {
            System.out.println("Uh oh - createConnection"); //TODO: remove it
        }
    }

    //removes one connection from neighbour list
    public void deleteConnection(int node1, int node2) {
        if ((this.city.containsKey(node1)) && (this.city.containsKey(node2))) {
            this.city.get(node1).remove(node2);
            this.city.get(node2).remove(node1);
        } else {
            System.out.println("Uh oh - deleteConnection"); //TODO: remove it
        }
    }
}
