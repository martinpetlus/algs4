import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private final Map<String, LineSegment> lineSegments = new HashMap<>();

    public FastCollinearPoints(Point[] points) {
        checkPoints(points);

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Point[] copy = new Point[points.length];
            System.arraycopy(points, 0, copy, 0, points.length);
            Arrays.sort(copy, p.slopeOrder());

            int j = 1;
            while (j < copy.length - 2) {
                double slope = p.slopeTo(copy[j]);
                int k = j + 1;
                while (k < copy.length && Double.compare(slope, p.slopeTo(copy[k])) == 0) k++;
                int count = k - j;
                if (count >= 3) {
                    Point[] linePoints = new Point[count + 1];
                    System.arraycopy(copy, j, linePoints, 0, count);
                    linePoints[count] = p;

                    List<Point> linePointsAsList = Arrays.asList(linePoints);

                    Point min = Collections.min(linePointsAsList);
                    Point max = Collections.max(linePointsAsList);

                    LineSegment lineSegment = new LineSegment(min, max);

                    if (!lineSegments.containsKey(lineSegment.toString())) {
                        lineSegments.put(lineSegment.toString(), lineSegment);
                    }

                    j += count;
                } else {
                    j++;
                }
            }
        }
    }

    private static void checkPoints(Point[] points) {
        Set<String> setOfPoints = new HashSet<>();

        if (points == null) {
           throw new NullPointerException();
        }

        for (Point p : points) {
           if (p == null) {
               throw new NullPointerException();
           }

           if (setOfPoints.contains(p.toString())) {
               throw new IllegalArgumentException();
           }

           setOfPoints.add(p.toString());
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.values().toArray(new LineSegment[numberOfSegments()]);
    }

    public static void main(String[] args) {
        // Read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
