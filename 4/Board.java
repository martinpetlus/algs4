import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class Board implements Iterable<Board> {
    private static void swap(Board board, int i1, int j1, int i2, int j2) {
        int tmp = board.blocks[i1][j1];
        board.blocks[i1][j1] = board.blocks[i2][j2];
        board.blocks[i2][j2] = tmp;
    }

    private final int n;
    private final int[][] blocks;

    public Board(int[][] blocks) {
        this.n = blocks.length;
        this.blocks = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }

    private int id(int i, int j) {
        if (i + 1 == n && j + 1 == n) {
            return 0;
        }

        return i * n + j + 1;
    }

    private int row(int id) {
        if (id == 0) return n - 1;

        return (id - 1) / n;
    }

    private int col(int id) {
        if (id == 0) return n - 1;

        return (id - 1) % n;
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int h = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0 && id(i, j) != blocks[i][j]) {
                    h++;
                }
            }
        }

        return h;
    }

    public int manhattan() {
        int m = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0) {
                    m += Math.abs(i - row(blocks[i][j])) + Math.abs(j - col(blocks[i][j]));
                }
            }
        }

        return m;
    }

    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != id(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    private Board left(int i, int j) {
        if (j == 0) return null;
        Board board = new Board(blocks);
        swap(board, i, j, i, j - 1);
        return board;
    }

    private Board right(int i, int j) {
        if (j == n - 1) return null;
        Board board = new Board(blocks);
        swap(board, i, j, i, j + 1);
        return board;
    }

    private Board up(int i, int j) {
        if (i == 0) return null;
        Board board = new Board(blocks);
        swap(board, i, j, i - 1, j);
        return board;
    }

    private Board down(int i, int j) {
        if (i == n - 1) return null;
        Board board = new Board(blocks);
        swap(board, i, j, i + 1, j);
        return board;
    }

    public Board twin() {
        Board board;

        int i = StdRandom.uniform(n);
        int j = StdRandom.uniform(n);

        if ((board = up(i, j)) != null) return board;
        if ((board = right(i, j)) != null) return board;
        if ((board = down(i, j)) != null) return board;
        return left(i, j);
    }

    public boolean equals(Object other) {
        if (this == other) return true;

        if (!(other instanceof Board)) return false;

        Board board = (Board) other;

        if (dimension() != board.dimension()) return false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != board.blocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterator<Board> iterator() {
        return new BoardIterator();
    }

    public Iterable<Board> neighbors() {
        return this;
    }

    private class BoardIterator implements Iterator<Board> {
        private int index = 0;
        private final List<Board> boards = new ArrayList<Board>();

        public BoardIterator() {
            int i = 0;
            int j = 0;

            outer:for (i = 0; i < n; i++) {
                for (j = 0; j < n; j++) {
                    if (blocks[i][j] == 0) {
                        break outer;
                    }
                }
            }

            Board board = up(i, j);

            if (board != null) boards.add(board);
            if ((board = right(i, j)) != null) boards.add(board);
            if ((board = down(i, j)) != null) boards.add(board);
            if ((board = left(i, j)) != null) boards.add(board);
        }

        public boolean hasNext() {
            return index < boards.size();
        }

        public Board next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return boards.get(index++);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public String toString() {
        String str = "";

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                str += blocks[i][j];

                if (j + 1 < n) str += " ";
            }

            if (i + 1 < n) str += "\n";
        }

        return str;
    }

    public static void main(String[] args) {
        int blocks[][] = new int[][] {
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5}
        };

        Board board = new Board(blocks);

        System.out.println(board.hamming());
        System.out.println(board.manhattan());
        System.out.println(board);
        System.out.println();

        for (Board b : board.neighbors()) {
            System.out.println(b);
            System.out.println();
        }
    }
}
