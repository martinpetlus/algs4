import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
       this.x = x;
       this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(x, y, that.x, that.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int compareTo(Point that) {
        if (y < that.y || (y == that.y && x < that.x)) {
            return -1;
        } else if (x == that.x && y == that.y) {
            return 0;
        } else {
            return 1;
        }
    }

    public double slopeTo(Point that) {
        if (x == that.x && y == that.y) {
            return Double.NEGATIVE_INFINITY;
        } else if (x == that.x) {
            return Double.POSITIVE_INFINITY;
        } else if (y == that.y) {
            return 0;
        } else {
            return ((double) (that.y - y)) / (that.x - x);
        }
    }

    private class SlopeComparator implements Comparator<Point> {
        @Override
        public int compare(Point q, Point r) {
            return Double.compare(Point.this.slopeTo(q), Point.this.slopeTo(r));
        }
    }

    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }
}
