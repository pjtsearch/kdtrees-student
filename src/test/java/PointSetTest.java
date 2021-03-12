import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSetTest {
  PointSET set;

  @Before
  public void setUp() throws Exception {
	  set = new PointSET();
  }

  @Test
  public void testIsEmpty() {
    assertTrue(set.isEmpty());
    set.insert(new Point2D(0, 0));
    assertFalse(set.isEmpty());
  }

  @Test
  public void testSize() {
    assertEquals(0, set.size());
    Point2D newPoint = new Point2D(0, 0);
    set.insert(newPoint);
    assertEquals(1, set.size());
    set.insert(newPoint);
    assertEquals(1, set.size());
    set.insert(new Point2D(0, 1));
    assertEquals(2, set.size());
  }

  @Test
  public void testInsert() {
    Point2D newPoint = new Point2D(0, 0);
    set.insert(newPoint);
    assertEquals(1, set.size());
  }

  @Test
  public void testContains() {
    Point2D newPoint = new Point2D(0, 0);
    assertFalse(set.contains(newPoint));
    set.insert(newPoint);
    assertTrue(set.contains(newPoint));
  }

  @Test
  public void testRange() {
    RectHV rect = new RectHV(0, 0, 10, 10);
    ArrayList<Point2D> pointsInside = new ArrayList<>();
    pointsInside.add(new Point2D(0, 0));
    pointsInside.add(new Point2D(0, 10));
    pointsInside.add(new Point2D(5, 5));
    ArrayList<Point2D> pointsOutside = new ArrayList<>();
    pointsOutside.add(new Point2D(11, 0));
    pointsOutside.add(new Point2D(0, 11));
    pointsOutside.add(new Point2D(11, 11));
    
    for (Point2D p : pointsInside) set.insert(p);
    for (Point2D p : pointsOutside) set.insert(p);
        
    for (Point2D p : set.range(rect)) {
    	assertTrue(pointsInside.remove(p));
    	assertFalse(pointsOutside.remove(p));
    }
    assertEquals(0, pointsInside.size());    
  }

  @Test
  public void testNearest() {
	Point2D expected = new Point2D(0, 1);
    set.insert(expected);
    set.insert(new Point2D(2, 2));
    set.insert(new Point2D(0, 5));

    assertEquals(expected, set.nearest(new Point2D(0, 0)));
  }

}
