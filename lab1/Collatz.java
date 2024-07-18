/** Class that prints the Collatz sequence starting from a given number.
<<<<<<< HEAD
 *  @ovideros YOUR NAME HERE
=======
 *  @author YOUR NAME HERE
>>>>>>> 160747451c147c59d8e3cbf70a7afee2b73bebdb
 */
public class Collatz {

    /** Buggy implementation of nextNumber! */
    public static int nextNumber(int n) {
        if (n  == 128) {
            return 1;
        } else if (n == 5) {
            return 3 * n + 1;
        } else {
            return n * 2;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

