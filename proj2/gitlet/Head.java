package gitlet;

import java.io.File;

import static gitlet.Utils.join;

public class Head extends Pointer {
    private static final String name = "HEAD";

    /** Construct head with SHA1. */
    public Head(String sha1) {
        super(sha1);
    }

    /** Store this head. */
    public void store() {
        Repository.storeInName(Repository.GITLET_DIR, this, name);
    }

    /** Read the head and return it.
     *
     * @return head
     */
    public static Head read() {
        File headFile = join(Repository.GITLET_DIR, name);
        return Utils.readObject(headFile, Head.class);
    }
}
