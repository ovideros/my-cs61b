package gitlet;

import java.io.Serializable;

public class Pointer implements Serializable {
    public String next;

    public Pointer(String sha1) {
        next = sha1;
    }
}
