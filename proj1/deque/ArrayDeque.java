package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static final int INIT_SIZE = 8;
    private static final int MAX_EMPTY_SIZE = 16;

    public ArrayDeque() {
        items = (T[]) new Object[INIT_SIZE];
        size = 0;
        nextFirst = INIT_SIZE-1;
        nextLast = 0;
    }

    public void addLast(T item) {
        checkExpand();
        items[nextLast] = item;
        nextLast = nextIndex(nextLast);
        size += 1;
    }

    public void addFirst(T item) {
        checkExpand();
        items[nextFirst] = item;
        nextFirst = prevIndex(nextFirst);
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        checkEmptySpace();
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
        checkEmptySpace();
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

    /** Check whether after add new item,
     * the AList needs to expand. If so,
     * call expandSize.
     */
    private void checkExpand() {
        if (size != items.length) {
            return;
        }
        resize(size * 2);
    }

    /** Check whether empty space exists.*/
    private void checkEmptySpace() {
        if (items.length >= MAX_EMPTY_SIZE
                && size < 0.25*items.length) {
            resize((int)(items.length / 2));
        }
        return;
    }

    /** Expand array to newSize, which is larger than now.*/
    private void resize(int newSize) {
        T[] newArray = (T[]) new Object[newSize];
        int first = nextIndex(nextFirst);
        // if the items are circular
        if (isCircular()){
            System.arraycopy(items,first,newArray,
                    0,items.length-first);
            System.arraycopy(items,0,newArray,
                    items.length-first,nextLast);
        }
        else {
            System.arraycopy(items,first,newArray,0,size);
        }
        items = newArray;
        nextFirst = newSize-1;
        nextLast = size;
    }

    public void printDeque() {
        int first = nextIndex(nextFirst);
        int last = prevIndex(nextLast);
        if (isCircular()) {
            for (int i = first; i < items.length; i++) {
                System.out.print(items[i] + " ");
            }
            for (int i = 0; i <= last; i++) {
                System.out.print(items[i] + " ");
            }
            System.out.println();
        }
        else {
            for (int i = first; i <= last; i++) {
                System.out.print(items[i] + " ");
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

    /** If circular, return true.*/
    private boolean isCircular() {
        int first = nextIndex(nextFirst);
        int last = prevIndex(nextLast);
        return first > last;
    }
}
