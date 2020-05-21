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
 *  Name: Percolation.java
 *  Date: 21/05/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;
    private WeightedQuickUnionUF uf;
    private byte[][] open;
    private int num;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n is less than 0");

        this.n = n + 1;
        uf = new WeightedQuickUnionUF(this.n * this.n);
        open = new byte[this.n][this.n];
    }

    private void validate(int row, int col) {
        if (row <= 0 || row >= n)
            throw new IllegalArgumentException("row is outside prescribed range");

        if (col <= 0 || col >= n)
            throw new IllegalArgumentException("column is outside prescribed range");
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        if (isOpen(row, col)) return;

        open[row][col] = 1;
        num++;

        // border case
        if (row == n - 1) open[row][col] = 2;

        if (row == 1) {
            uf.union(0, row * n + col);
            if (open[row][col] == 2) open[0][0] = 2;
        }

        // connect to others
        if (row - 1 > 0 && isOpen(row - 1, col)) {
            update(row - 1, col, row, col);
        }
        if (row + 1 < n && isOpen(row + 1, col)) {
            update(row + 1, col, row, col);
        }
        if (col - 1 > 0 && isOpen(row, col - 1)) {
            update(row, col - 1, row, col);
        }
        if (col + 1 < n && isOpen(row, col + 1)) {
            update(row, col + 1, row, col);
        }
    }

    private void update(int i1, int j1, int i2, int j2) {
        int p = uf.find(i1 * n + j1);
        int q = uf.find(i2 * n + j2);
        uf.union(i1 * n + j1, i2 * n + j2);
        if (open[p / n][p % n] == 2 || open[q / n][q % n] == 2) {
            int t = uf.find(i2 * n + j2);
            open[t / n][t % n] = 2;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return open[row][col] > 0;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return open[row][col] > 0 && (uf.find(0) == uf.find(row * n + col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return num;
    }

    // does the system percolate?
    public boolean percolates() {
        int root = uf.find(0);
        return open[root / n][root % n] == 2;
    }

    public static void main(String[] args) {

    }
}
