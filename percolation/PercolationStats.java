/* *****************************************************************************
 *  Name: Arief
 *  Date:
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
