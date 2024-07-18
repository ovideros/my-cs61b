package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
<<<<<<< HEAD
    public void testSquarePrimesSmall() {
        IntList lst = IntList.of(2,3,4,5,6,7);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 9 -> 4 -> 25 -> 6 -> 49", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesNotPrime() {
        IntList lst = IntList.of(4,8,10,12,14,18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 8 -> 10 -> 12 -> 14 -> 18", lst.toString());
        assertFalse(changed);
    }
    @Test
=======
>>>>>>> 160747451c147c59d8e3cbf70a7afee2b73bebdb
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }
}
