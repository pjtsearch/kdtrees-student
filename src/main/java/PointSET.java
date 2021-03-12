/******************************************************************************
 *  Name:    J.D. DeVaughn-Brown
 *  NetID:   jddevaug
 *  Precept: P05
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 *  
 *  Compilation:  javac-algs4 PointSET.java
 *  Execution:    java-algs4 PointSET
 *  Dependencies: Point2D.java RectHV.java 
 * 
 *  Description: Represents a set of points in the unit square 
 *  (all points have x- and y-coordinates between 0 and 1) 
 *  using a red-black BST to support range search 
 *  (find all of the points contained in a query rectangle) 
 *  and nearest-neighbor search (find a closest point to a query point).
 ******************************************************************************/

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;


public class PointSET {
	private SET<Point2D> set;

	// construct an empty set of points
	public PointSET() {
		set = new SET<Point2D>();
	}
	// is the set empty? 
	public boolean isEmpty() {
		return set.isEmpty();
	}
	// number of points in the set 
	public int size() {
		return set.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (!set.contains(p)) set.add(p);
	}

	// does the set contain point p? 
	public boolean contains(Point2D p) {
		return set.contains(p);
	}

	// draw all points to standard draw 
	public void draw() {
		for (Point2D point : set) {
			StdDraw.point(point.x(), point.y());
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		List<Point2D> result = new ArrayList<>();
		for (Point2D point : set) {
			if (rect.contains(point)) result.add(point);
		}
		return result;
	}

	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		List<Point2D> points = new ArrayList<>();
		for (Point2D point : set) {
			points.add(point);
		}
		points.sort(p.distanceToOrder());
		return points.get(0);
	}

	// unit testing of the methods (optional) 
	public static void main(String[] args) {

	}
}


