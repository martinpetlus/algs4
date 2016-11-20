import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n = 0;
    private int ii = 0;
    private Item[] q;

    public RandomizedQueue() {
        q = (Item[]) new Object[1];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];

        int idx = 0;

        for (int i = 0; i < ii; i++) {
            if (q[i] != null) {
                copy[idx++] = q[i];
            }
        }

        ii = idx;
        q = copy;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        if (ii == q.length) {
            resize(q.length * 2);
        }

        q[ii++] = item;
        n++;
    }

    private int randomIndex() {
        int i;

        while (true) {
            i = StdRandom.uniform(ii);
            if (q[i] != null) break;
        }

        return i;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int i = randomIndex();
        Item item = q[i];
        q[i] = null;
        n--;

        if (n > 0 && n == q.length / 4) {
            resize(q.length / 2);
        }

        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return q[randomIndex()];
    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {
        private Item[] items;
        private int iii = 0;

        public RandomizedQueueIterator(Item[] q) {
            items = (Item[]) new Object[size()];
            int idx = 0;
            for (int i = 0; i < ii; i++) {
                if (q[i] != null) {
                    items[idx++] = q[i];
                }
            }
            StdRandom.shuffle(items);
        }

        public boolean hasNext() {
            return iii < items.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return items[iii++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<Item>(q);
    }

    public static void main(String[] args) {
    }
}
