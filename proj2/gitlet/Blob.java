package gitlet;

import java.io.File;

import static gitlet.Utils.*;

public class Blob {
    public String text;

    public Blob(String text) {
        this.text = text;
    }

    public String toSha1() {
        return Utils.sha1(text);
    }


    /** Store text in hash name. */
    public void store() {
        String sha1 = sha1(text);
        File folder = join(Repository.BLOBS_DIR, sha1.substring(0, 2));
        File file = join(folder, sha1);
        if (!folder.exists()) {
            folder.mkdir();
        }
        writeContents(file, text);
    }

    /** Read text from sha1 name,
     * return the blob. */
    public String read() {
        // TODO: return something
        return "TODO";
    }
}
