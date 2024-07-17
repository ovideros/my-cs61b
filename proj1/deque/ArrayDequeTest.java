package deque;

import org.junit.Test;

import java.util.Iterator;

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

    @Test
    public void iteratorTest() {
        ArrayDeque<Integer> lld= new ArrayDeque<>();
        lld.addLast(1);
        lld.addLast(4);
        lld.addLast(5);
        Iterator<Integer> iter = lld.iterator();
        assertEquals(1,(int) iter.next());
        assertEquals(4,(int) iter.next());
        assertEquals(5,(int) iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void equalsTest() {
        LinkedListDeque<Integer> l1 = new LinkedListDeque<>();
        LinkedListDeque<Integer> l2;
        l1.addFirst(3);
        l2 = null;
        assertNotEquals(l1, l2);
        l2 = l1;
        assertEquals(l1, l2);
        l1.addLast(4);
        l2 = new LinkedListDeque<>();
        l2.addLast(4);
        l2.addFirst(3);
        assertEquals(l1, l2);
    }

    @Test
    public void getTest() {
        LinkedListDeque<Integer> l1 = new LinkedListDeque<>();
        l1.addLast(0);
        l1.addLast(1);
        assertEquals(0,(int) l1.get(0));
        assertEquals(1,(int) l1.get(1));
        l1.addLast(2);
        l1.addLast(3);
        l1.addLast(4);
        l1.addLast(5);
        l1.addLast(6);
        l1.addLast(7);
        l1.addLast(8);
        l1.addLast(9);
        l1.addLast(10);
        assertEquals(3,(int) l1.get(3));
        assertEquals(0,(int) l1.get(0));
        assertEquals(5,(int) l1.get(5));
        assertEquals(10,(int) l1.get(10));
    }


}
