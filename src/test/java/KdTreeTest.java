import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTreeTest {
    private KdTree tree;

    private static KdTree generateKdTree(String filename) {
        // create initial board from file
        In in = new In("kdtree-test-files/" + filename);
        KdTree tree = new KdTree();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] coords = line.split(" ");
            tree.insert(new Point2D(Double.valueOf(coords[0]), Double.valueOf(coords[1])));
        }

        // solve the puzzle
        return tree;
    }

    private static PointSET generatePointSET(String filename) {
        // create initial board from file
        In in = new In("kdtree-test-files/" + filename);
        PointSET tree = new PointSET();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] coords = line.split(" ");
            tree.insert(new Point2D(Double.valueOf(coords[0]), Double.valueOf(coords[1])));
        }

        // solve the puzzle
        return tree;
    }

    @Before
    public void setUp() throws Exception {
        tree = new KdTree();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tree.isEmpty());
        tree.insert(new Point2D(0, 0));
        assertFalse(tree.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, tree.size());
        for (int i = 1; i < 100; i++) {
            tree.insert(new Point2D(i, i));
            assertEquals(i, tree.size());
        }
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
        tree.insert(new Point2D(2, 2));
        assertEquals(7, tree.size());
        tree.insert(new Point2D(2, -1));
        assertEquals(7, tree.size());

        assertEquals("""
                (0.0, 0.0) {
                    (-1.0, 0.0) {
                        (-2.0, -1.0)
                        (-2.0, 2.0)
                    }
                    (1.0, 0.0) {
                        (2.0, -1.0)
                        (2.0, 2.0)
                    }
                }
                """, tree.toString());
    }

    @Test
    public void testInsertDuplicates() {
        for (int i = 1; i <= 500; i++) {
            tree.insert(new Point2D(i, i));
            assertEquals(i, tree.size());
        }
        for (int i = 1; i <= 500; i++) {
            tree.insert(new Point2D(i, i));
            assertEquals(500, tree.size());
        }
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
        for (int i = 1; i < 100; i++) {
            tree.insert(new Point2D(i, i));
        }
        for (int i = 1; i < 100; i++) {
            assertTrue(tree.contains(new Point2D(i, i)));
        }
        for (int i = 100; i < 200; i++) {
            assertFalse(tree.contains(new Point2D(i, i)));
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
            tree.insert(p);
        for (Point2D p : pointsOutside)
            tree.insert(p);

        for (Point2D p : tree.range(rect)) {
            assertTrue(pointsInside.remove(p));
            assertFalse(pointsOutside.remove(p));
        }
        assertEquals(0, pointsInside.size());
    }

    @Test
    public void testNearest() {
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));

        assertEquals(new Point2D(0.9, 0.6), tree.nearest(new Point2D(0.79, 0.49)));
    }

    @Test
    public void testNearestMany() {
        Random r = new Random();
        File testFiles = new File("kdtree-test-files/");
        String[] files = testFiles.list((dir, name) -> name.contains("circle") && name.contains(".txt"));
        for (String file : files) {
            KdTree tree = generateKdTree(file);
            PointSET set = generatePointSET(file);
            for (int i = 0; i < 100; i++) {
                Point2D targetPoint = new Point2D(r.nextDouble(), r.nextDouble());
                assertEquals(set.nearest(targetPoint), tree.nearest(targetPoint));
            }
        }
    }

}
