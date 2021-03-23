/******************************************************************************
 *  Name:    J.D. DeVaughn-Brown
 *  NetID:   jddevaug
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
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;


public class KdTree {
	
  private final RectHV maxRect = new RectHV(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	
  private enum NodeVariant {
	  X,
	  Y
  }
	
  private class Node implements Comparable<Point2D> {
	  private NodeVariant variant;
	  private Point2D data;
	  private Node lesserChild;
	  private Node greaterChild;
	  private RectHV rect;

	  public Node(NodeVariant variant, Point2D data, Node lesserChild, Node greaterChild, RectHV rect) {
		this.variant = variant;
		this.data = data;
		this.lesserChild = lesserChild;
		this.greaterChild = greaterChild;
		this.rect = rect;
	  }

	  public Node(NodeVariant variant, Point2D data, RectHV rect) {
		this.variant = variant;
		this.data = data;
		this.rect = rect;
	  }

	  public Node(NodeVariant variant, RectHV rect) {
		this.variant = variant;
		this.rect = rect;
	  }
	  
	  public String toString(String indent) {
		  StringBuilder builder = new StringBuilder();
		  builder.append(indent+data);
		  if (lesserChild != null || greaterChild != null) {
			  builder.append(" {\n");
			  if (lesserChild != null) builder.append(lesserChild.toString(indent+"    "));
			  if (greaterChild != null) builder.append(greaterChild.toString(indent+"    "));
			  builder.append(indent+"}");
		  }
		  builder.append("\n");
		  return builder.toString();
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
  
  private Node root;
  private int size;

  // construct an empty set of points
  public KdTree() {
	  root = null;
  }
  // is the set empty? 
  public boolean isEmpty() {
    return root == null;
  }
  
//  private int nodesUnder(Node<Point2D> current) {
//	  int result = 0;
//	  if (current.lesserChild != null && current.greaterChild != null) {
//		  if(current.lesserChild != null) result += nodesUnder(current.lesserChild);
//		  if(current.greaterChild != null) result += nodesUnder(current.greaterChild);
//	  } else {
//		  result = 1;
//	  }
//	  return result;
//  }
  // number of points in the set 
  public int size() {
	return size;
  }
  
  private void findAndInsert(Point2D inserted, Node currentParent) {
	  if (isEmpty()) {
		  root = new Node(NodeVariant.X, inserted, maxRect);
		  return;
	  }
	  if (currentParent.variant == NodeVariant.X) {
		if (inserted.x() < currentParent.data.x()) {
			if (currentParent.lesserChild == null)
				currentParent.lesserChild = new Node(
						NodeVariant.Y,
						inserted,
						new RectHV(currentParent.rect.xmin(), currentParent.rect.ymin(), currentParent.data.x(), currentParent.rect.ymax())
					);
			else findAndInsert(inserted, currentParent.lesserChild);
		} else {
			if (currentParent.greaterChild == null)
				currentParent.greaterChild = new Node(
						NodeVariant.Y,
						inserted,
						new RectHV(currentParent.data.x(), currentParent.rect.ymin(), currentParent.rect.xmax(), currentParent.rect.ymax())
					);
			else findAndInsert(inserted, currentParent.greaterChild);
		}
	  } else if (currentParent.variant == NodeVariant.Y) {
		if (inserted.y() < currentParent.data.y()) {
			if (currentParent.lesserChild == null)
				currentParent.lesserChild = new Node(
						NodeVariant.X,
						inserted,
						new RectHV(currentParent.rect.xmin(), currentParent.rect.ymin(), currentParent.rect.xmax(), currentParent.data.y())
					);
			else findAndInsert(inserted, currentParent.lesserChild);
		} else {
			if (currentParent.greaterChild == null)
				currentParent.greaterChild = new Node(
						NodeVariant.X,
						inserted,
						new RectHV(currentParent.rect.xmin(), currentParent.data.y(), currentParent.rect.xmax(), currentParent.rect.ymax())
					);
			else findAndInsert(inserted, currentParent.greaterChild);
		}
	  }
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
	  if (contains(p)) return;
	  if (p == null) throw new IllegalArgumentException();
	  findAndInsert(p, root);
	  size++;
  }

  private boolean contains(Point2D finding, Node currentParent) {
	  if (finding == null) throw new IllegalArgumentException();
	  if (currentParent == null) return false;
	  if (currentParent.data.equals(finding)) return true;
	  if (currentParent.compareTo(finding) > 0) {
		return contains(finding, currentParent.lesserChild);
	  } else {
	  	return contains(finding, currentParent.greaterChild);
	  }
  }
  
  // does the set contain point p? 
  public boolean contains(Point2D p) {
    return contains(p, root);
  }

  private void draw(Node currentNode) {
	if (currentNode == null) return;
	StdDraw.setPenColor(StdDraw.BLACK);
	StdDraw.setPenRadius(0.03);
	StdDraw.point(currentNode.data.x(), currentNode.data.y());
	StdDraw.setPenRadius(0.01);

	if (currentNode.variant == NodeVariant.X) {
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.line(currentNode.data.x(), currentNode.rect.ymin(), currentNode.data.x(), currentNode.rect.ymax());
	} else if (currentNode.variant == NodeVariant.Y) {
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.line(currentNode.rect.xmin(), currentNode.data.y(), currentNode.rect.xmax(), currentNode.data.y());
	}
	draw(currentNode.greaterChild);
	draw(currentNode.lesserChild);
  }
  // draw all points to standard draw 
  public void draw() {
	draw(root);
  }
  
  private Collection<Point2D> range(RectHV rect, Node currentNode) {
	ArrayList<Point2D> result = new ArrayList<Point2D>();
	if (currentNode == null) return new ArrayList<Point2D>();
	if (rect.contains(currentNode.data)) result.add(currentNode.data);
	
//	System.out.println(currentRect);
//	System.out.println(currentNode.data);

	if (currentNode.greaterChild != null && rect.intersects(currentNode.greaterChild.rect)) {
  		result.addAll(range(rect, currentNode.greaterChild));
	}
	if (currentNode.lesserChild != null &&rect.intersects(currentNode.lesserChild.rect)) {
  		result.addAll(range(rect, currentNode.lesserChild));
	}
    return result;
  }

  // all points that are inside the rectangle
  public Iterable<Point2D> range(RectHV rect) {
	if (rect == null) throw new IllegalArgumentException();
    return range(rect, root);
  }

  private Point2D nearest(Point2D to, Point2D currentClosest, Node currentNode) {
	Point2D newClosest = currentClosest;
	if (currentNode == null) return newClosest;
	// System.out.println("----------");
	// System.out.println(currentNode.data);
	// System.out.println(currentNode.data.distanceTo(to));
	// System.out.println(newClosest);
	// System.out.println(newClosest.distanceTo(to));
	if (currentNode.data.distanceTo(to) <= newClosest.distanceTo(to)) newClosest = currentNode.data;
	
//		System.out.println(currentRect);
		// System.out.println(currentNode.data);
	if (currentNode.compareTo(to) < 0) {
		if (currentNode.greaterChild != null && currentNode.greaterChild.rect.distanceTo(to) <= to.distanceTo(newClosest)) {
			newClosest = nearest(to, newClosest, currentNode.greaterChild);
		}
		if (currentNode.lesserChild != null && currentNode.lesserChild.rect.distanceTo(to) <= to.distanceTo(newClosest)) {
			newClosest = nearest(to, newClosest, currentNode.lesserChild);
		}
	} else {
		if (currentNode.lesserChild != null && currentNode.lesserChild.rect.distanceTo(to) <= to.distanceTo(newClosest)) {
			newClosest = nearest(to, newClosest, currentNode.lesserChild);
		}
		// System.out.println(greaterRect.distanceSquaredTo(newClosest) <= to.distanceTo(newClosest));
		if (currentNode.greaterChild != null && currentNode.greaterChild.rect.distanceTo(to) <= to.distanceTo(newClosest)) {
			newClosest = nearest(to, newClosest, currentNode.greaterChild);
		}
	}
	return newClosest;
  }
  
  // a nearest neighbor in the set to point p; null if the set is empty 
  public Point2D nearest(Point2D p) {
	if (p == null) throw new IllegalArgumentException();
	if (isEmpty()) return null;
    return nearest(p,
    		root.data,
    		root
		);
  }

  // unit testing of the methods (optional) 
  public static void main(String[] args) {
	var tree = new KdTree();
	tree.insert(new Point2D(0.7,0.2));
    tree.insert(new Point2D(0.5,0.4));
    tree.insert(new Point2D(0.2,0.3));
    tree.insert(new Point2D(0.4,0.7));
    tree.insert(new Point2D(0.9,0.6));
	tree.draw();
  }
  
  public String toString() {
	  return root.toString("");
  }
}


