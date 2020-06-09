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
 *  Name: Board.java
 *  Date: 05/06/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int dim;
    private final char[] tab;
    private int zero;
    private int hamming;
    private int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dim = tiles.length;
        tab = new char[dim * dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                tab[i * dim + j] = (char) tiles[i][j];
                if (tab[i * dim + j] == 0)
                    zero = i * dim + j;
            }
        }

        hamming = hammingFunction(tab);
        manhattan = manhattanFunction(tab);

    }

    private int hammingFunction(char[] tabs) {
        int count = 0;
        for (int i = 0; i < tabs.length; i++) {
            count += callHamming(tab[i], i);
        }
        return count;
    }

    private int manhattanFunction(char[] tabs) {
        int count = 0;
        for (int i = 0; i < tabs.length; i++) {
            count += callManhattan(tab[i], i);
        }
        return count;
    }

    private Board(char[] cells, int dimension, int zero, int hamming, int manhattan) {
        this.tab = cells;
        this.dim = dimension;
        this.zero = zero;
        this.hamming = hamming;
        this.manhattan = manhattan;
    }

    private int callHamming(char num, int i) {
        if (num == 0) return 0;
        if (num != (i + 1)) return 1;
        return 0;
    }

    private int callManhattan(char num, int i) {
        if (num == 0) return 0;
        else return Math.abs(i / dim - (num - 1) / dim) + Math.abs(i % dim - (num - 1) % dim);
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n ");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(String.format("%d ", (int) tab[i * dim + j]));
            }
            s.append("\n ");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return dim;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;

    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || this.getClass() != y.getClass())
            return false;
        else
            return new String(tab).equals(new String(((Board) y).tab));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbours = new ArrayList<>();
        if (zero / dim != 0) {
            neighbours.add(neighbour(zero - dim));
        }
        if (zero / dim != (dim - 1)) {
            neighbours.add(neighbour(zero + dim));
        }
        if (zero % dim != 0) {
            neighbours.add(neighbour(zero - 1));
        }
        if (zero % dim != (dim - 1)) {
            neighbours.add(neighbour(zero + 1));
        }
        return neighbours;
    }

    private Board neighbour(int i) {
        char[] copy = tab.clone();
        exch(copy, zero, i);
        int h = hamming;
        int m = manhattan;
        h += callHamming(copy[i], i) - callHamming(tab[i], i);
        m += callManhattan(copy[i], i) - callManhattan(tab[i], i);
        return new Board(copy, dim, i, h, m);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        char[] copy = tab.clone();
        for (int i = 0; i < tab.length; i++) {
            if (i % dim == 0 || tab[i] * tab[i - 1] == 0) continue;
            exch(copy, i, i - 1);
            int h = hamming;
            int m = manhattan;
            for (int j = i; j > i - 2; j--) {
                h += callHamming(copy[j], j) - callHamming(tab[j], j);
                m += callManhattan(copy[j], j) - callManhattan(tab[j], j);
            }
            return new Board(copy, dim, zero, h, m);
        }
        return null;
    }

    private void exch(char[] chars, int i, int j) {
        char tmp = chars[i];
        chars[i] = chars[j];
        chars[j] = tmp;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int size = in.readInt();
        int[][] blocks = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                blocks[i][j] = in.readInt();
        Board b = new Board(blocks);
        StdOut.println(b);
        StdOut.println("Hamming = " + b.hamming());
        for (Board board : b.neighbors()) {
            StdOut.println(board);
            StdOut.println("Hamming = " + board.hamming());
        }
    }
}
