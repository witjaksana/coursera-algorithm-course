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
 *  Name: PointSET.java
 *  Date: 13/06/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private final SET<Point2D> tree;

    // construct an empty set of points
    public PointSET() {
        tree = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    // number of points in the set
    public int size() {
        return tree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        tree.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        return tree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : tree) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Null argument!");
        List<Point2D> ranges = new ArrayList<>();
        for (Point2D p : tree) {
            if (rect.contains(p)) {
                ranges.add(p);
            }
        }
        return ranges;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument!");
        if (isEmpty())
            return null;

        Point2D result = null;
        double min = Double.MAX_VALUE;

        for (Point2D elem : tree) {
            double distance = elem.distanceSquaredTo(p);
            if (distance < min) {
                min = distance;
                result = elem;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        Point2D p1 = new Point2D(3, 5);
        Point2D p2 = new Point2D(5, 5);
        Point2D p3 = new Point2D(12, 3);
        ps.insert(p1);
        ps.insert(p2);
        StdOut.println("Size " + ps.size());
        StdOut.println("Does it contain ? " + ps.contains(p3));
        ps.insert(p3);
        StdOut.println("Does it contain ? " + ps.contains(p3));
        ps.draw();
    }
}
