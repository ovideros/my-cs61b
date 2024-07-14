package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static final int INIT_SIZE = 8;

    public ArrayDeque() {
        items = (T[]) new Object[INIT_SIZE];
        size = 0;
        nextFirst = INIT_SIZE-1;
        nextLast = 0;
    }

    public void addLast(T item) {
        items[nextLast] = item;
        nextLast = nextIndex(nextLast);
        size += 1;
    }

    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst = nextIndex(nextFirst);
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = items[nextIndex(nextFirst)];
        items[nextIndex(nextFirst)] = null;
        nextFirst = nextIndex(nextFirst);
        size -= 1;
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = items[prevIndex(nextLast)];
        items[prevIndex(nextLast)] = null;
        nextLast = prevIndex(nextLast);
        size -= 1;
        return item;
    }

    public T get(int index) {
        if (index >= items.length){
            return null;
        }
        return items[index];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int start = nextIndex(nextFirst);
        int end = prevIndex(nextLast);
        if (end > start) {
            for (int i = start; i <= end; i++) {
                System.out.print(items[i]);
                System.out.print(' ');
            }
            System.out.println();
        }
        else {
            for (int i = start; i < items.length; i++) {
                System.out.print(items[i]);
                System.out.print(' ');
            }
            for (int i = 0; i <= end; i++) {
                System.out.print(items[i]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    private int nextIndex(int n) {
        if (n+1 == items.length) {
            return 0;
        }
        return n+1;
    }

    private int prevIndex(int n) {
        if (n == 0) {
            return items.length-1;
        }
        return n-1;
    }
}
