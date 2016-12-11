import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private final Set<Point2D> set = new TreeSet<>();

    public PointSET() {
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
       if (!set.contains(p)) {
           set.add(p);
       }
    }

    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> points = new ArrayList<>();

        for (Point2D p : set) {
            if (rect.contains(p)) {
                points.add(p);
            }
        }

        return points;
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }

        Point2D result = null;

        for (Point2D point : set) {
            if (result == null) {
                result = point;
                continue;
            }

            if (point.distanceTo(p) < result.distanceTo(p)) {
                result = point;
            }
        }

        return result;
    }

    public static void main(String[] args) {
    }
}
