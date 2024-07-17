package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;
public class MaxArrayDequeTest {
    /** From https://cloud.tencent.com/developer/article/2339221*/
    private class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer num1, Integer num2) {
            return num1 - num2;
        }
    }

    private class ReverseComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer num1, Integer num2) {
            return num2 - num1;
        }
    }


    @Test
    public void maxTest() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new IntegerComparator());
        mad.addFirst(1);
        mad.addFirst(4);
        mad.addFirst(5);
        mad.addFirst(6);
        mad.addFirst(3);
        assertEquals(6,(int) mad.max());
        assertEquals(1,(int) mad.max(new ReverseComparator()));
    }
}
