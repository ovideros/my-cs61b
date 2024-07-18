package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
<<<<<<< HEAD
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        for (int n = 1000; n <= 128000; n*=2) {
            SLList<Integer> testList= new SLList<>();
            for (int i = 0; i < n; i++) {
                testList.addFirst(1);
            }
            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < 10000; i++) {
                testList.getLast();
            }
            Double time = sw.elapsedTime();
            Ns.addLast(n);
            times.addLast(time);
            opCounts.addLast(10000);
        }
        printTimingTable(Ns, times, opCounts);
=======
        // TODO: YOUR CODE HERE
>>>>>>> 160747451c147c59d8e3cbf70a7afee2b73bebdb
    }

}
