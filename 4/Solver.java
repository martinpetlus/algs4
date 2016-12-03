import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private Key goal;
    private MinPQ<Key> pq = new MinPQ<>(new KeyComparator());

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException("initial board is null");
        }

        pq.insert(new Key(null, initial, 0, initial.manhattan()));

        while (true) {
            Key min = pq.delMin();

            if (min.getBoard().isGoal()) {
                goal = min;
                break;
            }

            for (Board board : min.getBoard().neighbors()) {
                if (min.getPrev() != null &&
                    min.getPrev().getBoard().equals(board)) {
                    continue;
                }

                pq.insert(
                    new Key(min, board, min.getMoves() + 1, board.manhattan())
                );
            }
        }
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
        return true;
    }

    public int moves() {
        if (!isSolvable()) return -1;

        return goal.getMoves();
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        List<Board> boards = new ArrayList<>();
        Key curr = goal;

        while (curr != null) {
            boards.add(curr.getBoard());
            curr = curr.getPrev();
        }

        Collections.reverse(boards);
        return boards;
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
