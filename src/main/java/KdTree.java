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
	
  private enum NodeVariant {
	  X,
	  Y
  }
	
  private class Node<T> {
	  private NodeVariant variant;
	  private T data;
	  private Node<T> lesserChild;
	  private Node<T> greaterChild;

	  public Node(NodeVariant variant, T data, Node<T> lesserChild, Node<T> greaterChild) {
		this.variant = variant;
		this.data = data;
		this.lesserChild = lesserChild;
		this.greaterChild = greaterChild;
	  }

	  public Node(NodeVariant variant, T data) {
		this.variant = variant;
		this.data = data;
	  }

	  public Node(NodeVariant variant) {
		this.variant = variant;
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
  }
  
  private Node<Point2D> root;
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
  
  private void findAndInsert(Point2D inserted, Node<Point2D> currentParent) {
	  if (isEmpty()) {
		  root = new Node<Point2D>(NodeVariant.X, inserted);
		  return;
	  }
	  if (currentParent.variant == NodeVariant.X) {
		if (inserted.x() < currentParent.data.x()) {
			if (currentParent.lesserChild == null) currentParent.lesserChild = new Node<Point2D>(NodeVariant.Y, inserted);
			else findAndInsert(inserted, currentParent.lesserChild);
		} else {
			if (currentParent.greaterChild == null) currentParent.greaterChild = new Node<Point2D>(NodeVariant.Y, inserted);
			else findAndInsert(inserted, currentParent.greaterChild);
		}
	  } else if (currentParent.variant == NodeVariant.Y) {
		if (inserted.y() < currentParent.data.y()) {
			if (currentParent.lesserChild == null) currentParent.lesserChild = new Node<Point2D>(NodeVariant.X, inserted);
			else findAndInsert(inserted, currentParent.lesserChild);
		} else {
			if (currentParent.greaterChild == null) currentParent.greaterChild = new Node<Point2D>(NodeVariant.X, inserted);
			else findAndInsert(inserted, currentParent.greaterChild);
		}
	  }
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
	  if (contains(p)) return;
	  findAndInsert(p, root);
	  size++;
  }

  private boolean contains(Point2D finding, Node<Point2D> currentParent) {
	  if (currentParent == null) return false;
	  if (currentParent.data.equals(finding)) return true;
	  if (currentParent.variant == NodeVariant.X) {
		if (finding.x() < currentParent.data.x()) {
			return contains(finding, currentParent.lesserChild);
		} else {
			return contains(finding, currentParent.greaterChild);
		}
	  } else if (currentParent.variant == NodeVariant.Y) {
		if (finding.y() < currentParent.data.y()) {
			return contains(finding, currentParent.lesserChild);
		} else {
			return contains(finding, currentParent.greaterChild);
		}
	  }
	  return false;
  }
  
  // does the set contain point p? 
  public boolean contains(Point2D p) {
    return contains(p, root);
  }

  private void draw(Node<Point2D> currentNode, RectHV currentRect) {
	if (currentNode == null) return;
	StdDraw.setPenColor(StdDraw.BLACK);
	StdDraw.setPenRadius(0.03);
	StdDraw.point(currentNode.data.x(), currentNode.data.y());
	StdDraw.setPenRadius(0.01);

	RectHV greaterRect = null;
	RectHV lesserRect = null;
	if (currentNode.variant == NodeVariant.X) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(currentNode.data.x(), currentRect.ymin(), currentNode.data.x(), currentRect.ymax());
			greaterRect = new RectHV(currentNode.data.x(), currentRect.ymin(), currentRect.xmax(), currentRect.ymax());
			lesserRect = new RectHV(currentRect.xmin(), currentRect.ymin(), currentNode.data.x(), currentRect.ymax());
	} else if (currentNode.variant == NodeVariant.Y) {
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.line(currentRect.xmin(), currentNode.data.y(), currentRect.xmax(), currentNode.data.y());
		greaterRect = new RectHV(currentRect.xmin(), currentNode.data.y(), currentRect.xmax(), currentRect.ymax());
		lesserRect = new RectHV(currentRect.xmin(), currentRect.ymin(), currentRect.xmax(), currentNode.data.y());
	}
	draw(currentNode.greaterChild, greaterRect);
	draw(currentNode.lesserChild, lesserRect);
  }
  // draw all points to standard draw 
  public void draw() {
	draw(root, new RectHV(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
  }
  
  private Collection<Point2D> range(RectHV rect, Node<Point2D> currentNode, RectHV currentRect) {
	ArrayList<Point2D> result = new ArrayList<Point2D>();
	if (currentNode == null) return new ArrayList<Point2D>();
	if (rect.contains(currentNode.data)) result.add(currentNode.data);
	
//	System.out.println(currentRect);
//	System.out.println(currentNode.data);

	if (currentNode.variant == NodeVariant.X) {
		RectHV greaterRect = new RectHV(currentNode.data.x(), currentRect.ymin(), currentRect.xmax(), currentRect.ymax());
		if (rect.intersects(greaterRect)) {
			result.addAll(range(rect, currentNode.greaterChild, greaterRect));
		}
		RectHV lesserRect = new RectHV(currentRect.xmin(), currentRect.ymin(), currentNode.data.x(), currentRect.ymax());
		if (rect.intersects(lesserRect)) {
			result.addAll(range(rect, currentNode.lesserChild, lesserRect));
		}
	} else if (currentNode.variant == NodeVariant.Y) {
  		RectHV greaterRect = new RectHV(currentRect.xmin(), currentNode.data.y(), currentRect.xmax(), currentRect.ymax());
  		if (rect.intersects(greaterRect)) {
	  		result.addAll(range(rect, currentNode.greaterChild, greaterRect));
  		}
  		RectHV lesserRect = new RectHV(currentRect.xmin(), currentRect.ymin(), currentRect.xmax(), currentNode.data.y());
  		if (rect.intersects(lesserRect)) {
	  		result.addAll(range(rect, currentNode.lesserChild, lesserRect));
  		}
    }
    return result;
  }

  // all points that are inside the rectangle
  public Iterable<Point2D> range(RectHV rect) {
	if (rect == null) throw new IllegalArgumentException();
    return range(rect, root, new RectHV(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
  }

  private Point2D nearest(Point2D to, Point2D currentClosest, Node<Point2D> currentNode, RectHV currentRect) {
	Point2D newClosest = currentClosest;
	if (currentNode == null) return newClosest;
	// System.out.println("----------");
	// System.out.println(currentNode.data);
	// System.out.println(currentNode.data.distanceTo(to));
	// System.out.println(newClosest);
	// System.out.println(newClosest.distanceTo(to));
	// System.out.println("----------");
	if (currentNode.data.distanceTo(to) <= newClosest.distanceTo(to)) newClosest = currentNode.data;
	
//		System.out.println(currentRect);
//		System.out.println(currentNode.data);
	RectHV greaterRect = null;
	RectHV lesserRect = null;
	if (currentNode.variant == NodeVariant.X) {
  		greaterRect = new RectHV(currentNode.data.x(), currentRect.ymin(), currentRect.xmax(), currentRect.ymax());
  		lesserRect = new RectHV(currentRect.xmin(), currentRect.ymin(), currentNode.data.x(), currentRect.ymax());
	} else if (currentNode.variant == NodeVariant.Y) {
		greaterRect = new RectHV(currentRect.xmin(), currentNode.data.y(), currentRect.xmax(), currentRect.ymax());
		lesserRect = new RectHV(currentRect.xmin(), currentRect.ymin(), currentRect.xmax(), currentNode.data.y());
	}
	if (greaterRect.contains(to)) {
		if (greaterRect.distanceSquaredTo(newClosest) <= to.distanceTo(newClosest)) {
			newClosest = nearest(to, newClosest, currentNode.greaterChild, greaterRect);
		}
		if (lesserRect.distanceSquaredTo(newClosest) <= to.distanceTo(newClosest)) {
			newClosest = nearest(to, newClosest, currentNode.lesserChild, lesserRect);
		}
	} else {
		if (lesserRect.distanceSquaredTo(newClosest) <= to.distanceTo(newClosest)) {
			newClosest = nearest(to, newClosest, currentNode.lesserChild, lesserRect);
		}
		// System.out.println(greaterRect.distanceSquaredTo(newClosest) <= to.distanceTo(newClosest));
		if (greaterRect.distanceSquaredTo(newClosest) <= to.distanceTo(newClosest)) {
			newClosest = nearest(to, newClosest, currentNode.greaterChild, greaterRect);
		}
	}
	return newClosest;
  }
  
  // a nearest neighbor in the set to point p; null if the set is empty 
  public Point2D nearest(Point2D p) {
	if (p == null) throw new IllegalArgumentException();
	if (isEmpty()) return null;
    return nearest(p,
    		new Point2D(Integer.MAX_VALUE, Integer.MAX_VALUE),
    		root,
    		new RectHV(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
		);
  }

  // unit testing of the methods (optional) 
  public static void main(String[] args) {
	var tree = new KdTree();
	tree.insert(new Point2D(0.5, 0.5));
    tree.insert(new Point2D(0.6, 0.3));
    tree.insert(new Point2D(0.8, 0.9));
    tree.insert(new Point2D(0.1, 0.04));
	tree.draw();
  }
  
  public String toString() {
	  return root.toString("");
  }
}


