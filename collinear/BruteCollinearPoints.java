/* *****************************************************************************
 *  Name: BruteCollinearPoints.java
 *  Date: 23/05/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final LineSegment[] lineseg;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        /* Checking corner case */
        if (points == null) throw new IllegalArgumentException("Error: null argument!");

        int len = points.length;

        for (int i = 0; i < len; i++) {
            if (points[i] == null) throw new IllegalArgumentException("Error: null input!");
            for (int j = i + 1; j < len; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Error: repeated point input!");
            }
        }

        /* Construction */
        Point[] ps = points.clone();
        Arrays.sort(ps);
        List<LineSegment> ls = new ArrayList<>();

        // Brute force
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                for (int k = j + 1; k < len; k++) {
                    for (int m = k + 1; m < len; m++) {
                        Point[] p = new Point[4];
                        p[0] = ps[i];
                        p[1] = ps[j];
                        p[2] = ps[k];
                        p[3] = ps[m];
                        double s1 = p[0].slopeTo(p[1]);
                        double s2 = p[0].slopeTo(p[2]);
                        if (s1 != s2) continue;
                        double s3 = p[0].slopeTo(p[3]);
                        if (s1 == s3) {
                            Arrays.sort(p);
                            ls.add(new LineSegment(p[0], p[3]));
                        }
                    }
                }
            }
        }

        lineseg = ls.toArray(new LineSegment[ls.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineseg.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineseg.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
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
