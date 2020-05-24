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
 *  Name: BruteCollinearPoints.java
 *  Date: 23/05/2020
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final LineSegment[] lineseg;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        /* Checking corner case */
        if (points == null)
            throw new IllegalArgumentException("Error: null argument!");

        int len = points.length;

        for (int i = 0; i < len; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Error: null input!");
            for (int j = i + 1; j < len; j++) {
                if (points[i] == points[j])
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
                            ls.add(new LineSegment(ps[0], ps[3]));
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

    }
}
