package gitlet;

import java.io.File;

import static gitlet.Utils.join;

public class Head extends Pointer {
    private static final String NAME = "HEAD";

    /** Construct head with SHA1. */
    public Head(String sha1) {
        super(sha1);
    }

    /** Store this head. */
    public void store() {
        Repository.storeInName(Repository.GITLET_DIR, this, NAME);
    }

    /** Read the head and return it.
     *
     * @return head
     */
    public static Head read() {
        File headFile = join(Repository.GITLET_DIR, NAME);
        if (!headFile.exists()) {
            return null;
        }
        return Utils.readObject(headFile, Head.class);
    }
}
