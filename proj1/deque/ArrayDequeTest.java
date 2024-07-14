package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addIsEmptySizeTest() {

        ArrayDeque<String> lld1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();
    }


    @Test
    public void smallAddTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        assertTrue(ad.isEmpty());
        ad.addLast(5);
        assertFalse(ad.isEmpty());
        ad.addLast(1);
        ad.addFirst(4);
        ad.addLast(4);
        assertEquals(4, ad.size());
        ad.addFirst(1);
        ad.addFirst(1);
        assertEquals(1, (int)ad.removeFirst());
        assertEquals(1, (int)ad.removeFirst());
        assertEquals(4, (int)ad.removeLast());
        assertEquals(1, (int)ad.removeLast());
        assertEquals(4, (int)ad.removeFirst());
        assertEquals(5, (int)ad.removeFirst());
    }

    @Test
    public void removeEmptyTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }
    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 999999; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        assertEquals(1,lld1.size());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest2() {


        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

    }

}
