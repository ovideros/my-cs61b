package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void smallAddTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addLast(5);
        ad.addLast(1);
        ad.addFirst(4);
        ad.addLast(4);
        ad.addFirst(1);
        ad.addFirst(1);
        ad.printDeque();

    }
}
