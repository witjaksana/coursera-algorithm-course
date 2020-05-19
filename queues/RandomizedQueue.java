/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size;

    public RandomizedQueue() {
        this.size = 0;
        this.items = (Item[]) new Object[1];
    }


    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Invalid input");

        if (size == items.length) {
            resize(items.length * 2);
        }

        items[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("No such element");

        if (size == items.length / 4) {
            resize(size * 2);
        }

        int idx = StdRandom.uniform(size);
        Item item = items[idx];
        items[idx] = items[--size];
        items[size] = null;
        return item;
    }

    private void resize(int length) {
        Item[] copy = (Item[]) new Object[length];
        for (int i = 0; i < size; i++)
            copy[i] = items[i];

        items = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("No such element");

        return items[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private final int[] random;
        private int current;

        public RandomizedQueueIterator() {
            random = new int[size];
            for (int i = 0; i < size; i++)
                random[i] = i;
            StdRandom.shuffle(random);
            current = 0;
        }

        public boolean hasNext() {
            return current != random.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (current == random.length)
                throw new NoSuchElementException("No element to return");

            Item item = items[random[current]];
            current++;
            return item;
        }

    }

    public static void main(String[] args) {
        RandomizedQueue<String> str = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            str.enqueue(StdIn.readString());
        }
        while (!str.isEmpty())
            StdOut.print(str.dequeue() + " ");
    }
}
