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
 *  Name:
 *  Date: 24/05/2020
 *  Description:
 **************************************************************************** */

public class FastCollinearPoints {

    private LineSegment[] lineseg;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
