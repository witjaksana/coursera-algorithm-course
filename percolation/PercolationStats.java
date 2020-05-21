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
 *  Name: PercolationStats.java
 *  Date: 21/05/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double mean;
    private double stddev;
    private double confidencelow;
    private double confidencehigh;
    private double confidenceConstant = 1.96;
    private double[] arrayTrials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Invalid input !");

        arrayTrials = new double[trials];
        for (int i = 0; i < trials; i++) {
            double k = 0;
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (perc.isOpen(row, col)) continue;
                perc.open(row, col);
                k++;
            }
            arrayTrials[i] = k / (n * n);
        }

        mean = StdStats.mean(arrayTrials);
        stddev = StdStats.stddev(arrayTrials);
        double c = (confidenceConstant * stddev) / Math.sqrt(trials);
        confidencelow = mean - c;
        confidencehigh = mean + c;
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }


    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidencelow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidencehigh;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats pt = new PercolationStats(n, t);
        StdOut.println("mean                    = " + pt.mean());
        StdOut.println("stddev                  = " + pt.stddev());
        StdOut.println(
                "95% confidence interval = [" + pt.confidenceLo() + ", " + pt.confidenceHi() +
                        "]");
    }
}
