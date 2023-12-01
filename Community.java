import java.util.HashSet;
import java.util.Set;
import java.awt.Point;

public class Community {
    private final int id;

    private Point coordinates;

    //Node constructor
    public Community(int id) {
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

    public Point getPoint(){
        return coordinates;
    }

    public void setPoint(Point coordinates){
        this.coordinates = coordinates;
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
