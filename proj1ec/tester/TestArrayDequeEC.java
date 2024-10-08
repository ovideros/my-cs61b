package tester;

import static org.junit.Assert.*;

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
        for (int i = 0; i < N; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            int randVal = StdRandom.uniform(0, 100);
            if (numberBetweenZeroAndOne < 0.25) {
                L.addFirst(randVal);
                L2.addFirst(randVal);
                appendHelper( message, 0, randVal);
            } else if (numberBetweenZeroAndOne < 0.5) {
                L.addLast(randVal);
                L2.addLast(randVal);
                appendHelper( message, 1, randVal);
            } else if (numberBetweenZeroAndOne < 0.75 && !L.isEmpty() && !L2.isEmpty()) {
                appendHelper(message, 2, randVal);
                assertEquals(messageToStr(message),L.removeFirst(), L2.removeFirst());
            } else if (!L.isEmpty() && !L2.isEmpty()){
                appendHelper(message, 3, randVal);
                assertEquals(messageToStr(message),L.removeLast(), L2.removeLast());
            }

        }
    }

    private void appendHelper(ArrayDequeSolution<String> m, int type, int rV) {
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
    }

    private String messageToStr(ArrayDequeSolution<String> m) {
        StringBuilder str = new StringBuilder();
        str.append("\n");

        for (int i = 0; i < m.size(); i++) {
            str.append(m.get(i));
        }
        return str.toString();
    }

}
