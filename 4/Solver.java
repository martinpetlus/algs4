import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private Key goal;
    private int moves;
    private boolean solvable = true;
    private List<Board> sequenceOfBoards;
    private MinPQ<Key> pq1 = new MinPQ<>(new KeyComparator());
    private MinPQ<Key> pq2 = new MinPQ<>(new KeyComparator());

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException("initial board is null");
        }

        pq1.insert(new Key(null, initial, 0, initial.manhattan()));
        pq2.insert(new Key(null, initial.twin(), 0, initial.manhattan()));

        while (true) {
            Key min1 = pq1.delMin();
            Key min2 = pq2.delMin();

            if (min1.getBoard().isGoal()) {
                goal = min1;
                break;
            }

            if (min2.getBoard().isGoal()) {
                solvable = false;
                break;
            }

            for (Board board : min1.getBoard().neighbors()) {
                if (min1.getPrev() != null &&
                    min1.getPrev().getBoard().equals(board)) {
                    continue;
                }

                pq1.insert(
                    new Key(min1, board, min1.getMoves() + 1, board.manhattan())
                );
            }

            for (Board board : min2.getBoard().neighbors()) {
                if (min2.getPrev() != null &&
                    min2.getPrev().getBoard().equals(board)) {
                    continue;
                }

                pq2.insert(
                    new Key(min2, board, min2.getMoves() + 1, board.manhattan())
                );
            }
        }

        if (isSolvable()) {
            sequenceOfBoards = new ArrayList<>();
            Key curr = goal;

            while (curr != null) {
                sequenceOfBoards.add(curr.getBoard());
                curr = curr.getPrev();
            }

            Collections.reverse(sequenceOfBoards);

            moves = goal.getMoves();
        }

        // Clean up
        pq1 = null;
        pq2 = null;
        goal = null;
    }

    private static class Key {
        private final Key prev;
        private final Board board;
        private final int moves;
        private final int cost;

        public Key(Key prev, Board board, int moves, int cost) {
            this.prev = prev;
            this.board = board;
            this.moves = moves;
            this.cost = cost;
        }

        public Key getPrev() {
            return prev;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public int priority() {
            return moves + cost;
        }
    }

    private static class KeyComparator implements Comparator<Key> {
        public int compare(Key b1, Key b2) {
            return b1.priority() - b2.priority();
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (!isSolvable()) return -1;

        return moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        return sequenceOfBoards;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
                StdOut.println();
            }
        }
    }
}
