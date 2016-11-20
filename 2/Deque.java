import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    private static class Node<Item> {
        private Node<Item> next;
        private Node<Item> prev;
        private final Item item;

        public Node(Item item) {
            this.item = item;
        }

        public Item getItem() {
            return item;
        }

        public Node<Item> getNext() {
            return next;
        }

        public Node<Item> getPrev() {
            return prev;
        }

        public void setNext(Node<Item> node) {
            next = node;
        }

        public void setPrev(Node<Item> node) {
            prev = node;
        }
    }

    private class DequeIterator<Item> implements Iterator<Item> {
        private Node<Item> node;

        public DequeIterator(Node<Item> first) {
            node = first;
        }

        public boolean hasNext() {
            return node != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = node.getItem();
            node = node.getNext();
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Deque() {
        size = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node<Item> node = new Node<>(item);

        if (isEmpty()) {
            first = node;
            last = node;
        } else {
            first.setPrev(node);
            node.setNext(first);
            first = node;
        }

        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node<Item> node = new Node<>(item);

        if (isEmpty()) {
            first = node;
            last = node;
        } else {
            node.setPrev(last);
            last.setNext(node);
            last = node;
        }

        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = first.getItem();
        first = first.getNext();

        if (first != null) {
            first.setPrev(null);
        } else {
            last = null;
        }

        size--;

        return item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = last.getItem();
        last = last.getPrev();

        if (last != null) {
            last.setNext(null);
        } else {
            first = null;
        }

        size--;

        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator<Item>(first);
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();

        deque.addFirst("C");
        deque.addLast("X");
        System.out.println("last: " + deque.removeLast());
//        System.out.println("first: " + deque.removeFirst());
        System.out.println("first: " + deque.removeFirst());
        deque.addFirst("B");
        deque.addLast("Y");
        deque.addFirst("A");
        deque.addLast("Z");

        for (String s : deque) {
            System.out.println(s);
        }
    }
}
