
/******************************************************************************
 *  Name:    Peter
 *  NetID:   pjtsearch
 *  Precept: P05
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 *
 *  Compilation:  javac-algs4 KdTree.java
 *  Execution:    java-algs4 KdTree
 *  Dependencies: Point2D.java RectHV.java
 *
 *  Description: Represents a set of points in the unit square
 *  (all points have x- and y-coordinates between 0 and 1)
 *  using a 2d-tree to support efficient range search
 *  (find all of the points contained in a query rectangle)
 *  and nearest-neighbor search (find a closest point to a query point).
 ******************************************************************************/

import java.util.ArrayList;
import java.util.Collection;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A 2D tree representing a set of points.
 */
public class KdTree {

    /** The largest possible rectangle. */
    private final RectHV maxRect = new RectHV(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE,
            Integer.MAX_VALUE);

    /** The different possible {@link Node} variants. */
    private enum NodeVariant {
        /** A node that splits the x axis. */
        X,
        /** A node that splits the y axis. */
        Y
    }

    /** A node that splits the 2D tree. */
    private class Node implements Comparable<Point2D> {
        /** The variant of the node. */
        private NodeVariant variant;
        /** The {@link Point2D} that the node represents. */
        private Point2D data;
        /** The lesser child of the node. */
        private Node lesserChild;
        /** The greater child of the node. */
        private Node greaterChild;
        /** The rectangle where the children of this node can be. */
        private RectHV rect;

        /**
         * Constructs a new instance of the Node class.
         * @param variant The variant of the node
         * @param data The {@link Point2D} that the node represents
         * @param lesserChild The lesser child of the node
         * @param greaterChild The greater child of the node
         * @param rect The rectangle where the children of this node can be
         */
        public Node(NodeVariant variant, Point2D data, Node lesserChild, Node greaterChild, RectHV rect) {
            this.variant = variant;
            this.data = data;
            this.lesserChild = lesserChild;
            this.greaterChild = greaterChild;
            this.rect = rect;
        }

        /**
         * Constructs a new instance of the Node class.
         * @param variant The variant of the node
         * @param data The {@link Point2D} that the node represents
         * @param rect The rectangle where the children of this node can be
         */
        public Node(NodeVariant variant, Point2D data, RectHV rect) {
            this.variant = variant;
            this.data = data;
            this.rect = rect;
        }

        /**
         * Constructs a new instance of the Node class.
         * @param variant The variant of the node
         * @param rect The rectangle where the children of this node can be
         */
        public Node(NodeVariant variant, RectHV rect) {
            this.variant = variant;
            this.rect = rect;
        }

        /**
         * Generates a string representation of this node.
         * @param indent A string to prepend to the lines of the output string.
         * @return A string representation of this node
         */
        public String toString(String indent) {
            StringBuilder builder = new StringBuilder();
            builder.append(indent + data);
            if (lesserChild != null || greaterChild != null) {
                builder.append(" {\n");
                if (lesserChild != null)
                    builder.append(lesserChild.toString(indent + "    "));
                if (greaterChild != null)
                    builder.append(greaterChild.toString(indent + "    "));
                builder.append(indent + "}");
            }
            builder.append("\n");
            return builder.toString();
        }
        
        @Override
        public String toString() {
        	return toString("");
        }

        @Override
        public int compareTo(Point2D that) {
            if (variant == NodeVariant.X) {
                return Double.compare(data.x(), that.x());
            } else {
                return Double.compare(data.y(), that.y());
            }
        }
    }

    /** The root node of the tree. */
    private Node root;
    /** The amount of nodes in the tree. */
    private int size;

    /** The pen radius for a point. */
    private final double penPointRadius = 0.03;
        /** The pen radius for a line. */
    private final double penLineRadius = 0.01;


    /** Constructs a new instance of the KdTree class. */
    public KdTree() {
        root = null;
    }

    /**
     * Checks whether the tree is empty.
     * @return Whether the tree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Gets the amount of items in the tree.
     * @return The amount of items in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Finds where to insert a point and inserts it.
     * @param inserted The point to be inserted
     * @param currentParent The current node that is being checked for a insertion point
     */
    private void insert(Point2D inserted, Node currentParent) {
        if (isEmpty()) {
            root = new Node(NodeVariant.X, inserted, maxRect);
            return;
        }
        // Inserted is less than parent
        if (currentParent.compareTo(inserted) > 0) {
        	if (currentParent.lesserChild == null)
        		if (currentParent.variant == NodeVariant.X)
        			currentParent.lesserChild = new Node(NodeVariant.Y, inserted, new RectHV(currentParent.rect.xmin(),
                            currentParent.rect.ymin(), currentParent.data.x(), currentParent.rect.ymax()));
        		else
        			currentParent.lesserChild = new Node(NodeVariant.X, inserted, new RectHV(currentParent.rect.xmin(),
                            currentParent.rect.ymin(), currentParent.rect.xmax(), currentParent.data.y()));
        	else
        	    insert(inserted, currentParent.lesserChild);
        // Inserted is greater than or equal to parent
        } else {
        	if (currentParent.greaterChild == null)
        		if (currentParent.variant == NodeVariant.X)
        			currentParent.greaterChild = new Node(NodeVariant.Y, inserted, new RectHV(currentParent.data.x(),
                            currentParent.rect.ymin(), currentParent.rect.xmax(), currentParent.rect.ymax()));
        		else
        			currentParent.greaterChild = new Node(NodeVariant.X, inserted, new RectHV(currentParent.rect.xmin(),
                            currentParent.data.y(), currentParent.rect.xmax(), currentParent.rect.ymax()));
	    	else
	    	    insert(inserted, currentParent.greaterChild);
        }
    }

