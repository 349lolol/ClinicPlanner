//deprecated my ass

import java.util.HashSet;

public class Node { 
    private final int id;
    private final HashSet<Node> neighbours;

    //Node constructor
    public Node(int id) {
        this.id = id;
        neighbours = new HashSet<Node>();
    }

    //get neighbour list
    public HashSet<Node> getNeighbours() {
        return this.neighbours;
    }

    //get id
    public int getId() {
        return this.id;
    }

    //changes hash function
    @Override
    public int hashCode() {
        return this.getId();
    }

    //adds one connection
    public void addConnection(Node other) {
        this.neighbours.add(other);
    }

    //removes one connection from neighbour list
    public void deleteConnection(Node other) {
        this.neighbours.remove(other);
    }
    
    //removes all connections to this node, and then kills it lol
    public void deleteNode(Node other){
        for(Node neighbour : this.neighbours) {
            neighbour.deleteConnection(this);
        }
    }
}   