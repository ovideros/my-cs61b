package gitlet;

import java.io.Serializable;

public class Pointer implements Serializable {
    private String next;

    /** Construct a pointer with SHA1. */
    public Pointer(String sha1) {
        next = sha1;
    }

    /** Return the next of this pointer. */
    public String next() {
        return next;
    }

    /** Update this pointer with new next value. */
    public void updateNext(String next) {
        this.next = next;
    }
}
