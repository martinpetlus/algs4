import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private final Map<String, LineSegment> lineSegments = new HashMap<>();

    public BruteCollinearPoints(Point[] points) {
        checkPoints(points);

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (j == i) continue;
                for (int k = 0; k < points.length; k++) {
                    if (k == i || k == j) continue;
                    for (int l = 0; l < points.length; l++) {
                        if (l == i || l == j || l == k) continue;

                        Point p = points[i];

                        double slopePj = p.slopeTo(points[j]);
                        double slopePk = p.slopeTo(points[k]);
                        double slopePl = p.slopeTo(points[l]);

                        if (Double.compare(slopePj, slopePk) == 0 && Double.compare(slopePk, slopePl) == 0) {
                            Point[] linePoints = new Point[] {
                                p,
                                points[j],
                                points[k],
                                points[l]
                            };

                            Arrays.sort(linePoints);
                            LineSegment lineSegment = new LineSegment(linePoints[0], linePoints[3]);

                            if (!lineSegments.containsKey(lineSegment.toString())) {
                                lineSegments.put(lineSegment.toString(), lineSegment);
                            }
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
