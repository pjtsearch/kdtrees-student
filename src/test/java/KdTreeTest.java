import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTreeTest {
  private KdTree tree;

  @Before
  public void setUp() throws Exception {
	  tree = new KdTree();
  }

  @Test
  public void testIsEmpty() {
    assertTrue(tree.isEmpty());
  }

  @Test
  public void testSize() {
    assertEquals(0, tree.size());
  }

  @Test
  public void testInsert() {
    tree.insert(new Point2D(0, 0));
    assertEquals(1, tree.size());
    tree.insert(new Point2D(1, 0));
    assertEquals(2, tree.size());
    tree.insert(new Point2D(-1, 0));
    assertEquals(3, tree.size());
    tree.insert(new Point2D(-2, -1));
    assertEquals(4, tree.size());
    tree.insert(new Point2D(-2, 2));
    assertEquals(5, tree.size());
    tree.insert(new Point2D(2, -1));
    assertEquals(6, tree.size());
    tree.insert(new Point2D(2, 2));
    assertEquals(7, tree.size());

    System.out.println(tree.toString());
  }

  @Test
  public void testContains() {
    tree.insert(new Point2D(0, 0));
    tree.insert(new Point2D(1, 0));
    tree.insert(new Point2D(-1, 0));
    assertTrue(tree.contains(new Point2D(-1, 0)));
    assertTrue(tree.contains(new Point2D(0, 0)));
    assertTrue(tree.contains(new Point2D(1, 0)));
    assertFalse(tree.contains(new Point2D(-2, 0)));
  }

  @Test
  public void testRange() {
	RectHV rect = new RectHV(0, 0, 0.5, 0.5);
    ArrayList<Point2D> pointsInside = new ArrayList<>();
    pointsInside.add(new Point2D(0, 0));
    pointsInside.add(new Point2D(0, 0.1));
    pointsInside.add(new Point2D(0.5, 0.5));
    pointsInside.add(new Point2D(0, 0.5));
    ArrayList<Point2D> pointsOutside = new ArrayList<>();
    pointsOutside.add(new Point2D(0.9, 0));
    pointsOutside.add(new Point2D(0, 0.9));
    pointsOutside.add(new Point2D(0.8, 0.8));
    
    for (Point2D p : pointsInside) tree.insert(p);
    for (Point2D p : pointsOutside) tree.insert(p);
        
    for (Point2D p : tree.range(rect)) {
    	assertTrue(pointsInside.remove(p));
    	assertFalse(pointsOutside.remove(p));
    }
    assertEquals(0, pointsInside.size());    
  }

  @Test
  public void testNearest() {
	Point2D expected = new Point2D(0, 1);
    tree.insert(expected);
    tree.insert(new Point2D(2, 2));
    tree.insert(new Point2D(0, 5));

    assertEquals(expected, tree.nearest(new Point2D(0, 0)));
  }

}
