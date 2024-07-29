package gitlet;

import java.io.File;

import static gitlet.Utils.join;

public class Head extends Pointer {

    private static final String name = "HEAD";

    public Head(String sha1) {
        super(sha1);
    }

    public void store() {
        Repository.storeInName(Repository.GITLET_DIR, this, name);
    }

    public static Head read() {
        File headFile = join(Repository.GITLET_DIR, name);
        return Utils.readObject(headFile, Head.class);
    }
}