    /**
     * Adds a point to the tree if it is not already in the tree.
     * @param p The point to be inserted.
     */
    public void insert(Point2D p) {
        if (contains(p))
            return;
        if (p == null)
            throw new IllegalArgumentException();
        insert(p, root);
        size++;
    }

    /**
     * Checks whether a node contains a point under it.
     * @param finding The point to be found
     * @param currentParent The current node that is being searched for the point
     * @return Whether the node contains a point under it
     */
    private boolean contains(Point2D finding, Node currentParent) {
        if (finding == null)
            throw new IllegalArgumentException();
        if (currentParent == null)
            return false;
        if (currentParent.data.equals(finding))
            return true;
        if (currentParent.compareTo(finding) > 0) {
            return contains(finding, currentParent.lesserChild);
        } else {
            return contains(finding, currentParent.greaterChild);
        }
    }

    /**
     * Checks whether the tree contains a point.
     * @param p The point to be checked for.
     * @return Whether the tree contains a point
     */
    public boolean contains(Point2D p) {
        return contains(p, root);
    }

    /**
     * Draws a node using StdDraw.
     * @param currentNode The node to be drawn
     */
    private void draw(Node currentNode) {
        if (currentNode == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(penPointRadius);
        // Draw current node
        StdDraw.point(currentNode.data.x(), currentNode.data.y());
        StdDraw.setPenRadius(penLineRadius);

        if (currentNode.variant == NodeVariant.X) {
        	// Draw line on x coordinate inside the node's rectangle
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(currentNode.data.x(), currentNode.rect.ymin(), currentNode.data.x(), currentNode.rect.ymax());
        } else if (currentNode.variant == NodeVariant.Y) {
        	// Draw line on y coordinate inside the node's rectangle
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(currentNode.rect.xmin(), currentNode.data.y(), currentNode.rect.xmax(), currentNode.data.y());
        }
        // Draw children
        draw(currentNode.greaterChild);
        draw(currentNode.lesserChild);
    }

    /** Draws the tree with StdDraw. */
    public void draw() {
        draw(root);
    }

    /**
     * Finds all points under a node inside of a rectangle.
     * @param rect The rectangle to be searched
     * @param currentNode The node to search under
     * @return All points under a node inside of a rectangle.
     */
    private Collection<Point2D> range(RectHV rect, Node currentNode) {
        ArrayList<Point2D> result = new ArrayList<Point2D>();
        if (currentNode == null)
            return new ArrayList<Point2D>();
        if (rect.contains(currentNode.data))
            result.add(currentNode.data);

        // If rectangle intersects greater child, at points in range in greater child
        if (currentNode.greaterChild != null && rect.intersects(currentNode.greaterChild.rect)) {
            result.addAll(range(rect, currentNode.greaterChild));
        }
        // If rectangle intersects lesser child, at points in range in lesser child
        if (currentNode.lesserChild != null && rect.intersects(currentNode.lesserChild.rect)) {
            result.addAll(range(rect, currentNode.lesserChild));
        }
        return result;
    }

    /**
     * Finds all points in the tree contained in a rectangle.
     * @param rect The rectangle to be searched.
     * @return All points in the tree contained in a rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        return range(rect, root);
    }

    /**
     * Finds the nearest point under a node to another point.
     * @param to The point to be searched for
     * @param currentClosest The current closest point
     * @param currentNode The node to be searched under
     * @return The nearest point under the node to another point
     */
    private Point2D nearest(Point2D to, Point2D currentClosest, Node currentNode) {
        Point2D newClosest = currentClosest;
        if (currentNode == null)
            return newClosest;

        // If closer than current closest, then make it the new closest
        if (currentNode.data.distanceTo(to) <= newClosest.distanceTo(to))
            newClosest = currentNode.data;

        if (currentNode.compareTo(to) < 0) {
        	// If greater child rectangle is closer, then get nearest in rectangle
            if (currentNode.greaterChild != null
                    && currentNode.greaterChild.rect.distanceTo(to) <= to.distanceTo(newClosest)) {
                newClosest = nearest(to, newClosest, currentNode.greaterChild);
            }
        	// If lesser child rectangle is closer, then get nearest in rectangle
            if (currentNode.lesserChild != null
                    && currentNode.lesserChild.rect.distanceTo(to) <= to.distanceTo(newClosest)) {
                newClosest = nearest(to, newClosest, currentNode.lesserChild);
            }
        } else {
        	// If greater child rectangle is closer, then get nearest in rectangle
            if (currentNode.lesserChild != null
                    && currentNode.lesserChild.rect.distanceTo(to) <= to.distanceTo(newClosest)) {
                newClosest = nearest(to, newClosest, currentNode.lesserChild);
            }

        	// If lesser child rectangle is closer, then get nearest in rectangle
            if (currentNode.greaterChild != null
                    && currentNode.greaterChild.rect.distanceTo(to) <= to.distanceTo(newClosest)) {
                newClosest = nearest(to, newClosest, currentNode.greaterChild);
            }
        }
        return newClosest;
    }

    /**
     * Finds the nearest point in the tree to another point.
     * @param p The point to search for
     * @return The nearest point in the tree to another point
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        return nearest(p, root.data, root);
    }

    /**
     * The CLI entry point.
     * @param args The CLI args
     */
    public static void main(String[] args) {

    }

    /**
     * Gets a string representation of the tree.
     * @return A string representation of the tree
     */
    public String toString() {
        return root.toString();
    }
}
