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
 *  Name: Solver.java
 *  Date: 08/06/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private int count = 0;
    private final MinPQ<Node> pq1;
    private final MinPQ<Node> pq2;

    private Comparator<Node> manhattanPriority = new ManhattanPriority();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Null argument!\n");

        pq1 = new MinPQ<>(manhattanPriority);
        pq2 = new MinPQ<>(manhattanPriority);
        pq1.insert(new Node(initial, count, null));
        pq2.insert(new Node(initial.twin(), count, null));
        Node cur1;
        Node cur2;
        while (!pq1.min().board.isGoal() && !pq2.min().board.isGoal()) {
            cur1 = pq1.delMin();
            cur2 = pq2.delMin();
            for (Board board : cur1.board.neighbors()) {
                if (cur1.prev != null && board.equals(cur1.prev.board)) continue;
                pq1.insert(new Node(board, cur1.moves + 1, cur1));
            }
            for (Board board : cur2.board.neighbors()) {
                if (cur2.prev != null && board.equals(cur2.prev.board)) continue;
                pq2.insert(new Node(board, cur2.moves + 1, cur2));
            }
        }
        if (!pq1.min().board.isGoal()) count = -1;
        else count = pq1.min().moves;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return count != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return count;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        Stack<Board> stack = new Stack<>();
        Node path = pq1.min();
        while (path != null) {
            stack.push(path.board);
            path = path.prev;
        }

        return stack;
    }

    private class Node {
        private Board board;
        private int moves;
        private Node prev;

        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }
    }

    private class ManhattanPriority implements Comparator<Node> {
        public int compare(Node o1, Node o2) {
            int num1 = o1.board.manhattan() + o1.moves;
            int num2 = o2.board.manhattan() + o2.moves;
            if (num1 == num2) { /* manhattan ties */
                if (o1.board.hamming() == o2.board.hamming()) return 0;
                if (o1.board.hamming() < o2.board.hamming()) return -1;
                return 1;
            }
            if (num1 < num2) return -1;
            else return 1;
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
