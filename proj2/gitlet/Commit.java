package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  Containg message, timeStamp, parent and files.
 *
 *  @author OvidEros
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private String timeStamp;
    /** The parent node of this Commit. */
    private String parent;
    /** The Mapping from file names to SHA1 values. */
    private Map<String, String> files;
    /** Default first commit time stamp. */
    private static final Timestamp EPOCH_TIME = new Timestamp(0);

    /** Get the files mapping from this commit.
     *
     * @return files
     * */
    public Map<String, String> files() {
        return files;
    }

    /** Construct a new commit with message and parent.
     *  If parent is "", means this is the first commit;
     *  if not, copy files mapping from parent commit.
     *
     * @param msg  message of this commit
     * @param parent the parent of this commit
     * */
    public Commit(String msg, String parent) {
        this.message = msg;
        this.parent = parent;
        if (parent.isEmpty()) {
            this.timeStamp = EPOCH_TIME.toString();
            this.files = new HashMap<>();
        } else {
            Timestamp timeNow = new Timestamp(System.currentTimeMillis());
            this.timeStamp = timeNow.toString();
            this.files = read(parent).files;
        }
    }

    /** Creates a new commit from old one.
     *
     * @return new commit
     * */
    public Commit newCommit(String msg) {
        return new Commit(msg, this.toSha1());
    }

    /** Update this commit through staging area.
     * Add files in addtionArea, remove files in removeArea.
     *
     * @param sa  staging area
     */
    public void updateFiles(StagingArea sa) {
        Map<String, String> newFiles = sa.getAdditionArea();
        Map<String, String> rmFiles = sa.getAdditionArea();
        for (String name : newFiles.keySet()) {
            files.put(name, newFiles.get(name));
        }
        for (String name : rmFiles.keySet()) {
            files.remove(name);
        }
    }

    /** Get SHA1 value for commit as filename,
     *  and store in .gitlet/commits/FT/ ,
     *  FT means the first two character of SHA1.
     */
    public void store() {
        Repository.storeInHashTable(Repository.COMMITS_DIR, this, toSha1());
    }

    /** Convert this commit to SHA1.
     *
     * @return SHA1 value of this commit
     * */
    public String toSha1() {
        StringBuilder tmp = new StringBuilder();
        for (String name : files.keySet()) {
            tmp.append(files.get(name));
        }
        return Utils.sha1(message, timeStamp, parent, tmp.toString());
    }

    /** Through SHA1 value read the commit and return it.
     *
     * @param sha1 the SHA1 value
     * @return the commit of this SHA1 value
     */
    public static Commit read(String sha1) {
        File file = Repository.fileFromHashTable(Repository.COMMITS_DIR, sha1);
        return readObject(file, Commit.class);
    }
}
