/*
 * MIT License
 *
 * Copyright (c) 2020 Arief Wicaksana (arief.wicaksana@outlook.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/* *****************************************************************************
 *  Name: KdTree.java
 *  Date: 13/06/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root = null;
    private int count = 0;

    public KdTree() {
    }

    private static class Node {
        private final Point2D point;
        private Node left;
        private Node right;
        private final RectHV rect;

        private final boolean vertical;

        public Node(Point2D point, boolean vertical, Node left, Node right, RectHV rect) {
            this.point = point;
            this.vertical = vertical;
            this.left = left;
            this.right = right;
            this.rect = rect;
        }

        public int compareTo(Point2D that) {
            if (vertical) {
                if (this.point.x() < that.x()) return -1;
                if (this.point.x() > that.x()) return 1;
            }
            else {
                if (this.point.y() < that.y()) return -1;
                if (this.point.y() > that.y()) return 1;
            }
            return 0;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // number of points in the set
    public int size() {
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        root = insert(p, root, true, new RectHV(0, 0, 1.0, 1.0));
    }

    private Node insert(Point2D p, Node n, boolean vertical, RectHV rect) {
        if (n == null) {
            count++;
            return new Node(p, vertical, null, null, rect);
        }

        if (n.point.equals(p)) return n;

        if (n.compareTo(p) > 0)
            n.left = insert(p, n.left, !vertical, childRect(n, true));
        else
            n.right = insert(p, n.right, !vertical, childRect(n, false));

        return n;
    }

    private RectHV childRect(Node n, boolean left) {
        RectHV rect;
        RectHV temp = n.rect;
        if (left) {
            if (n.left != null) {
                return n.left.rect;
            }

            if (n.vertical) {
                rect = new RectHV(temp.xmin(), temp.ymin(), n.point.x(), temp.ymax());
            }
            else {
                rect = new RectHV(temp.xmin(), temp.ymin(), temp.xmax(), n.point.y());
            }
        }
        else {
            if (n.right != null) {
                return n.right.rect;
            }

            if (n.vertical) {
                rect = new RectHV(n.point.x(), temp.ymin(), temp.xmax(), temp.ymax());
            }
            else {
                rect = new RectHV(temp.xmin(), n.point.y(), temp.xmax(), temp.ymax());
            }
        }
        return rect;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        return get(root, p) != null;
    }

    private Node get(Node x, Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null Point2D value");
        if (x == null) return null;
        int cmp = x.compareTo(p);

        if (x.point.equals(p)) {
            return x;
        }
        if (cmp > 0) return get(x.left, p);
        else return get(x.right, p);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node n) {
        if (n == null) return;

        draw(n.left);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.point.draw();
        StdDraw.setPenRadius();
        if (n.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), n.rect.ymin(), n.point.x(), n.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.point.y(), n.rect.xmax(), n.point.y());
        }
        draw(n.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Null RectHV value");

        List<Point2D> set = new ArrayList<>();
        range(set, rect, root);
        return set;
    }

    private void range(List<Point2D> set, RectHV rect, Node n) {
        if (null == n || !n.rect.intersects(rect)) return;

        boolean left = (n.vertical && rect.xmin() < n.point.x()) || (!n.vertical
                && rect.ymin() < n.point.y());
        boolean right = (n.vertical && rect.xmax() >= n.point.x()) || (!n.vertical
                && rect.ymax() >= n.point.y());

        if (left) {
            range(set, rect, n.left);
        }
        if (rect.contains(n.point)) {
            set.add(n.point);
        }
        if (right) {
            range(set, rect, n.right);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null Point2D value");
        if (null == root) return null;

        Point2D ret = null;
        double min = Double.MAX_VALUE;
        Queue<Node> queue = new Queue<>();
        queue.enqueue(root);

        while (queue.isEmpty()) {
            Node x = queue.dequeue();
            double dis = p.distanceSquaredTo(x.point);
            if (dis < min) {
                ret = x.point;
                min = dis;
            }

            if (x.left != null && x.left.rect.distanceSquaredTo(p) < min) {
                queue.enqueue(x.left);
            }
            if (x.right != null && x.right.rect.distanceSquaredTo(p) < min) {
                queue.enqueue(x.right);
            }
        }

        return ret;
    }

    public static void main(String[] args) {
        /* Test program found in KdTreeVizualizer*/
    }
}
