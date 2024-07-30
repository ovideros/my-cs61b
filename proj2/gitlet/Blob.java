package gitlet;

import java.io.File;

import static gitlet.Utils.*;

public class Blob {
    private String text;

    /** Gets the text of this Blob.
     * @return text
     * */
    public String text() {
        return text;
    }

    /** Construct a Blob with text.
     *
     * @param text  the text
     */
    public Blob(String text) {
        this.text = text;
    }


    /** Gets the SHA1 value of text in this Blob.
     *
     * @return the SHA1 value of text
     */
    public String toSha1() {
        return Utils.sha1(text);
    }


    /** Store original text by hash name in the Blobs folder. */
    public void store() {
        String sha1 = sha1(text);
        File folder = join(Repository.BLOBS_DIR, sha1.substring(0, 2));
        File file = join(folder, sha1);
        if (!folder.exists()) {
            folder.mkdir();
        }
        writeContents(file, text);
    }

    /** Read text from sha1 name, return the blob. */
    public String read() {
        // TODO: return something
        return "TODO";
    }
}
