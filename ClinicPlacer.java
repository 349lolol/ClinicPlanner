import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/** 
 * Represents a city graph and places clinics
 * @author Patrick Wei
 * @version 1.0 - November 27th 2023
 */
public class ClinicPlacer {
    private final Map<Integer, Set<Integer>> city;
    private final Set<Integer> clinicLocations;

    public ClinicPlacer() {
        this.city = new HashMap<>();
        this.clinicLocations = new HashSet<>();
    }

    public Set<Integer> getClinicLocations(){
        return clinicLocations;
    }

    public Map<Integer, Set<Integer>> getCity(){
        return city;
    }

    //used to calculate all neighbors that are to become a clinic
	public void run() {
		// Every lone node becomes a clinic
        for (Map.Entry<Integer, Set<Integer>> entry : this.city.entrySet()) {
            if (entry.getValue().size() == 0) {
				this.clinicLocations.add(entry.getKey());
			}
        }

		// Place clinics based on priority
        Set<Integer> uncoveredNeighbors = computeUncoveredLocations();

        while (uncoveredNeighbors.size() > 0) {
            int newClinicId = findPriorityNode(uncoveredNeighbors);

            addClinic(newClinicId);

            uncoveredNeighbors = computeUncoveredLocations();
        }
	}
    
    //finds all neighbors that are covered by a clinic
    public Set<Integer> computeCoveredLocations() {
        Set<Integer> coveredLocations = new HashSet<>();
        
        for (int location : this.clinicLocations) {
            coveredLocations.add(location);
            coveredLocations.addAll(city.get(location));
        }

        return coveredLocations;
    }

    //finds all neighbors uncovered, all neighbors minus covered neighbors
    public Set<Integer> computeUncoveredLocations(){
        Set<Integer> uncoveredNeighbors = this.city.keySet();
		Set<Integer> uncoveredNeighborsCopy = new HashSet<>(uncoveredNeighbors);

        uncoveredNeighborsCopy.removeAll(this.computeCoveredLocations());

        return uncoveredNeighborsCopy;
    }

    //finds the node that has the most uncovered neighbors
    public int findPriorityNode(Set<Integer> uncoveredNeighbors){
        int maxNode = -1;
        int nodeWeight = -1;

        for (int node : this.city.keySet()) {
            Set<Integer> neighbors = this.city.get(node);

			int count = 0;

			for (int neighbor : neighbors) {
				if (uncoveredNeighbors.contains(neighbor)) {
					count++;
				}
			}
			
			if (uncoveredNeighbors.contains(node)) {
				count++;
			}

			if (count > nodeWeight){
				nodeWeight = count;
				maxNode = node;
            }
        }
    
		return maxNode;
    }

    public boolean addClinic(int newClinicId){
        return clinicLocations.add(newClinicId);
    }

    //links two neighbors
    public void createConnection(int node1, int node2) {
        if ((this.city.containsKey(node1)) && (this.city.containsKey(node2))) {
            this.city.get(node1).add(node2);
            this.city.get(node2).add(node1);
        }
    }

	public void deleteNode(int node) {
        Set<Integer> neighbors = this.city.get(node);

		// Remove neighbor connections
		for (int neighbor : neighbors) {
			this.city.get(neighbor).remove(node);
		}

		this.city.remove(node);
		clinicLocations.remove(node);
    }

    //deletes a connection between two neighbors
    public void deleteConnection(int node1, int node2) {
        if ((this.city.containsKey(node1)) && (this.city.containsKey(node2))) {
            this.city.get(node1).remove(node2);
            this.city.get(node2).remove(node1);
        }
    }

	public void addCity(int node) {
		if (this.city.containsKey(node)) {
			throw new IllegalArgumentException("City already exists");
		}

		this.city.put(node, new HashSet<>());
	}

	public boolean containsCity(int city){
        return this.city.keySet().contains(city);
    }

	@Override
	public String toString() {
		String str = "";

		Map<Integer, Set<Integer>> city = this.getCity();
		Set<Integer> clinics = this.getClinicLocations();

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

			str += (line + "\n");
		}

		return str;
	}
}
