/* *****************************************************************************
 *  Name: FastCollinearPoints.java
 *  Date: 24/05/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final LineSegment[] lineseg;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

        /* construction */
        Point[] ps = points.clone();
        Arrays.sort(ps);

        List<LineSegment> ls = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            Point[] p = ps.clone();
            Arrays.sort(p, p[i].slopeOrder());
            int j = 1;
            while (j < len - 2) {
                int k = j;
                double s1 = p[0].slopeTo(p[k++]);
                double s2;
                do {
                    if (k == len) {
                        k++;
                        break;
                    }
                    s2 = p[0].slopeTo(p[k++]);
                } while (s1 == s2);
                if (k - j < 4) {
                    j++;
                    continue;
                }
                int lth = k-- - j;
                Point[] line = new Point[lth];
                line[0] = p[0];
                for (int m = 1; m < lth; m++) {
                    line[m] = p[j + m - 1];
                }
                Arrays.sort(line);
                if (line[0] == p[0]) {
                    ls.add(new LineSegment(line[0], line[lth - 1]));
                }
                j = k;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
