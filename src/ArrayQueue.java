// array-based implementation of queue
public class ArrayQueue<T> {
    private int size;
    private static int MAX_SIZE = 100;
    private T[] items;

    public ArrayQueue() {
        size = 0;
        items = (T[])new Object[MAX_SIZE];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean enQueue(T item) {
        // make sure the queue still have empty slot
        if (size < MAX_SIZE) {
            items[size] = item;
            size++;
            return true;
        }
        return false;
    }

    public boolean deQueue() {
        // make sure the queue is not empty
        if (isEmpty()) {
            return false;
        }
        // shifting left
        for (int i = 0; i < size - 1; i++) {
            items[i] = items[i + 1];
        }
        size--;
        return true;
    }

    public T peekFront() {
        // make sure the queue is not empty
        if (isEmpty()) {
            return null;
        }
        return items[0];
    }

}