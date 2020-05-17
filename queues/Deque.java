/* *****************************************************************************
 *  Name: Arief
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int n;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    private class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null) || (last == null);
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("error null argument");

        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldfirst;
        if (last == null)
            last = first;
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("error null argument");

        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (first == null)
            first = last;
        else
            oldlast.next = last;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("deque is empty");

        Item item = first.item;
        first = first.next;
        n--;
        if (first == null) last = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("deque is empty");

        Node<Item> beforelast = first;
        while (beforelast != last && beforelast.next != last)
            beforelast = beforelast.next;

        Item item = last.item;
        if (beforelast == last) {
            last = null;
            first = null;
        }
        else {
            last = beforelast;
            beforelast.next = null;
        }
        n--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<String> dc = new Deque<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                dc.addLast(item);
            else if (!dc.isEmpty())
                StdOut.print(dc.removeFirst() + " ");
        }
        StdOut.println("(" + dc.size() + " left on queue)");

        for (String st : dc)
            StdOut.print(st + " ");
    }

}
