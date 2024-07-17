package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

/** @source StudentArrayDequeLauncher */
 public class TestArrayDequeEC {
    @Test
    public void easyTest() {
        StudentArrayDeque<Integer> L = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> L2 = new ArrayDequeSolution<>();
        int N = 5000;
        ArrayDequeSolution<String> message = new ArrayDequeSolution<>();
        int messageSize = 0;
        for (int i = 0; i < N; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            int randVal = StdRandom.uniform(0, 100);
            if (numberBetweenZeroAndOne < 0.25) {
                L.addFirst(randVal);
                L2.addFirst(randVal);
                appendHelper(messageSize, message, 0, randVal);
                messageSize += 1;
                assertEquals(messageToStr(message),L.get(0), L2.get(0));
            } else if (numberBetweenZeroAndOne < 0.5) {
                L.addLast(randVal);
                L2.addLast(randVal);
                appendHelper(messageSize, message, 1, randVal);
                messageSize += 1;
                assertEquals(messageToStr(message),L.get(L.size() - 1), L2.get(L2.size() - 1));
            } else if (numberBetweenZeroAndOne < 0.75 && !L.isEmpty() && !L2.isEmpty()) {
                appendHelper(messageSize, message, 2, randVal);
                messageSize += 1;
                assertEquals(messageToStr(message),L.removeFirst(), L2.removeFirst());
            } else if (!L.isEmpty() && !L2.isEmpty()){
                appendHelper(messageSize, message, 3, randVal);
                messageSize += 1;
                assertEquals(messageToStr(message),L.removeLast(), L2.removeLast());
            }

        }
    }

    /** True means <=3; false means > 3.*/
    private void appendHelper(int mS,ArrayDequeSolution<String> m, int type, int rV) {

        switch (type) {
            case 0:
                m.addLast("addFirst(" + rV + ")\n");
                break;
            case 1:
                m.addLast("addLast(" + rV + ")\n");
                break;
            case 2:
                m.addLast("removeFirst()\n");
                break;
            case 3:
                m.addLast("removeLast()\n");
                break;
        }
        if (mS > 4) {
            m.removeFirst();
        }
    }

    private String messageToStr(ArrayDequeSolution<String> m) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < m.size(); i++) {
            str.append(m.get(i));
            i += 1;
        }
        return str.toString();
    }

}
