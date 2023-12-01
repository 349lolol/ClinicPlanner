import java.awt.Point;

public final class Util {
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public static double distance(Point p1, Point p2) {
		return distance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public static double distanceToLine(Point linePoint1, Point linePoint2, Point p) {
		int minX = Math.min((int) linePoint1.getX(), (int) linePoint2.getX());
		int minY = Math.min((int) linePoint1.getY(), (int) linePoint2.getY());
		int maxX = Math.max((int) linePoint1.getX(), (int) linePoint2.getX());
		int maxY = Math.max((int) linePoint1.getY(), (int) linePoint2.getY());

		int x = (int) p.getX();
		int y = (int) p.getY();

		// Not within box bound by the line
		if ((x < minX) || (y < minY) || (x > maxX) || (y > maxY)) {
			return Double.MAX_VALUE;
		}

		// Vertical Line
		if (minX == maxX) {
			return distance(p, new Point(minX, (int) p.getY()));
		}

		// Horizontal line
		if (minY == maxY) {
			return distance(p, new Point((int) p.getX(), minY));
		}

		double slope = (linePoint1.getY() - linePoint2.getY()) / (linePoint1.getX() - linePoint2.getX());
		double yInt = linePoint1.getY() - (slope * linePoint1.getX());

		double perpendicularSlope = -(1 / slope);
		double yIntPerpendicular = y - (perpendicularSlope * x);

		double xCoord = (yInt - yIntPerpendicular) / (perpendicularSlope - slope);
		double yCoord = (slope * xCoord) + yInt;

		return distance(xCoord, yCoord, p.getX(), p.getY());
	}
}
