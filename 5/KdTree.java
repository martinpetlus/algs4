import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.List;
import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int treeSize = 0;
    private final RectHV rootRect = new RectHV(0, 0, 1, 1);

    public KdTree() {
    }

    private static final class Node {
        private Node left;
        private Node right;
        private final RectHV leftRect;
        private final RectHV rightRect;
        private final Point2D point;
        private final boolean vertical;

        public Node(Point2D p, boolean v, RectHV rect) {
            point = p;
            vertical = v;

            if (vertical) {
                leftRect = new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
                rightRect = new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
                rightRect = new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
            }
        }

        public RectHV getLeftRect() {
            return leftRect;
        }

        public RectHV getRightRect() {
            return rightRect;
        }

        public boolean isVertical() {
            return vertical;
        }

        public Point2D getPoint() {
            return point;
        }

        public void setLeft(Node node) {
            left = node;
        }

        public void setRight(Node node) {
            right = node;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return treeSize;
    }

    private Node insert(Node n, Point2D p, boolean vertical, RectHV rect) {
        if (n == null) {
            return new Node(p, vertical, rect);
        } else if (vertical) {
            if (p.x() < n.getPoint().x()) {
                n.setLeft(insert(n.getLeft(), p, false, n.getLeftRect()));
            } else {
                n.setRight(insert(n.getRight(), p, false, n.getRightRect()));
            }
        } else {
            if (p.y() < n.getPoint().y()) {
                n.setLeft(insert(n.getLeft(), p, true, n.getLeftRect()));
            } else {
                n.setRight(insert(n.getRight(), p, true, n.getRightRect()));
            }
        }

        return n;
    }

    public void insert(Point2D p) {
        root = insert(root, p, true, rootRect);
        treeSize++;
    }

    private boolean contains(Node n, Point2D p) {
        if (n == null) {
            return false;
        } else if (n.getPoint().equals(p)) {
            return true;
        } else if (n.isVertical()) {
            if (p.x() < n.getPoint().x()) {
                return contains(n.getLeft(), p);
            } else {
                return contains(n.getRight(), p);
            }
        } else {
            if (p.y() < n.getPoint().y()) {
                return contains(n.getLeft(), p);
            } else {
                return contains(n.getRight(), p);
            }
        }
    }

    public boolean contains(Point2D p) {
        return contains(root, p);
    }

    private void draw(Node n) {
        if (n == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(n.getPoint().x(), n.getPoint().y());

        if (n.isVertical()) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(
                n.getPoint().x(),
                n.getLeftRect().ymax(),
                n.getPoint().x(),
                n.getLeftRect().ymin()
            );
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(
                n.getLeftRect().xmin(),
                n.getPoint().y(),
                n.getLeftRect().xmax(),
                n.getPoint().y()
            );
        }

        draw(n.getLeft());
        draw(n.getRight());
    }

    public void draw() {
        draw(root);
    }

    private void range(List<Point2D> points, Node node, RectHV rect, RectHV query) {
        if (node == null) {
            return;
        } else if (!rect.intersects(query)) {
            return;
        }

        if (query.contains(node.getPoint())) {
            points.add(node.getPoint());
        }

        range(points, node.getLeft(), node.getLeftRect(), query);
        range(points, node.getRight(), node.getRightRect(), query);
    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> points = new ArrayList<>();

        range(points, root, rootRect, rect);

        return points;
    }

    private Point2D nearest(Point2D closest, Node node, RectHV rect, Point2D query) {
        if (node == null) return closest;

        if (closest != null) {
            if (closest.distanceTo(query) < rect.distanceTo(query)) {
                return closest;
            }
        } else {
            closest = root.getPoint();
        }

        if (node.isVertical()) {
            if (query.x() < node.getPoint().x()) {
                closest = nearest(closest, node.getLeft(), node.getLeftRect(), query);
                closest = nearest(closest, node.getRight(), node.getRightRect(), query);
            } else {
                closest = nearest(closest, node.getRight(), node.getRightRect(), query);
                closest = nearest(closest, node.getLeft(), node.getLeftRect(), query);
            }
        } else {
            if (query.y() < node.getPoint().x()) {
                closest = nearest(closest, node.getLeft(), node.getLeftRect(), query);
                closest = nearest(closest, node.getRight(), node.getRightRect(), query);
            } else {
                closest = nearest(closest, node.getRight(), node.getRightRect(), query);
                closest = nearest(closest, node.getLeft(), node.getLeftRect(), query);
            }
        }

        return closest;
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;

        return nearest(null, root, rootRect, p);
    }

    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();

        KdTree kdTree = new KdTree();

        kdTree.insert(new Point2D(0.3, 0.5));
        kdTree.insert(new Point2D(0.1, 0.5));
        kdTree.insert(new Point2D(0.5, 0.8));

        kdTree.draw();
        StdDraw.show();

        System.out.println(kdTree.contains(new Point2D(5, 5)));
        System.out.println(kdTree.size());
    }
}
