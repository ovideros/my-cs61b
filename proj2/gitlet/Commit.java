package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Map;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author OvidEros
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private String timeStamp;
    /** The parent node of this Commit. */
    private Commit parent;
    private Map<String, String> files;
    private static final Timestamp EPOCH_TIME = new Timestamp(0);

    /** Construct a new commit with message and parent. */
    public Commit(String msg, Commit parent) {
        this.message = msg;
        this.parent = parent;
        this.files = new HashMap<>();
        if (parent == null) {
            this.timeStamp = EPOCH_TIME.toString();
        } else {
            Timestamp timeNow = new Timestamp(System.currentTimeMillis());
            this.timeStamp = timeNow.toString();
        }
    }

    public Map<String, String> getFiles() {
        return files;
    }

    /** Get message from commit. */
    public String getMessage() {
        return message;
    }

    /** Get pareng from commit. */
    public Commit getParent() {
        return parent;
    }

    /** Get timeStamp from commit. */
    public String getTimeStamp() {
        return timeStamp;
    }

    /** Get sha1 value for commit as filename,
     *  and store in .gitlet/commits/FT/ ,
     *  FT means first two character.
     */
    public void store() {
        Repository.storeInHashTable(Repository.COMMITS_DIR, this, toSha1());
    }

    /** Convert message and timeStamp to sha1. */
    public String toSha1() {
        if (parent == null) {
            return Utils.sha1(message, timeStamp);
        }
        return Utils.sha1(message, timeStamp, parent);
    }

    public void addFile(String name, String sha1) {
        files.put(name, sha1);
    }

    public static Commit read(String sha1) {
        File file = Repository.fileFromHashTable(Repository.COMMITS_DIR, sha1);
        return readObject(file, Commit.class);
    }
}
