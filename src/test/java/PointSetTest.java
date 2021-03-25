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
        for (int i = 1; i < 100; i++) {
            set.insert(new Point2D(i, i));
            assertEquals(i, set.size());
        }
    }

    @Test
    public void testInsert() {
        set.insert(new Point2D(0, 0));
        assertEquals(1, set.size());
        set.insert(new Point2D(1, 0));
        assertEquals(2, set.size());
        set.insert(new Point2D(-1, 0));
        assertEquals(3, set.size());
        set.insert(new Point2D(-2, -1));
        assertEquals(4, set.size());
        set.insert(new Point2D(-2, 2));
        assertEquals(5, set.size());
        set.insert(new Point2D(2, -1));
        assertEquals(6, set.size());
        set.insert(new Point2D(2, 2));
        assertEquals(7, set.size());
        set.insert(new Point2D(2, 2));
        assertEquals(7, set.size());
        set.insert(new Point2D(2, -1));
        assertEquals(7, set.size());
    }

    @Test
    public void testInsertDuplicates() {
        for (int i = 1; i <= 500; i++) {
            set.insert(new Point2D(i, i));
            assertEquals(i, set.size());
        }
        for (int i = 1; i <= 500; i++) {
            set.insert(new Point2D(i, i));
            assertEquals(500, set.size());
        }
    }

    @Test
    public void testContains() {
        set.insert(new Point2D(0, 0));
        set.insert(new Point2D(1, 0));
        set.insert(new Point2D(-1, 0));
        assertTrue(set.contains(new Point2D(-1, 0)));
        assertTrue(set.contains(new Point2D(0, 0)));
        assertTrue(set.contains(new Point2D(1, 0)));
        assertFalse(set.contains(new Point2D(-2, 0)));
        for (int i = 1; i < 100; i++) {
            set.insert(new Point2D(i, i));
        }
        for (int i = 1; i < 100; i++) {
            assertTrue(set.contains(new Point2D(i, i)));
        }
        for (int i = 100; i < 200; i++) {
            assertFalse(set.contains(new Point2D(i, i)));
        }
    }

    @Test
    public void testRange() {
        RectHV rect = new RectHV(0, 0, 5, 5);
        ArrayList<Point2D> pointsInside = new ArrayList<>();
        for (int i = 0; i <= 5; i += 1) {
            for (int j = 0; j <= 5; j += 1) {
                pointsInside.add(new Point2D(i, j));
            }
        }
        ArrayList<Point2D> pointsOutside = new ArrayList<>();
        pointsOutside.add(new Point2D(9, 0));
        pointsOutside.add(new Point2D(0, 9));
        pointsOutside.add(new Point2D(8, 8));
        for (int i = 6; i <= 10; i += 1) {
            for (int j = 0; j <= 5; j += 1) {
                pointsOutside.add(new Point2D(i, j));
            }
        }
        for (int i = 0; i <= 5; i += 1) {
            for (int j = 6; j <= 10; j += 1) {
                pointsOutside.add(new Point2D(i, j));
            }
        }

        for (Point2D p : pointsInside)
            set.insert(p);
        for (Point2D p : pointsOutside)
            set.insert(p);

        for (Point2D p : set.range(rect)) {
            assertTrue(pointsInside.remove(p));
            assertFalse(pointsOutside.remove(p));
        }
        assertEquals(0, pointsInside.size());
    }

    @Test
    public void testNearest() {
        set.insert(new Point2D(0.7, 0.2));
        set.insert(new Point2D(0.5, 0.4));
        set.insert(new Point2D(0.2, 0.3));
        set.insert(new Point2D(0.4, 0.7));
        set.insert(new Point2D(0.9, 0.6));

        assertEquals(new Point2D(0.9, 0.6), set.nearest(new Point2D(0.79, 0.49)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nearestNullArg() {
        set.nearest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertNullArg() {
        set.insert(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rangeNullArg() {
        set.range(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsNullArg() {
        set.contains(null);
    }

    @Test
    public void nearestEmpty() {
        assertNull(set.nearest(new Point2D(0, 0)));
    }

    @Test
    public void containsEmpty() {
        assertFalse(set.contains(new Point2D(0, 0)));
    }

}
