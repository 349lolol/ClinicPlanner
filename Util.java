import java.awt.Point;

public final class Util {
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public static double distance(Point p1, Point p2) {
		return distance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
}
