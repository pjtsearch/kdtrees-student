
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

/** A set of points in a red-black BST. */
public class PointSET {
    /** The set of points. */
    private SET<Point2D> set;

    /** Constructs a new instance of the PointSET class. */
    public PointSET() {
        set = new SET<Point2D>();
    }

    /**
     * Checks whether the set is empty.
     * @return Whether the set is empty
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * Gets the amount of items in the set.
     * @return The amount of items in the set
     */
    public int size() {
        return set.size();
    }

/**
     * Adds a point to the set if it is not already in the set.
     * @param p The point to be inserted.
     */
    public void insert(Point2D p) {
        if (!set.contains(p))
            set.add(p);
    }

    /**
     * Checks whether the set contains a point.
     * @param p The point to be checked for.
     * @return Whether the set contains a point
     */
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    /** Draws the set with StdDraw. */
    public void draw() {
        for (Point2D point : set) {
            StdDraw.point(point.x(), point.y());
        }
    }

    /**
     * Finds all points in the set contained in a rectangle.
     * @param rect The rectangle to be searched.
     * @return All points in the set contained in a rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> result = new ArrayList<>();
        for (Point2D point : set) {
            if (rect.contains(point))
                result.add(point);
        }
        return result;
    }

    /**
     * Finds the nearest point in the set to another point.
     * @param p The point to search for
     * @return The nearest point in the set to another point
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        // Convert to List
        List<Point2D> points = new ArrayList<>();
        for (Point2D point : set) {
            points.add(point);
        }
        // Sort points by distance to p
        points.sort(p.distanceToOrder());
        return points.get(0);
    }

/**
     * The CLI entry point.
     * @param args The CLI args
     */
    public static void main(String[] args) {

    }
}
