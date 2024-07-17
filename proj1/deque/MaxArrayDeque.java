package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque{
    private Comparator<T> originalComparator;

    public MaxArrayDeque(Comparator<T> c) {
         originalComparator = c;
    }

    public T max() {
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (originalComparator.compare((T) get(i), (T) get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return (T) get(maxIndex);
    }

    public T max(Comparator<T> c) {
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (c.compare((T) get(i), (T) get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return (T) get(maxIndex);
    }
}
