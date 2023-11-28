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

    public int findClinicCount(){
        return clinicLocations.size();
    }

	public void run() {
        Set<Integer> uncoveredNeighbors = computeUncoveredLocations();
        while (uncoveredNeighbors.size() > 0) {
            int newClinicId = findPriorityNode(uncoveredNeighbors);

            if (!addClinic(newClinicId)) {
                System.out.println("already have clinic in system");
            }

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

			if(count > nodeWeight){
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
        else {
            System.out.println("Uh oh - createConnection"); //TODO: remove it
        }
    }

    //removes one connection from neighbor list
    public void deleteConnection(int node1, int node2) {
        if ((this.city.containsKey(node1)) && (this.city.containsKey(node2))) {
            this.city.get(node1).remove(node2);
            this.city.get(node2).remove(node1);
        } else {
            System.out.println("Uh oh - deleteConnection"); //TODO: remove it
        }
    }
}
