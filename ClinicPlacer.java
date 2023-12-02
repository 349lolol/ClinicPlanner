import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class ClinicPlacer {
    private final Map<Integer, Set<Integer>> city;
    private final Set<Integer> clinicLocations;

	// TODO: REMOVE THIS
	public ClinicPlacer(Map<Integer, Set<Integer>> city, Set<Integer> clinicLocations) {
        this.city = city;
        this.clinicLocations = clinicLocations;
    }

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

	public void run() {
		// Every lone node becomes a clin
        for (Map.Entry<Integer, Set<Integer>> entry : this.city.entrySet()) {
            if (entry.getValue().size() == 0) {
				this.clinicLocations.add(entry.getKey());
			}
        }

        Set<Integer> uncoveredNeighbors = computeUncoveredLocations();

        while (uncoveredNeighbors.size() > 0) {
            int newClinicId = findPriorityNode(uncoveredNeighbors);

            addClinic(newClinicId);

            uncoveredNeighbors = computeUncoveredLocations();
        }
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
		Set<Integer> uncoveredNeighborsCopy = new HashSet<>(uncoveredNeighbors);

        uncoveredNeighborsCopy.removeAll(this.computeCoveredLocations());

        return uncoveredNeighborsCopy;
    }
    
    public Map<Integer, Integer> computeWeights(Set<Integer> uncoveredNeighbors) { //old version
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

    //adds one connection
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

    //removes one connection from neighbor list
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
}
