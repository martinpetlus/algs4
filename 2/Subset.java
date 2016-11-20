import edu.princeton.cs.algs4.StdIn;
import java.util.NoSuchElementException;

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<>();
        String s;

        try {
            while (true) {
                q.enqueue(StdIn.readString());
            }
        } catch (NoSuchElementException ex) {
            // Ignore exception
        }

        for (int i = 0; i < k; i++) {
            System.out.println(q.dequeue());
        }
    }
}
