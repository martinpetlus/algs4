import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.List;
import java.util.ArrayList;

public class Solver {
    private int numberOfMoves;
    private Board prevBoard;
    private List<Board> sequenceOfBoards = new ArrayList<>();
    private MinPQ<Board> pq = new MinPQ<>(Board.MANHATTAN_COMPARATOR);

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException("initial board is null");
        }

        pq.insert(initial);

        while (true) {
            Board min = pq.delMin();

            sequenceOfBoards.add(min);

            if (min.isGoal()) break;

            for (Board board : min) {
                if (prevBoard != null && prevBoard.equals(board)) continue;

                pq.insert(board);
            }

            numberOfMoves++;

            prevBoard = min;
        }
    }

    public boolean isSolvable() {
        return true;
    }

    public int moves() {
        if (!isSolvable()) {
            return -1;
        } else {
            return numberOfMoves;
        }
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        } else {
            return sequenceOfBoards;
        }
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
