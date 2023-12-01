import java.util.Set;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Deque;
import java.util.ArrayDeque;

public class ObamaCare {
    private static final int MAX_RADIUS = 69;
    private static final int MIN_RADIUS = 10;
    private static final String Deque = null;

    public void traverse(Map<Integer, Set<Integer>> city) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();

        for (Map.Entry<Integer, Set<Integer>> entry : city.entrySet()) {
            queue.addFirst(entry.getKey());

            while (!queue.isEmpty()) {
                int current = queue.removeLast();
                
                if (visited.contains(current)) {
                    continue;
                }

                visited.add(current);

                for (int neighbor : entry.getValue()) {
                    if (visited.contains(neighbor)) {
                        continue;
                    }

                    queue.addFirst(neighbor);
                }



            }
        }
        

    }

    public int calculateRadius(Set<Integer> connections) {
        int radius = (int)(MIN_RADIUS * Math.sqrt(connections.size()));
        if(radius > MAX_RADIUS){
            return MAX_RADIUS;
        }
        return radius;
    }

    public Map<Integer, Integer> locateNeighbors(Point currentPosition, int searchRadius, Set<Integer> connections, Map<Integer, Point> placedNeighbors) {
        for(int id : connections){
            Point target = placedNeighbors.get(id);
            double polarAngle = 0;
            double polarDistance = 0;
            polarDistance = Math.hypot((target.getX() - currentPosition.getX()),(target.getY() - currentPosition.getY()));
            if((Math.abs(target.getX() - currentPosition.getX()) <= searchRadius) && (Math.abs(target.getY() - currentPosition.getY()) <= searchRadius)){
                //target is within search box, mark down polar coordinate
                if(target.getX() - currentPosition.getX() == 0){ //quadrantal angle
                    if((target.getY() - currentPosition.getY()) >= 0){
                        polarAngle = Math.PI/2.0;
                    }
                    else {
                        polarAngle = Math.PI * 3.0 / 2.0;
                    }
                } 
                else {
                    Math.tan((target.getY() - currentPosition.getY())/(target.getX() - currentPosition.getX()));

                    if((target.getY() - currentPosition.getY()) <= 0){
                        polarAngle = Math.PI + polarAngle;
                    }
                }
            }
        }
        return null;
    }

    
}
