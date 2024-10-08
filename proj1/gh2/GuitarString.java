package gh2;

<<<<<<< HEAD
import deque.Deque;
import deque.LinkedListDeque;
=======
// TODO: uncomment the following import once you're ready to start this portion
// import deque.Deque;
// TODO: maybe more imports
>>>>>>> 160747451c147c59d8e3cbf70a7afee2b73bebdb

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
<<<<<<< HEAD
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int initialSize = (int) Math.round(SR / frequency);
        buffer = new LinkedListDeque<>();
        for (int i = 0; i < initialSize; i++) {
            buffer.addFirst(0.0);
        }
=======
    // TODO: uncomment the following line once you're ready to start this portion
    // private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        // TODO: Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this division operation into an int. For
        //       better accuracy, use the Math.round() function before casting.
        //       Your should initially fill your buffer array with zeros.
>>>>>>> 160747451c147c59d8e3cbf70a7afee2b73bebdb
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
<<<<<<< HEAD
        int size = buffer.size();
        for (int i = 0; i < size; i++) {
            double r = Math.random() - 0.5;
            buffer.removeFirst();
            buffer.addLast(r);
        }
=======
        // TODO: Dequeue everything in buffer, and replace with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
>>>>>>> 160747451c147c59d8e3cbf70a7afee2b73bebdb
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
<<<<<<< HEAD
        double first = buffer.removeFirst();
        double second = buffer.get(0);
        double newSample = (first + second) / 2 * DECAY;
        buffer.addLast(newSample);
=======
        // TODO: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       **Do not call StdAudio.play().**
>>>>>>> 160747451c147c59d8e3cbf70a7afee2b73bebdb
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
<<<<<<< HEAD
        return buffer.get(0);
    }
}
=======
        // TODO: Return the correct thing.
        return 0;
    }
}
    // TODO: Remove all comments that say TODO when you're done.
>>>>>>> 160747451c147c59d8e3cbf70a7afee2b73bebdb
