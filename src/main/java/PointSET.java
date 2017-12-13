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

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;


public class PointSET {

	// construct an empty set of points
	public PointSET() {

	}
	// is the set empty? 
	public boolean isEmpty() {
		return false;
	}
	// number of points in the set 
	public int size() {
		return 0;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		
	}

	// does the set contain point p? 
	public boolean contains(Point2D p) {
		return false;
	}

	// draw all points to standard draw 
	public void draw() {
		
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		return null;
	}

	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		return null;
	}

	// unit testing of the methods (optional) 
	public static void main(String[] args) {

	}
}


